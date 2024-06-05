const path = require("path");
const { ProgressPlugin, container } = require("webpack");
const ESLintPlugin = require("eslint-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = (_env, argv) => {
  let { mode } = argv;

  mode ??= "development";
  const isDev = mode !== "production";
  const assetGenerator = {
    publicPath: "static/", // NOTE: CDN 생길 결우 변경 필요
    outputPath: "static/",
  };

  return {
    mode,
    devServer: {
      open: true,
      hot: true,
    },
    entry: path.resolve(__dirname, "src/main.tsx"),
    devtool: "source-map",
    output: {
      filename: "index.js",
      path: path.resolve(__dirname, "dist"),
      clean: true,
    },
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "src"),
      },
      extensions: [".ts", ".tsx", ".js"],
    },
    module: {
      rules: [
        {
          test: /\.[jt]sx?$/,
          exclude: /node_modules/,
          use: {
            loader: "swc-loader",
          },
        },
        {
          test: /\.css$/i,
          use: [
            isDev ? "style-loader" : MiniCssExtractPlugin.loader,
            "css-loader",
          ],
        },
        {
          test: /\.svg$/i,
          oneOf: [
            {
              use: [
                {
                  loader: "@svgr/webpack",
                  options: {
                    exportType: "named",
                  },
                },
              ],
              issuer: /\.[jt]sx?$/,
              resourceQuery: { not: [/url/] },
            },
            {
              type: "asset/resource",
              resourceQuery: /url/, // *.svg?url
              generator: assetGenerator,
            },
          ],
        },
        {
          test: /\.(png|jpg|gif)$/i,
          type: "asset/resource",
          generator: assetGenerator,
        },
      ],
    },
    plugins: [
      new ProgressPlugin(),
      new ESLintPlugin({
        extensions: [".ts", ".tsx"],
      }),
    ].concat(
      isDev
        ? [
            new HtmlWebpackPlugin({
              template: path.join(__dirname, "index.html"),
            }),
          ]
        : [
            new container.ModuleFederationPlugin({
              name: "ui",
              filename: "remoteEntry.js",
              exposes: {},
              shared: {
                react: { singleton: true },
                "react-dom": { singleton: true },
              },
            }),
            new MiniCssExtractPlugin(),
          ]
    ),
  };
};
