const path = require("path");

const { ProgressPlugin } = require("webpack");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = (env, argv) => {
  const { mode } = argv;
  const isDev = mode !== "production";

  return {
    mode,
    entry: path.resolve(__dirname, "src/index.ts"),
    devtool: "source-map",
    output: {
      filename: "index.js",
      path: path.resolve(__dirname, "dist"),
      library: {
        type: "module",
      },
      clean: true,
    },
    experiments: {
      outputModule: true,
    },
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "src"),
      },
      extensions: [".ts", ".tsx"],
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
    externals: {
      react: "react",
      "react-dom": "react-dom",
    },
    plugins: [new ProgressPlugin()].concat(
      isDev ? [] : [new MiniCssExtractPlugin()]
    ),
  };
};
