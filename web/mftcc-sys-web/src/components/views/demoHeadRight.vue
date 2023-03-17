<template>
  <div style="padding: 10px">
    <el-row type="flex">
      <el-col :span="24" style="display: flex">
        <img class="shenqingxinxi" src="@/assets/demo/shenqingxinxi.jpg" />
        <div class="shenqingInfo">
          <div>
            <span class="yingshou">应收账款融资</span>
          </div>
          <div>
            <img src="@/assets/demo/Group 40.png" />
            <span>{{ wan }}</span>
            <span class="houzui">万</span>
            <img src="@/assets/demo/Group 28.png" />
            <span>{{ yue }}</span>
            <span class="houzui">个月</span>
            <img src="@/assets/demo/Group 31.png" />
            <span>{{ baifenbi }}</span>
            <span class="houzui">%</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row type="flex">
      <el-col :span="24" class="headBottom">
        客户共有申请业务<a>2</a>笔，在履行合同<a>2</a>笔，在履行借据<a>2</a>笔，已结清借据<a>2</a>笔，已还款<a>2</a>次
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "demoHeadRight",
  title: "demoHeadRight",
  data() {
    return {
      wan: "1000.00",
      yue: "36",
      baifenbi: "15.30"
    };
  },
  props: {
    props: {
      type: Object,
      default() {
        return {};
      }
    },
    parentVm: {
      type: Object,
      required: true
    },
    isPreview: Boolean,
    paramData: {
      type: Object,
      default: function() {
        return {};
      }
    }
  },
  created() {
    this.$bus.$on("changeDemoHeadRightData", () => {
      if (this.wan == "1000.00") {
        this.wan = "500.00";
        this.yue = "24";
        this.baifenbi = "22.45";
      } else {
        this.wan = "1000.00";
        this.yue = "36";
        this.baifenbi = "15.30";
      }
    });
  }
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
.shenqingInfo span {
  color: #d97e12;
  font-size: 24px;
}
.shenqingInfo span.yingshou {
  font-size: 18px;
  color: #4a7ec3;
  font-weight: 700;
}
.shenqingInfo span.houzui {
  font-size: 14px;
  color: #3d3d3d;
  margin-right: 10px;
}
.shenqingInfo img {
  width: 18px;
  height: 18px;
  margin-right: 5px;
}
.headBottom {
  font-size: 12px;
  color: #3d3d3d;
  margin-top: 8px;
}
.headBottom > a {
  font-size: 14px;
  color: #4b7dc4;
  font-weight: 700;
  margin: 0 1px;
  text-decoration: underline;
}
</style>
