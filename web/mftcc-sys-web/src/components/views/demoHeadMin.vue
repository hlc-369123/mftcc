<template>
  <div>
    <el-row>
      <el-col :span="5" class="mftcc-details-header-img">
        <img src="@/assets/demo/title.jpg" style="height: 64px; width: 64px" />
      </el-col>
      <el-col :span="19">
        <div>
          <el-row style="margin: 30px 0px 10px; font-size: 13px" class="iClass">
            <span style="font-size: 18px; line-height: 25px; color: #4a7ec3">
              XXX公司
            </span>
            <i class="el-icon-phone"></i>
            "未登记"
            <i class="el-icon-postcard"></i>
            "未登记"
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "cusHead",
  data() {
    return {
      disabled: true,
      cusClassifyList: [],
      email: "",
      evalDetail: {},
      monitorStateName: "",
      monitorStateType: "",
      formShow: false,
    };
  },
  props: ["customerInfo", "detailInfo", "cusNo"],
  // 监控customerInfo中的数据变化
  created() {
    console.log("cusHead", this.customerInfo);
    console.log("cusHead", this.detailInfo);
    console.log("cusHead", this.cusNo);
  },
};
</script>
<style scoped>
.iClass i:not(:first-child) {
  margin-left: 15px;
}
</style>
