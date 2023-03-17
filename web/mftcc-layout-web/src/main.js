import singleSpaVue from "single-spa-vue";
import App from "./App";
import { setPublicPath } from "systemjs-webpack-interop";
const packageConfig = require("../package.json");
setPublicPath(packageConfig.name, 3);
const router = window.$initRouter($productName, require(`@/router/top.js`).default, require(`@/router/auto.js`).default);
const vueOptions = {
  el: "#root",
  router: router,
  render: function (createElement) {
    if (
      "-ms-scroll-limit" in document.documentElement.style &&
      "-ms-ime-align" in document.documentElement.style
    ) {
      window.addEventListener(
        "hashchange",
        () => {
          var currentPath = window.location.hash.slice(1);
          if (this.$route.path !== currentPath) {
            this.$router.push(currentPath);
          }
        },
        false
      );
    }
    return createElement(App);
  }
};

if (!window.singleSpaNavigate) {
  // 如果不是single-spa模式
  delete vueOptions.el;
  new Vue(vueOptions).$mount("#app");
}

const vueLifecycles = singleSpaVue({
  Vue,
  appOptions: vueOptions
});

/**
 * 判断是否是pc端
 */
function IsPC() {
  var userAgentInfo = navigator.userAgent;
  var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPod"];
  var flag = true;
  for (var v = 0; v < Agents.length; v++) {
    if (userAgentInfo.indexOf(Agents[v]) > 0) {
      flag = false;
      break;
    }
  }
  if (window.screen.width >= 768) {
    flag = true;
  }
  return flag;
}
Vue.prototype.$model = "pc";
if (!IsPC()) {
  require("@/assets/css/mobile.css");
  Vue.prototype.$model = "mobile";
}

import bizTimeLine from "@/views/toDoflowable/timeline/bizTimeLine.vue";
import timeLine from "@/views/toDoflowable/timeline/timeLine.vue";
import appDetail from "@/views/toDoflowable/detail/appDetail.vue";
import appDetailPage from "@/views/toDoflowable/detail/appDetailPage.vue";
import wkfUserDialogForDesign from "@/views/toDoflowable/detail/wkfUserDialogForDesign.vue";
import selectBrUser from "@/views/toDoflowable/detail/selectBrUser.vue";

Vue.component("bizTimeLine", bizTimeLine);
Vue.component("timeLine", timeLine);
Vue.component("appDetail", appDetail);
Vue.component("appDetailPage", appDetailPage);
Vue.component("wkfUserDialogForDesign", wkfUserDialogForDesign);
Vue.component("selectBrUser", selectBrUser);

export const bootstrap = vueLifecycles.bootstrap; // 启动时
export const mount = vueLifecycles.mount; // 挂载时
export const unmount = vueLifecycles.unmount; // 卸载时
