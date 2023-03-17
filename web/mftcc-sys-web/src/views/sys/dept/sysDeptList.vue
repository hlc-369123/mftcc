<template>
  <el-container class="sys-dept">
    <el-aside width="350px" class="dept-border">
      <div style="margin-left: 80px; margin-top: 20px; margin-bottom: 30px">
        <el-button type="primary" @click="addCorpBr">新增法人机构</el-button>
      </div>
      <el-tree
        :data="sysDeptData"
        :props="sysDeptProps"
        node-key="brId"
        ref="sysDeptTree"
        :highlight-current="true"
        :check-on-click-node="true"
        :expand-on-click-node="false"
        :default-expanded-keys="[0]"
        :render-content="renderContent"
        @node-click="sysDeptNodeClick"
      >
      </el-tree>
      <mftcc-dialog-form
        formId="sys/sysDeptInsert"
        :title="sysDeptFormTitle"
        :show.sync="sysDeptFormVisible"
        :center="true"
        :parentVm="this"
        class="sys-dept-mole-dialog"
        ref="sysDeptMoleDialog"
        confirmButtonText="保 存"
        @callback="commitSysDept"
      ></mftcc-dialog-form>
      <!-- <el-dialog
            :title="sysDeptFormTitle"
            :visible.sync="sysDeptFormVisible"
            :close-on-click-modal="false"
            :center="true"
            class="sys-dept-mole-dialog">
            <div slot="footer" class="dialog-footer">
                <mftcc-form
                    formId="sys/sysDeptInsert"
                    ref="sysDeptForm"></mftcc-form>
              <el-button
                type="primary"
                @click="commitSysDept" :style="{ display: visibleCommit }">保 存</el-button>
                <el-button
                    @click="sysDeptFormVisible = false" >取 消</el-button>
            </div>
    </el-dialog> -->
    </el-aside>
    <el-container class="dept-border search-container">
      <el-main>
        <mftcc-table
          tableId="sys/sysDeptList"
          :parentVm="this"
          ref="sysDeptList"
        ></mftcc-table>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import sysDeptApi from "@/api/sys/sysDept";
