'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  API_HOST:'"http://127.0.0.1:8088/"',
  WKF_HOST:'"http://127.0.0.1:8090/"',
  IS_CRYPTO: false  //是否加密
})
