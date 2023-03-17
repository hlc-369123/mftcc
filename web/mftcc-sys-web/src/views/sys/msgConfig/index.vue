<template>
  <el-container class="sys-msg-config">
    <el-aside
      width="300px"
      class="msg-border"
      style="margin: 10px 0 10px 10px; overflow-x: hidden"
    >
      <div style="margin: 20px; text-align: right">
        <el-button type="primary" size="mini" @click="insertRootParmMold">新增</el-button>
        <!-- <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" circle @click="insertParmMold()"></el-button>
        <el-button type="primary" icon="el-icon-edit" size="small" circle @click="updateParmMold()"></el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" circle @click="deleteParmMold()"></el-button> -->
      </div>
      <el-tree
        :data="sysMsgMoldData"
        :props="sysMsgMoldProps"
        node-key="moldId"
        ref="sysMsgMoldTree"
        :highlight-current="true"
        :check-on-click-node="true"
        :expand-on-click-node="false"
        :render-content="renderContent"
        accordion
        @node-click="sysMsgMoldNodeClick"
      >
      </el-tree>
      <mftcc-dialog-form
        formId="sys/sysParmMoleForm"
        :title="sysMsgMoldFormTitle"
        :show.sync="sysMsgMoldFormVisible"
        :center="true"
        :parentVm="this"
        class="msg-mole-dialog"
        ref="msgMoleDialog"
        confirmButtonText="保 存"
        @callback="commitsysMsgMold"
      ></mftcc-dialog-form>
      <!-- <el-dialog 
            :title="sysMsgMoldFormTitle" 
            :visible.sync="sysMsgMoldFormVisible" 
            :close-on-click-modal="false"
            :center="true"
            class="msg-mole-dialog"> 
            <div slot="footer" class="dialog-footer">
                <mftcc-form 
                    formId="sys/sysMsgMoldForm" 
                    ref="sysMsgMoldForm"></mftcc-form>
                <el-button 
                    @click="sysMsgMoldFormVisible = false">取 消</el-button>
                <el-button 
                    type="primary" 
                    @click="commitsysMsgMold">保 存</el-button>
            </div>
    </el-dialog> -->
    </el-aside>
    <el-container class="msg-border msg-container">
      <el-header class="msg-border">
        <el-row style="width: 100%">
          <el-col :span="22">
            <!-- <div>
                    <mftcc-form 
                        formId="sys/sysMsgSearchForm" 
                        ref="sysMsgSearchForm"></mftcc-form> 
                    <el-button 
                        type="primary" 
                        @click="onSearch" class="search-button">查询</el-button>
                </div> -->
            <div class="custom-search">
              <mftcc-search-form
                formId="sys/sysMsgSearchForm"
                ref="sysMsgSearchForm"
              ></mftcc-search-form>
            </div>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="onSearch" class="search-button"
              >查询</el-button
            >
            <el-button
              type="primary"
              icon="el-icon-refresh"
              size="mini"
              circle
              @click="initCaChe"
              class="cache-button"
            ></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <div class="msg-border sys-msg-config-table">
          <div class="sys-msg-config-insert">
            <el-button type="primary" size="mini" @click="sysMsgConfigFunc('', 'insert')"
              >新增</el-button
            >
          </div>
          <el-table
            ref="sysMsgConfigList"
            :data="sysMsgConfigData"
            stripe
            :highlight-current-row="true"
            height="86%"
          >
            <el-table-column prop="msgCode" label="信息码"> </el-table-column>
            <el-table-column prop="msgCnt" label="信息模板"> </el-table-column>
            <el-table-column prop="msgLvl" label="信息级别"> </el-table-column>
            <el-table-column prop="msgType" label="信息类型"> </el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button
                  @click="sysMsgConfigFunc(scope.row, 'update')"
                  type="text"
                  size="small"
                  :disabled="scope.row.edit === '0'"
                  >修改</el-button
                >
                <el-button
                  @click="sysMsgConfigFunc(scope.row, 'delete')"
                  type="text"
                  size="small"
                  :disabled="scope.row.edit === '0'"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            hide-on-single-page
            background
            small
            @size-change="sysMsgConfigSizeChange"
            @current-change="sysMsgConfigCurrentChange"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="sysMsgConfigPageSize"
            :current-page="sysMsgConfigPageNo"
            layout="total, sizes, prev, pager, next, jumper"
            :total="sysMsgConfigPageTotal"
          >
          </el-pagination>
          <mftcc-dialog-form
            formId="sys/sysMsgConfigForm"
            :title="sysMsgConfigFormTitle"
            :show.sync="sysMsgConfigFormVisible"
            :center="true"
            :parentVm="this"
            class="parm-mole-dialog"
            ref="sysMsgConfigFormDialog"
            confirmButtonText="保 存"
            @callback="commitsysMsgConfig"
          ></mftcc-dialog-form>
          <!-- <el-dialog 
                :title="sysMsgConfigFormTitle" 
                :visible.sync="sysMsgConfigFormVisible" 
                :close-on-click-modal="false"
                :center="true"
                class="msg-mole-dialog"> 
                <div slot="footer" class="dialog-footer">
                    <mftcc-form 
                        formId="sys/sysMsgConfigForm" 
                        ref="sysMsgConfigForm"></mftcc-form>
                    <el-button 
                        @click="sysMsgConfigFormVisible = false">取 消</el-button>
                    <el-button 
                        type="primary" 
                        @click="commitsysMsgConfig">保 存</el-button>
                </div>
            </el-dialog> -->
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import sysMsgMoldApi from "@/api/sys/sysMsgMold";
import sysMsgConfigApi from "@/api/sys/sysMsgConfig";
export default {
  name: "sysMsgConfig",
  title: "错误码配置",
  data() {
    return {
      sysMsgMoldFormVisible: false,
      sysMsgMoldFormTitle: "类别",
      sysMsgMoldFormType: "1",
      sysMsgMoldData: [],
      sysMsgMoldProps: {
        children: "children",
        label: this.labelFunc,
      },

      sysMsgConfigFormVisible: false,
      sysMsgConfigFormTitle: "类别",
      sysMsgConfigFormType: "1",
      sysMsgConfigData: [],
      sysMsgConfigPageNo: 1,
      sysMsgConfigPageSize: 10,
      sysMsgConfigPageTotal: 0,

      pramDicItem: {},
    };
  },
  methods: {
    labelFunc(data, node) {
      return data.moldName + "(" + data.moldCnt + ")";
    },
    formatter(row, column, cellValue, index) {
      let value = cellValue;
      let parmArray = this.pramDicItem[column.property];
      for (let i in parmArray) {
        if (parmArray[i]["optCode"] === cellValue) {
          value = parmArray[i]["optName"];
        }
      }
      return value;
    },
    findRootByMoldId(currentNode) {
      if (
        this.$formUtil.isEmpty(currentNode) ||
        this.$formUtil.isEmpty(currentNode.pid)
      ) {
        return null;
      }
      while (currentNode.pid !== "root") {
        currentNode = this.findPathByMoldId(currentNode.pid, this.sysMsgMoldData);
      }
      return currentNode;
    },
    sysMsgConfigFunc(row, type) {
      let _this = this;
      let currentNode = _this.$refs.sysMsgMoldTree.getCurrentNode();
      if (currentNode === null) {
        _this.$message.error("请先选择类别");
        return false;
      }
      let rootNode = _this.findRootByMoldId(currentNode);
      let prepend = rootNode.moldName.toUpperCase() + "_";
      let dicItem = [{ optCode: currentNode.moldId, optName: currentNode.moldName }];
      switch (type) {
        case "insert":
          _this.sysMsgConfigFormTitle = "新增";
          _this.sysMsgConfigFormType = "1";
          _this.sysMsgConfigFormVisible = true;
          _this.$refs.sysMsgConfigFormDialog.getFormRef((form) => {
            form.resetForm(function () {
              form.attr("msgCode", "disabled", false);
              form.attr("msgCode", "prepend", prepend);
              form.attr("moldId", "dicItem", dicItem);
              form.setFormValue("moldId", currentNode.moldId);
            });
          });
          break;
        case "update":
          _this.sysMsgConfigFormTitle = "修改";
          _this.sysMsgConfigFormType = "2";
          _this.sysMsgConfigFormVisible = true;
          _this.$refs.sysMsgConfigFormDialog.getFormRef((form) => {
            form.attr("msgCode", "prepend", "");
            form.resetForm(function () {
              form.attr("msgCode", "disabled", true);
              form.attr("moldId", "dicItem", dicItem);
              form.setFormValue(row);
            });
          });

          break;
        case "delete":
          _this
            .$confirm("此操作将永久删除数据, 是否继续?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            })
            .then(() => {
              sysMsgConfigApi.deleteById(row.msgCode, (reponse) => {
                if (reponse.code === 0) {
                  _this.$alert("删除成功", "提示", {
                    type: "info",
                    callback: (action) => {
                      _this.sysMsgConfigRefresh();
                    },
                  });
                } else {
                  _this.$message.error(reponse.msg);
                }
              });
            });
          break;
      }
    },
    commitsysMsgConfig(data) {
      let _this = this;
      if (_this.sysMsgConfigFormType === "1") {
        let currentNode = _this.$refs.sysMsgMoldTree.getCurrentNode();
        if (currentNode === null) {
          _this.$message.error("请先选择类别");
          return false;
        }
        let rootNode = _this.findRootByMoldId(currentNode);
        data.msgCode = rootNode.moldName.toUpperCase() + "_" + data.msgCode;
        sysMsgConfigApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.sysMsgConfigRefresh();
                _this.sysMsgConfigFormVisible = false;
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (_this.sysMsgConfigFormType === "2") {
        sysMsgConfigApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.sysMsgConfigRefresh();
                _this.sysMsgConfigFormVisible = false;
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
    getAllLeaf(data) {
      let result = "";
      let getLeaf = (data) => {
        result = result + "|" + data.moldId;
        if (!this.$formUtil.isEmpty(data.children)) {
          for (let item of data.children) {
            getLeaf(item);
          }
        }
      };
      getLeaf(data);
      return result;
    },
    getsysMsgConfigTableData(pageNo, pageSize) {
      var _this = this;
      let currentNode = _this.$refs.sysMsgMoldTree.getCurrentNode();
      if (currentNode === null) {
        _this.$message.error("请先选择类别");
        return false;
      }
      let moldid = this.getAllLeaf(currentNode);
      let searchData = {
        pageNo: pageNo,
        pageSize: pageSize,
        moldId: moldid,
      };
      sysMsgConfigApi.findByPage(searchData, function (tableData, current, size, total) {
        _this.sysMsgConfigData = tableData;
        _this.sysMsgConfigPageNo = current; // 当前页数
        _this.sysMsgConfigPageSize = size; // 每页数量
        _this.sysMsgConfigPageTotal = total; // 页面总数
      });
    },
    sysMsgConfigSizeChange(val) {
      this.getsysMsgConfigTableData(1, val);
    },
    sysMsgConfigCurrentChange(val) {
      this.getsysMsgConfigTableData(val, this.sysMsgConfigPageSize);
    },
    sysMsgConfigRefresh() {
      this.getsysMsgConfigTableData(this.sysMsgConfigPageNo, this.sysMsgConfigPageSize);
    },
    findPathByMoldId(moldId, nodes) {
      let _this = this;
      for (var i = 0; i < nodes.length; i++) {
        if (moldId == nodes[i].moldId) {
          return nodes[i];
        }
        if (nodes[i].children) {
          var findResult = _this.findPathByMoldId(moldId, nodes[i].children);
          if (findResult) {
            return findResult;
          }
        }
      }
    },
    getTreeList() {
      let _this = this;
      sysMsgMoldApi.getList(
        "",
        (reponse) => {
          if (reponse.code === 0) {
            _this.sysMsgMoldData = reponse.list;
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
        <span class="msg-tree-node">
          <span title={node.label}>{node.label}</span>
          <span>
            <i
              class="el-icon-plus"
              title="新增目录"
              on-click={() => this.sysMsgMoldTreeFun(node, "insert")}
            />
            <i
              class="el-icon-edit"
              title="编辑目录"
              on-click={() => this.sysMsgMoldTreeFun(node, "update")}
            />
            <i
              class="el-icon-delete"
              title="删除"
              on-click={() => this.sysMsgMoldTreeFun(node, "delete")}
            />
          </span>
        </span>
      );
    },
    commitsysMsgMold(data) {
      let _this = this;
      if (_this.sysMsgMoldFormType === "1") {
        sysMsgMoldApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            data.moldId = reponse.moldId;
            _this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                if (data.pid === "root") {
                  _this.sysMsgMoldData.push(data);
                } else {
                  let parent = _this.findPathByMoldId(data.pid, _this.sysMsgMoldData);
                  if (!parent.children) {
                    _this.$set(parent, "children", []);
                  }
                  parent.children.push(data);
                }
                _this.sysMsgMoldFormVisible = false;
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (_this.sysMsgMoldFormType === "2") {
        sysMsgMoldApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                if (data.pid === "root") {
                  let index = _this.sysMsgMoldData.findIndex(
                    (d) => d.moldId === data.moldId
                  );
                  let node = _this.sysMsgMoldData[index];
                  _this.$set(node, "moldName", data.moldName);
                  _this.$set(node, "moldCnt", data.moldCnt);
                } else {
                  let parent = _this.findPathByMoldId(data.moldId, _this.sysMsgMoldData);
                  _this.$set(parent, "moldName", data.moldName);
                  _this.$set(parent, "moldCnt", data.moldCnt);
                }
                _this.sysMsgMoldFormVisible = false;
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
    sysMsgMoldNodeClick(data) {
      this.getsysMsgConfigTableData(1, this.sysMsgConfigPageSize);
    },
    insertRootParmMold() {
      let _this = this;
      _this.sysMsgMoldFormTitle = "新增";
      _this.sysMsgMoldFormType = "1";
      _this.sysMsgMoldFormVisible = true;
      let dicItem = [{ optCode: "root", optName: "root" }];
      _this.$refs.msgMoleDialog.getFormRef((form) => {
        form.attr("pid", "dicItem", dicItem);
        form.resetForm(function () {
          form.setFormValue("pid", "root");
        });
      });
    },
    sysMsgMoldTreeFun(node, type) {
      event.stopPropagation(); // 阻止冒泡给nodeClick
      let data = node.data;
      let parentData = node.parent.data;

      let _this = this;
      let dicItem = [{ optCode: data.moldId, optName: data.moldName }];
      switch (type) {
        case "insert":
          _this.sysMsgMoldFormTitle = "新增";
          _this.sysMsgMoldFormType = "1";
          _this.sysMsgMoldFormVisible = true;
          _this.$refs.msgMoleDialog.getFormRef((form) => {
            form.attr("pid", "dicItem", dicItem);
            form.resetForm(function () {
              form.setFormValue("pid", data.moldId);
            });
          });
          break;
        case "update":
          if (data.moldId === "root") {
            _this.$message.error("不能修改根节点");
            return false;
          }
          _this.sysMsgMoldFormTitle = "修改";
          _this.sysMsgMoldFormType = "2";
          _this.sysMsgMoldFormVisible = true;
          _this.$refs.msgMoleDialog.getFormRef((form) => {
            form.attr("pid", "option", dicItem);
            form.resetForm(function () {
              form.setFormValue(data);
            });
          });
          break;
        case "delete":
          if (data.moldId === "root") {
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
              sysMsgMoldApi.delBatch(data.moldId, (reponse) => {
                if (reponse.code === 0) {
                  _this.$alert("删除成功", "提示", {
                    type: "info",
                    callback: (action) => {
                      if (data.pid === "root") {
                        let index = _this.sysMsgMoldData.findIndex(
                          (d) => d.moldId === data.moldId
                        );
                        _this.sysMsgMoldData.splice(index, 1);
                      } else {
                        let parent = _this.findPathByMoldId(
                          data.pid,
                          _this.sysMsgMoldData
                        );
                        let index = parent.children.findIndex(
                          (d) => d.moldId === data.moldId
                        );
                        parent.children.splice(index, 1);
                      }
                      _this.sysMsgMoldFormVisible = false;
                    },
                  });
                } else {
                  _this.$message.error(reponse.msg);
                }
              });
            });
          break;
      }
    },
    onSearch() {
      let _this = this;
      let currentNode = _this.$refs.sysMsgMoldTree.getCurrentNode();
      if (currentNode === null) {
        _this.$message.error("请先选择类别");
        return false;
      }
      let moldid = this.getAllLeaf(currentNode);
      _this.$refs.sysMsgSearchForm.validateForm((valid) => {
        if (valid) {
          let data = _this.$refs.sysMsgSearchForm.getFormValue();
          data.pageNo = 1;
          data.pageSize = _this.sysMsgConfigPageSize;
          data.moldId = moldid;
          sysMsgConfigApi.findByPage(data, function (tableData, current, size, total) {
            _this.sysMsgConfigData = tableData;
            _this.sysMsgConfigPageNo = current; // 当前页数
            _this.sysMsgConfigPageSize = size; // 每页数量
            _this.sysMsgConfigPageTotal = total; // 页面总数
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    getParmDic() {
      let dicKeyArray = [{ dicKey: "YES_NO", fieldName: "msgLvl" }];
      //请求字典json文件
      var dicKeyData = this.$formUtil.getParmDic(dicKeyArray);
      for (let index in dicKeyData) {
        this.$set(
          this.pramDicItem,
          dicKeyData[index].fieldName,
          dicKeyData[index].parmDicValue
        );
      }
    },
    initCaChe() {
      sysMsgConfigApi.initCaChe("", (reponse) => {
        if (reponse.code === 0) {
          this.$alert("缓存刷新成功", "提示", {
            type: "info",
            callback: (action) => {},
          });
        } else {
          this.$alert("缓存刷新失败", "提示", {
            type: "error",
            callback: (action) => {},
          });
        }
      });
    },
  },
  created() {
    this.getTreeList();
    this.getParmDic();
  },
};
</script>
<style>
.sys-msg-config .msg-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.sys-msg-config .msg-tree-node i {
  margin-left: 10px;
}
.sys-msg-config .msg-border {
  border: solid 1px #ddd;
  border-radius: 4px;
}
.sys-msg-config .msg-border .el-tree {
  margin: 10px;
}

.sys-msg-config
  .el-tree
  div:not(.is-expanded)
  .el-tree-node__content
  .msg-tree-node
  span {
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 175px;
  /* border:1px solid red; */
  overflow: hidden;
}
.sys-msg-config
  .el-tree
  .is-expanded
  .el-tree-node__children
  .el-tree-node
  .msg-tree-node
  span {
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 156px;
  /* border:1px solid red; */
  overflow: hidden;
}

.sys-msg-config .msg-container {
  margin-left: 10px !important;
}
.sys-msg-config .msg-container .el-header {
  padding: 0px 30px 0px 0px;
  height: 140px !important;
  width: 98%;
  margin: 10px auto;
}

.sys-msg-config .msg-container .el-header .el-row .el-form {
  float: left;
  width: 85%;
}
.sys-msg-config .msg-container .el-header .el-row .search-button {
  float: left;
  width: 90%;
  margin-top: 0px;
}
.sys-msg-config .msg-container .el-header .el-row .cache-button {
  margin-top: 8px;
}
.sys-msg-config .msg-mole-dialog .el-dialog__body {
  padding: 0px !important;
}
.sys-msg-config .msg-mole-dialog .el-dialog__footer {
  padding: 25px 0px 30px;
}
.sys-msg-config .msg-mole-dialog .el-form {
  width: 90%;
  padding: 0px 0px 30px;
}
.sys-msg-config .msg-container .el-main {
  padding: 0;
  margin: 0 auto;
  width: 98%;
  padding-bottom: 10px;
}
.sys-msg-config .msg-container .el-main .sys-msg-config-table {
  height: 98%;
  padding-left: 10px;
  padding-right: 10px;
}
.sys-msg-config .msg-container .el-main .sys-msg-config-table .el-pagination {
  margin-top: 5px;
  text-align: center;
}
.sys-msg-config .sys-msg-config-insert {
  text-align: right;
  margin-top: 10px;
  margin-right: 10px;
}
</style>
