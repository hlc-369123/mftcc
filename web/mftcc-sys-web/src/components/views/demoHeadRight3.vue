<!-- 业务申请详情页面右侧信息变更记录 -->
<template>
  <div style="padding: 10px">
    <el-row style="margin-bottom: 15px">
      <!-- <span>信息变更记录</span> -->
      <el-link
        :underline="false"
        style="color: #4a7ec3; font-weight: bold; font-size: 18px"
        >信息变更记录</el-link
      >
    </el-row>
    <el-row class="setBottom" v-for="item in listTemp" :key="item.id">
      <el-col :span="24" style="color: #141414; font-size: 14px">
        {{ item.title }}
        <el-row class="setTop">
          <span style="font-size: 12px; color: #3d3d3d"
            >{{ item.opName }} {{ item.date }}</span
          >
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
      listTemp: [
        {
          id: 1,
          title: "操作员：李慧新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "李慧",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 2,
          title: "操作员：张令南新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张令南",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 3,
          title: "操作员：张云柯新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张云柯",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 4,
          title: "操作员：张云柯新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张云柯",
          date: "2020-10-26 10:21:48",
        },
      ],
    };
  },
  mounted() {
    $bus.$on("pinggu", () => {
      this.listTemp = [
        {
          id: 1,
          title: "操作员：李慧新增了负债信息，债权人 张云柯",
          opName: "李慧",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 2,
          title: "操作员：李慧新增了负债信息，债权人 张云柯",
          opName: "张令南",
          date: "2020-10-26 10:21:48",
        },
      ];
    });
    $bus.$on("daihou", () => {
      this.listTemp = [
        {
          id: 1,
          title: "操作员：李慧新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "李慧",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 2,
          title: "操作员：张令南新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张令南",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 3,
          title: "操作员：张云柯新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张云柯",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 4,
          title: "操作员：张云柯新增了职业信息，所在单位名称 ：北京微金时代",
          opName: "张云柯",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 5,
          title: "操作员：李慧新增了负债信息，债权人 张云柯",
          opName: "李慧",
          date: "2020-10-26 10:21:48",
        },
        {
          id: 6,
          title: "操作员：李慧新增了负债信息，债权人 张云柯",
          opName: "张令南",
          date: "2020-10-26 10:21:48",
        },
      ];
    });
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
