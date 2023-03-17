<template>
  <el-container class="moblie-content">
    <!-- <el-header class="moblie-theme-header" height="48px">
      <div class="moblie-header">
        <i class="el-icon-arrow-left" @click="goBack"></i>
        <div>审批</div>
      </div>
    </el-header> -->
    <el-main style="position: relative;padding:16px;">
      <el-form
        ref="appForm"
        :model="appForm"
        :rules="appFormRules"
        label-position="top"
      >
        <el-form-item label="意见类型" prop="type">
          <div class="mobile-app-btns">
            <template v-for="btn in approveBtnList">
              <div
                :key="btn.id"
                :class="
                  'mobile-app-btn ' +
                    btn.class +
                    (approveBtn.id == btn.id ? ' active' : '')
                "
                @click="appBtnActive(btn)"
              >
                {{ btn.approveIdea }}
              </div>
            </template>
          </div>
          <!-- <el-select
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
          </el-select> -->
        </el-form-item>
        <el-form-item label="节点选择" prop="type" v-show="nodeShow">
          <el-select v-model="appForm.targetNodeId" placeholder="请选择">
            <el-option
              v-for="node in nodeList"
              :key="node.id"
              :label="node.name"
              :value="node.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="signName" prop="type" v-show="listBtnShow">
          <el-button
            type="primary"
            icon="el-icon-plus"
            class="addSignBtn"
            @click="addSignNode"
            >添加节点</el-button
          >
        </el-form-item>
        <el-form-item label="审批说明" prop="approve" v-if="textShow">
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
      </el-form>
      <el-button
        type="primary"
        class="submit-btn"
        @click="approve"
        v-if="finishType != '1'"
        >审 批</el-button
      >
    </el-main>
  </el-container>
</template>

<script>
import api from "@/api/flowable/flowable";
export default {
  data() {
    return {
      approveBtn: {},
      appForm: {
        type: "",
        approve: "",
        targetNodeId: ""
      },
      signName: "",
      appFormRules: {
        type: [
          { required: true, message: "请选择审批类型", trigger: "change" }
        ],
        approve: [
          { required: true, message: "请填写审批说明", trigger: "blur" }
        ]
      },
      backNodeHasSearch: false,
      submitNodeHasSearch: false,
      backNodeList: [],
      submitNodeList: [],
      nodeList: [],
      nodeShow: false,
      listBtnShow: false,
      textShow: true
    };
  },
  watch: {},
  props: ["taskId", "finishType", "bizMark", "approveBtnList"],
  mounted() {},
  methods: {
    approve() {
      this.$refs.appForm.validate(valid => {
        if (valid === true) {
          this.$confirm("此操作将提交该笔业务, 是否继续?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            let data = {
              appForm: this.appForm,
              approveBtn: this.approveBtn
            };
            this.$emit("approve", data);
          });
        }
      });
    },
    addSignNode() {},
    appBtnActive(btn) {
      this.approveBtn = btn;
      this.appForm.type = btn.id;
      /* if (this.approveBtn.approveType != "1") {
        this.appFormRules.approve = [
          { required: true, message: "请填写审批说明", trigger: "blur" }
        ];
      } else {
        delete this.appFormRules.approve;
      }
      this.textShow = false;
      this.$nextTick(() => {
        this.textShow = true;
      }); */

      if (
        btn.approveType == 5 ||
        btn.approveType == 55 ||
        btn.approveType == 50
      ) {
        this.nodeShow = true;
        this.listBtnShow = false;
        this.node = "";
        if (this.backNodeHasSearch) {
          this.nodeList = this.backNodeList;
        } else {
          let data = {
            taskId: this.taskId
          };
          if (btn.approveType == 50) {
            api.getFinishParentNodeList(data, res => {
              if (res.code == 0) {
                this.nodeList = res.data.NodeList;
                this.backNodeList = res.data.NodeList;
                this.backNodeHasSearch = true;
              } else {
                this.$message.error(res.msg);
              }
            });
          } else {
            api.getFinishNodeList(data, res => {
              if (res.code == 0) {
                this.nodeList = res.data.NodeList;
                this.backNodeList = res.data.NodeList;
                this.backNodeHasSearch = true;
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      } else if (btn.approveType == 2) {
        this.nodeShow = true;
        this.listBtnShow = false;
        this.node = "";
        if (this.submitNodeHasSearch) {
          this.nodeList = this.submitNodeList;
        } else {
          let data = {
            taskId: this.taskId
          };
          api.getNextAllNodeList(data, res => {
            if (res.code == 0) {
              this.nodeList = res.data.NodeList;
              this.submitNodeList = res.data.NodeList;
              this.submitNodeHasSearch = true;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      } else if (btn.approveType == 7 || btn.approveType == 8) {
        if (btn.approveType == 7) {
          this.signName = "前加签";
        } else {
          this.signName = "后加签";
        }
        this.nodeShow = false;
        this.listBtnShow = true;
      } else {
        this.nodeList = [];
        this.nodeShow = false;
        this.listBtnShow = false;
      }
    }
    // goBack() {
    //   this.$router.back(-1);
    // }
  }
};
</script>
<style>
.moblie-theme-header {
  background-color: #51a0fa;
  color: #ffffff;
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
.moblie-content .el-form-item__label {
  font-size: 14px;
  color: #666666;
  padding-bottom: 5px;
}
.moblie-content .mobile-app-btns {
  display: flex;
  flex-wrap: wrap;
  margin-left: -8px;
}
.moblie-content .mobile-app-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 36px;
  background: #ffffff;
  border-radius: 2px;
  padding: 0 15px;
  margin: 0 0 8px 8px;
}
.moblie-content .mobile-app-btn.qian {
  border: 1px solid #ff7e00;
  color: #ff7e00;
}
.moblie-content .mobile-app-btn.hou {
  color: #61a9fa;
  border: 1px solid #61a9fa;
}
.moblie-content .mobile-app-btn.over {
  border: 1px solid #f66c5e;
  color: #f66c5e;
}
.moblie-content .mobile-app-btn.qian.active {
  background-color: #ff7e00;
  color: #fff;
}
.moblie-content .mobile-app-btn.hou.active {
  background-color: #61a9fa;
  color: #fff;
}
.moblie-content .mobile-app-btn.over.active {
  background-color: #f66c5e;
  color: #fff;
}
.moblie-content .el-select {
  width: 100%;
}
.moblie-content .el-input__inner,
.moblie-content .el-textarea__inner {
  border-radius: 2px;
}
.moblie-content .submit-btn {
  width: 100%;
  border-radius: 2px !important;
  height: 50px !important;
  opacity: 1;
  background: #61a9fa;
  font-weight: bold;
  text-align: center;
  font-size: 16px;
  margin: 0 !important;
}
.moblie-content .addSignBtn {
  margin: 0 !important;
  border-radius: 2px !important;
  padding: 0 8px !important;
}
</style>
