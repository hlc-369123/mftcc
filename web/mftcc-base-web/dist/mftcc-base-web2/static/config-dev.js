var config = {
  //浏览器页签显示描述
  title: "微前端基座项目",
  //浏览器页签图标
  icon: "/mftcc-base-web/static/images/header/logo_wj.png",
  host: {
    //公共后台API地址（gateway地址），一般情况下所有微应用的请求地址都是这一个
    gateway_path: "http://127.0.0.1:7019",
    //自定义后台API地址：-----项目名：地址（用于测试，如某些特定服务需访问他人gateway地址时使用）
    // custom_gateway_path: {
    //   "mftcc-flowable-web": "http://192.168.110.183:8088",
    //   "mftcc-cus-web": "http://192.168.110.204:7019"
    // }
    /* 单点登录服务地址 */
    sso_server: "http://127.0.0.1:8099/oauth/login",
    /* 服务标识 */
    sso_client_id: "normal-biz",
    /* 是否启用单点登录 */
    sso_sts: false
  },
  //表单配置文件地址---页面展现时读取表单配置文件
  //eg: http://ip:port/服务名/static/data
  form_profile_path: "http://127.0.0.1:7008/mftcc-layout-web/static/data",
  //路由白名单---不会验证token不会被拦截
  router_white_list: ["/login", "/wkfDesginer", "/wkfCharts", "/sp"],
  //要注册的前端项目
  web_path: [
    {
      name: "mftcc-layout-web", //项目名称
      path: "http://127.0.0.1:7008" //项目地址
    },
    {
      name: "mftcc-example-web",
      path: "http://127.0.0.1:7001"
    },
    {
      name: "mftcc-mq-web", //消息中心
      path: "http://127.0.0.1:7077"
    },
    {
      name: "mftcc-sys-web",
      path: "http://127.0.0.1:7038"
    },
    {
      name: "vue-element-admin",
      path: "http://127.0.0.1:9527"
    },
    {
      name: "vue-admin-beautiful",
      path: "http://127.0.0.1:9582"
    },
    {
      name: "mftcc-doc-web",
      path: "http://127.0.0.1:7011"
    }
  ],
  //api中的后台服务名称配置; 用法： $servers.plt
  /* const api = {
    brFindByPage: (data, success) => {
      postJson($servers.sys + "/sys/sysDept/findByPage", data, true, success);
    },
  }; */
  servers: {
    sys: "mftcc-sys-server", //平台基础后台
    auth: "mftcc-auth-server", //授权平台
    flowable: "mftcc-flowable-server", //流程基础后台
    rule: "http://192.168.255.1:9998", //规则引擎
    example: "mftcc-example-server"
  },
  //表单设计器需要访问的后台服务名称（默认：mftcc-sys-server）
  designer_server: "mftcc-sys-server",
  //消息中心后台ip以及端口
  // mq: {
  //   host: "192.168.1.102",
  //   port: "8060"
  // }
  //首页路由地址
  index_router: "mftcc-base-web/main",
  //首页是否显示菜单
  index_menu_show: true,
  //数据传输是否加密
  data_crypto: false,
  //独立页面路由名单，不展示头部和菜单栏
  // router_standalone_list: ["/mftcc-sys-web/demo/form"]
  //是否显示表单/列表的title(默认：true)
  show_title: true,
  //是否启用水印(默认：false)
  watermark: false,
  //是否在地址栏中拼接base-web名称
  show_base_path: false,
  //主题
  theme: "blue"
};

(function (window) {
  window.config = config;
})(this);
