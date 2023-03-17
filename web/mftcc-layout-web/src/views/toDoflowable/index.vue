<!--
<template>
  <div style="height: 100%; position: relative">
    <el-button type="primary"
               v-if="isManager && activeName == 'unfinished'"
               style="position: absolute; top: 8px; right: 24px; z-index: 1"
               @click="batchCommit">批量提交</el-button>
    <el-tabs v-model="activeName"
             @tab-click="handleClick"
             class="approvalList">
      <el-tab-pane label="待处理"
                   name="unfinished">
        <app :type="activeName"
             v-if="activeName == 'unfinished'"
             :isManager="isManager"
             @selection-change="selectionChange"
             @selection-bizMark="selectionBizMark"
             ref="app"></app>
      </el-tab-pane>
      <el-tab-pane label="已处理"
                   name="finished">
        <app :type="activeName"
             v-if="activeName == 'finished'"
             :isManager="isManager"
             @selection-change="selectionChange"
             @selection-bizMark="selectionBizMark"
             ref="app"></app>
      </el-tab-pane>
      <el-tab-pane label="我发起的"
                   name="started">
        <app :type="activeName"
             v-if="activeName == 'started'"
             @selection-change="selectionChange"
             @selection-bizMark="selectionBizMark"
             ref="app"></app>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import app from "@/views/toDoflowable/appList";
import api from "@/api/flowable/flowable";
export default {
  name: "approvalList",
  title: "审批中心",
  data() {
    return {
      activeName: "unfinished",
      isManager: false,
      selectionData: [],
      bizMark: ""
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
    batchCommit() {
      let arr = [];
      for (let i = 0; i < this.selectionData.length; i++) {
        let data2 = {};
        data2[this.selectionData[i].TASK_ID] = {
          traceNo: this.selectionData[i].TRACE_NO
        };
        arr.push(data2);
      }
      let data = {
        list: JSON.stringify(arr),
        bizMark: this.bizMark,
        approvalContents: "同意",
        approveBtnId: "6",
        currentUser: this.$store.getters.user.opNo
      };
      api.batchComplete(data, res => {
        if (res.code == 0) {
          this.$message.success("批量审批成功！");
          this.$refs.app.submitSuccess();
        } else {
          this.$alert(res.msg, "提示", {
            type: "error",
            dangerouslyUseHTMLString: true
          });
        }
      });
    },
    selectionChange(rows) {
      this.selectionData = rows;
    }
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
</style>
-->
