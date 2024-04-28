const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
  entry: "./src/index.js",
  mode: "development",
  output: {
    path: path.resolve(__dirname, "../dist"),
    filename: "app.bundle.js",
  },
  devServer: {
    contentBase: path.join(__dirname, "dist"),
    compress: true,
    port: 9000
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: "src/index.html",
    }),
    new CopyPlugin({
      patterns: [{ from: "src/assets", to: "assets" }],
    }),
  ],
};
