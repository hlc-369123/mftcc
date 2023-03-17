<template>
  <div>
    <div class="mftcc-details-header-bottom">
      <el-button icon="el-icon-edit-outline">联网核查</el-button>
      <el-button icon="el-icon-price-tag">客户标签</el-button>
      <el-button icon="el-icon-edit">信息完善</el-button>
      <el-button icon="el-icon-s-custom">关联关系</el-button>
      <el-button icon="el-icon-money">财务报表</el-button>
    </div>
  </div>
</template>
<script>
export default {
  name: "cusOperate",
  props: ["checkUseFlag", "refreshPage", "refreshCusHead"],
  data() {
    return {
      visible: false,
      title: "完善资料",
      disabled: true,
      tableShow: false,
      tableOption: {},
    };
  },
  created() {
    console.log("checkUseFlag", this.checkUseFlag);
    console.log("refreshPage", this.refreshPage);
    console.log("refreshCusHead", this.refreshCusHead);
  },
};
</script>
<style></style>
