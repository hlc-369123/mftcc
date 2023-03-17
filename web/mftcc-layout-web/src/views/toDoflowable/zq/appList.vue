<template>
  <div style="display: flex; height: 100%">
    <div class="appList-content">
      <zq-user-list ref="flowableList" :type="type" @command="command"></zq-user-list>
      <!--  <mftcc-table v-if="showTaskList"
                   :tableId="tableId"
                   :parentVm="this"
                   :initOption="initData"
                   :initSearchData="initSearchData"
                   @selection-change="$emit('selection-change', $event)"
                   ref="list"></mftcc-table> -->
    </div>
    <app-detail
      :show.sync="appDetailShow"
      :appData="appData"
      @submitSuccess="submitSuccess"
      :query="query"
    ></app-detail>
    <el-dialog title="选择处理人" append-to-body :visible.sync="showAssignedDialog">
      <select-br-user @current="setUser"></select-br-user>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showAssignedDialog = false">取 消</el-button>
        <el-button type="primary" @click="selectUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import zqUserList from "@/views/toDoflowable/zq/ZqUserList";
import api from "@/api/flowable/flowable";
export default {
  name: "approvalList",
  title: "审批中心",
  data() {
    return {
      tableId: "flowable/taskList",
      bizMark: "",
      menuData: [],
      initData: {},
      initSearchData: {},
      dynamicQuery: "",
      showTaskList: false,
      appDetailShow: false,
      appData: {},
      showAssignedDialog: false,
      userData: null,
      query: false,
    };
  },
  components: {
    zqUserList,
  },
  props: ["type", "isManager"],
  mounted() {},
  methods: {
    command(data) {
      let type = data.type;
      let item = data.item;
      if ("app" == type) {
        item.finishType = "0";
        this.appData = item;
        this.query = false;
        this.appDetailShow = true;
      } else if ("query" == type) {
        item.finishType = "1";
        this.appData = item;
        this.query = true;
        this.appDetailShow = true;
      } else if ("design" == type) {
        item.finishType = "0";
        this.appData = item;
        this.showAssignedDialog = true;
      } else if ("flowCancel" == type) {
        this.flowCancel("", item);
      } else if ("flowRecall" == type) {
        this.flowRecall("", item);
      }
    },
    submitSuccess() {
      this.$refs.flowableList.refresh();
    },
    setUser(data) {
      this.userData = data;
    },
    selectUser() {
      let name = this.userData.opName;
      name = name.substring(name.lastIndexOf("/") + 1);
      let data = {
        id: this.appData.ID,
        taskId: this.appData.TASK_ID,
        bizMark: this.appData.BIZ_ID,
        userId: this.userData.opNo,
        userName: name,
        type: this.appData.TASK_TYPE,
        opNo: this.$store.getters.user.opNo,
      };
      api.designate(data, (res) => {
        if (res.code == 0) {
          this.$message.success("指派成功！");
          this.userData = null;
          this.submitSuccess();
          this.showAssignedDialog = false;
        } else {
          this.$alert(res.msg, "提示", {
            type: "error",
            dangerouslyUseHTMLString: true,
          });
        }
      });
    },
    flowRecall(index, row) {
      api.recallAndCancel(
        {
          taskId: row.TASK_ID,
          bizMark: row.BIZ_ID,
          type: "0",
          userId: row.ASSIGNEE,
        },
        (res) => {
          if (res.code == 0) {
            this.$message.success(res.msg);
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
          }
        }
      );
    },
    flowCancel(index, row) {
      api.recallAndCancel(
        {
          taskId: row.TASK_ID,
          bizMark: row.BIZ_ID,
          type: "1",
          userId: row.ASSIGNEE,
        },
        (res) => {
          if (res.code == 0) {
            this.$message.success(res.msg);
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
          }
        }
      );
    },
  },
};
</script>
<style>
.app-menu {
  border-right: 1px solid #dfdfdf;
  width: calc(12% - 2px);
  display: inline-block;
  box-sizing: border-box;
}
.appSearch {
  padding: 10px 20px;
}
.appSearch .el-input__suffix {
  display: flex;
  align-items: center;
  width: 20px;
  font-size: 16px;
  cursor: pointer;
}
.busTypeMenu > div {
  height: 50px;
  font-size: 14px;
  display: flex;
  align-items: center;
  padding: 0 15px;
  justify-content: space-between;
  cursor: pointer;
}
.busTypeMenu > div > span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.busTypeMenu > div:hover,
.busTypeMenu > div.active {
  background: #f5f5f5;
}
.busTypeMenu > div > .el-badge {
  height: 20px;
  /* width: 20px; */
}
.busTypeMenu > div > .el-badge.finished > .el-badge__content {
  background: transparent;
  color: #c0c4cc;
  font-size: 14px;
  border: none;
}
.appList-content {
  padding: 0 !important;
  width: 100% !important;
  box-sizing: border-box;
  flex-grow: 1;
  display: flex;
  background: #ececec;
}
.appList-content .mftcc-table .el-table th {
  background: #fff;
  border: none;
  border-bottom: 1px solid #dfdfdf;
}
.appList-content .mftcc-table .el-pagination {
  background: #fff;
}
.appList-content .mftcc-table .el-table__body tr td {
  padding: 10px 0;
}
.appList-content .mftcc-table .el-table__body tr:nth-child(2n) {
  background: transparent;
}
@media screen and (max-width: 1440px) {
  .busTypeMenu > div {
    padding: 0 2px;
  }
  .appSearch {
    padding: 5px;
  }
  .appList-content {
    padding: 12px 0px;
  }
}
</style>
