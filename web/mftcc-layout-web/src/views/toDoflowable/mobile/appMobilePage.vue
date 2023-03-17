<template>
  <el-container class="moblie-container">
    <el-header class="moblie-theme-header" height="48px">
      <div class="moblie-header">
        <i class="el-icon-arrow-left" @click="goBack"></i>
        <div>审批详情</div>
      </div>
    </el-header>
    <el-main style="position: relative;">
      <el-scrollbar>
        <div :class="appClassName" style="height: 100%; overflow: auto">
          <component :is="page" v-bind="methodParam" ref="component">
          </component>
        </div>
      </el-scrollbar>
      <el-drawer
        title="审批历史"
        :visible.sync="drawer"
        direction="btt"
        custom-class="timeLineDrawer"
        ref="timeLineDrawer"
        :modal-append-to-body="false"
      >
        <time-line
          :timeLineShow="drawer"
          :traceNo="traceNo"
          :bizMark="bizMark"
          :taskDefId="taskDefId"
        ></time-line>
      </el-drawer>
      <el-drawer
        title="审批"
        :visible.sync="appdrawer"
        direction="btt"
        custom-class="appDrawer"
        :modal-append-to-body="false"
      >
        <app-content
          :taskId="taskId"
          :finishType="finishType"
          :bizMark="bizMark"
          :approveBtnList="approveBtnList"
          @approve="approve"
        ></app-content>
      </el-drawer>
      <el-drawer
        :visible.sync="userdrawer"
        direction="btt"
        custom-class="userDrawer"
        :modal-append-to-body="false"
        @opened="openedUserDrawer"
      >
        <div slot="title">选择处理人</div>
        <el-scrollbar ref="scrollbar">
          <ul
            class="assignList"
            v-infinite-scroll="load"
            :infinite-scroll-disabled="disabled"
          >
            <li
              v-for="user in assignList"
              :key="user.userId"
              @click="selectUser(user)"
              :class="{
                'user-item': true,
                active: activeUserList.indexOf(user.opNo) > -1
              }"
            >
              <div class="user-opNo">{{ user.opNo }}-{{ user.opName }}</div>
              <div class="user-brNo">{{ user.brName }}</div>
              <i
                class="el-icon-check"
                v-show="activeUserList.indexOf(user.opNo) > -1"
              ></i>
            </li>
          </ul>
          <p v-show="loading">加载中...</p>
          <p v-show="noMore">没有更多了</p>
        </el-scrollbar>
        <el-button type="primary" class="submit-btn" @click="approveByUser"
          >提 交</el-button
        >
      </el-drawer>
    </el-main>
    <el-footer class="moblie-theme-footer" height="48px">
      <div class="moblie-footer">
        <div class="footer-btn" @click="openAppDetail">
          <i class="el-icon-s-check"></i>
          <div>审批</div>
        </div>
        <div class="footer-btn" @click="openTimeLine">
          <i class="el-icon-s-claim"></i>
          <div>审批历史</div>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<script>
