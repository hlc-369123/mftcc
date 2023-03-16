var config = {
  //浏览器页签显示描述
  title: "微前端基座项目",
  //浏览器页签图标
  icon: "/mftcc-base-web/static/images/header/logo_wj.png",
  host: {
    //后台API地址（gateway地址）
    gateway_path: "http://192.168.255.1:7019/",
    /* 单点登录服务地址 */
    sso_server: "http://192.168.255.1:8099/oauth.login",
    /* 服务标识 */
    sso_client_id: "normal-biz",
    /* 是否启用单点登录 */
    ssoSts: true
  },
  //表单配置文件地址---页面展现时读取表单配置文件
  //eg: http://ip:port/服务名/static/data

  form_profile_path: "http://192.168.255.1:9998/mftcc-base-web/web/mftcc-layout-web/static/data",
  //路由白名单---不会验证token不会被拦截
  // router_white_list: ["/login"],
  router_white_list: ["/login", "/wkfDesginer", "/wkfCharts", "/sp"],
  //要注册的前端项目
  web_path: [
    "mftcc-layout-web",
    "mftcc-sys-web",
    "mftcc-example-web"
  ],
  //api中的后台服务名称配置; 用法： $servers.plt
  /* const api = {
    brFindByPage: (data, success) => {
      postJson($servers.plt + "/sys/sysDept/findByPage", data, true, success);
    },
  }; */
  servers: {
    sys: "mftcc-sys-server", //平台基础后台
    auth: "mftcc-auth-server", //授权平台
    flowable: "mftcc-flowable-server", //流程基础后台
    rule: "http://192.168.255.1:9999" //规则引擎
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
  //首页是否隐藏菜单
  index_menu_show: true,
  //数据传输是否加密
  data_crypto: true,
  //独立页面路由名单，不展示头部和菜单栏
  // router_standalone_list: ["/mftcc-sys-web/demo/CURD/list"]
  //是否显示表单/列表的title(默认：true)
  show_title: true,
  //是否启用水印(默认：false)
  watermark: false,
  //是否在地址栏中拼接base-web名称
  show_base_path: true,
  //主题
  theme: "blue"
};

(function (window) {
  window.config = config;
})(this);
