<template>
  <el-container class="parm-config">
    <el-aside width="300px" class="parm-border" style="margin: 10px 0 10px 10px;overflow-x: hidden;">
      <div style="margin: 20px; text-align: right">
        <el-button type="primary" size="mini" @click="insertRootParmMold">新增</el-button>
        <!-- <el-button type="primary" size="mini" @click="getParmDicFile(true)"
          >下载全部</el-button
        > -->
        <!-- <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" circle @click="insertParmMold()"></el-button>
        <el-button type="primary" icon="el-icon-edit" size="small" circle @click="updateParmMold()"></el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" circle @click="deleteParmMold()"></el-button> -->
      </div>
      <el-tree
        :data="parmMoldData"
        :props="parmMoldProps"
        node-key="moldId"
        ref="parmMoldTree"
        :highlight-current="true"
        :check-on-click-node="true"
        :expand-on-click-node="false"
        :render-content="renderContent"
        accordion
        @node-click="parmMoldNodeClick"
      >
      </el-tree>
      <mftcc-dialog-form
        formId="sys/sysParmMoleForm"
        :title="parmMoleFormTitle"
        :show.sync="parmMoleFormVisible"
        :center="true"
        :parentVm="this"
        class="parm-mole-dialog"
        ref="sysParmMoleFormDialog"
        confirmButtonText="保 存"
        @callback="commitParmMole"
      ></mftcc-dialog-form>
    </el-aside>
    <el-container class="parm-border parm-container">
      <el-header class="parm-border">
        <el-row style="width: 100%">
          <el-col :span="22">
            <div class="custom-search">
              <mftcc-search-form
                formId="sys/sysParmSearchForm"
                ref="parmSearchForm"
              ></mftcc-search-form>
            </div>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="onSearch" class="search-button"
              >查询</el-button
            >
            <el-button
              type="primary"
              size="mini"
              circle
              @click="initCaChe"
              class="cache-button"
            >刷新</el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row>
          <el-col :span="12">
            <div class="parm-border parm-key-table">
              <div class="parm-key-insert">
                <el-button type="primary" size="mini" @click="parmKeyFunc('', 'insert')"
                  >新增</el-button
                >
                <!-- <el-button type="primary" size="mini" @click="getParmDicFile"
                  >下载</el-button
                > -->
              </div>
              <el-table
                ref="parmKeyList"
                :data="parmKeyData"
                @current-change="parmKeyClick"
                stripe
                :highlight-current-row="true"
                style="height: calc(100% - 91px);overflow: auto;"
              >
                <el-table-column type="selection" width="30"> </el-table-column>
                <el-table-column prop="keyName" label="字典标识"> </el-table-column>
                <el-table-column prop="keyCnt" label="字典名称"> </el-table-column>
                <!-- <el-table-column prop="edit" label="是否可修改" :formatter="formatter"> -->
                </el-table-column>
                <el-table-column prop="sts" label="启用" :formatter="formatter" width="50">
                </el-table-column>
                <el-table-column label="操作" width="90">
                  <template slot-scope="scope">
                    <el-button
                      @click="parmKeyFunc(scope.row, 'update')"
                      type="text"
                      size="small"
                      :disabled="scope.row.edit === '0'"
                      >修改</el-button
                    >
                    <el-button
                      @click="parmKeyFunc(scope.row, 'delete')"
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
                @size-change="parmKeySizeChange"
                @current-change="parmKeyCurrentChange"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="parmKeyPageSize"
                :current-page="parmKeyPageNo"
                layout="total, sizes, prev, pager, next, jumper"
                :total="parmKeyPageTotal"
              >
              </el-pagination>
              <mftcc-dialog-form
                formId="sys/sysParmKeyForm"
                :title="parmKeyFormTitle"
                :show.sync="parmKeyFormVisible"
                :center="true"
                :parentVm="this"
                class="parm-mole-dialog"
                ref="sysParmKeyFormDialog"
                confirmButtonText="保 存"
                @callback="commitParmKey"
              ></mftcc-dialog-form>
              <!-- <el-dialog 
                        :title="parmKeyFormTitle" 
                        :visible.sync="parmKeyFormVisible" 
                        :close-on-click-modal="false"
                        :center="true"
                        class="parm-mole-dialog"> 
                        <div slot="footer" class="dialog-footer">
                            <mftcc-form 
                                formId="sys/sysParmKeyForm" 
                                ref="parmKeyForm"></mftcc-form>
                            <el-button 
                                @click="parmKeyFormVisible = false">取 消</el-button>
                            <el-button 
                                type="primary" 
                                @click="commitParmKey">保 存</el-button>
                        </div>
                    </el-dialog> -->
            </div>
          </el-col>
          <el-col :span="12">
            <div class="parm-border parm-dic-table">
              <div class="parm-dic-insert">
                <el-button
                  type="primary"
                  size="mini"
                  @click="parmDicFunc('', 'insert')"
                  :disabled="parmDicEdit"
                  >新增</el-button
                >
              </div>
              <el-table
                ref="parmDicList"
                :data="parmDicData"
                stripe
                :highlight-current-row="true"
                style="height: calc(100% - 91px);overflow: auto;"
              >
                <el-table-column prop="keyName" label="字典标识" width="150"> </el-table-column>
                <el-table-column prop="optCode" label="编号" width="50"> </el-table-column>
                <el-table-column prop="optName" label="名称"> </el-table-column>
                <!-- <el-table-column prop="seqn" label="顺序"  width="50"> </el-table-column> -->
                <el-table-column prop="sts" label="启用" :formatter="formatter"  width="50">
                </el-table-column>
                <el-table-column label="操作" width="90">
                  <template slot-scope="scope">
                    <el-button
                      @click="parmDicFunc(scope.row, 'update')"
                      type="text"
                      size="small"
                      :disabled="parmDicEdit"
                      >修改</el-button
                    >
                    <el-button
                      @click="parmDicFunc(scope.row, 'delete')"
                      type="text"
                      size="small"
                      :disabled="parmDicEdit"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                hide-on-single-page
                background
                small
                @size-change="parmDicSizeChange"
                @current-change="parmDicCurrentChange"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="parmDicPageSize"
                :current-page="parmDicPageNo"
                layout="total, sizes, prev, pager, next, jumper"
                :total="parmDicPageTotal"
              >
              </el-pagination>
              <mftcc-dialog-form
                formId="sys/sysParmDicForm"
                :title="parmDicFormTitle"
                :show.sync="parmDicFormVisible"
                :center="true"
                :parentVm="this"
                class="parm-mole-dialog"
                ref="sysParmDicFormDialog"
                confirmButtonText="保 存"
                @callback="commitParmDic"
              ></mftcc-dialog-form>
              <!-- <el-dialog 
                        :title="parmDicFormTitle" 
                        :visible.sync="parmDicFormVisible" 
                        :close-on-click-modal="false"
                        :center="true"
                        class="parm-mole-dialog"> 
                        <div slot="footer" class="dialog-footer">
                            <mftcc-form 
                                formId="sys/sysParmDicForm" 
                                ref="parmDicForm"></mftcc-form>
                            <el-button 
                                @click="parmDicFormVisible = false">取 消</el-button>
                            <el-button 
                                type="primary" 
                                @click="commitParmDic">保 存</el-button>
                        </div>
                    </el-dialog> -->
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import sysParmMoldApi from "@/api/sys/sysParmMold";
import sysParmKeyApi from "@/api/sys/sysParmKey";
import sysParmDicApi from "@/api/sys/sysParmDic";
export default {
  name: "parmDic",
  title: "数据字典项",
  data() {
    return {
      parmMoleFormVisible: false,
      parmMoleFormTitle: "类别",
      parmMoleFormType: "1",
      parmMoldData: [],
      parmMoldProps: {
        children: "children",
        label: this.labelFunc,
      },
      parmKeyFormVisible: false,
      parmKeyFormTitle: "类别",
      parmKeyFormType: "1",
      parmKeyData: [],
      parmKeyPageNo: 1,
      parmKeyPageSize: 10,
      parmKeyPageTotal: 0,
      parmKeySelectRow: null,

      parmDicFormVisible: false,
      parmDicFormTitle: "类别",
      parmDicFormType: "1",
      parmDicData: [],
      parmDicPageNo: 1,
      parmDicPageSize: 10,
      parmDicPageTotal: 0,
      parmDicEdit: false,
    };
  },
  methods: {
    labelFunc(data, node) {
      return data.moldName + "(" + data.moldCnt + ")";
    },
    formatter(row, column, cellValue, index) {
      let value = cellValue;
      if (value == "1") {
        return "是";
      } else {
        return "否";
      }
    },
    parmKeyFunc(row, type) {
      let _this = this;
      let currentNode = _this.$refs.parmMoldTree.getCurrentNode();
      if (currentNode === null) {
        _this.$message.error("请先选择类别");
        return false;
      }
      let rootNode = _this.findRootByMoldId(currentNode);
      let prepend = rootNode.moldName.toUpperCase() + "_";
      let dicItem = [{ optCode: currentNode.moldId, optName: currentNode.moldName }];
      switch (type) {
        case "insert":
          _this.parmKeyFormTitle = "新增";
          _this.parmKeyFormType = "1";
          _this.parmKeyFormVisible = true;
          _this.$refs.sysParmKeyFormDialog.getFormRef((form) => {
            form.resetForm(function () {
              form.attr("keyName", "disabled", false);
              form.attr("keyName", "prepend", prepend);
              form.attr("moldId", "dicItem", dicItem);
              form.setFormValue("moldId", currentNode.moldId);
            });
          });
          break;
        case "update":
          _this.parmKeyFormTitle = "修改";
          _this.parmKeyFormType = "2";
          _this.parmKeyFormVisible = true;
          _this.$refs.sysParmKeyFormDialog.getFormRef((form) => {
            form.resetForm(function () {
              form.attr("keyName", "disabled", true);
              form.attr("keyName", "prepend", "");
              form.attr("moldId", "dicItem", dicItem);
              debugger
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
              sysParmKeyApi.deleteById(row.keyName, (reponse) => {
                if (reponse.code === 0) {
                  _this.$alert("删除成功", "提示", {
                    type: "info",
                    callback: (action) => {
                      _this.parmKeyRefresh();
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
    findRootByMoldId(currentNode) {
      if (
        this.$formUtil.isEmpty(currentNode) ||
        this.$formUtil.isEmpty(currentNode.pid)
      ) {
        return null;
      }
      while (currentNode.pid !== "root") {
        currentNode = this.findPathByMoldId(currentNode.pid, this.parmMoldData);
      }
      return currentNode;
    },
    commitParmKey(data) {
      let _this = this;
      if (_this.parmKeyFormType === "1") {
        let currentNode = _this.$refs.parmMoldTree.getCurrentNode();
        if (currentNode === null) {
          _this.$message.error("请先选择类别");
          return false;
        }
        let rootNode = _this.findRootByMoldId(currentNode);
        data.keyName = rootNode.moldName.toUpperCase() + "_" + data.keyName;
        sysParmKeyApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.parmKeyRefresh();
                _this.parmKeyFormVisible = false;
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (_this.parmKeyFormType === "2") {
        sysParmKeyApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.parmKeyRefresh();
                _this.parmKeyFormVisible = false;
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
    getParmKeyTableData(pageNo, pageSize) {
      var _this = this;
      let currentNode = this.$refs.parmMoldTree.getCurrentNode();
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
      sysParmKeyApi.findByPage(searchData, function (tableData, current, size, total) {
        _this.parmKeyData = tableData;
        _this.parmKeyPageNo = current; // 当前页数
        _this.parmKeyPageSize = size; // 每页数量
        _this.parmKeyPageTotal = total; // 页面总数
      });
    },
    parmKeyRefresh() {
      this.getParmKeyTableData(this.parmKeyPageNo, this.parmKeyPageSize);
    },
    parmKeySizeChange(val) {
      this.getParmKeyTableData(1, val);
    },
    parmKeyCurrentChange(val) {
      this.getParmKeyTableData(val, this.parmKeyPageSize);
    },
    parmKeyClick(currentRow, oldCurrentRow) {
      this.parmKeySelectRow = currentRow;
      if (currentRow != null) {
        this.parmDicEdit = currentRow.edit === "0";
        this.getParmDicTableData(1, this.parmDicPageSize);
      }
    },

    parmDicFunc(row, type) {
      let _this = this;
      let currentRow = _this.parmKeySelectRow;
      if (currentRow === null) {
        _this.$message.error("请先选择数据库字典键值");
        return false;
      }
      switch (type) {
        case "insert":
          _this.parmDicFormTitle = "新增";
          _this.parmDicFormType = "1";
          _this.parmDicFormVisible = true;
          _this.$refs.sysParmDicFormDialog.getFormRef((form) => {
            form.attr("optCode", "disabled", false);
            form.resetForm(function () {
              form.setFormValue("keyName", currentRow.keyName);
            });
          });
          break;
        case "update":
          _this.parmDicFormTitle = "修改";
          _this.parmDicFormType = "2";
          _this.parmDicFormVisible = true;
          _this.$refs.sysParmDicFormDialog.getFormRef((form) => {
            form.attr("optCode", "disabled", true);
            form.resetForm(function () {
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
              sysParmDicApi.deleteById(row.id, (reponse) => {
                if (reponse.code === 0) {
                  _this.$alert("删除成功", "提示", {
                    type: "info",
                    callback: (action) => {
                      _this.parmDicRefresh();
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
    commitParmDic(data) {
      let _this = this;
      if (_this.parmDicFormType === "1") {
        sysParmDicApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.parmDicRefresh();
                _this.parmDicFormVisible = false;
              },
            });
          } else {
            _this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (_this.parmDicFormType === "2") {
        sysParmDicApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            _this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                _this.parmDicRefresh();
                _this.parmDicFormVisible = false;
              },
            });
          } else {
            this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      }
    },
    getParmDicTableData(pageNo, pageSize) {
      var _this = this;
      let currentRow = _this.parmKeySelectRow;
      if (currentRow === null) {
        _this.$message.error("请先选择数据库字典键值");
        return false;
      }
      let searchData = {
        pageNo: pageNo,
        pageSize: pageSize,
        keyName: currentRow.keyName,
      };
      sysParmDicApi.findByPage(searchData, function (tableData, current, size, total) {
        _this.parmDicData = tableData;
        _this.parmDicPageNo = current; // 当前页数
        _this.parmDicPageSize = size; // 每页数量
        _this.parmDicPageTotal = total; // 页面总数
      });
    },
    parmDicSizeChange(val) {
      this.getParmDicTableData(1, val);
    },
    parmDicCurrentChange(val) {
      this.getParmDicTableData(val, this.parmDicPageSize);
    },
    parmDicRefresh() {
      this.getParmDicTableData(this.parmDicPageNo, this.parmDicPageSize);
    },
    initCaChe() {
      let _this = this;
      sysParmDicApi.initCaChe("", (reponse) => {
        if (reponse.code === 0) {
          _this.$alert("缓存刷新成功", "提示", {
            type: "info",
            callback: (action) => {},
          });
        } else {
          _this.$alert("缓存刷新失败", "提示", {
            type: "error",
            callback: (action) => {},
          });
        }
      });
    },
    getParmDicFile(all) {
      let _this = this;
      let moldid = "";
      if (all !== true) {
        let currentNode = _this.$refs.parmMoldTree.getCurrentNode();
        if (currentNode === null) {
          _this.$message.error("请先选择类别");
          return false;
        }
        moldid = this.getAllLeaf(currentNode);
      }

      let parmKeyArray = _this.$refs.parmKeyList.store.states.selection;
      let downloadData = {
        moldId: moldid,
        parmKeyArray: parmKeyArray,
      };
      sysParmKeyApi.downloadFile(downloadData);
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
      sysParmMoldApi.getList(
        "",
        (reponse) => {
          if (reponse.code === 0) {
            _this.parmMoldData = reponse.list;
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
        <span class="parm-tree-node">
          <span title={node.label}>{node.label}</span>
          <span>
            <i
              class="el-icon-plus"
              title="新增目录"
              on-click={() => this.parmMoleTreeFun(node, "insert")}
            />
            <i
              class="el-icon-edit"
              title="修改目录"
              on-click={() => this.parmMoleTreeFun(node, "update")}
            />
            <i
              class="el-icon-delete"
              title="删除"
              on-click={() => this.parmMoleTreeFun(node, "delete")}
            />
          </span>
        </span>
      );
    },
    commitParmMole(data) {
      if (this.parmMoleFormType === "1") {
        sysParmMoldApi.insert(data, (reponse) => {
          if (reponse.code === 0) {
            data.moldId = reponse.moldId;
            this.$alert("新增成功", "提示", {
              type: "info",
              callback: (action) => {
                if (data.pid === "root") {
                  this.parmMoldData.push(data);
                } else {
                  let parent = this.findPathByMoldId(data.pid, this.parmMoldData);
                  if (!parent.children) {
                    this.$set(parent, "children", []);
                  }
                  parent.children.push(data);
                }
                this.parmMoleFormVisible = false;
              },
            });
          } else {
            this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      } else if (this.parmMoleFormType === "2") {
        sysParmMoldApi.update(data, (reponse) => {
          if (reponse.code === 0) {
            this.$alert("修改成功", "提示", {
              type: "info",
              callback: (action) => {
                if (data.pid === "root") {
                  let index = this.parmMoldData.findIndex(
                    (d) => d.moldId === data.moldId
                  );
                  let node = this.parmMoldData[index];
                  this.$set(node, "moldName", data.moldName);
                  this.$set(node, "moldCnt", data.moldCnt);
                } else {
                  let parent = this.findPathByMoldId(data.moldId, this.parmMoldData);
                  this.$set(parent, "moldName", data.moldName);
                  this.$set(parent, "moldCnt", data.moldCnt);
                }
                this.parmMoleFormVisible = false;
              },
            });
          } else {
            this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {},
            });
          }
        });
      }
    },
    parmMoldNodeClick(data) {
      this.getParmKeyTableData(1, this.parmKeyPageSize);
      this.parmKeySelectRow = null;
      this.parmDicEdit = true;
      this.parmDicPageTotal = 0;
      this.parmDicData = [];
    },
    insertRootParmMold() {
      this.parmMoleFormTitle = "新增";
      this.parmMoleFormType = "1";
      this.parmMoleFormVisible = true;
      let dicItem = [{ optCode: "root", optName: "root" }];
      this.$refs.sysParmMoleFormDialog.getFormRef((form) => {
        form.attr("pid", "dicItem", dicItem);
        form.resetForm(() => {
          form.setFormValue("pid", "root");
        });
      });
    },
    parmMoleTreeFun(node, type) {
      event.stopPropagation(); // 阻止冒泡给nodeClick
      let data = node.data;
      let parentData = node.parent.data;

      let _this = this;
      let dicItem = [{ optCode: data.moldId, optName: data.moldName }];
      switch (type) {
        case "insert":
          _this.parmMoleFormTitle = "新增";
          _this.parmMoleFormType = "1";
          _this.parmMoleFormVisible = true;
          _this.$refs.sysParmMoleFormDialog.getFormRef((form) => {
            form.attr("pid", "dicItem", dicItem);
            form.resetForm(function () {
              form.setFormValue("pid", data.moldId);
            });
          });
          break;
        case "update":
          if (data.moldId === "root") {
            this.$message.error("不能修改根节点");
            return false;
          }
          _this.parmMoleFormTitle = "修改";
          _this.parmMoleFormType = "2";
          _this.parmMoleFormVisible = true;
          _this.$refs.sysParmMoleFormDialog.getFormRef((form) => {
            form.attr("pid", "dicItem", dicItem);
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
              sysParmMoldApi.delBatch(data.moldId, (reponse) => {
                if (reponse.code === 0) {
                  _this.$alert("删除成功", "提示", {
                    type: "info",
                    callback: (action) => {
                      if (data.pid === "root") {
                        let index = _this.parmMoldData.findIndex(
                          (d) => d.moldId === data.moldId
                        );
                        _this.parmMoldData.splice(index, 1);
                      } else {
                        let parent = _this.findPathByMoldId(data.pid, _this.parmMoldData);
                        let index = parent.children.findIndex(
                          (d) => d.moldId === data.moldId
                        );
                        parent.children.splice(index, 1);
                      }
                      _this.parmMoleFormVisible = false;
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
      let currentNode = _this.$refs.parmMoldTree.getCurrentNode();
      if (currentNode === null) {
        _this.$message.error("请先选择类别");
        return false;
      }
      let moldid = this.getAllLeaf(currentNode);
      _this.$refs.parmSearchForm.validateForm((valid) => {
        if (valid) {
          let data = _this.$refs.parmSearchForm.getFormValue();
          data.pageNo = 1;
          data.pageSize = _this.parmKeyPageSize;
          data.moldId = moldid;
          sysParmKeyApi.findByPage(data, function (tableData, current, size, total) {
            _this.parmKeyData = tableData;
            _this.parmKeyPageNo = current; // 当前页数
            _this.parmKeyPageSize = size; // 每页数量
            _this.parmKeyPageTotal = total; // 页面总数
            if (tableData.length > 0) {
              _this.$refs.parmKeyList.setCurrentRow(tableData[0]);
            } else {
              _this.parmKeySelectRow = null;
              _this.parmDicEdit = true;
              _this.parmDicPageTotal = 0;
              _this.parmDicData = [];
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
  created() {
    this.getTreeList();
  },
};
</script>
<style>
.parm-config .parm-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.parm-config .parm-tree-node i {
  margin-left: 10px;
}

.parm-config .el-tree div:not(.is-expanded) .el-tree-node__content .parm-tree-node span {
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 175px;
  /* border:1px solid red; */
  overflow: hidden;
}
.parm-config
  .el-tree
  .is-expanded
  .el-tree-node__children
  .el-tree-node
  .parm-tree-node
  span {
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 156px;
  /* border:1px solid red; */
  overflow: hidden;
}

.parm-config .parm-border {
  border: solid 1px #ddd;
  border-radius: 4px;
}
.parm-config .parm-border .el-tree {
  margin: 10px;
}
.parm-config .parm-container {
  margin-left: 10px !important;
}
.parm-config .parm-container .el-header {
  padding: 0px 30px 0px 0px;
  height: 140px !important;
  width: 98%;
  margin: 10px auto;
}
.parm-config .parm-container .el-header .el-row {
  padding-top: 5px;
  /* height: 60px; */
}
.parm-config .parm-container .el-header .el-row .el-form {
  float: left;
  width: 85%;
}
.parm-config .parm-container .el-header .el-row .search-button {
  float: left;
  width: 90%;
  margin-top: 0px;
}
.parm-config .parm-container .el-header .el-row .cache-button {
  margin-top: 8px;
}
.parm-config .parm-mole-dialog .el-dialog__body {
  padding: 0px !important;
}
.parm-config .parm-mole-dialog .el-dialog__footer {
  padding: 25px 0px 30px;
}
.parm-config .parm-mole-dialog .el-form {
  width: 90%;
  padding: 0px 0px 30px;
}
.parm-config .parm-container .el-main {
  padding: 0;
  margin: 0 auto;
  width: 98%;
  padding-bottom: 10px;
}
.parm-config .parm-container .el-main .el-row,
.parm-config .parm-container .el-main .el-row .el-col,
.parm-config .parm-container .el-main .el-row .el-col .parm-dic-table {
  height: 100%;
}
.parm-config .parm-container .el-main .el-row .el-col .parm-key-table .el-pagination,
.parm-config .parm-container .el-main .el-row .el-col .parm-dic-table .el-pagination {
  margin-top: 5px;
  text-align: center;
}
.parm-config .parm-container .el-main .el-row .el-col .parm-key-table {
  height: 100%;
  width: 98%;
}
.parm-config .parm-key-insert,
.parm-config .parm-dic-insert {
  text-align: right;
  margin-top: 10px;
  margin-right: 10px;
}
.parm-border .el-pagination{
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
