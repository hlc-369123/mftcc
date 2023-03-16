/**
 * 初始化组件库
 */
function initComponents() {
  /* 自动路由懒加载 */
  System.import("/" + $productName + "/static/libs/mf-components/mf-router.umd.min.js").then(function (res) {
    window.$initRouter = res.initRouter;
  });
  /* excel导出 */
  System.import("/" + $productName + "/static/libs/export/xlsx.full.min.js").then(function (res) {
    window.XLSX = res;
  });
}

/**
 * 初始化平台封装的基础库
 * @param {*} base 
 */
function initPltLib(base) {
  window.$component = base.component;
  window.$store = base.store;
  window.$hasPerm = base.hasBtnPermission;
  window.$axios = base.axios;
  window.$bus = base.bus;
  window.$flowableHandler = base.flowableHandler;
  window.$formUtil = base.formUtil;
  window.$sso = base.sso;

  /* 将配置文件放到上下文中 */
  for (let key in config) {
    if (window.config.router_white_list) {
      window.config.router_white_list = window.config.router_white_list.concat(window.config.router_standalone_list)
    }
    window["$" + key] = config[key];
    Vue.prototype["$" + key] = config[key];
  }

  Vue.use($component);
  Vue.prototype.$axios = $axios;
  Vue.prototype.$bus = $bus;
  Vue.prototype.$store = $store;
  Vue.prototype.$hasPerm = $hasPerm;
  Vue.prototype.$flowableHandler = $flowableHandler;
  Vue.prototype.$formUtil = $formUtil;
  Vue.prototype.$alertTitle = '提示'
  Vue.prototype.$sso = $sso;
  Vue.config.productionTip = false
  Vue.config.debug = true

  Promise.all([
    System.import('plt-dev'),
    System.import('flowable-dev')
  ]).then(function (res) {
    var formDev = res[0];
    var flowableDev = res[1];
    Vue.use(formDev.designer);
    Vue.use(flowableDev.flowCharts);
    var router = new VueRouter({
      base: $productName,
      mode: "history",
      routes: [
        { path: '/dev', redirect: '/dev/main' },
        {
          //设计器---登录
          path: '/dev/login',
          component: formDev.login
        },
        {
          //设计器---首页
          path: '/dev/main',
          component: formDev.dev
        },
        {
          //流程设计器
          path: '/flowable/dev',
          component: flowableDev.designer
        },
        {
          //流程图
          path: '/flowable/charts',
          component: flowableDev.charts
        }
      ]
    });
    router.beforeEach((to, from, next) => {
      if ((to.fullPath == "/dev/main" || to.fullPath == "/flowable/dev" || to.fullPath == "/flowable/charts") && !sessionStorage.getItem("mftcc_vuex")) {
        next("/");
      }
      var devDom = document.getElementById("dev");
      var rootDom = document.getElementById("root");
      if (to.path.indexOf("/dev") == 0 || to.path.indexOf("/flowable") == 0) {
        devDom.style.display = "block";
        rootDom.style.display = "none";
      } else {
        devDom.style.display = "none";
        rootDom.style.display = "block";
      }
      next();
    });
    new Vue({
      el: '#dev',
      router: router
    })
  });
  //初始化组件库
  initComponents();
}