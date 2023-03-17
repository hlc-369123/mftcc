<template>
  <el-container class="appDetails">
    <el-dialog
      :visible.sync="visible"
      @close="$emit('update:show', false)"
      :show="show"
      v-dialogDrag
      ref="appDetails"
      :close-on-click-modal="false"
      custom-class="appDialog"
      append-to-body
    >
      <div slot="title" class="dialog-header" style="display: flex">
        <label>{{ title ? title : 审批详情 }}</label>
      </div>
      <div class="dialog-content">
        <el-alert
          v-show="messageShow"
          title="审批建议"
          type="warning"
          :description="approveGuide"
          close-text="知道了"
          @close="closeMessage"
          v-if="query !== true"
          style="position: absolute; top: 0; border-radius: 0"
          show-icon
        ></el-alert>
        <div :class="appClassName" style="height: 100%; overflow: auto">
          <component
            :is="page"
            v-bind="methodParam"
            ref="component"
            @pushMain="pushMain"
          ></component>
        </div>
        <el-drawer
          title="审批意见"
          :visible.sync="approveShow"
          direction="btt"
          :custom-class="'approveDrawer ' + (appPage ? 'appPage' : '')"
          :modal="false"
          :with-header="false"
        >
          <el-row v-if="appFormId != '' && appFormShow" class="appFormRow mf">
            <mftcc-form
              :formId="appFormId"
              :parentVm="this"
              ref="appBizForm"
            ></mftcc-form>
          </el-row>
          <el-row v-if="appPage && appFormShow" class="appFormRow">
            <div :class="appPageClassName">
              <component
                :is="page4app"
                v-bind="appPageMethodParam"
                ref="componentAppPage"
                @pushMain="pushMain"
              ></component>
            </div>
          </el-row>
          <el-form
            ref="appForm"
            :model="appForm"
            label-width="150px"
            :rules="appFormRules"
            class="appFormRow2"
          >
            <el-row>
              <el-col :span="6">
                <el-form-item :label="approvalTypeTitle" prop="type">
                  <el-select
                    v-model="appForm.type"
                    placeholder="请选择"
                    @change="selectType"
                  >
                    <el-option
                      v-for="approveBtn in approveBtnList"
                      :key="approveBtn.id"
                      :label="approveBtn.approveIdea"
                      :value="approveBtn.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="节点选择" prop="type" v-show="nodeShow">
                  <el-select v-model="nodeId" placeholder="请选择">
                    <template v-if="customNodeList">
                      <el-option
                        v-for="node in customNodeList"
                        :key="node.id"
                        :label="node.name"
                        :value="node.id"
                      ></el-option>
                    </template>
                    <template v-else>
                      <el-option
                        v-for="node in nodeList"
                        :key="node.id"
                        :label="node.name"
                        :value="node.id"
                      ></el-option>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="配置" prop="type" v-show="listBtnShow">
                  <el-button type="primary" @click="showNodeList">配置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-popover
                  v-if="approveTemplateList.length > 1"
                  ref="templatePopover"
                  placement="top-start"
                  popper-class="app-template"
                  trigger="click"
                >
                  <div
                    v-for="temp in approveTemplateList"
                    class="template-item"
                    :key="temp.id"
                    @click="selectTemplate(temp)"
                  >
                    {{ temp.approveTemplate }}
                  </div>
                  <el-button
                    type="text"
                    slot="reference"
                    style="margin-left: 150px; padding-top: 0px"
                    >审批常用语</el-button
                  >
                </el-popover>
                <el-form-item :label="approvalDescTitle" prop="approve" v-if="textShow">
                  <el-input
                    resize="none"
                    rows="3"
                    maxlength="500"
                    :autosize="{ minRows: 3, maxRows: 6 }"
                    show-word-limit
                    type="textarea"
                    v-model="appForm.approve"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-drawer>
        <el-drawer
          title="审批历史"
          :visible.sync="timeLineShow"
          direction="rtl"
          custom-class="timeLineDrawer"
          :modal="false"
          size="45%"
          :with-header="false"
        >
          <time-line
            :timeLineShow="timeLineShow"
            :traceNo="traceNo"
            :procId="procId"
            :bizMark="bizMark"
            :taskDefId="taskDefId"
          ></time-line>
        </el-drawer>
        <el-drawer
          title="审批示意图"
          :visible.sync="flowChartsShow"
          :modal="false"
          size="100%"
          :with-header="false"
        >
          <flow-charts
            :flowChartsShow="flowChartsShow"
            :traceNo="traceNo"
            :bizMark="bizMark"
            :procId="procId"
            :taskDefId="taskDefId"
            :opNo="$store.getters.user.opNo"
            type="query"
          ></flow-charts>
        </el-drawer>
        <el-dialog
          title="加签列表"
          :visible.sync="listShow"
          custom-class="addNodelog"
          :close-on-click-modal="false"
          :close-on-press-escape="false"
          :modal="false"
        >
          <div style="padding: 0 12px 12px">
            <el-button type="primary" @click="handleAdd">新增</el-button>
          </div>
          <mftcc-table
            tableId="flowable/addNodeList"
            :parentVm="this"
            ref="nodeList"
          ></mftcc-table>
          <div slot="footer" class="dialog-footer">
            <el-button @click="listShow = false">取 消</el-button>
            <el-button type="primary" @click="saveNode">确 定</el-button>
          </div>
        </el-dialog>
      </div>
      <div slot="footer" class="dialog-footer approve-footer">
        <div style="text-align: center">
          <el-button type="primary" @click="approve" v-if="query !== true">
            {{ approveBtnText }}
          </el-button>
          <el-button type="primary" @click="openTimeLine">审批历史</el-button>
          <el-button type="primary" @click="openFlowCharts">审批示意图</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>
    <mftcc-dialog-table
      @callback="userDialogCallback"
      :show.sync="userDialog"
      :parentVm="this"
      :option="userOption"
    ></mftcc-dialog-table>
    <mftcc-plt-user-table
      @callback="userDialogCallback"
      :show.sync="userAllDialog"
      :parentVm="this"
      :option="userAllOption"
    ></mftcc-plt-user-table>
    <mftcc-dialog-radio
      :show.sync="nodeIdRadio"
      :option="nodeIdOption"
      @callback="nodeIdCallback"
    ></mftcc-dialog-radio>
  </el-container>
