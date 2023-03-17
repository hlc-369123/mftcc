<!-- 业务申请详情页面右侧信息变更记录 -->
<template>
  <div style="padding: 10px">
    <el-row style="margin-bottom: 15px">
      <!-- <span>信息变更记录</span> -->
      <el-link
        :underline="false"
        style="color: #4a7ec3; font-weight: bold; font-size: 18px"
        >历史完结任务</el-link
      >
    </el-row>
    <el-row class="setBottom">
      <el-col :span="8" style="color: #3d3d3d; font-size: 14px; text-align: center">
        历史成交金额
        <el-row class="setTop">
          <span style="font-size: 24px; color: #141414">0.00</span>
        </el-row>
      </el-col>
      <el-col :span="8" style="color: #3d3d3d; font-size: 14px; text-align: center">
        历史成交次数
        <el-row class="setTop">
          <span style="font-size: 24px; color: #141414">0</span>
        </el-row>
      </el-col>
      <el-col :span="8" style="color: #3d3d3d; font-size: 14px; text-align: center">
        逾期情况
        <el-row class="setTop">
          <span style="font-size: 18px; color: #141414">无</span>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    // 这里存放数据
    return {
      listTemp: [],
    };
  },
};
</script>
<style scoped>
.setTop {
  margin-top: 5px;
}
.setBottom {
  margin-bottom: 10px;
}
</style>
