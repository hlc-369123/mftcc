'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  CONFIG: '"config-dev.js"',
  IS_CRYPTO: false  //是否加密
})