</template>

<script>
import api from "@/api/flowable/flowable";
import wkfUserDialog from "../detail/wkfUserDialog";

export default {
  name: "appDetail",
  data() {
    return {
      page: null,
      page4app: null,
      timeLineShow: false,
      flowChartsShow: false,
      nodeShow: false,
      listShow: false,
      listBtnShow: false,
      textShow: true,
      approveShow: false,
      appFormShow: false,
      visible: this.show,
      approveBtnText: "审 批",
      appForm: {
        type: "",
        approve: "",
      },
      appFormRules: {
        type: [{ required: true, message: "请选择审批类型", trigger: "change" }],
      },
      messageShow: false,
      traceNo: "",
      procId: "",
      bizMark: "",
      approveGuide: "",
      approveBtnList: [],
      approveTemplateObj: {},
      approveTemplateList: [],
      nodeList: [],
      userDialog: false,
      userAllDialog: false,
      userOption: {},
      userAllOption: {},
      nodeIdRadio: false,
      nodeIdOption: {},

      taskId: "",
      taskDefId: "",
      node: {
        id: "",
        seqList: "",
      },
      nodeId: "",
      method: "",
      methodParam: {},
      lastMethodParam: {},
      appPageMethodParam: {},
      variablesMethod: "",
      appPageDataCheckMethod: "",
      variables: {},
      backNodeList: [],
      submitNodeList: [],
      addNodeList: [],
      initData: [],
      clickIndex: "",
      finishType: 0,
      appFormId: "",
      appPage: null,
      appFormInitMethod: "",
      appPageDataMethod: "",
      appBizFormData: null,
      appClassName: "",
      appPageClassName: "",
      selectUserMultiStr: "",
      userMultiList: [],
      approvalTypeTitle: "意见类型",
      approvalDescTitle: "审批说明",
      approvalBtnTitle: "审 批",
      noValidateApproveType: [],
      needValidate: true,
    };
  },
  props: ["show", "appData", "query", "customNodeList", "title"],
  watch: {
    approveShow(val) {
      if (val === true) {
        this.approveBtnText = "提 交";
        this.$nextTick(() => {
          this.appFormShow = true;
        });
      } else {
        this.approveBtnText = this.approvalBtnTitle;
        this.appFormShow = false;
      }
    },
    appData: {
      handler(row) {
        this.procId = row.PROC_INST_ID;
        if (!this.procId) {
          return;
        }
        this.bizMark = row.BIZ_ID;
        this.taskId = row.TASK_ID;
        this.taskDefId = row.TASK_DEF_ID;
        this.traceNo = row.TRACE_NO;
        this.finishType = row.finishType;
        this.getApprovalDetail(this.taskId, this.finishType);
      },
      immediate: true,
    },
    show() {
      this.visible = this.show;
      this.timeLineShow = false;
      this.flowChartsShow = false;
      this.nodeShow = false;
      this.textShow = true;
      this.listShow = false;
      this.listBtnShow = false;
      this.approveShow = false;
      this.page = null;
      this.page4app = null;
      this.selectUserMultiStr = "";
      this.approveTemplateList = [];
      if (this.show) {
        if (this.$refs.nodeList) {
          this.$refs.nodeList.option.tableData = [];
        }
        this.appForm = {
          type: "",
          approve: "",
        };
        this.$nextTick(() => {
          if (this.$refs.appForm) {
            this.$refs.appForm.clearValidate();
          }
        });
        let dragDom = this.$refs.appDetails.$el.firstChild;
        dragDom.style.top = 0;
        dragDom.style.left = 0;
        let DomClass = dragDom.className;
        DomClass = DomClass.replace("fullDialog", "");
        dragDom.className = DomClass;
        this.getApprovalDetail(this.taskId, this.finishType);

        //获取哪些审批类型不需要校验表单
        let dicKeyArray = ["PLT_NO_VALIDATE_APPROVE_TYPE"];
        this.$formUtil.getParmDic(dicKeyArray, (dicKeyData) => {
          this.noValidateApproveType = dicKeyData.PLT_NO_VALIDATE_APPROVE_TYPE;
        });
      }
    },
    "appForm.type": {
      handler(val) {
        let flag = false;
        for (let approveBtn of this.approveBtnList) {
          if (approveBtn.id == val && approveBtn.approveType == "1") {
            flag = true;
            break;
          }
        }
        if (!flag) {
          this.appFormRules.approve = [
            { required: true, message: "请填写审批说明", trigger: "blur" },
          ];
        } else {
          delete this.appFormRules.approve;
          // if (!this.appForm.approve) {
          //   this.appForm.approve = "同意";
          // }
        }
        this.textShow = false;
        this.$nextTick(() => {
          this.textShow = true;
        });
      },
      immediate: true,
    },
  },
  components: {
    wkfUserDialog,
  },
  methods: {
    /* 选择模板时触发 */
    selectTemplate(temp) {
      this.appForm.approve = temp.approveTemplate;
      this.$refs.templatePopover.doClose();
    },
    handleAdd() {
      this.$refs.nodeList.addTableRow();
    },
    userDialogGetAllCallback(value) {
      let fieldValue = "";
      for (let user of value) {
        fieldValue = user.id + "|" + fieldValue;
      }
      this.$refs.nodeList.setFormValue(this.clickIndex, "assigneeId", fieldValue);
    },
    saveNode() {
      this.listShow = false;
      this.addNodeList = this.$refs.nodeList.option.tableData;
    },
    showNodeList() {
      if (this.listShow) {
        this.listShow = false;
      } else {
        this.listShow = true;
      }
    },
    goback() {
      this.listShow = false;
    },
    edit(index, row) {
      this.$refs.nodeList.editTable(index, true);
    },
    delete(index, row) {
      this.$refs.nodeList.editTable(index, false);
    },
    selectType() {
      let approveBtnSub = "";
      this.nodeId = "";
      for (let approveBtn of this.approveBtnList) {
        if (approveBtn.id === this.appForm.type) {
          approveBtnSub = approveBtn;
        }
      }
      if (
        approveBtnSub.approveType == 5 ||
        approveBtnSub.approveType == 55 ||
        approveBtnSub.approveType == 50
      ) {
        this.nodeShow = false;
        this.textShow = true;
        this.listBtnShow = false;
        this.listShow = false;
        this.node = "";
        let data = {
          taskId: this.taskId,
        };
        if (approveBtnSub.approveType == 50) {
          if (this.movingNodes) {
            let nodeObj = JSON.parse(this.movingNodes);
            if (nodeObj[approveBtnSub.id]) {
              let nodeArr = nodeObj[approveBtnSub.id].nodeArr;
              if (nodeArr) {
                this.nodeList = nodeArr;
                this.$nextTick(() => {
                  this.nodeShow = true;
                });
                return;
              }
            }
          }
          api.getFinishParentNodeList(data, (res) => {
            if (res.code == 0) {
              this.nodeList = res.data.NodeList;
              this.$nextTick(() => {
                this.nodeShow = true;
              });
              this.backNodeList = res.data.NodeList;
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          api.getFinishNodeList(data, (res) => {
            if (res.code == 0) {
              this.nodeList = res.data.NodeList;
              this.$nextTick(() => {
                this.nodeShow = true;
              });
              this.backNodeList = res.data.NodeList;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      } else if (approveBtnSub.approveType == 2 || approveBtnSub.approveType == 22) {
        this.nodeShow = false;
        this.textShow = true;
        this.listBtnShow = false;
        this.listShow = false;
        this.node = "";
        let data = {
          taskId: this.taskId,
        };
        if (approveBtnSub.approveType == 2) {
          api.getNextAllNodeList(data, (res) => {
            if (res.code == 0) {
              this.nodeList = res.data.NodeList;
              this.$nextTick(() => {
                this.nodeShow = true;
              });
              this.submitNodeList = res.data.NodeList;
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          api.getFinishNodeList(data, (res) => {
            if (res.code == 0) {
              this.nodeList = res.data.NodeList;
              this.$nextTick(() => {
                this.nodeShow = true;
              });
              this.submitNodeList = res.data.NodeList;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      } else if (approveBtnSub.approveType == 7 || approveBtnSub.approveType == 8) {
        this.textShow = false;
        this.nodeShow = false;
        this.listBtnShow = true;
      } else {
        this.nodeList = [];
        this.nodeShow = false;
        this.textShow = true;
        this.listBtnShow = false;
        this.listShow = false;
      }
      this.handlerAppTemplate(approveBtnSub);
    },
    handlerAppTemplate(btn) {
      let ids = this.approveTemplateObj[btn.id];
      if (ids) {
        api.findTemplateByIds({ id: ids }, (res) => {
          if (res.code == 0) {
            this.approveTemplateList = res.list;
            if (this.approveTemplateList.length == 1) {
              this.appForm.approve = this.approveTemplateList[0].approveTemplate;
            }
          }
        });
      } else {
        this.approveTemplateList = [];
      }
    },
    nodeIdCallback(node) {
      this.node = node;
      if (node.needAssign === true) {
        if (node.assignList == "getAll") {
          this.userAllOption = {
            title: "选择处理人",
          };
          this.userAllDialog = true;
        } else {
          this.userOption = {
            title: "选择处理人",
            tableId: "sys/flowableUserList",
            search: false,
            type: "radio", //checkbox,radio
            data: [],
            value: [],
            initSearchData: {
              assignList: node.assignList,
            },
            isPagination: true,
            url: "/" + $servers.flowable + "/user/pageByUserId",
          };
          this.userDialog = true;
        }
      } else {
        this.$confirm(
          "此操作将提交该笔业务到" + node.name + "节点",
          "是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).then(() => {
          this.doCommit(node.id, node.seqList, "", "");
        });
      }
    },
    userDialogCallback(user) {
      let opNo = "";
      if (user.length > 0) {
        for (let i = 0; i < user.length; i++) {
          opNo += user[i].opNo;
          if (i < user.length - 1) {
            opNo += ",";
          }
        }
        if (this.selectUserMultiStr) {
          this.selectUserMultiStr += "|" + opNo;
        } else {
          this.selectUserMultiStr = opNo;
        }
        if (this.userMultiList && this.userMultiList.length > 0) {
          this.userMultiList.shift();
          // this.userMultiList = this.userMultiList.splice(0, 1);
          if (this.userMultiList.length > 0) {
            this.selectUserMulti(this.userMultiList[0]);
            return;
          }
        }
        this.$confirm("此操作将提交该笔业务, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.doCommit(this.node.id, this.node.seqList, this.selectUserMultiStr, "");
            this.node = {
              id: "",
              seqList: this.node.seqList,
            };
          })
          .catch(() => {
            this.selectUserMultiStr = "";
          });
      } else {
        this.$alert("请选择审批人员", "提示", {
          type: "error",
          dangerouslyUseHTMLString: true,
        });
      }
    },
    cancel() {
      if (this.approveShow) {
        this.approveShow = false;
      } else {
        this.visible = false;
      }
    },
    closeMessage() {
      this.messageShow = false;
    },
    needOperated() {
      let approveBtnSub = "";
      for (let approveBtn of this.approveBtnList) {
        if (approveBtn.id === this.appForm.type) {
          approveBtnSub = approveBtn;
        }
      }
      api.needOperated(
        {
          taskId: this.taskId,
          approveType: approveBtnSub.approveType,
          targetNodeId: this.nodeId,
          variables: JSON.stringify(this.variables),
        },
        (res) => {
          if (res.code == 0) {
            if (res.hasComplete === 0) {
              this.userMultiList = [];
              this.userMultiList = res.result.list;
              if (this.userMultiList) {
                this.selectUserMulti(this.userMultiList[0]);
              } else {
                let nodeList = res.result.nodeList;
                if (nodeList !== undefined && nodeList !== null) {
                  this.nodeIdRadio = true;
                  this.nodeIdOption = {
                    realField: "id",
                    showField: "name",
                    search: true,
                    insert: false,
                    group: false,
                    data: nodeList.list,
                    value: "",
                  };
                } else {
                  this.node = {
                    id: res.result.targetFlowId,
                    seqList: res.result.seqList[0],
                  };
                  if (res.result.assignList == "getAll") {
                    this.userAllOption = {
                      title: "选择处理人",
                      type: "checkbox",
                    };
                    this.userAllDialog = true;
                  } else {
                    this.userOption = {
                      title: "选择审批人",
                      tableId: "sys/flowableUserList",
                      search: false,
                      type: "checkbox", //checkbox,radio
                      data: [],
                      value: [],
                      initSearchData: {
                        assignList: res.result.assignList,
                      },
                      isPagination: true,
                      url: "/" + $servers.flowable + "/user/pageByUserId",
                    };
                    this.userDialog = true;
                  }
                }
              }
            } else {
              this.$confirm("此操作将提交该笔业务, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }).then(() => {
                this.doCommit();
              });
            }
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
          }
        }
      );
    },
    selectUserMulti(data) {
      if (this.node.id) {
        this.node.id += "|" + data.targetFlowId;
      } else {
        this.node.id = data.targetFlowId;
      }
      this.userOption = {
        title: "选择【" + data.nodeName + "】审批人",
        tableId: "sys/flowableUserList",
        search: false,
        type: "checkbox", //checkbox,radio
        data: [],
        value: [],
        initSearchData: {
          assignList: data.assignList,
        },
        isPagination: true,
        url: "/" + $servers.flowable + "/user/pageByUserId",
      };
      this.userDialog = true;
    },
    doCommit(targetNodeId, seqList, nextUserId, listStr) {
      let approveBtnSub = "";
      for (let approveBtn of this.approveBtnList) {
        if (approveBtn.id === this.appForm.type) {
          approveBtnSub = approveBtn;
        }
      }
      let flowType;
      if (approveBtnSub.approveType == "1" && nextUserId != null) {
        flowType = "11";
      } else {
        flowType = approveBtnSub.approveType;
      }
      targetNodeId = targetNodeId || this.nodeId;
      if (!targetNodeId) {
        targetNodeId = this.node.id || "";
      }
      // if (approveBtnSub.approveType == 2 || approveBtnSub.approveType == 5) {
      // targetNodeId = this.node || "";
      // }
      if (approveBtnSub.approveType == 7 || approveBtnSub.approveType == 8) {
        if (this.addNodeList.length == 0) {
          this.$alert("请配置节点信息", "提示", {
            type: "error",
            dangerouslyUseHTMLString: true,
          });
          return;
        } else {
          var nodes = [];
          for (var i = 0; i < this.addNodeList.length; i++) {
            var node = {};
            node["name"] = this.addNodeList[i].nodeName;
            node["assignee"] = this.addNodeList[i].assigneeId.join(",");
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
        targetNodeId: targetNodeId,
        listStr: listStr,
        nextUserId: nextUserId,
        seqList: seqList,
        bizMark: this.bizMark,
        variables: this.variables,
        methodParam: this.methodParam,
      };

      if (this.appBizFormData) {
        data.appBizFormData = JSON.stringify(this.appBizFormData);
      }
      if (this.appPageDataMethod) {
        if (typeof this.$refs.componentAppPage[this.appPageDataMethod] == "function") {
          this.$refs.componentAppPage[this.appPageDataMethod]((res) => {
            data.appPageData = res;
            this.doCommit4Iframe(data);
          }, this.needValidate);
        } else {
          this.$alert(
            "未找到审批弹层页面数据回调函数【" + this.appPageDataMethod + "】",
            "提示",
            {
              type: "error",
              dangerouslyUseHTMLString: true,
            }
          );
        }
      } else {
        this.doCommit4Iframe(data);
      }
    },
    doCommit4Iframe(data) {
      this.approveShow = false;
      if (typeof this.$refs.component[this.method] == "function") {
        this.$refs.component[this.method](JSON.stringify(data), (res) => {
          if (res.code == 0) {
            this.$message.success(res.msg);
            this.visible = false;
            this.$emit("submitSuccess");
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
          }
        });
      } else {
        this.$alert("未找到审批提交函数【" + this.method + "】", "提示", {
          type: "error",
          dangerouslyUseHTMLString: true,
        });
      }
    },
    approve() {
      debugger
      if (this.approveShow) {
        if (this.appFormId != "") {
          let flag = false;
          for (let approveBtn of this.approveBtnList) {
            if (approveBtn.id == this.appForm.type && approveBtn.approveType == "1") {
              flag = true;
              break;
            }
          }
          /* 如果是流程流转类型的审批意见，则需要校验弹出的表单 */
          if (flag) {
            this.$refs.appBizForm.validateForm((valid) => {
              if (valid === true) {
                this.appBizFormData = this.$refs.appBizForm.getFormValue();
                this.appFormSubmit();
              }
            });
          } else {
            /* 否则不需要校验表单 */
            this.appBizFormData = this.$refs.appBizForm.getFormValue();
            this.appFormSubmit();
          }
        } else {
          this.appFormSubmit();
        }
      } else {
        this.approveShow = true;
        if (this.appFormId != "") {
          if (this.appFormInitMethod) {
            if (typeof this.$refs.component[this.appFormInitMethod] == "function") {
              this.$refs.component[this.appFormInitMethod]((res) => {
                this.$refs.appBizForm.setFormValue(res);
              });
            }
          }
        }
      }
    },
    appFormSubmit() {
      let approveBtnSub = "";
      for (let approveBtn of this.approveBtnList) {
        if (approveBtn.id === this.appForm.type) {
          approveBtnSub = approveBtn;
        }
      }
      //是否需要校验表单，true校验，false不校验
      if (this.noValidateApproveType.length > 0) {
        for (let index in this.noValidateApproveType) {
          var optCode = this.noValidateApproveType[index].optCode;
          if (optCode == approveBtnSub.approveType) {
            this.needValidate = false;
            break;
          }
        }
      }
      if (this.needValidate && this.appPageDataCheckMethod) {
        if (typeof this.$refs.componentAppPage[this.appPageDataCheckMethod] == "function") {
          this.$refs.componentAppPage[this.appPageDataCheckMethod]((res) => {
            if (res) {
              this.validateToNeedOperated();
            }
          });
        }
      } else {
        this.validateToNeedOperated();
      }
    },
    validateToNeedOperated() {
      this.$refs.appForm.validate((valid) => {
        if (valid === true) {
          if (!this.method) {
            this.$alert("缺少审批提交函数", "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
            return;
          }
          if (this.variablesMethod) {
            if (typeof this.$refs.component[this.variablesMethod] == "function") {
              let formData = {};
              if (this.$refs.appBizForm) {
                formData = this.$refs.appBizForm.getFormValue();
              }
              this.$refs.component[this.variablesMethod](formData, (res) => {
                this.variables = res;
                this.needOperated();
              });
            }
          } else {
            this.needOperated();
          }
        }
      });
    },
    showMessage() {
      this.$message({
        type: "warning",
        dangerouslyUseHTMLString: true,
        message: "<strong>这是 <i>HTML</i> 片段</strong>",
      });
    },
    openTimeLine() {
      if (this.timeLineShow) {
        this.timeLineShow = false;
      } else {
        this.timeLineShow = true;
      }
    },
    openFlowCharts() {
      if (this.flowChartsShow) {
        this.flowChartsShow = false;
      } else {
        this.flowChartsShow = true;
      }
    },
    getApprovalDetail(taskId, taskType) {
      let data = {
        taskId: taskId,
        taskType: taskType,
        bizMark: this.bizMark,
      };
      api.getApprovalDetail(data, (res) => {
        if (res.code == 0) {
          this.methodParam = res.data.methodParam || {};
          this.methodParam.traceNo = this.traceNo;

          this.method = res.data.method;
          this.variablesMethod = res.data.variablesMethod;
          this.appPageDataCheckMethod = res.data.appPageDataCheckMethod;
          if (res.data.showGuide == "1") {
            this.messageShow = true;
          } else {
            this.messageShow = false;
          }

          this.resAppPage(res.data.formUrl);

          if (res.data.approveGuide) {
            this.approveGuide = res.data.approveGuide.guide;
          }
          this.approveBtnList = res.data.approveBtnList;
          if (this.approveBtnList && this.approveBtnList.length == 1) {
            this.appForm.type = this.approveBtnList[0].id;
          }
          let approvalTemplate = res.data.approvalTemplate;
          if (approvalTemplate) {
            this.approveTemplateObj = JSON.parse(approvalTemplate);
          }

          this.appPage = res.data.appPage;

          this.resAppPageInner(this.appPage);
          if (!this.appPage) {
            this.appFormId = res.data.appFormId;
            this.appFormInitMethod = res.data.appFormInitMethod;
          }
          this.appPageMethodParam = res.data.appPageMethodParam || {};
          this.appPageDataMethod = res.data.appPageDataMethod;
          if (res.data.approvalTypeTitle) {
            this.approvalTypeTitle = res.data.approvalTypeTitle;
          }
          if (res.data.approvalDescTitle) {
            this.approvalDescTitle = res.data.approvalDescTitle;
          }
          if (res.data.approvalBtnTitle) {
            this.approvalBtnTitle = res.data.approvalBtnTitle;
            this.approveBtnText = this.approvalBtnTitle;
          }

          this.movingNodes = res.data.movingNodes;
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
        .then((res) => {
          res.loadApp().then((res) => {
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
    pushMain(url, query) {
      this.lastMethodParam = JSON.parse(JSON.stringify(this.methodParam));
      if (query) {
        this.methodParam = query;
      }
      this.resAppPage(url);
    },
  },
};
</script>
<style>
.appDialog {
  margin-top: 6vh !important;
  width: 90%;
  height: 90%;
  display: flex;
  flex-direction: column;
}
.fullDialog {
  margin: 0 !important;
  width: 100%;
  height: 100%;
}
.appDialog > .el-dialog__body {
  height: calc(100% - 52px);
  padding: 0px 20px 10px 20px;
  overflow: auto;
}
.appDialog > .el-dialog__body > .dialog-content {
  height: 100%;
  overflow: auto;
  position: relative;
}
.appDialog > .el-dialog__body > .dialog-content > .component {
  height: calc(100% - 78px);
  overflow-y: auto;
  margin-top: 20px;
}
.appDialog > .el-dialog__body > .dialog-content > .component-full {
  height: calc(100% - 20px);
  overflow-y: auto;
  margin-top: 20px;
}
.appDialog .el-drawer__wrapper {
  position: absolute;
  /* margin-bottom: 70px; */
}
.approveDrawer,
.timeLineDrawer {
  outline: none;
  /* padding: 25px; */
  height: auto !important;
  overflow: auto;
  max-height: 100%;
  flex-direction: row;
}
.approveDrawer.appPage {
  height: 100% !important;
}
.timeLineDrawer > .el-drawer__body {
  height: 100%;
}
.addNodelog {
  height: 60%;
}
.addNodelog > .el-dialog__body {
  height: 70%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}
.addNodelog > .el-dialog__body > .dialog-content {
  height: 100%;
}
.addNodelog > .el-dialog__body > .dialog-content > .component {
  height: 90%;
  overflow-y: auto;
  margin-top: 20px;
}
.appDialog .appFormRow {
  margin-bottom: 22px;
  /* height: calc(100% - 160px); */
  overflow: auto;
  border-bottom: 1px solid #f1f1f1;
}
.appDialog .appFormRow.mf {
  margin: 0;
  padding: 20px 10px 0;
}
.appDialog .appFormRow2 {
  text-align: left;
  height: 160px;
  padding: 10px;
}
.app-template {
  max-width: 800px;
}
.app-template .template-item {
  cursor: pointer;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  padding: 8px;
  line-height: 16px;
  color: #606266;
}
.app-template .template-item:hover {
  background: #f5f7fa;
  color: #409eff;
}
</style>
