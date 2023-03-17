<template>
  <el-tree
    element-loading-text="加载中"
    :data="brData"
    highlight-current
    node-key="id"
    :expand-on-click-node="false"
    :default-expanded-keys="expanded"
    @node-click="handleNodeClick"
    style="height: 350px; overflow: auto"
  ></el-tree>
</template>
<script>
import api from "@/api/flowable/flowable";
export default {
  data() {
    return {
      brData: [],
      expanded: []
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      api.getBrTree(null, res => {
        if (res.code == 0) {
          this.brData = res.data;
          if (this.brData.length > 0) {
            this.expanded.push(this.brData[0].id);
          }
        } else {
          this.$alert(res.msg, "错误", {
            type: "error",
            dangerouslyUseHTMLString: true
          });
        }
      });
    },
    handleNodeClick(data, node) {
      let text = "";
      text = this.getParentsLabel(node, text);
      if (text.startsWith("/")) {
        text = text.substring(1);
      }
      let currentData = this.clone(data);
      currentData.label = text;
      this.$emit("current", currentData);
    },
    getParentsLabel(node, text) {
      let label = node.label;
      if (label) {
        text = "/" + label + text;
        let parent = node.parent;
        text = this.getParentsLabel(parent, text);
      }
      return text;
    },
    clone(data) {
      return JSON.parse(JSON.stringify(data));
    }
  }
};
</script>
