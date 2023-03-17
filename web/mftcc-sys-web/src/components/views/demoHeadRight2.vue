<template>
  <div style="padding: 10px">
    <el-row type="flex">
      <el-col :span="24" style="display: flex">
        <img class="shenqingxinxi" src="@/assets/demo/danbao.jpg" />
        <div class="shenqingInfo">
          <div>
            <span class="yingshou">添加担保信息</span>
          </div>
          <div style="height: 27px"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "demoHeadRight2",
  title: "demoHeadRight2",
  props: ["aaa", "bbb", "ccc"],
  data() {
    return {};
  },
  created() {
    console.log("aaa", this.aaa);
    console.log("bbb", this.bbb);
    console.log("ccc", this.ccc);
    debugger;
  },
};
</script>
<style>
.shenqingxinxi {
  width: 84px;
  height: 84px;
  margin-right: 12px;
}
.shenqingInfo {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.shenqingInfo > div {
  width: 100%;
}
.shenqingInfo span.yingshou {
  font-size: 18px;
  color: #4a7ec3;
  font-weight: 700;
}
</style>