import appContent from "../mobile/appContent.vue";
import api from "@/api/flowable/flowable";
export default {
  data() {
    return {
      drawer: false,
      appdrawer: false,
      userdrawer: false,
      page: null,
      appClassName: "",
      pageNo: 1,
      pageSize: 10,
      assignList: [],
      activeUserList: [],
      loading: false
    };
  },
  components: {
    appContent
  },
  computed: {
    noMore() {
      return this.total == this.assignList.length;
    },
    disabled() {
      return this.loading || this.noMore;
    }
  },
  props: ["appData"],
  created() {
    let $query;
    if (this.appData) {
      $query = this.appData;
    } else {
      $query = this.$route.query;
    }
    this.traceNo = $query.traceNo;
    this.taskDefId = $query.taskDefId;
    this.procId = $query.procId;
    this.bizMark = $query.bizMark;
    this.taskId = $query.taskId;
    this.finishType = $query.finishType;
  },
  mounted() {
    this.getApprovalDetail(this.taskId, this.finishType);
  },
  methods: {
    approveByUser() {
      if (this.activeUserList.length > 0) {
        let opNo = this.activeUserList.join(",");
        this.doCommit(this.node.id, this.node.seqList, opNo, "");
      } else {
        this.$alert("请选择审批人员", "提示", {
          type: "error",
          dangerouslyUseHTMLString: true
        });
      }
    },
    openedUserDrawer() {
      let offsetHeight = this.$refs.scrollbar.$el.parentElement.offsetHeight;
      this.$refs.scrollbar.$el.style.height = offsetHeight - 66 + "px";
    },
    load() {
      this.loading = true;
      setTimeout(() => {
        this.pageNo += 1;
        api.getNextUserList(
          {
            pageNo: this.pageNo,
            pageSize: this.pageSize,
            assignList: this.assignListSearchList
          },
          res => {
            if (res.code == 0) {
              this.assignList = this.assignList.concat(res.list.records);
            } else {
              this.$alert(res.msg, "提示", {
                type: "error",
                dangerouslyUseHTMLString: true
              });
            }
            this.loading = false;
          }
        );
      }, 2000);
    },
    selectUser(user) {
      if (this.activeUserList.indexOf(user.opNo) == -1) {
        this.activeUserList.push(user.opNo);
      } else {
        this.activeUserList.splice(this.activeUserList.indexOf(user.opNo), 1);
      }
    },
    approve(data) {
      // this.userdrawer = true;
      // return;
      if (data) {
        this.appForm = data.appForm;
        this.approveBtn = data.approveBtn;
      }
      if (!this.method) {
        this.$alert("缺少审批提交函数", "提示", {
          type: "error",
          dangerouslyUseHTMLString: true
        });
        return;
      }
      if (this.variablesMethod) {
        if (typeof this.$refs.component[this.variablesMethod] == "function") {
          let formData = {};
          if (this.$refs.appBizForm) {
            formData = this.$refs.appBizForm.getFormValue();
          }
          this.$refs.component[this.variablesMethod](formData, res => {
            this.variables = res;
            this.needOperated();
          });
        }
      } else {
        this.needOperated();
      }
    },
    needOperated() {
      let approveBtnSub = this.approveBtn;
      api.needOperated(
        {
          taskId: this.taskId,
          approveType: approveBtnSub.approveType,
          variables: JSON.stringify(this.variables)
        },
        res => {
          if (res.code == 0) {
            if (res.hasComplete === 0) {
              this.node = {
                id: res.result.targetFlowId,
                seqList: res.result.seqList[0]
              };
              if (res.result.assignList) {
                this.assignListSearchList = res.result.assignList;
                this.pageNo = 1;
                api.getNextUserList(
                  {
                    pageNo: this.pageNo,
                    pageSize: this.pageSize,
                    assignList: res.result.assignList
                  },
                  res => {
                    if (res.code == 0) {
                      this.assignList = res.list.records;
                      this.total = res.list.total;
                      this.userdrawer = true;
                    } else {
                      this.$alert(res.msg, "提示", {
                        type: "error",
                        dangerouslyUseHTMLString: true
                      });
                    }
                  }
                );
              }
            } else {
              this.doCommit();
            }
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true
            });
          }
        }
      );
    },
    doCommit(targetNodeId, seqList, nextUserId, listStr) {
      let approveBtnSub = this.approveBtn;
      let flowType = "";
      if (seqList != null || nextUserId != null) {
        flowType = "11";
      } else {
        flowType = approveBtnSub.approveType;
      }
      // if (approveBtnSub.approveType == 2 || approveBtnSub.approveType == 5) {
      targetNodeId = this.node || "";
      // }
      if (approveBtnSub.approveType == 7 || approveBtnSub.approveType == 8) {
        if (this.addNodeList.length == 0) {
          this.$alert("请配置加签信息", "提示", {
            type: "error",
            dangerouslyUseHTMLString: true
          });
          return;
        } else {
          var nodes = [];
          for (var i = 0; i < this.addNodeList.length; i++) {
            var node = {};
            node["name"] = this.addNodeList[i].nodeName;
            node["assignee"] = this.addNodeList[i].assigneeId;
            nodes.push(node);
          }
          listStr = JSON.stringify(nodes);
        }
      }
      let data = {
        taskId: this.taskId,
        flowType: flowType,
        approvalContents: this.appForm.approve,
        approveBtnId: approveBtnSub.id,
        approveBtnName: approveBtnSub.approveIdea,
        targetNodeId: this.appForm.targetNodeId,
        listStr: listStr,
        nextUserId: nextUserId,
        seqList: seqList,
        bizMark: this.bizMark,
        variables: this.variables,
        methodParam: this.methodParam
      };

      if (this.appBizFormData) {
        data.appBizFormData = JSON.stringify(this.appBizFormData);
      }
      if (this.appPageDataMethod) {
        if (
          typeof this.$refs.componentAppPage[this.appPageDataMethod] ==
          "function"
        ) {
          const innerPageData = this.$refs.componentAppPage[
            this.appPageDataMethod
          ](res => {
            data.appPageData = res;
            this.doCommit4Iframe(data);
          });
        } else {
          this.$alert(
            "未找到审批弹层页面数据回调函数【" + this.appPageDataMethod + "】",
            "提示",
            {
              type: "error",
              dangerouslyUseHTMLString: true
            }
          );
        }
      } else {
        this.doCommit4Iframe(data);
      }
    },
    doCommit4Iframe(data) {
      if (typeof this.$refs.component[this.method] == "function") {
        this.$refs.component[this.method](JSON.stringify(data), res => {
          if (res.code == 0) {
            this.$message.success(res.msg);
            this.appdrawer = false;
            this.userdrawer = false;
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true
            });
          }
        });
      } else {
        this.$alert("未找到审批提交函数【" + this.method + "】", "提示", {
          type: "error",
          dangerouslyUseHTMLString: true
        });
      }
    },
    getApprovalDetail(taskId, taskType) {
      let data = {
        taskId: taskId,
        taskType: taskType,
        bizMark: this.bizMark
      };
      api.getApprovalDetail(data, res => {
        if (res.code == 0) {
          this.backNodeHasSearch = false;
          this.submitNodeHasSearch = false;

          this.methodParam = res.data.methodParam || {};
          this.methodParam.traceNo = this.traceNo;

          this.method = res.data.method;
          this.variablesMethod = res.data.variablesMethod;
          if (res.data.showGuide == "1") {
            this.messageShow = true;
          } else {
            this.messageShow = false;
          }

          this.resAppPage(res.data.formUrl);
          // this.resAppPage("mftcc-sys-web/demo/app");

          if (res.data.approveGuide) {
            this.approveGuide = res.data.approveGuide.guide;
          }
          let blist = res.data.approveBtnList;
          let qian = ["3", "4", "44", "5", "50", "55"];
          let hou = ["1", "2", "7", "8", "12"];
          let over = ["10", "99"];
          for (let i in blist) {
            for (let q in qian) {
              if (blist[i].approveType == qian[q]) {
                blist[i].class = "qian";
                break;
              }
            }
            if (blist[i].class) {
              continue;
            }
            for (let h in hou) {
              if (blist[i].approveType == hou[h]) {
                blist[i].class = "hou";
                break;
              }
            }
            if (blist[i].class) {
              continue;
            }
            for (let o in over) {
              if (blist[i].approveType == over[o]) {
                blist[i].class = "over";
                break;
              }
            }
          }
          this.approveBtnList = blist;
          // this.appPage = res.data.appPage;

          // this.resAppPageInner(this.appPage);
          // if (!this.appPage) {
          //   this.appFormId = res.data.appFormId;
          //   this.appFormInitMethod = res.data.appFormInitMethod;
          // }
          // this.appPageMethodParam = res.data.appPageMethodParam || {};
          // this.appPageDataMethod = res.data.appPageDataMethod;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    resAppPage(pagePath) {
      this.page = null;
      const pathObj = this.resPath(pagePath);
      this.getApp(pathObj, "out");
    },
    resAppPageInner(pagePath) {
      this.page4app = null;
      const pathObj = this.resPath(pagePath);
      this.getApp(pathObj, "in");
    },
    getApp(pathObj, state) {
      if (!pathObj) {
        return;
      }
      this.$singleSpa
        .toLoadPromise(this.$singleSpa.getApp(pathObj.productName))
        .then(res => {
          res.loadApp().then(res => {
            const apps = res.default;
            const app = apps[pathObj.spaPath];
            if (state == "out") {
              this.page = app;
              this.appClassName = pathObj.productName;
            } else if (state == "in") {
              this.page4app = app;
              this.appPageClassName = pathObj.productName;
            }
          });
        });
    },
    resPath(pagePath) {
      if (!pagePath) {
        return null;
      }
      pagePath = pagePath.replace(/\s+/g, "");
      if (/.vue$/.test(pagePath)) {
        pagePath = pagePath.substring(0, pagePath.length - 4);
      }
      if (pagePath.startsWith("/")) {
        pagePath = pagePath.substring(1);
      }
      let productName = pagePath.split("/")[0];
      let spaPath = pagePath.substring(productName.length);
      return { productName, spaPath };
    },
    openTimeLine() {
      this.appdrawer = false;
      if (this.drawer) {
        this.drawer = false;
      } else {
        this.drawer = true;
      }
    },
    openAppDetail() {
      this.drawer = false;
      if (this.appdrawer) {
        this.appdrawer = false;
      } else {
        this.appdrawer = true;
      }
    },
    goBack() {
      this.$router.back(-1);
    }
  }
};
</script>
<style>
.moblie-theme-header {
  background-color: #51a0fa;
  color: #ffffff;
}
.moblie-container .moblie-theme-footer {
  background-color: #ffffff;
  color: #51a0fa;
  border-top: 1px solid #f0f0f0;
}
.moblie-theme-header .moblie-header {
  display: flex;
  align-items: center;
}
.moblie-theme-header .moblie-header > i {
  margin-left: 15px;
  margin-right: 8px;
  font-size: 22px;
}
.moblie-theme-header .moblie-header > div {
  font-size: 16px;
}
.moblie-container .moblie-footer {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-around;
}
.moblie-container .moblie-footer .footer-btn {
  display: flex;
  flex-direction: column;
  min-width: 50px;
  line-height: 15px;
}
.moblie-container .moblie-footer .footer-btn > i {
  font-size: 26px;
}
.moblie-container .moblie-footer .footer-btn > div {
  font-size: 12px;
}

/* 审批历史时间轴 */
.moblie-container .el-drawer__wrapper {
  position: absolute;
}
.moblie-container .timeLineDrawer {
  height: 80% !important;
  flex-direction: column;
  border-radius: 16px 16px 0 0;
}
.moblie-container .appDrawer {
  max-height: 80%;
  height: auto !important;
  flex-direction: column;
  border-radius: 16px 16px 0 0;
}
.moblie-container .userDrawer p {
  font-size: 14px;
  text-align: center;
  margin: 5px 0;
}
.moblie-container .userDrawer .submit-btn {
  width: 100%;
  border-radius: 2px !important;
  height: 50px !important;
  opacity: 1;
  background: #61a9fa;
  font-weight: bold;
  text-align: center;
  font-size: 16px;
  margin: 8px 16px 8px 16px !important;
  width: calc(100% - 38px);
}
.moblie-container .el-drawer__header {
  margin: 0;
  padding: 12px 0 0 12px;
  font-size: 16px;
  font-weight: bold;
  color: #1a1a1a;
}
.moblie-container .el-drawer__header > span {
  outline: none;
}
.moblie-container .el-drawer__close-btn {
  margin-right: 8px;
  font-size: 24px;
}
.moblie-container .appTimeLine {
  padding: 15px;
}
.moblie-container .el-timeline-item__node--normal {
  width: 10px;
  height: 10px;
}
.moblie-container .el-timeline-item__node--large {
  left: -1px;
  width: 10px;
  height: 10px;
  background-color: #61a9fa !important;
}
.moblie-container .el-timeline-item__tail {
  left: 3px;
}
.moblie-container .timeLineItem .taskName {
  font-size: 14px;
  color: #1a1a1a;
  left: 23px;
  font-weight: normal;
}
.moblie-container .appTimeLine .el-timeline-item__timestamp {
  padding: 0;
  text-align: right;
  padding-right: 2px;
  color: #1a1a1a;
}
.moblie-container .el-card.is-always-shadow {
  box-shadow: none;
}
.moblie-container .el-timeline-item__wrapper {
  padding-left: 23px;
}
.moblie-container .timeLineItem .app-header .approveType {
  color: #61a9fa;
  font-size: 16px;
  font-weight: bold;
}
.moblie-container .openTask .duration,
.moblie-container .timeLineItem .app-header .duration {
  font-size: 12px;
  color: #999999;
}
.moblie-container .timeLineItem .app-content,
.moblie-container .timeLineItem .app-footer,
.moblie-container .timeLineItem .app-header {
  line-height: 30px;
  padding: 0;
  margin-bottom: 0px;
}
.moblie-container .timeLineItem .app-content .approveIdea {
  min-height: 60px;
  color: #1a1a1a;
  background: none;
  line-height: 24px;
  margin-bottom: 3px;
}
.moblie-container .timeLineItem .app-footer .endTime,
.moblie-container .timeLineItem .app-footer .assignee {
  color: #999999;
  font-size: 12px;
}
.moblie-container .timeLineItem .app-footer .assignee-name {
  min-width: auto;
  color: #1a1a1a;
  margin: 0;
  border: none;
  height: auto;
  line-height: inherit;
}
.moblie-container .openTask .assignee-name,
.moblie-container .openTask .appIng {
  font-size: 15px;
  color: #1a1a1a;
  font-weight: bold;
}
.moblie-container .openTask {
  padding: 5px 0;
  flex-wrap: wrap;
}
.moblie-container .openTask .appIng {
  color: #61a9fa !important;
}
.moblie-container .v-modal {
  top: -48px;
}
.moblie-container .assignList {
  list-style: none;
  margin: 0;
  padding: 16px 16px 0 16px;
}
.moblie-container .assignList .user-item {
  background: #f7f7f7;
  border: 1px solid #dfdfdf;
  height: 57px;
  padding: 0 10px;
  margin-bottom: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
}
.moblie-container .assignList .user-item.active {
  background: #f0f7ff;
  border: 1px solid #61a9fa;
}
.moblie-container .assignList .user-item.active > i {
  position: absolute;
  top: 5px;
  font-size: 22px;
  color: #61a9fa;
  right: 10px;
}
.moblie-container .assignList .user-item .user-opNo {
  color: #1a1a1a;
  font-size: 14px;
  margin-bottom: 2px;
}
.moblie-container .assignList .user-item .user-brNo {
  color: #666666;
  font-size: 12px;
}
/* @media screen and (max-width: 750px) {
  .el-message-box,
  .el-message {
    width: 60% !important;
    min-width: 60% !important;
  }
} */
</style>
