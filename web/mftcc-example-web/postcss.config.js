// https://github.com/michael-ciniawsky/postcss-load-config
const packageConfig = require('./package.json')
//项目名称
const productName = packageConfig.name;

module.exports = {
  "plugins": {
    "postcss-import": {},
    "postcss-url": {},
    // to edit target browsers: use "browserslist" field in package.json
    "autoprefixer": {},
    'postcss-selector-namespace': {
      namespace(css) {
        // element-ui的样式不需要添加命名空间
        // if (css.includes('element-variables.scss')) return '';
        return "."+productName // 返回要添加的类名
      }
    }
  }
}
