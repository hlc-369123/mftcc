<template>
  <el-scrollbar>
    <el-container>
      <el-timeline class="appTimeLine">
        <el-timeline-item
          v-for="(timeline, index) in timeLineData"
          :key="index"
          :color="timeline.dotcolor"
          :size="timeline.dotsize"
          placement="top"
          :timestamp="getCreateTime(timeline)"
        >
          <!-- 会签 -->
          <el-card
            class="timeLineItem"
            body-style="padding:10px;"
            v-if="timeline.isMultiSign === true"
          >
            <span class="taskName">{{ getTaskName(timeline) }}</span>
            <el-collapse>
              <el-collapse-item
                v-for="(item, index) in timeline.multiSignGroup"
                :key="index"
              >
                <template slot="title">
                  <div style="width: 100%">
                    <div v-if="timeline.isOpen" class="openTask">
                      <span style="font-weight: bold"
                        ><span class="assignee-name">{{ item.ASSIGNEE_NAME }} </span>
                        <span
                          v-if="timeline.TASK_TYPE != '分配任务'"
                          class="appIng"
                          :style="'color:' + dotcolor"
                          >审批中...</span
                        >
                        <span
                          v-if="timeline.TASK_TYPE == '分配任务'"
                          class="appIng"
                          :style="'color:' + dotcolor"
                          >分配中...</span
                        ></span
                      >
                      <span class="duration">已送达：{{ item.DURATION }}</span>
                      <template v-if="timeline.isOpen && isTest">
                        <el-button type="primary" size="mini" @click="doCommitTest(item)"
                          >审批</el-button
                        >
                        <el-button type="primary" size="mini" @click="assignedTest(item)"
                          >指派</el-button
                        >
                      </template>
                    </div>
                    <div class="app-footer" v-if="!timeline.isOpen">
                      <span class="endTime"
                        ><i class="el-icon-time"></i>{{ item.END_TIME }}</span
                      >
                      <span class="assignee" v-if="item.ASSIGNEE_NAME"
                        >处理人：<span class="assignee-name">{{
                          item.ASSIGNEE_NAME
                        }}</span></span
                      >
                    </div>
                  </div>
                </template>
                <div class="app-header" v-if="!timeline.isOpen">
                  <span class="approveType">{{ item.APPROVE_TYPE }}</span>
                  <span class="duration">耗时：{{ item.DURATION }}</span>
                </div>
                <div class="app-content" v-if="!timeline.isOpen">
                  <div class="approveIdea">
                    {{ resApproveIdea(item.APPROVE_IDEA) }}
                  </div>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-card>
          <!-- 子流程 -->
          <el-card
            class="timeLineItem"
            body-style="padding:10px;"
            v-else-if="timeline.TASK_TYPE == '9'"
          >
            <span class="taskName">{{ getTaskName(timeline) }}</span>
            <div v-if="!timeline.END_TIME" class="openTask">
              <span style="font-weight: bold">
                <span class="assignee-name">
                  {{ timeline.ASSIGNEE_NAME }}
                </span>
                <span
                  v-if="timeline.TASK_TYPE != '分配任务'"
                  class="appIng"
                  :style="'color:' + dotcolor"
                >
                  审批中...
                </span>
                <span
                  v-if="timeline.TASK_TYPE == '分配任务'"
                  class="appIng"
                  :style="'color:' + dotcolor"
                >
                  分配中...
                </span>
              </span>
              <span class="duration">已送达：{{ timeline.DURATION }}</span>
            </div>
            <template v-if="!timeline.END_TIME && isTest">
              <el-button type="primary" @click="queryChildProcess(timeline)"
                >查看详情</el-button
              >
            </template>
            <div
              v-if="timeline.END_TIME"
              style="
                display: flex;
                flex-wrap: wrap;
                align-items: center;
                justify-content: flex-end;
                padding: 0 10px;
              "
            >
              <div style="width: 100%; display: flex; justify-content: space-between">
                <span :style="'color:#f56c6c'" v-if="timeline.APPROVE_TYPE == '99'"
                  >终止流程</span
                >
                <span :style="'color:' + dotcolor" v-else>已完成</span>
                <span class="duration" style="color: #909399; font-size: 13px"
                  >耗时：{{ timeline.DURATION }}</span
                >
              </div>
              <el-button type="text" @click="queryChildProcess(timeline)"
                >查看详情</el-button
              >
            </div>
          </el-card>
          <el-card
            class="timeLineItem onlyName"
            body-style="padding:10px;"
            v-else-if="!timeline.PROC_INST_ID"
          >
            <span class="taskName">{{ getTaskName(timeline) }}</span>
          </el-card>
          <el-card class="timeLineItem" body-style="padding:10px;" v-else>
            <span class="taskName">{{ getTaskName(timeline) }}</span>
            <div v-if="timeline.isOpen" class="openTask">
              <span style="font-weight: bold">
                <span class="assignee-name">
                  {{ timeline.ASSIGNEE_NAME }}
                </span>
                <span
                  v-if="timeline.TASK_TYPE == '10'"
                  class="appIng"
                  :style="'color:' + dotcolor"
                >
                  等待处理中...
                </span>
                <span
                  v-else-if="timeline.TASK_TYPE == '8'"
                  class="appIng"
                  :style="'color:' + dotcolor"
                >
                  办理中...
                </span>
                <span v-else class="appIng" :style="'color:' + dotcolor">
                  审批中...
                </span>
              </span>
              <span class="duration">已送达：{{ timeline.DURATION }}</span>
            </div>
            <template v-if="timeline.isOpen && isTest">
              <el-button type="primary" size="mini" @click="doCommitTest(timeline)"
                >审批</el-button
              >
              <el-button type="primary" size="mini" @click="assignedTest(timeline)"
                >指派</el-button
              >
            </template>
            <div class="app-header" v-if="!timeline.isOpen">
              <span
                v-if="timeline.TASK_TYPE == 8 && timeline.APPROVE_TYPE"
                class="approveType"
                >{{ timeline.APPROVE_TYPE }}</span
              >
              <span
                v-else-if="timeline.TASK_TYPE == 8 && !timeline.APPROVE_TYPE"
                class="approveType"
                >业务完成</span
              >
              <span v-else class="approveType">{{ timeline.APPROVE_TYPE }}</span>
              <span class="duration">耗时：{{ timeline.DURATION }}</span>
            </div>
            <div
              class="app-content"
              v-if="!timeline.isOpen && resApproveIdea(timeline.APPROVE_IDEA) !== ''"
            >
              <div class="approveIdea">
                {{ resApproveIdea(timeline.APPROVE_IDEA) }}
              </div>
            </div>
            <div class="app-footer" v-if="!timeline.isOpen">
              <span class="endTime"
                ><i class="el-icon-time"></i>{{ timeline.END_TIME }}</span
              >
              <span
                v-if="timeline.TASK_TYPE != 5 && timeline.ASSIGNEE_NAME"
                class="assignee"
                >处理人：<span class="assignee-name">{{
                  timeline.ASSIGNEE_NAME
                }}</span></span
              >
              <div class="addNodeList">
                <div
                  v-for="(item, index) in getAddNodeList(timeline)"
                  :key="index"
                  class="addNodeItem"
                >
                  <span> 加签任务：{{ item.name }}</span>
                  <span>加签处理人：{{ item.assigneeName }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-drawer
        title="审批历史"
        :visible.sync="childProcessShow"
        direction="rtl"
        custom-class="timeLineDrawer"
        :modal="false"
        size="85%"
        :with-header="false"
      >
        <time-line
          :timeLineShow="childProcessShow"
          :initData="childProc"
          :isTest="isTest"
          :isChild="true"
          @doCommitTest="doCommitTest"
          @assignedTest="assignedTest"
        ></time-line>
      </el-drawer>
    </el-container>
  </el-scrollbar>
</template>

<script>
import api from "@/api/flowable/flowable";
export default {
  name: "timeLine",
  data() {
    return {
      childProc: [],
      childProcessShow: false,
      timeLineData: [],
      dotcolor: "#1EC5B5",
      dotsize: "large",
    };
  },
  props: [
    "timeLineShow",
    "traceNo",
    "bizMark",
    "taskDefId",
    "isShowSign",
    "isTest",
    "isChild",
    "initData",
  ],
  watch: {
    timeLineShow(value) {
      if (value) {
        this.init();
      } else {
        this.childProcessShow = false;
      }
    },
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      if (this.initData) {
        this.timeLineData = this.initData;
        return;
      }
      //业务唯一标识为空时，不展示审批历史
      if (this.traceNo == "" || this.traceNo == null) {
        return;
      }
      let param = {
        traceNo: this.traceNo,
        bizMark: this.bizMark,
        taskDefId: this.taskDefId,
        isShowSign: this.isShowSign === false ? "0" : this.isShowSign === true ? "1" : "",
        opNo: this.$store.getters.user.opNo,
      };
      api.getTimeLine(param, (res) => {
        if (res.code == 0) {
          this.timeLineData = res.data.hisTask;
          if (res.data.task && res.data.task.length > 0) {
            for (let i in res.data.task) {
              res.data.task[i].isOpen = true;
              res.data.task[i].dotcolor = this.dotcolor;
              res.data.task[i].dotsize = this.dotsize;
            }
            this.timeLineData = this.timeLineData.concat(res.data.task);
          }
          if (res.data.assign && res.data.assign.length > 0) {
            for (let i in res.data.assign) {
              res.data.assign[i].isOpen = true;
              res.data.assign[i].dotcolor = this.dotcolor;
              res.data.assign[i].dotsize = this.dotsize;
            }
            this.timeLineData = this.timeLineData.concat(res.data.assign);
          }
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    queryChildProcess(timeline) {
      if (timeline.TASK_TYPE == "9" && timeline.childProc) {
        this.childProc = timeline.childProc;
        this.childProcessShow = true;
      }
    },
    resApproveIdea(value) {
      if (!value) {
        return "";
      }
      // let data = JSON.parse(value);
      // if(data.newApprove){
      //     return data.newApprove[0].name;
      // }else{
      //     return data;
      // }
      return value;
    },
    getCreateTime(timeline) {
      if (timeline.isMultiSign) {
        return timeline.multiSignGroup[0].CREATE_TIME;
      } else {
        return timeline.CREATE_TIME;
      }
    },
    getTaskName(timeline) {
      if (timeline.isMultiSign) {
        return timeline.multiSignGroup[0].TASK_NAME;
      } else {
        if (timeline.TASK_NAME == "launchNode") {
          return "开始";
        }
        return timeline.TASK_NAME;
      }
    },
    doCommitTest(timeline) {
      if (this.isChild) {
        timeline.isChild = true;
      }
      this.$emit("doCommitTest", timeline);
    },
    assignedTest(timeline) {
      if (this.isChild) {
        timeline.isChild = true;
      }
      this.$emit("assignedTest", timeline);
    },
    getAddNodeList(timeline) {
      let arr = [];
      if (timeline["FORM_DATA"] && timeline["FORM_DATA"] != "{}") {
        let formData = JSON.parse(timeline["FORM_DATA"]);
        if (typeof formData.addNodeType != "undefined") {
          return formData.addNodeList;
        }
      }
      return arr;
    },
  },
};
</script>
<style>
.appTimeLine {
  overflow: auto;
  padding: 20px;
  width: 100%;
}
.appTimeLine .el-timeline-item__timestamp {
  padding-top: 20px;
}
.timeLineItem.onlyName {
  box-shadow: none;
  border: none;
}
.timeLineItem.onlyName .el-card__body {
  padding: 0px !important;
}
.timeLineItem .taskName {
  font-weight: bold;
  position: absolute;
  top: -3px;
  left: 28px;
  font-size: 15px;
  color: #909399;
}
.timeLineItem .el-collapse,
.timeLineItem .el-collapse-item__header,
.timeLineItem .el-collapse-item__wrap {
  border: none;
}

.timeLineItem .app-header .duration,
.openTask .duration {
  color: #909399;
  font-size: 13px;
  float: right;
}
.timeLineItem .app-header .approveType {
  color: #1ec5b5;
}
.timeLineItem .app-footer .endTime {
  color: #909399;
  font-size: 13px;
}
.timeLineItem .app-footer .endTime i {
  margin-right: 5px;
}
.timeLineItem .app-footer .assignee {
  color: #909399;
  font-size: 13px;
  float: right;
}
.timeLineItem .app-footer .assignee-name {
  font-size: 15px;
  color: #000;
  font-weight: bold;
  float: right;
  margin: 0 0 0 6px;
  border-bottom: solid 1px #eee;
  min-width: 90px;
  text-align: center;
  height: 25px;
  line-height: 25px;
}
.timeLineItem .app-content,
.timeLineItem .app-header,
.timeLineItem .app-footer {
  line-height: 30px;
  padding: 0 10px;
  margin-bottom: 10px;
}
.timeLineItem .app-content .approveIdea {
  min-height: 60px;
  background: linear-gradient(to bottom, transparent 29px, #eee 1px);
  background-size: 100% 30px;
  color: #ff9800;
  overflow: hidden;
  white-space: normal;
  word-break: break-all;
  word-wrap: break-word;
}
.timeLineDrawer .el-drawer__wrapper {
  position: absolute;
  margin-bottom: 0px;
}
.openTask {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 10px;
}
.appTimeLine .addNodeList > .addNodeItem {
  display: flex;
  justify-content: space-between;
  background: #f6f6f6;
  margin: 3px 0;
  padding: 0px 8px;
  color: #595d64;
}
</style>
