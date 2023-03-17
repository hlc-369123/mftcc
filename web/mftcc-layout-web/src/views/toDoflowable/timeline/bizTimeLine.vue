<template>
  <div style="display: flex;align-items: center;padding: 10px;" v-if="isLoad">
    <i
      v-if="isScroll"
      class="scrollBtn left el-icon-d-arrow-left"
      @click="clickScrollBtn('left')"
    ></i>
    <div style="overflow: auto;display: flex;">
      <el-steps
        :active="active"
        class="flow-steps"
        ref="flowSteps"
        v-if="isRefresh"
        simple
      >
        <el-step v-for="item in stepData" :key="item.id" :class="item.type">
          <template slot="icon">
            <!-- <img
            v-if="item.active"
            src="@/assets/flowable/active.png"
            style="cursor: pointer"
            @click="bizStepClick"
          /> -->
            <i v-if="item.active" class="el-icon-more"></i>
            <!-- <i v-else-if="item.stoped" class="icon"></i> -->
            <i v-else class="el-icon-check"></i>
          </template>
          <template slot="title">
            <span :class="'title ' + item.class">{{ item.name }}</span>
          </template>
        </el-step>
      </el-steps>
    </div>
    <i
      v-if="isScroll"
      class="scrollBtn right el-icon-d-arrow-right"
      @click="clickScrollBtn('right')"
    ></i>
    <i
      v-if="isChild"
      class="el-icon-back"
      @click="returnClick"
      style="position: absolute; top: 0; cursor: pointer; color: #6493d5"
    ></i>
  </div>
</template>

