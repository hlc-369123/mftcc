<template>
  <div style="height: 100%; position: relative;background: #FFF;">
    <div class="flowable-search">
      <el-input size="small"
                placeholder="请输入查询内容"
                v-model="dynamicQuery"
                @keyup.enter.native="searchFlowableList">
        <i slot="prefix"
           class="el-input__icon el-icon-search"
           style="cursor: pointer;"
           @click="$router.back(-1)"></i>
      </el-input>
    </div>
    <div class="flowable-title">
      <i class="el-icon-arrow-left goBack"></i>
      <span style="margin: 0 20px 0 15px;font-weight: bold;">流程工作台</span>
    </div>
    <el-tabs v-model="activeName"
             @tab-click="handleClick"
             class="approvalList">

      <el-tab-pane name="unfinished">
        <el-button slot="label"
                   plain>待处理</el-button>
        <app :type="activeName"
             v-if="activeName == 'unfinished'"
             :isManager="isManager"
             @selection-change="selectionChange"
             @selection-bizMark="selectionBizMark"
             ref="app"></app>
      </el-tab-pane>
      <el-tab-pane name="finished">
        <el-button slot="label"
                   plain>已处理</el-button>
        <app :type="activeName"
             v-if="activeName == 'finished'"
             :isManager="isManager"
             @selection-change="selectionChange"
             @selection-bizMark="selectionBizMark"
             ref="app"></app>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import app from "@/views/toDoflowable/zq/appList";
export default {
  name: "approvalList",
  title: "审批中心",
  data() {
    return {
      activeName: "unfinished",
      isManager: false,
      selectionData: [],
      bizMark: "",
      dynamicQuery: ""
    };
  },
  provide() {
    return {
      dynamicQuery: this.dynamicQuery
    };
  },
  components: {
    app
  },
  created() {
    let opNo = this.$store.getters.user.opNo;
    if (opNo == "dongguoyong" || opNo == "yuanlaizhong" || opNo == "zhoujun") {
      this.isManager = true;
    }
  },
  methods: {
    selectionBizMark(bizMark) {
      this.bizMark = bizMark;
    },
    selectionChange(rows) {
      this.selectionData = rows;
    },
    searchFlowableList() { }
  }
};
</script>
<style>
.approvalList,
.approvalList .el-tab-pane {
  height: 100%;
}
.approvalList .el-tabs__content {
  height: calc(100% - 50px);
}
.approvalList .el-tabs__header {
  margin: 0;
}
.approvalList .el-tabs__nav {
  height: 50px;
  margin-left: 20px;
}
.approvalList .el-tabs__item {
  height: 50px;
  line-height: 50px;
  width: 80px;
  text-align: center;
  box-shadow: none !important;
}
#tab-unfinished {
  margin-left: 145px;
}
.flowable-search {
  position: absolute;
  right: 15px;
  top: 10px;
  z-index: 10;
}
.approvalList .el-tabs__active-bar,
.approvalList .el-tabs__nav-wrap::after {
  display: none;
}
.flowable-title {
  position: absolute;
  left: 0;
  top: 13px;
  z-index: 10;
}
.flowable-title .goBack {
  font-size: 20px;
  font-weight: bold;
  height: 20px;
  width: 40px;
  line-height: 20px;
  border-right: 1px solid #f1f1f1;
  text-align: center;
  color: #0a7cff;
  cursor: pointer;
}
.approvalList .is-active .el-button,
.approvalList button.el-button.el-button--default:hover,
.approvalList button.el-button.el-button--default:focus {
  border-color: #409eff;
  background-color: #fff;
  color: #409eff;
}
</style>
