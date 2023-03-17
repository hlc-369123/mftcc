<template>
  <router-view v-if="
      $route.path == '/' ||
        $route.path == '/login' ||
        $route.path == '/dev' ||
        $route.path == '/doc' ||
        $route.path == '/appPage'
    "
               :key="$route.path"></router-view>
  <component v-else
             :is="main"
             :fixMenuList="fixMenuList"
             :url="url"
             @menuClick="menuClick"
             @fixMenuClick="fixMenuClick"
             @init="init"
             ref="mftccMain">
  </component>
  <!-- <mftcc-main
    v-else
    :fixMenuList="fixMenuList"
    :url="url"
    @menuClick="menuClick"
    @fixMenuClick="fixMenuClick"
    @init="init"
    ref="mftccMain"
  ></mftcc-main> -->
</template>

<script>
// import mftccMain from "@/layout/blue/main";
// import "./theme/blue/index.css";
export default {
  name: "appDetail",
  data() {
    return {
      fixMenuList: [],
      url: {
        signOut: "/" + $servers.sys + "/loginOut",
        validatePassword: "/" + $servers.sys + "/sys/sysUser/validatePassword",
        validateNewPassword: "/" + $servers.sys + "/sys/sysSecAuditConfig/validatePWAjax",
        updatePassword: "/" + $servers.sys + "/sys/sysUser/updatePassword"
      },
      main: null
    };
  },
  created() {
    /* 主题 */
    this.main = require(`@/layout/${this.$theme}/main`).default;
    require(`@/theme/default/index.css`);
    if (this.$theme != "default") {
      require(`@/theme/${this.$theme}/index.css`);
    }
  },
  // components: {
  //   mftccMain
  // },
  watch: {
    "$route.path": {
      handler(val) {
        if (val == "/login") {
          this.$watermark.close();
        } else {
          this.createWaterMark();
        }
      },
      immediate: true
    }
  },
  methods: {
    createWaterMark() {
      let user = this.$store.getters["user"];
      const year = new Date().getFullYear();
      const month = new Date().getMonth() + 1 < 10 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1;
      const date = new Date().getDate() < 10 ? "0" + new Date().getDate() : new Date().getDate();
      const hh = new Date().getHours() < 10 ? "0" + new Date().getHours() : new Date().getHours();
      const mm = new Date().getMinutes() < 10 ? "0" + new Date().getMinutes() : new Date().getMinutes();
      const ss = new Date().getSeconds() < 10 ? "0" + new Date().getSeconds() : new Date().getSeconds();
      const week = new Date().getDay();
      let nowTime = year + "-" + month + "-" + date + " " + hh + ":" + mm + ":" + ss;
      this.$watermark.init(user.opName + " " + nowTime);
    },
    init() {
      this.fixMenuList = require(`#/json/fixMenu.json`);
    },
    fixMenuClick(type) {
      switch (type) {
        case "fixMenu":
          console.log("菜单点击事件");
          break;
      }
    },
    //右上菜单点击事件回调
    menuClick(type) {
      switch (type) {
        case "user":
          console.log("用户点击事件");
          break;
        case "search":
          console.log("查询点击事件");
          break;
        case "notice":
          this.$router.pushMain("/mftcc-msg-web/detail/msgDetails");
          this.$refs.mftccMain.reload();
          break;
        case "approval":
          this.$router.push("/toDoflowable/index");
          this.$refs.mftccMain.reload();
          break;
      }
    }
  }
};
</script>
<style>
.mftcc-form .el-input.is-disabled .el-input__inner {
  color: #22252b;
}
#nprogress .bar {
  background: #26578a;
}
</style>
