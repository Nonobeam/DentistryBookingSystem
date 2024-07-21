// config-overrides.js
const path = require('path');

module.exports = function override(config, env) {
  // Add source-map-loader to ignore specific modules
  config.module.rules.push({
    test: /\.js$/,
    enforce: 'pre',
    use: ['source-map-loader'],
    exclude: [/@babel(?:\/|\\{1,2})runtime/, /node_modules/],
  });

  return config;
};