<script>
import api from "@/api/flowable/flowable";
export default {
  name: "bizTimeLine",
  title: "bizTimeLine",
  data() {
    return {
      stepData: [],
      active: -1,
      // space: 106,
      task: null,
      isRefresh: true,
      isChild: false,
      childBizMark: null,
      childTraceNo: null,
      procInstId: "",
      isLoad: false,
      isScroll: false
    };
  },
  props: ["traceNo", "bizMark", "option"],
  watch: {
    isRefresh(val) {
      if (val) {
        this.$nextTick(() => {
          this.init();
        });
      }
    },
    isLoad(val) {
      this.$nextTick(() => {
        let el = this.$refs.flowSteps.$el;
        let width = el.offsetWidth;
        let scrollWidth = el.parentElement.offsetWidth;
        if (width > scrollWidth) {
          this.isScroll = true;
        }
        /* 流程图应跟随业务阶段，自动将当前阶段显示到主视线范围内 */
        this.$nextTick(() => {
          // el.parentElement.scrollLeft =
          //   this.active * 184 - scrollWidth / 2 + 100;
          el.parentElement.scrollTo({
            left: this.active * 188 - scrollWidth / 2 + 100,
            behavior: "smooth"
          });
        });
      });
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    clickScrollBtn(state) {
      let el = this.$refs.flowSteps.$el;
      let width = el.parentElement.offsetWidth + el.parentElement.scrollLeft;
      let children = el.children;
      if (state == "left") {
        /* for (let i = children.length - 1; i >= 0; i--) {
          let step = children[i];
          let offsetLeft = step.offsetLeft;
          if (
            offsetLeft - el.parentElement.scrollLeft <
            step.offsetWidth - 30
          ) {
            el.parentElement.scrollTo({
              left:
                el.parentElement.scrollLeft -
                step.offsetWidth -
                (offsetLeft - el.parentElement.scrollLeft) -
                30,
              behavior: "smooth"
            });
            break;
          }
        } */
        el.parentElement.scrollTo({
          left: el.parentElement.scrollLeft - 200,
          behavior: "smooth"
        });
      } else {
        /* for (let i = 0; i < children.length; i++) {
          let step = children[i];
          let offsetLeft = step.offsetLeft;
          if (offsetLeft > width) {
            // el.parentElement.scrollLeft += offsetLeft - width + 30;
            el.parentElement.scrollTo({
              left: el.parentElement.scrollLeft + offsetLeft - width + 30,
              behavior: "smooth"
            });
            break;
          }
        } */
        el.parentElement.scrollTo({
          left: el.parentElement.scrollLeft + 200,
          behavior: "smooth"
        });
      }
    },
    bizStepClick() {
      console.log("this.task", this.task);
      if (
        this.task.TASK_TYPE == "9" &&
        this.task.children &&
        this.task.children.length > 0
      ) {
        let childTask = this.task.children[0];
        this.childBizMark = childTask.BIZ_ID;
        this.childTraceNo = childTask.TRACE_NO;
        this.procInstId = childTask.PROC_INST_ID;
        this.isChild = true;
        this.refresh();
      } else {
        this.$emit("bizStepClick", this.task);
      }
    },
    returnClick() {
      this.isChild = false;
      this.refresh();
    },
    refresh() {
      this.isRefresh = false;
      this.$nextTick(() => {
        this.isRefresh = true;
      });
    },
    init() {
      let param = {};
      if (this.option) {
        param.option = this.option;
      } else {
        if (this.isChild) {
          param.traceNo = this.childTraceNo;
          param.bizMark = this.childBizMark;
          param.procInstId = this.procInstId;
        } else {
          param.traceNo = this.traceNo;
          param.bizMark = this.bizMark;
        }
      }
      this.simulationByTraceNo(param);
    },
    simulationByTraceNo(param) {
      api.simulationByTraceNo(param, res => {
        if (res.code == 0) {
          let array = [];
          this.task = res.task;
          let isEnd = res.isEnd;
          if (!this.task) {
            this.task = {
              isEnd,
              isSuspended: false
            };
          } else {
            this.task.isEnd = isEnd;
            this.task.isSuspended = false;
          }
          let nodes = res.data.node;
          for (let i in nodes) {
            let node = nodes[i];
            if (this.resLineNode(node)) {
              let item = {
                id: node.id,
                name: node.name,
                class: ""
              };
              if (this.task && this.task.TASK_DEF_ID == node.id) {
                if (this.task.APPROVE_TYPE == "stoped") {
                  item.stoped = true;
                } else {
                  item.active = true;
                  if (!this.task.TASK_NAME) {
                    this.task.TASK_NAME = node.name;
                  }
                  if (node.type == "org.flowable.bpmn.model.ReceiveTask") {
                    this.task.isSuspended = true;
                  }
                }
                this.active = array.length;
              }

              // if (this.active == Number(i) - 1) {
              //   item.type = "active";
              //   if (item.stoped) {
              //     item.type = "finish";
              //   }
              // } else if (this.active != -1 && this.active < Number(i) - 1) {
              //   item.type = "wait";
              // } else {
              //   item.type = "finish";
              // }
              array.push(item);
            }
          }

          if (this.active == -1 && isEnd == true) {
            this.active = array.length;
          }

          /* 根据节点执行状态添加class */
          array.forEach((taskNode, t) => {
            if (t == this.active) {
              if (taskNode.stoped) {
                taskNode.type = "finish";
              } else {
                taskNode.type = "active";
              }
            } else if (t < this.active) {
              taskNode.type = "finish";
            } else if (t >= this.active) {
              taskNode.type = "wait";
            }
          });

          this.$emit("getTask", this.task);
          this.stepData = array;

          // let width = el.clientWidth;
          // if (width / 106 < array.length) {
          //   this.space = 61;
          //   el.classList.add("up");
          //   for (let a in array) {
          //     if (a % 2 != 0) {
          //       array[a].class = "up";
          //     }
          //   }
          // } else {
          //   this.space = "10%";
          // }

          this.isLoad = true;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    resLineNode(node) {
      let nodeTypeObj = node.attributes.nodeType;
      if (nodeTypeObj && nodeTypeObj[0].value == "bizTask") {
        //业务任务
        return true;
      } else if (node.type == "org.flowable.bpmn.model.CallActivity") {
        //子流程
        return true;
      } else if (node.type == "org.flowable.bpmn.model.ServiceTask") {
        //服务任务
        return false;
      } else if (node.type == "org.flowable.bpmn.model.ReceiveTask") {
        //接收任务
        return true;
      } else if (node.type == "org.flowable.bpmn.model.UserTask") {
        //审批任务
        return true;
      }
      return false;
    }
  }
};
</script>
<style>
.flow-steps {
  background: #fff;
  padding: 0px;
  display: flex;
  align-items: center;
}
.flow-steps > .el-step {
  height: 40px;
  flex-basis: auto !important;
  background-color: #409eff;
  margin: 0 4px;
  clip-path: polygon(
    calc(100% - 10px) 0%,
    100% 50%,
    calc(100% - 10px) 100%,
    0% 100%,
    10px 50%,
    0% 0%
  );
}
.flow-steps > .el-step .el-step__main {
  height: 38px;
  background-color: #d9ecff;
  clip-path: polygon(
    calc(100% - 11px) 0%,
    calc(100% - 1.5px) 50%,
    calc(100% - 11px) 100%,
    2px 100%,
    11.5px 50%,
    2px 0%
  );
  min-width: 180px;
  padding-left: 62px;
  padding-right: 34px;
}
.flow-steps > .el-step:first-child {
  clip-path: polygon(
    calc(100% - 10px) 0%,
    100% 50%,
    calc(100% - 10px) 100%,
    0px 100%,
    0px 0%
  );
  border-bottom-left-radius: 20px;
  border-top-left-radius: 20px;
}
.flow-steps > .el-step:first-child .el-step__main {
  clip-path: polygon(
    calc(100% - 11px) 0%,
    calc(100% - 1.5px) 50%,
    calc(100% - 11px) 100%,
    0px 100%,
    0px 0%
  );
  border-bottom-left-radius: 20px;
  border-top-left-radius: 20px;
  margin-left: 1px;
}
.flow-steps > .el-step:last-child {
  clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 10px 50%, 0% 0%);
  border-bottom-right-radius: 20px;
  border-top-right-radius: 20px;
}
.flow-steps > .el-step:last-child .el-step__main {
  clip-path: polygon(100% 0%, 100% 100%, 2px 100%, 11.5px 50%, 2px 0%);
  border-bottom-right-radius: 20px;
  border-top-right-radius: 20px;
  margin-right: 1px;
}

.flow-steps > .el-step .el-step__title {
  max-width: none !important;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}
.flow-steps > .el-step .title {
  color: #3d3d3d;
  font-size: 14px;
  font-weight: normal;
}

/* .flow-steps > .el-step:after {
  content: "";
  position: absolute;
  right: 100%;
  top: 26px;
  width: 0;
  height: 0;
  border-top: 13px solid transparent;
  border-right: 26px solid red;
  border-bottom: 13px solid transparent;
} */
.flow-steps > .el-step .el-step__arrow {
  display: none;
}
.flow-steps > .el-step .el-step__head {
  position: absolute;
  left: 34px;
}
.flow-steps > .el-step .el-step__head .el-step__icon {
  color: #fff;
  border: none;
  background: #409eff;
}
.flow-steps > .el-step.wait .el-step__head {
  display: none;
}

/* 未完成节点样式 */
.flow-steps > .el-step.wait {
  background: #a1a1a1;
}
.flow-steps > .el-step.wait .el-step__main {
  background: #fff;
  padding-left: 44px;
}
.flow-steps > .el-step.wait .title {
  color: #a1a1a1;
}

/* 当前节点样式 */
.flow-steps > .el-step.active {
  height: 48px;
  background-color: #409eff;
  margin: 0 4px;
  clip-path: polygon(
    calc(100% - 12px) 0%,
    100% 50%,
    calc(100% - 12px) 100%,
    0% 100%,
    12px 50%,
    0% 0%
  );
}
.flow-steps > .el-step.active:first-child {
  clip-path: polygon(
    calc(100% - 14px) 0%,
    100% 50%,
    calc(100% - 14px) 100%,
    0% 100%,
    0% 0%
  );
  border-bottom-left-radius: 29px;
  border-top-left-radius: 29px;
}
.flow-steps > .el-step.active:last-child {
  clip-path: polygon(100% 0%, 100% 100%, 0% 100%, 14px 50%, 0% 0%);
  border-bottom-right-radius: 29px;
  border-top-right-radius: 29px;
}
.flow-steps > .el-step.active .el-step__main {
  height: 100%;
  background-color: transparent;
  clip-path: none;
  padding-left: 72px;
  padding-right: 44px;
  min-width: 200px;
}
.flow-steps > .el-step.active .el-step__head .el-step__icon {
  color: #409eff;
  background: #fff;
}
.flow-steps > .el-step.active .el-step__head {
  left: 50px;
}
.flow-steps > .el-step.active .title {
  color: #ffffff;
  font-size: 15px;
  font-weight: normal;
}
.scrollBtn {
  cursor: pointer;
  color: #a1a1a1;
}
.scrollBtn:hover {
  color: #409eff;
}
.scrollBtn.left {
  margin-right: 5px;
}
.scrollBtn.right {
  margin-left: 5px;
}
/* ----------------old----------------- */
/* .flow-steps > .el-step {
  height: 40px;
  background-color: rgba(64, 158, 255, 0.2);
  border: 1px solid #409eff;
  margin: 0 4px;
  border-left: 10px solid transparent;
  border-top: 20px solid red;
  border-bottom: 20px solid red;
} */
/* .flow-steps {
  padding-top: 16px;
  height: 62px;
  background: #fff;
}
.flow-steps.up {
  padding-top: 24px;
}
.flow-steps .el-step__icon.is-text {
  height: 12px;
  width: 12px;
  top: 9px;
  position: absolute;
}
.flow-steps .is-process .el-step__icon.is-text {
  border: none;
  top: 10px;
}
.flow-steps .is-finish .el-step__icon.is-text {
  border: 1px solid #5d92dd;
}
.flow-steps .is-wait .el-step__icon.is-text {
  border: 1px solid #cfddf1;
}
.flow-steps .el-step.is-horizontal .el-step__line {
  height: 12px;
  top: 9px;
  left: 6px;
  right: -6px;
}
.flow-steps .is-finish .el-step__line {
  background-image: linear-gradient(#7ba8e8, #79a7e8, #5d92dd);
}

.flow-steps .is-process .el-step__line,
.flow-steps .is-wait .el-step__line {
  background-color: #e8eff8;
}
.flow-steps .el-step__title {
  line-height: unset;
  font-size: 14px;
  font-weight: unset;
  width: 90px;
  margin-left: -14px;
}
.flow-steps .el-step .el-step__main {
  top: 36px;
  position: absolute;
}
.flow-steps .el-step .el-step__main:nth-child(2) {
  top: 36px;
  position: absolute;
}
.flow-steps .title {
  text-align: center;
  display: block;
}
.flow-steps .is-finish .title {
  color: #3d3d3d;
}
.flow-steps .is-process .title {
  color: rgba(75, 125, 196, 0.8);
}
.flow-steps .title.up {
  margin-top: -60px;
}
.flow-steps .is-wait .title {
  color: #999999;
}
.flow-steps .el-step__head {
  margin-left: 26px;
}
.flow-steps .el-step__line-inner {
  border: none;
} */
</style>
