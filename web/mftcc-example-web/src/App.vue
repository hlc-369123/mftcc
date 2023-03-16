<template>
  <div id="app" :class="className">
    <keep-alive>
      <router-view />
    </keep-alive>
  </div>
</template>

<script>
const packageConfig = require("../package.json");
export default {
  name: "App",
  data() {
    return {
      className: packageConfig.name,
    };
  },
};
</script>
