import singleSpaVue from "single-spa-vue";
import { router } from 'mftcc-router-npm';
import App from './App';
import { setPublicPath } from 'systemjs-webpack-interop'
const packageConfig = require('../package.json')
setPublicPath(packageConfig.name);
import spaData from './export-spa.js';

const vueOptions = {
  el: '#microApp',
  router: router,
  render: function (createElement) {
    if ('-ms-scroll-limit' in document.documentElement.style && '-ms-ime-align' in document.documentElement.style) {
      window.addEventListener('hashchange', () => {
        var currentPath = window.location.hash.slice(1)
        if (this.$route.path !== currentPath) {
          this.$router.push(currentPath)
        }
      }, false)
    }
    return createElement(App);
  }
};

if (!window.singleSpaNavigate) { // 如果不是single-spa模式
  delete vueOptions.el;
  new Vue(vueOptions).$mount('#app');
}

const vueLifecycles = singleSpaVue({
  Vue,
  appOptions: vueOptions
});

const microApp = document.getElementById("microApp");
if (microApp != null) {
  microApp.className = packageConfig.name;
}

export const bootstrap = vueLifecycles.bootstrap; // 启动时
export const mount = vueLifecycles.mount; // 挂载时
export const unmount = vueLifecycles.unmount; // 卸载时
export default spaData;
