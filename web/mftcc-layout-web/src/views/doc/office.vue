<template>
  <div :class="appClassName"
       style="height: 100%; overflow: auto">
    <component :is="page"
               ref="component"> </component>
  </div>
</template>
<script>
export default {
  data() {
    return {
      page: null,
      appClassName: null
    };
  },
  mounted() {
    /* window.addEventListener("single-spa:routing-event", evt => {
      var name = evt.detail.appsByNewStatus.MOUNTED[0];
      const packageConfig = require("../../../package.json");
      const productName = packageConfig.name;
      if (name == productName) {
        this.resAppPage("mftcc-doc-web/template/docTemplageModelConfig");
      }
    }); */
    this.resAppPage("mftcc-doc-web/template/docTemplageModelConfig");
  },
  methods: {
    resAppPage(pagePath) {
      this.page = null;
      const pathObj = this.resPath(pagePath);
      this.getApp(pathObj);
    },
    resPath(pagePath) {
      if (!pagePath) {
        return null;
      }
      pagePath = pagePath.replace(/\s+/g, "");
      if (/.vue$/.test(pagePath)) {
        pagePath = pagePath.substring(0, pagePath.length - 4);
      }
      if (pagePath.startsWith("/")) {
        pagePath = pagePath.substring(1);
      }
      let productName = pagePath.split("/")[0];
      let spaPath = pagePath.substring(productName.length);
      return { productName, spaPath };
    },
    getApp(pathObj) {
      if (!pathObj) {
        return;
      }
      this.$singleSpa
        .toLoadPromise(this.$singleSpa.getApp(pathObj.productName))
        .then(res => {
          res.loadApp().then(res => {
            const apps = res.default;
            const app = apps[pathObj.spaPath];
            this.page = app;
            this.appClassName = pathObj.productName;
          });
        });
    }
  }
};
</script>