export default {
  name: "sysDept",
  title: "机构管理",
  data() {
    return {
      sysDeptFormVisible: false,
      sysDeptFormTitle: "类别",
      sysDeptFormType: "1",
      sysDeptData: [],
      sysDeptProps: {
        children: "children",
        label: "brName",
      },
      sysDeptPageNo: 1,
      sysDeptPageSize: 10,
      sysDeptPageTotal: 0,
      sysDeptSelectRow: null,
      selectBrId: "",
      visibleCommit: "",
      pramDicItem: {},
      brData: {},
    };
  },
  methods: {
    isCorpChange(value) {
      this.$refs.sysDeptMoleDialog.getFormRef((form) => {
        if (value == "0") {
          form.setFormValue("corpId", this.brData.corpId);
        }
      });
    },
    getSysDepTreeList() {
      let _this = this;
      sysDeptApi.getList(
        "",
        (reponse) => {
          if (reponse.code === 0) {
            _this.sysDeptData = reponse.list;
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {
                _this.$router.back(-1);
              },
            });
          }
        },
        (error) => {
          _this.$alert("获取失败", "提示", {
            type: "error",
            callback: (action) => {
              _this.$router.back(-1);
            },
          });
        }
      );
    },
    renderContent(h, { node, data }) {
      return (
        <span class="dept-tree-node">
          <span style="display: flex;align-items: center;">
            <span class={node.data.isCorp == "1" ? "isCorp" : "isNotCorp"}>法人</span>
            {node.label}
          </span>
          <span>
            <i
              class="el-icon-plus"
              title="新增机构"
              on-click={() => this.sysDeptTreeFun(node, "insert")}
            />
            <i
              class="el-icon-edit"
              title="修改机构"
              on-click={() => this.sysDeptTreeFun(node, "update")}
            />
            <i
              class="el-icon-delete"
              title="删除"
              on-click={() => this.sysDeptTreeFun(node, "delete")}
            />
          </span>
        </span>
      );
    },
    commitSysDept(data) {
      debugger;
      let _this = this;
      if (_this.sysDeptFormType === "1") {
        sysDeptApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            data.brId = reponse.brId;
            _this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.sysDeptFormVisible = false;
                _this.getSysDepTreeList();
                _this.onSearch();
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (_this.sysDeptFormType === "2") {
        sysDeptApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.getSysDepTreeList();
                _this.onSearch();
                _this.sysDeptFormVisible = false;
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      }
    },

    addCorpBr() {
      let corpBr = new Object();
      corpBr["brId"] = "0";
      corpBr["brNo"] = "0";
      corpBr["upOne"] = "0";
      corpBr["brLvl"] = 0;
      corpBr["corpId"] = "";
      let node = new Object();
      node["data"] = corpBr;
      this.sysDeptTreeFun(node, "insert");
    },

    sysDeptNodeClick(data) {
      this.selectBrId = data.brId;
      this.onSearch();
    },
    sysDeptTreeFun(node, type) {
      let data = node.data;
      this.brData = data;
      let _this = this;
      switch (type) {
        case "insert":
          _this.visibleCommit = "";
          _this.sysDeptFormTitle = "新增";
          _this.sysDeptFormType = "1";
          _this.sysDeptFormVisible = true;
          _this.$refs.sysDeptMoleDialog.getFormRef((form) => {
            form.resetForm(function () {
              debugger;
              form.setFormValue("parentBrId", data.brId);
              form.setFormValue("upOne", data.brNo);
              form.setFormValue("upTwo", data.upOne == null ? "0" : data.upOne);
              form.setFormValue("brLvl", data.brLvl + 1);
              form.setFormValue("corpId", data.corpId);
            });
          });
          break;
        case "update":
          if (data.brId === 0) {
            this.$message.error("不能修改根节点");
            return false;
          }
          _this.visibleCommit = "";
          _this.sysDeptFormTitle = "修改";
          _this.sysDeptFormType = "2";
          _this.sysDeptFormVisible = true;
          _this.$refs.sysDeptMoleDialog.getFormRef((form) => {
            form.resetForm(function () {
              form.setFormValue(data);
            });
          });
          break;
        case "delete":
          if (data.brId === 0) {
            _this.$message.error("不能删除根节点");
            return false;
          }
          _this
            .$confirm("此操作将永久删除该类型与其子类型, 是否继续?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            })
            .then(() => {
              sysDeptApi.delBatch(data.brId, (reponse) => {
                if (reponse.code === 0) {
                  _this.$message({
                    type: reponse.msgType,
                    message: reponse.msg,
                  });
                  _this.getSysDepTreeList();
                  _this.onSearch();
                  _this.sysDeptFormVisible = false;
                } else {
                  _this.$message.error(reponse.msg);
                  _this.$alert(reponse.msg, _this.$alertTitle, {
                    type: reponse.msgType,
                    dangerouslyUseHTMLString: true,
                    callback: (action) => {},
                  });
                }
              });
            });
          break;
      }
    },
    onSearch() {
      //   let formData = this.$refs.sysDeptSearchForm.getFormValue();
      //   if(this.selectBrId!=""){
      //     formData["brId"]=this.selectBrId;
      //   }
      this.$refs.sysDeptList.search({ brId: this.selectBrId });
    },
    edit(index, row) {
      this.$router.push({ path: "/sys/dept/sysDeptUpdate/", query: { brId: row.brId } });
    },
    delete(index, row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        sysDeptApi.deleteById(row.brId, (reponse) => {
          if (reponse.code === 0) {
            this.$message({
              type: reponse.msgType,
              message: reponse.msg,
            });
            this.getSysDepTreeList();
            this.$refs.sysDeptList.refresh();
          } else {
            this.$alert(reponse.msg, this.$alertTitle, {
              type: reponse.msgType,
              dangerouslyUseHTMLString: true,
              callback: (action) => {},
            });
          }
        });
      });
    },
  },
  created() {
    this.getSysDepTreeList();
  },
};
</script>
<style>
.sys-dept {
  padding: 20px;
}
.sys-dept .dept-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.sys-dept .dept-tree-node .isCorp {
  background: #f5555bf7;
  color: #fff;
  border-radius: 5px;
  font-size: 12px;
  transform: scale(0.7, 0.7);
  display: inline-block;
  padding: 1px 3px;
}
.sys-dept .dept-tree-node .isNotCorp {
  display: none;
}
.sys-dept .dept-tree-node i {
  margin-left: 10px;
}
.sys-dept .dept-border {
  border: solid 1px #ddd;
  border-radius: 4px;
}
.sys-dept .dept-border .el-tree {
  margin: 10px;
}
.sys-dept .search-container {
  margin-left: 10px !important;
}
.sys-dept .search-container .el-header {
  width: 98%;
  margin: 10px auto;
}
.sys-dept .search-container .el-header .el-row {
  padding-top: 5px;
  height: 60px;
  width: 100%;
}
.sys-dept .search-container .el-header .el-row .el-form {
  float: left;
  width: 85%;
}
.sys-dept .search-container .el-header .el-row .search-button {
  margin-top: 5px;
  float: right;
}

.sys-dept .sys-dept-mole-dialog .el-dialog__body {
  padding: 0px !important;
}
.sys-dept .sys-dept-mole-dialog .el-dialog__footer {
  padding: 25px 0px 30px;
}
.sys-dept .sys-dept-mole-dialog .el-form {
  width: 90%;
  margin: 0 auto;
  margin-bottom: 30px;
}
</style>
