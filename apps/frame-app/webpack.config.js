const path = require("path");
const {
  container: { ModuleFederationPlugin },
} = require("webpack");
const ESLintPlugin = require("eslint-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");

/* Minimizer */
const ImageMinimizerPlugin = require("image-minimizer-webpack-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");

module.exports = (_env, argv) => {
  let { mode } = argv;

  mode ??= "development";
  const isDev = mode !== "production";
  const assetGenerator = {
    publicPath: "static/", // NOTE: CDN 생길 결우 변경 필요
    outputPath: "static/",
  };

  const publicPath = "/static";

  return {
    mode,
    devServer: {
      static: [
        {
          directory: path.join(__dirname, "public"),
          publicPath,
        },
      ],
      open: true,
      hot: true,
      historyApiFallback: true,
    },
    entry: path.resolve(__dirname, "src/main.tsx"),
    devtool: "source-map",
    output: {
      filename: "index.js",
      path: path.resolve(__dirname, "dist"),
      clean: true,
      publicPath: "/",
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
      new HtmlWebpackPlugin({
        templateParameters: {
          lang: "ko-KR",
          title: "ifnos",
          publicPath,
        },
        template: "index.html",
        minify: !isDev,
      }),
      new ESLintPlugin({
        extensions: [".ts", ".tsx"],
      }),
      new ModuleFederationPlugin({
        name: "host-frame",
        remotes: {},
        // shared: {
        //   react: { eager: true, singleton: true },
        //   "react-dom": { eager: true, singleton: true },
        // },
      }),
    ].concat(
      isDev
        ? []
        : [
            new CopyPlugin({
              patterns: [{ from: "public", to: "static" }],
            }),
            new MiniCssExtractPlugin(),
          ]
    ),
    optimization: {
      minimizer: [
        new ImageMinimizerPlugin({
          loader: true,
          minimizer: {
            implementation: ImageMinimizerPlugin.imageminMinify,
            options: {
              plugins: [
                "imagemin-gifsicle",
                "imagemin-mozjpeg",
                "imagemin-pngquant",
                "imagemin-svgo",
              ],
            },
          },
          generator: [
            {
              type: "asset",
              implementation: ImageMinimizerPlugin.imageminGenerate,
              options: {
                plugins: [
                  "imagemin-gifsicle",
                  "imagemin-mozjpeg",
                  "imagemin-pngquant",
                  "imagemin-svgo",
                ],
              },
            },
          ],
        }),
        new CssMinimizerPlugin(),
      ],
    },
  };
};
