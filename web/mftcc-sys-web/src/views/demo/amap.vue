<template>
  <div id="container" style="width:100%;height:100%"></div>
</template>
<script>
// import AMapLoader from "@amap/amap-jsapi-loader";
export default {
  data() {
    return {
      map: null
    };
  },
  created() {
    // AMapLoader.load({
    //   //首次调用 load
    //   key: "608d75903d29ad471362f8c58c550daf", //首次load key为必填
    //   version: "2.0",
    //   plugins: []
    // })
    //   .then(AMap => {
    //     map = new AMap.Map("container");
    //   })
    //   .catch(e => {
    //     console.error(e);
    //   });
  },
  mounted() {
    debugger;
    this.map = new AMap.Map("container");
  }
};
</script>
