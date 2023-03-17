<template>
  <div style="display: flex; height: 100%">
    <div class="app-menu">
      <div class="appSearch">
        <el-input placeholder="请输入查询条件" v-model="dynamicQuery" size="mini">
          <i slot="suffix" class="el-icon-search" @click="search"></i>
        </el-input>
      </div>
      <div class="busTypeMenu">
        <div
          v-for="(menu, index) in menuData"
          :key="index"
          @click="showTable(menu)"
          :class="{ active: menu.bizMark == bizMark }"
        >
          <span>{{ menu.bizName }} </span>
          <el-badge
            v-if="menu.cut > 0"
            :value="menu.cut"
            :max="99"
            :class="type"
          ></el-badge>
        </div>
      </div>
    </div>
    <div class="appList-content">
      <mftcc-table
        v-if="showTaskList"
        :tableId="tableId"
        :parentVm="this"
        :initOption="initData"
        :initSearchData="initSearchData"
        @selection-change="$emit('selection-change', $event)"
        ref="list"
      ></mftcc-table>
    </div>
    <app-detail
      :show.sync="appDetailShow"
      :title="appData.TASK_NAME"
      :appData="appData"
      @submitSuccess="submitSuccess"
      :query="query"
    ></app-detail>
    <el-dialog title="选择处理人" append-to-body :visible.sync="showAssignedDialog">
      <select-br-user @current="setUser"></select-br-user>
      <el-form
        label-position="right"
        label-width="80px"
        :model="designForm"
        :rules="designRules"
        class="designForm"
        ref="designForm"
      >
        <el-form-item label="原因" prop="reason">
          <el-input
            type="textarea"
            v-model="designForm.reason"
            resize="none"
            :autosize="{ minRows: 3, maxRows: 10 }"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showAssignedDialog = false">取 消</el-button>
        <el-button type="primary" @click="selectUser">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="操作原因" append-to-body :visible.sync="showReasonDialog">
      <el-form
        label-position="right"
        label-width="80px"
        :model="reasonForm"
        :rules="designRules"
        class="reasonForm"
        ref="reasonForm"
      >
        <el-form-item label="原因" prop="reason">
          <el-input
            type="textarea"
            v-model="reasonForm.reason"
            resize="none"
            :autosize="{ minRows: 3, maxRows: 10 }"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showReasonDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitReason">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
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
      designForm: {
        reason: "",
      },
      designRules: {
        reason: [{ required: true, message: "请填写原因", trigger: "blur" }],
      },
      showReasonDialog: false,
      reasonForm: {
        reason: "",
      },
      recallAndCancelType: null,
      recallAndCancelData: {},
    };
  },
  props: ["type", "isManager"],
  mounted() {
    this.getBizList();
  },
  methods: {
    search() {
      let queryType = "task";
      if (this.type == "finished") {
        queryType = "hisTask";
      }
      let searchData = {
        dynamicQuery: this.dynamicQuery,
        bizMark: this.bizMark,
        queryType: queryType,
        opNo: this.$store.getters.user.opNo,
      };
      this.$refs.list.search(searchData);
    },
    getBizList() {
      this.menuData = [];
      api.getBizList(
        {
          opNo: this.$store.getters.user.opNo,
          type: this.type,
        },
        (res) => {
          if (res.code == 0) {
            let tabData = [];
            let data = res.list;
            let countList = res.countList;
            let busTypeData = null;
            let unfinishedCount = 0;
            if (data.length > 0) {
              for (let item of data) {
                item.cut = countList[item.bizMark];
                if (item.cut > 0) {
                  if (item.bizMark == this.bizMark) {
                    busTypeData = item;
                  }
                  tabData.push(item);
                  if (this.type == "unfinished") {
                    unfinishedCount += parseInt(item.cut);
                  }
                }
              }
            }

            if (this.type == "unfinished") {
              this.$bus.$emit("setCountForToDoTask", unfinishedCount);
            }

            this.menuData = tabData;
            if (busTypeData) {
              this.showTable(busTypeData);
            } else {
              if (this.menuData.length > 0) {
                this.showTable(this.menuData[0]);
              } else {
                this.$refs.list.refresh();
              }
            }
          } else {
            this.$message.error(res.msg);
          }
        }
      );
    },
    async showTable(menu) {
      let queryType = "task";
      if (this.type == "finished") {
        queryType = "hisTask";
        this.tableId = "flowable/hisTaskList";
      }
      this.bizMark = menu.bizMark;
      this.$emit("selection-bizMark", this.bizMark);
      this.initData = {};
      this.initSearchData = {
        dynamicQuery: this.dynamicQuery,
        bizMark: this.bizMark,
        queryType: queryType,
        opNo: this.$store.getters.user.opNo,
      };
      let jsonData = await axios.get(
        this.$formUtil.getFormProfilePath() + "/table/" + this.tableId + ".json"
      );
      let columns = jsonData.data.columns;
      api.getColList(this.bizMark, (res) => {
        if (res.code == 0) {
          for (let i = columns.length - 1; i >= 0; i--) {
            if (columns[i].tmp === true) {
              columns.splice(i, 1);
            }
          }
          for (let i in res.list) {
            let optionType = "";
            if (res.list[i].dicKey != "") {
              optionType = "2";
            }
            let cnWidth = res.list[i].cnWidth;
            if (cnWidth) {
              if (typeof cnWidth === "number" && cnWidth % 1 === 0) {
                cnWidth += "px";
              }
            } else {
              cnWidth = "";
            }
            let col = {
              label: res.list[i].cnName,
              prop: res.list[i].name,
              width: cnWidth,
              align: "center",
              isShow: true,
              dicKey: res.list[i].dicKey,
              optionType: optionType,
              tmp: true,
            };
            let unit = res.list[i].unit;
            if (unit) {
              col.fieldType = "text";
              col.suffix = unit;
            }
            columns.splice(i, 0, col);
          }
          this.initData.columns = columns;
          if (this.isManager) {
            this.initData.isSelect = true;
          }
          // list.initTable();
          this.showTaskList = false;
          this.$nextTick(() => {
            this.showTaskList = true;
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    app(index, row) {
      row.finishType = "0";
      this.appData = row;
      this.query = false;
      this.appDetailShow = true;
      // let $query = {
      //   procId: row.PROC_INST_ID,
      //   bizMark: row.BIZ_ID,
      //   taskId: row.TASK_ID,
      //   taskDefId: row.TASK_DEF_ID,
      //   traceNo: row.TRACE_NO,
      //   finishType: row.finishType,
      // }
      // this.$router.push({ path: "/appPage", query: $query });
    },
    apphist(index, row) {
      row.finishType = "1";
      this.appData = row;
      this.query = true;
      this.appDetailShow = true;
    },
    submitSuccess() {
      this.getBizList();
    },
    design(index, row) {
      row.finishType = "0";
      this.appData = row;
      this.showAssignedDialog = true;
      this.$nextTick(() => {
        this.$refs.designForm.resetFields();
      });
    },
    setUser(data) {
      this.userData = data;
    },
    selectUser() {
      this.$refs.designForm.validate((valid) => {
        if (valid) {
          //指派原因
          let reason = this.designForm.reason;
          let name = this.userData.opName;
          name = name.substring(name.lastIndexOf("/") + 1);
          let data = {
            id: this.appData.ID,
            taskId: this.appData.TASK_ID,
            bizMark: this.appData.BIZ_ID,
            userId: this.userData.opNo,
            userName: name,
            type: this.appData.TASK_TYPE,
            reason,
            opNo: this.$store.getters.user.opNo,
          };
          api.designate(data, (res) => {
            if (res.code == 0) {
              this.$message.success("操作成功!");
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
        } else {
          return false;
        }
      });
    },
    flowRecall(index, row) {
      this.recallAndCancelType = 1;
      this.recallAndCancelData = row;
      this.showReasonDialog = true;
      this.$nextTick(() => {
        this.$refs.reasonForm.resetFields();
      });
    },
    flowCancel(index, row) {
      this.recallAndCancelType = 0;
      this.recallAndCancelData = row;
      this.showReasonDialog = true;
      this.$nextTick(() => {
        this.$refs.reasonForm.resetFields();
      });
    },
    submitReason() {
      let row = this.recallAndCancelData;
      this.$refs.reasonForm.validate((valid) => {
        if (valid) {
          //指派原因
          let reason = this.reasonForm.reason;
          if (this.recallAndCancelType === 0) {
            //撤销
            api.recallAndCancel(
              {
                taskId: row.TASK_ID,
                bizMark: row.BIZ_ID,
                type: "1",
                userId: row.ASSIGNEE,
                reason,
              },
              (res) => {
                if (res.code == 0) {
                  this.$message.success(res.msg);
                  this.showReasonDialog = false;
                } else {
                  this.$alert(res.msg, "提示", {
                    type: "error",
                    dangerouslyUseHTMLString: true,
                  });
                }
              }
            );
          } else if (this.recallAndCancelType === 1) {
            //召回
            api.recallAndCancel(
              {
                taskId: row.TASK_ID,
                bizMark: row.BIZ_ID,
                type: "0",
                userId: row.ASSIGNEE,
                reason,
              },
              (res) => {
                if (res.code == 0) {
                  this.$message.success(res.msg);
                  this.showReasonDialog = false;
                } else {
                  this.$alert(res.msg, "提示", {
                    type: "error",
                    dangerouslyUseHTMLString: true,
                  });
                }
              }
            );
          }
        } else {
          return false;
        }
      });
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
  width: calc(88% - 3px);
  box-sizing: border-box;
  flex-grow: 1;
  padding: 20px 8px;
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
.designForm {
  border-top: 1px solid #f1f8ff;
  padding: 10px 0 0 0;
}
</style>
