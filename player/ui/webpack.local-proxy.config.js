const webpack = require('webpack');
const basic = require('./webpack.config.js');

const config = {... basic};

var appTarget = 'LocalProxy';

config.plugins.push(new webpack.NormalModuleReplacementPlugin(
    /(.*)Real(\.*)/,
    function (resource) {
        resource.request = resource.request.replace(
          /Real/,
          `${appTarget}`
        );
      }
    ));

module.exports = config;