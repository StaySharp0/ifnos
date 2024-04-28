const path = require("path");
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
  target: "electron-main",
  mode: "none",
  entry: "./src/main.ts",
  output: {
    filename: "[name]-bundle.js",
    path: path.resolve(__dirname + "/dist"),
  },
  module: {
    rules: [
      {
        test: /\.(m?js?|ts)$/,
        exclude: /(node_modules)/,
        use: {
          loader: "swc-loader",
        },
      },
    ],
  },
  plugins: [
    new CopyPlugin({
      patterns: [{ from: "public", to: "public" }],
    }),
  ],
};
