<template>
  <div style="padding: 10px">
    <el-row type="flex">
      <el-col :span="24" class="headRight">
        客户共有申请业务<a>2</a>笔，在履行合同<a>2</a>笔，在履行借据<a>2</a>笔，为他人担保<a>2</a>笔，已结清借据<a>2</a>笔，已还款<a>2</a>次
      </el-col>
    </el-row>
    <el-row type="flex">
      <el-col :span="22" style="display: flex">
        <img class="touxiang" src="@/assets/demo/title.jpg" />
        <div class="headInfo">
          <div>
            <span class="headName">襄垣县康源食品有限责任公司</span>
            <img src="@/assets/demo/Group 29.png" style="height: 14px; width: 14px" />
            <span>18637752336</span>
            <img src="@/assets/demo/Group 41.png" style="height: 14px; width: 14px" />
            <span>911402197802065432</span>
          </div>
          <div>
            <el-button class="headBtn success" type="success" round
              ><img
                src="@/assets/demo/Group 38.png"
                style="height: 14px; width: 14px"
              />普通客户</el-button
            >
            <el-button class="headBtn danger" type="danger" round
              ><img
                src="@/assets/demo/Group 37.png"
                style="height: 14px; width: 14px"
              />完整度5%</el-button
            >
            <el-button
              class="headBtn success"
              type="success"
              round
              v-if="$hasPerm('head:associated')"
              ><img
                src="@/assets/demo/Group 36.png"
                style="height: 14px; width: 14px"
              />关联关系</el-button
            >
            <el-button class="headBtn info" type="info" round @click="pinggu"
              ><img
                src="@/assets/demo/Group 35.png"
                style="height: 14px; width: 14px"
              />未评估</el-button
            >
            <el-button
              class="headBtn warning"
              type="warning"
              round
              v-if="$hasPerm('head:credit')"
              ><img
                src="@/assets/demo/Group 34.png"
                style="height: 14px; width: 14px"
              />授信中</el-button
            >
            <el-button class="headBtn info" type="info" round @click="daihou"
              ><img
                src="@/assets/demo/Group 33.png"
                style="height: 14px; width: 14px"
              />贷后检查</el-button
            >
            <el-button class="headBtn danger" type="danger" round
              ><img
                src="@/assets/demo/Group 32.png"
                style="height: 14px; width: 14px"
              />业务拒绝</el-button
            >
            <!-- <el-button class="headBtn zhankai"
              ><img
                src="@/assets/demo/Group 21.png"
                style="height: 14px; width: 14px; cursor: pointer"
              />展开</el-button
            > -->
          </div>
        </div>
      </el-col>
      <el-col :span="2">
        <img class="touxiangType" src="@/assets/demo/over.jpg" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "demoHead",
  title: "demoHead",
  data() {
    return {};
  },
  props: ["aaa", "bbbb", "ccc"],
  created() {
    console.log("demoHead", this.aaa);
    console.log("demoHead", this.bbbb);
    console.log("demoHead", this.ccc);
  },
  methods: {
    pinggu() {
      $bus.$emit("pinggu");
    },
    daihou() {
      $bus.$emit("daihou");
    },
  },
};
</script>
<style>
.headRight {
  font-size: 12px;
  text-align: right;
}
.headRight > a {
  font-size: 14px;
  color: #4b7dc4;
  font-weight: 700;
  margin: 0 1px;
  text-decoration: underline;
}
.touxiang {
  border-radius: 50%;
  height: 84px;
  width: 84px;
  display: inline;
  margin-right: 12px;
  border: 1px solid #4a7ec3;
}
.headInfo {
  display: inline-block;
}
.headInfo > div {
  height: 42px;
  line-height: 42px;
}
.touxiangType {
  float: right;
  height: 84px;
  width: 84px;
}
.headInfo span {
  font-size: 14px;
  color: #000;
  margin-right: 16px;
}
.headInfo img {
  height: 14px;
  width: 14px;
  margin-right: 3px;
}
.headInfo span.headName {
  color: #4a7ec3;
  font-size: 18px;
  margin-right: 23px;
  font-weight: 700;
}
.headBtn {
  height: 24px;
  line-height: 23px;
  width: 98px;
  padding: 0 !important;
  font-size: 14px;
  color: #fff;
  margin-left: 0 !important;
  margin-right: 8px;
}
.headBtn span {
  /* display: flex;
  justify-content: center;
  align-items: center; */
  color: #fff;
  margin: 0 auto;
}
.headBtn.success {
  background: #31b5cb;
  border-color: #31b5cb;
}
.headBtn.danger {
  background: #e14a48;
  border-color: #e14a48;
}
.headBtn.info {
  background: #d3d3d3;
  border-color: #d3d3d3;
}
.headBtn.warning {
  background: #fcb864;
  border-color: #fcb864;
}
.headBtn.zhankai {
  background: transparent;
  border-color: transparent;
}
.headBtn.zhankai span {
  color: #4a7ec3;
}
</style>
