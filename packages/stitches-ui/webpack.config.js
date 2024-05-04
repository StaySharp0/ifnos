const path = require("path");
const { ProgressPlugin, container } = require("webpack");
const ESLintPlugin = require("eslint-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = (_env, argv) => {
  let { mode } = argv;

  mode = mode ?? "development";
  const isDev = mode !== "production";

  return {
    mode,
    devServer: {
      port: 3001,
    },
    entry: path.resolve(__dirname, "src/index.ts"),
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
          test: /\.(js|ts)x?$/,
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
      ],
    },
    plugins: [
      new ProgressPlugin(),
      new ESLintPlugin({
        extensions: [".ts", ".tsx"],
      }),
      new container.ModuleFederationPlugin({
        name: "ui",
        filename: "remoteEntry.js",
        exposes: {
          "./Button": "./src/components/Button",
          "./Header": "./src/components/Header",
        },
        shared: {
          react: { singleton: true },
          "react-dom": { singleton: true },
        },
      }),
    ].concat(isDev ? [] : [new MiniCssExtractPlugin()]),
  };
};
