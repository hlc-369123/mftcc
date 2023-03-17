<template>
  <div style="width: 100%; height: 100%" oncontextmenu="return false">
    <div id="chart" ref="chart" style="width: 100%; height: 100%"></div>
    <contextmenu
      :show.sync="cmVisible"
      :position="cmPosition"
      :items="cmItems"
      @callback="cmCallBack"
    ></contextmenu>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      class="authDialog"
    >
      <el-form
        v-if="dialogType == 'addMenu' || dialogType == 'editMenu'"
        :model="menuForm"
        :rules="menuRules"
        ref="menuForm"
      >
        <el-form-item label="菜单名称" label-width="100px" prop="menuName">
          <el-input v-model="menuForm.menuName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" label-width="100px" prop="menuIcon">
          <el-input v-model="menuForm.menuIcon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          v-if="menuForm.newTab == 1 || menuForm.newTab == null"
          label="菜单地址"
          label-width="100px"
          prop="menuUrl"
        >
          <el-input v-model="menuForm.menuUrl" autocomplete="off"></el-input>
        </el-form-item>
        <!--0开、1关-->
        <el-form-item label="是否打开新页签" label-width="100px" prop="menuUrl">
          <el-switch
            v-model="menuForm.newTab"
            active-value="0"
            inactive-value="1"
            @change="changeType"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="排序索引" label-width="100px" prop="menuUrl">
          <el-input v-model="menuForm.sn" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-form
        v-if="dialogType == 'addView' || dialogType == 'editView'"
        :model="viewForm"
        :rules="viewRules"
        ref="viewForm"
      >
        <el-form-item label="视角名称" label-width="100px" prop="viewName">
          <el-input v-model="viewForm.viewName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-form
        v-if="dialogType == 'addViewMenu' || dialogType == 'editViewMenu'"
        :model="viewCmptMenuForm"
        :rules="viewCmptMenuRules"
        ref="viewCmptMenuForm"
      >
        <el-input
          type="hidden"
          v-model="viewCmptMenuForm.viewCmptTyp"
          autocomplete="off"
        ></el-input>
        <el-form-item label="菜单名称" label-width="100px" prop="viewCmptName">
          <el-input
            v-model="viewCmptMenuForm.viewCmptName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="菜单地址" label-width="100px" prop="menuUrl">
          <el-input
            v-model="viewCmptMenuForm.menuUrl"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否刷新页面" prop="refreshFlg">
          <el-switch
            v-model="viewCmptMenuForm.refreshFlg"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="默认加载" prop="loadFlg">
          <el-switch
            v-model="viewCmptMenuForm.loadFlg"
            active-value="1"
            inactive-value="0"
            >>
          </el-switch>
        </el-form-item>
      </el-form>
      <el-form
        v-if="dialogType == 'addViewBtn' || dialogType == 'editViewBtn'"
        :model="viewCmptBtnForm"
        :rules="viewCmptBtnRules"
        ref="viewCmptBtnForm"
      >
        <el-input
          type="hidden"
          v-model="viewCmptBtnForm.viewCmptTyp"
          autocomplete="off"
        ></el-input>
        <el-form-item label="按钮名称" label-width="100px" prop="viewCmptName">
          <el-input
            v-model="viewCmptBtnForm.viewCmptName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="按钮函数" label-width="100px" prop="btnMthd">
          <el-input
            v-model="viewCmptBtnForm.btnMthd"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="显隐表达式" label-width="100px" prop="expr">
                    <el-input v-model="viewCmptBtnForm.expr" autocomplete="off"></el-input>
                </el-form-item> -->
      </el-form>
      <el-form
        v-if="dialogType == 'addModel' || dialogType == 'editModel'"
        :model="modelForm"
        :rules="modelRules"
        ref="modelForm"
      >
        <el-divider content-position="left">功能信息定义</el-divider>
        <el-form-item label="功能类型" label-width="100px" prop="modelTyp">
          <el-radio-group v-model="modelForm.modelTyp">
            <el-radio label="1">按钮</el-radio>
            <el-radio label="2">视图模块</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="功能名称" label-width="100px" prop="modelName">
          <el-input v-model="modelForm.modelName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="按钮标识" label-width="100px" prop="btnKey">
          <el-input v-model="modelForm.btnKey" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="功能描述" label-width="100px" prop="modelRmk">
          <el-input v-model="modelForm.modelRmk" autocomplete="off"></el-input>
        </el-form-item>
        <el-divider content-position="left">数据权限定义</el-divider>
        <el-form-item label="持久层路径" label-width="100px" prop="mapperPath">
          <el-input
            v-model="modelForm.mapperPath"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-table
          v-if="dialogType == 'addModel' || dialogType == 'editModel'"
          :data="dataAuthTable"
          ref="dataAuthTable"
          @selection-change="dataAuthTableSelectionChange"
          class="dataAuthTable"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="text" label="权限名称">
            <template slot-scope="scope">
              <el-input
                v-if="(scope.row.id + '').length > 3"
                v-model="scope.row.text"
              ></el-input>
              <p v-else>{{ scope.row.text }}</p>
            </template>
          </el-table-column>
          <el-table-column label="权限类型">
            <template slot-scope="scope">
              <el-select
                v-if="(scope.row.id + '').length > 3"
                autocomplete="off"
                clearable
                filterable
                v-model="scope.row.type"
                placeholder="请选择权限类型"
              >
                <el-option
                  v-for="selectItem in dataAuthTypeList"
                  :key="selectItem.id"
                  :label="selectItem.text"
                  :value="selectItem.id"
                ></el-option>
              </el-select>
              <p v-else>{{ scope.row.text }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="field" width="350" label="权限字段">
            <template slot-scope="scope">
              <template v-if="scope.row.type == '88'">
                <el-input
                  v-model="scope.row.field"
                  style="width: 200px"
                ></el-input>
                <el-button
                  type="primary"
                  size="mini"
                  @click.native="editCorpAuth(scope)"
                  >选择机构
                </el-button>
              </template>
              <el-input
                v-else-if="scope.row.id != 99"
                v-model="scope.row.field"
              ></el-input>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" @click="handleAddCustomAuth"
          >新增本人自定义权限
        </el-button>
        <el-divider content-position="left">鉴权请求地址定义</el-divider>
        <el-button v-if="$hasPerm('')" type="primary" @click="handleAdd"
          >新增URL
        </el-button>
        <mftcc-edit-table
          v-if="dialogType == 'addModel' || dialogType == 'editModel'"
          tableId="sys/sysAuthList"
          :parentVm="this"
          :initSearchData="urlTableData"
          ref="urlList"
        ></mftcc-edit-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="validateForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="数据权限选择"
      :visible.sync="dataAuthDialogVisible"
      class="authDialog"
    >
      <el-table
        :data="dataAuthTable"
        ref="dataAuthConifgTable"
        @select="dataAuthTableSelect"
        style="width: 100%"
        class="dataAuthConifgTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="权限名称">
          <template slot-scope="scope">
            <p>{{ scope.row.text }}</p>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dataAuthDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dataAuthDialogSubmit"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <mftcc-dialog-checkbox
      @callback="checkboxFunc"
      :show.sync="checkboxShow"
      :option="checkboxOption"
    ></mftcc-dialog-checkbox>
    <mftcc-plt-br-tree
      @callback="pltBrTreeFunc"
      :show.sync="pltBrTreeShow"
      :option="pltBrTreeOption"
      :checkOver="true"
    ></mftcc-plt-br-tree>
  </div>
</template>
<script>
import {
  convertAuthData,
  findUpIsImport,
  findChildIsImport,
  getDeleteIds,
  childSelected,
  parentSelected,
  setAuthByRole,
  dataAuthList
} from "@/utils/sys/authUtil";
import G6 from "@antv/g6";
import api from "@/api/sys/auth";
import uuid from "uuid";

export default {
  name: "authDef",
  title: "功能权限定义",
  data() {
    let width = 180;
    let height = 50;
    let markerR = 8;
    let list = JSON.parse(JSON.stringify(dataAuthList));
    let dataAuthTypeList = [];
    for (let item of list) {
      if (item.id != "99") {
        dataAuthTypeList.push(item);
      }
    }
    dataAuthTypeList.push({
      id: "88",
      text: "多机构"
    });
    return {
      nodeBox: {
        width,
        height
      },
      markerBox: {
        r: markerR,
        x: width - markerR,
        y: height / 2
      },
      checkBoxMarkerBox: {
        x: 10,
        y: 7,
        width: 35,
        height: 35
      },
      nodeStyle(type) {
        let config = {};
        switch (type) {
          case "menu":
            config = {
              fontColor: "#FFFFFF",
              borderColor: "#556f8e",
              bgColor: "#849bb7"
            };
            break;
          case "view":
            config = {
              fontColor: "#F9767D",
              borderColor: "#F9767D",
              bgColor: "#FFF1F0"
            };
            break;
          case "viewMenu":
            config = {
              fontColor: "#FA901E",
              borderColor: "#FCD8AF",
              bgColor: "#FCF4E3"
            };
            break;
          case "viewBtn":
            config = {
              fontColor: "#66b1ff",
              borderColor: "#66b1ff",
              bgColor: "#ecf5ff"
            };
            break;
          case "model":
            config = {
              fontColor: "#66b1ff",
              borderColor: "#66b1ff",
              bgColor: "#ecf5ff"
            };
            break;
          case "modelView":
            config = {
              fontColor: "#722ED1",
              borderColor: "#722ED1",
              bgColor: "#F6EDFC"
            };
            break;
          default:
            config = {
              fontColor: "#666",
              borderColor: "#666",
              bgColor: "#f3f4f5"
            };
            break;
        }
        return config;
      },
      graph: {},
      minimap: {},
      currentItem: {},
      cmVisible: false,
      cmPosition: {
        top: 0,
        left: 0
      },
      cmItems: [],
      checkboxShow: false,
      checkboxOption: {
        title: "引用功能项",
        search: true,
        insert: false,
        group: false,
        realField: "modelId",
        showField: "modelName",
        data: [],
        value: []
      },
      pltBrTreeShow: false,
      pltBrTreeOption: {
        title: "选择机构",
        type: "multiple"
      },
      authBrNoIndex: null,
      rootNode: {
        id: "0",
        text: "系统功能",
        type: "root",
        collapsed: false,
        children: []
      },
      dialogVisible: false,
      dialogTitle: "",
      dialogType: "",
      dataAuthDialogVisible: false,
      menuForm: {
        menuName: "",
        menuIcon: "",
        menuUrl: "",
        newTab: "1"
      },
      menuRules: {
        menuName: [
          { required: true, message: "请输入菜单名称", trigger: "blur" }
        ]
      },
      viewForm: {
        viewName: ""
      },
      viewRules: {
        viewName: [
          { required: true, message: "请输入视角名称", trigger: "blur" }
        ]
      },
      viewCmptMenuForm: {
        viewCmptTyp: "1",
        viewCmptName: "",
        menuUrl: "",
        refreshFlg: "0",
        loadFlg: "0"
      },
      viewCmptMenuRules: {
        viewCmptName: [
          { required: true, message: "请输入菜单名称", trigger: "blur" }
        ],
        menuUrl: [
          { required: true, message: "请输入菜单地址", trigger: "blur" }
        ]
      },
      viewCmptBtnForm: {
        viewCmptTyp: "2",
        viewCmptName: "",
        btnMthd: "",
        expr: ""
      },
      viewCmptBtnRules: {
        viewCmptName: [
          { required: true, message: "请输入按钮名称", trigger: "blur" }
        ]
      },
      modelForm: {
        btnKey: "",
        mapperPath: "",
        modelName: "",
        modelTyp: "1",
        modelRmk: "",
        expr: ""
      },
      modelRules: {
        modelName: [
          { required: true, message: "请输入功能名称", trigger: "blur" }
        ],
        btnKey: [{ required: true, message: "请输入按钮标识", trigger: "blur" }]
      },
      SELECTED_ICON: require("@/assets/sys/auth_images/selected.png"),
      UNSELECTED_ICON: require("@/assets/sys/auth_images/unselected.png"),
      dataAuthTable: dataAuthList,
      dataAuthTypeList: dataAuthTypeList,
      dataAuthTableSelection: [],
      dataAuthTableSelected: {},
      urlTableData: {
        modelId: ""
      }
    };
  },
  props: ["editAuth", "roleId"],
  created() {
    this.initCustomNode();
    this.initCustomEdge();
    this.initCustomEvent();
    this.initChart(res => {
      this.initChartOPtion();
      if (this.editAuth) {
        api.getAuthByRole(this.roleId, res2 => {
          if (res2.code == 0) {
            let data = res2.data;
            setAuthByRole(this, res, data);
            this.rootNode.children = convertAuthData(
              res.menuList,
              res.viewList,
              res.viewCmptList,
              res.modelList
            );
            this.createChart(this.rootNode);
          } else {
            this.$message.error(res2.msg);
          }
        });
      } else {
        this.rootNode.children = convertAuthData(
          res.menuList,
          res.viewList,
          res.viewCmptList,
          res.modelList
        );
        this.createChart(this.rootNode);
      }
    });
  },
  methods: {
    initChart(callback) {
      api.getAll(res => {
        if (res.code == 0) {
          callback(res);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    initCustomNode() {
      G6.registerNode(
        "tree-node",
        {
          draw: (cfg, group) => {
            let container = this.createNodeBox(group, cfg);
            let hasChildren = cfg.children && cfg.children.length > 0;
            if (hasChildren) {
              this.createNodeMarker(group, cfg);
            }
            if (this.editAuth && cfg.id != "0") {
              this.createCheckBoxMarker(group, cfg);
            }
            return container;
          }
        },
        "single-shape"
      );
    },
    initCustomEdge() {
      let _this = this;
      G6.registerEdge(
        "tree-edge",
        {
          itemType: "edge",
          draw: function draw(cfg, group) {
            let sourceNode = cfg.sourceNode.getModel();
            let targetNode = cfg.targetNode.getModel();
            let config = _this.nodeStyle(sourceNode.type);

            const startPoint = cfg.startPoint;
            const endPoint = cfg.endPoint;
            const shape = group.addShape("path", {
              attrs: {
                path: [
                  ["M", startPoint.x, startPoint.y],
                  ["L", endPoint.x / 3 + (2 / 3) * startPoint.x, startPoint.y], // 三分之一处
                  ["L", endPoint.x / 3 + (2 / 3) * startPoint.x, endPoint.y], // 三分之二处
                  ["L", endPoint.x, endPoint.y]
                ],
                lineWidth: 1,
                stroke: config.borderColor,
                zIndex: 1,
                lineAppendWidth: 12
              },

              // must be assigned in G6 3.3 and later versions. it can be any value you want
              name: "path-shape"
            });
            return shape;

            /* let sourceNode = cfg.sourceNode.getModel();
                      let targetNode = cfg.targetNode.getModel();
                      let startPoint = cfg.startPoint;
                      let endPoint = cfg.endPoint;
                      let points = [ startPoint ];
                      let controlPoints = this.getControlPoints(cfg);
                      // 添加控制点
                      if (controlPoints) {
                          points = points.concat(controlPoints);
                      }
                      points.push(endPoint);
                      let path = this.getPath(points);

                      let config = _this.nodeStyle(sourceNode.type);

                      group.addShape('path', {
                          attrs: {
                              path,
                              lineWidth: 12,
                              opacity: 0,
                              zIndex: 0
                          },
                          className: 'line-bg'
                      });
                      let keyShape = group.addShape('path', {
                          attrs: {
                              path,
                              lineWidth: 1,
                              stroke: config.borderColor,
                              zIndex: 1,
                              lineAppendWidth: 12
                          }
                      });

                      return keyShape; */
          }
        } /* 'cubic-horizontal' */
      );
    },
    initCustomEvent() {
      let _this = this;
      G6.registerBehavior("node-edit", {
        getDefaultCfg() {
          return {
            multiple: true
          };
        },
        getEvents() {
          return {
            "node:click": "onNodeClick",
            "node:dblclick": "onNodeDBlclick",
            "canvas:click": "onCanvasClick",
            "node:contextmenu": "onNodeContextmenu"
          };
        },
        onNodeClick(e) {
          _this.currentItem = e.item;
          if (_this.editAuth && e.target.get("className") == "checkbox-image") {
            let model = _this.currentItem.get("model");
            let modelData = _this.graph.findDataById(model.id);
            if (modelData.collapsed) {
              modelData.collapsed = false;
            } else {
              modelData.collapsed = true;
            }
            if (model.selected) {
              childSelected(_this, modelData, false);
            } else {
              parentSelected(_this, modelData, true);
              childSelected(_this, modelData, true);
            }
            _this.graph.changeData();
          }
        },
        onNodeDBlclick(e) {
          _this.currentItem = e.item;
          if (_this.editAuth) {
            _this.openAuthEdit();
          } else {
            _this.openEdit();
          }
        },
        onNodeContextmenu(e) {
          _this.currentItem = e.item;
          _this.cmPosition.top = e.canvasY;
          _this.cmPosition.left = e.canvasX;
          let model = e.item.getModel();
          if (_this.editAuth) {
            switch (model.type) {
              case "model":
                _this.cmItems = [
                  {
                    type: "authentication",
                    text: "功能鉴权"
                  }
                ];
                break;
              default:
                _this.cmItems = [];
                break;
            }
          } else {
            switch (model.type) {
              case "root":
                _this.cmItems = [
                  {
                    type: "addMenu",
                    text: "插入菜单"
                  },
                  {
                    type: "addView",
                    text: "插入视角"
                  }
                ];
                break;
              case "menu":
                _this.cmItems = [
                  {
                    type: "addMenu",
                    text: "插入菜单"
                  },
                  {
                    type: "addView",
                    text: "插入视角"
                  },
                  {
                    type: "addModel",
                    text: "插入功能项"
                  },
                  // {
                  //   type: "addModelLink",
                  //   text: "引用现有功能项",
                  // },
                  {
                    type: "delete",
                    text: "删除"
                  }
                ];
                break;
              case "view":
                _this.cmItems = [
                  {
                    type: "addViewMenu",
                    text: "插入视角菜单"
                  },
                  {
                    type: "addViewBtn",
                    text: "插入视角按钮"
                  },
                  {
                    type: "addModel",
                    text: "插入功能项"
                  },
                  {
                    type: "delete",
                    text: "删除"
                  }
                ];
                break;
              case "viewMenu":
                _this.cmItems = [
                  {
                    type: "addViewMenu",
                    text: "插入视角菜单"
                  },
                  {
                    type: "addModel",
                    text: "插入功能项"
                  },
                  // {
                  //   type: "addModelLink",
                  //   text: "引用现有功能项",
                  // },
                  {
                    type: "delete",
                    text: "删除"
                  }
                ];
                break;
              case "viewBtn":
                _this.cmItems = [
                  {
                    type: "delete",
                    text: "删除"
                  }
                ];
                break;
              case "model":
                _this.cmItems = [
                  {
                    type: "addView",
                    text: "插入视角"
                  },
                  {
                    type: "addModel",
                    text: "插入功能项"
                  },
                  // {
                  //   type: "addModelLink",
                  //   text: "引用现有功能项",
                  // },
                  {
                    type: "delete",
                    text: "删除"
                  }
                ];
                break;
              default:
                _this.cmItems = [];
                break;
            }
          }
          _this.cmVisible = true;
        },
        onCanvasClick(e) {
          _this.dialogVisible = false;
        }
      });
    },
    initChartOPtion() {
      let chart = this.$refs.chart;
      let width = chart.clientWidth;
      let height = chart.clientHeight;
      height = document.body.clientHeight - 166;
      let selectedItem;
      this.minimap = new G6.Minimap({
        size: [200, 150]
      });
      this.graph = new G6.TreeGraph({
        container: "chart",
        width,
        height,
        modes: {
          default: [
            {
              type: "collapse-expand",
              shouldUpdate: e => {
                /* 点击 node 禁止展开收缩 */
                if (e.target.get("className") !== "collapse-icon") {
                  return false;
                }
                return true;
              },
              onChange: (item, collapsed) => {
                selectedItem = item;
                let icon = item.get("group").findByClassName("collapse-icon");
                if (collapsed) {
                  icon.attr("symbol", this.EXPAND_ICON);
                } else {
                  icon.attr("symbol", this.COLLAPSE_ICON);
                }
              },
              animate: {
                callback: () => {
                  this.graph.focusItem(selectedItem);
                }
              }
            },
            {
              type: "drag-canvas",
              shouldUpdate: function shouldUpdate() {
                return false;
              },
              shouldEnd: function shouldUpdate() {
                return false;
              }
            },
            "drag-canvas",
            "zoom-canvas",
            "node-edit"
          ]
        },
        defaultNode: {
          shape: "tree-node",
          anchorPoints: [
            [0, 0.5],
            [1, 0.5]
          ]
        },
        defaultEdge: {
          type: "tree-edge",
          style: {
            stroke: "#A3B1BF"
          }
        },
        layout: {
          type: "compactBox",
          direction: "LR",
          getId: d => {
            return d.id;
          },
          getWidth: () => {
            return 150;
          },
          getVGap: () => {
            return 20;
          },
          getHGap: () => {
            return 100;
          }
        },
        plugins: [this.minimap]
      });
    },
    createChart(data) {
      this.graph.data(data);
      this.graph.render();
      this.graph.fitView(20);
    },
    COLLAPSE_ICON(x, y, r) {
      return [
        ["M", x - r, y],
        ["a", r, r, 0, 1, 0, r * 2, 0],
        ["a", r, r, 0, 1, 0, -r * 2, 0],
        ["M", x - r + 4, y],
        ["L", x - r + 2 * r - 4, y]
      ];
    },
    EXPAND_ICON(x, y, r) {
      return [
        ["M", x - r, y],
        ["a", r, r, 0, 1, 0, r * 2, 0],
        ["a", r, r, 0, 1, 0, -r * 2, 0],
        ["M", x - r + 4, y],
        ["L", x - r + 2 * r - 4, y],
        ["M", x - r + r, y - r + 4],
        ["L", x, y + r - 4]
      ];
    },
    createNodeBox(group, cfg) {
      let config = this.nodeStyle(cfg.type);
      if (cfg.type == "model") {
        if (cfg.modelTyp == 2) {
          config = this.nodeStyle("modelView");
        }
      }
      let width = this.nodeBox.width;
      let height = this.nodeBox.height;
      let container = group.addShape("rect", {
        attrs: {
          x: 0,
          y: 0,
          width,
          height
        }
      });
      /* 矩形 */
      group.addShape("rect", {
        attrs: {
          x: 0,
          y: 0,
          width: width - this.markerBox.r * 2,
          height,
          fill: config.bgColor,
          stroke: config.borderColor,
          radius: 2
        },
        className: "node-rect"
      });

      /* 左边的粗线 */
      group.addShape("rect", {
        attrs: {
          x: 0,
          y: 0,
          width: 5,
          height,
          fill: config.borderColor,
          radius: 1.5
        },
        className: "node-left-line"
      });

      let textX = 20;
      if (this.editAuth) {
        textX = 50;
      }
      /* 文字 */
      group.addShape("text", {
        attrs: {
          text: cfg.text,
          x: textX,
          y: height / 2,
          fontSize: 14,
          fontWeight: 700,
          textAlign: "left",
          textBaseline: "middle",
          fill: config.fontColor
        },
        className: "node-text"
      });
      return container;
    },
    createNodeMarker(group, cfg) {
      let r = this.markerBox.r;
      let x = this.markerBox.x;
      let y = this.markerBox.y;
      group.addShape("marker", {
        attrs: {
          x,
          y,
          r,
          symbol: cfg.collapsed ? this.EXPAND_ICON : this.COLLAPSE_ICON,
          stroke: "#666",
          lineWidth: 1,
          cursor: "pointer"
        },
        className: "collapse-icon"
      });
    },
    createCheckBoxMarker(group, cfg) {
      let x = this.checkBoxMarkerBox.x;
      let y = this.checkBoxMarkerBox.y;
      let width = this.checkBoxMarkerBox.width;
      let height = this.checkBoxMarkerBox.height;
      let image = group.addShape("image", {
        attrs: {
          x,
          y,
          width,
          height,
          img: cfg.selected ? this.SELECTED_ICON : this.UNSELECTED_ICON,
          cursor: "pointer"
        },
        className: "checkbox-image"
      });
    },
    createEdgeText(cfg, group) {
      let targetNode = cfg.targetNode.getModel();
      let startPoint = cfg.startPoint;
      let endPoint = cfg.endPoint;
      if (
        targetNode.linkTyp &&
        targetNode.linkTyp != "1" &&
        targetNode.linkTyp != "2"
      ) {
        let linkName = targetNode.linkName;
        let config;
        if (targetNode.linkTyp == "3") {
          config = this.nodeStyle("button");
        } else if (targetNode.linkTyp == "4") {
          config = this.nodeStyle("href");
        }

        /* 连接线的中间点 */
        let centerPoint = {
          x: startPoint.x + (endPoint.x - startPoint.x) / 2,
          y: startPoint.y + (endPoint.y - startPoint.y) / 2
        };
        let textRect = group.addShape("rect", {
          attrs: {
            fill: config.bgColor,
            stroke: config.borderColor,
            radius: 2,
            opacity: 1
          },
          className: "edge-text-rect"
        });
        let text = group.addShape("text", {
          attrs: {
            text: linkName,
            x: 0,
            y: 0,
            fontSize: 12,
            textAlign: "left",
            textBaseline: "middle",
            fill: config.fontColor,
            opacity: 1
          },
          className: "edge-text"
        });
        let textBBox = text.getBBox();
        /* text 的位置 */
        text.attr({
          x: centerPoint.x - textBBox.width / 2,
          y: centerPoint.y
        });
        /* text 的框框 */
        textRect.attr({
          x: centerPoint.x - textBBox.width / 2 - 4,
          y: centerPoint.y - textBBox.height / 2 - 6,
          width: textBBox.width + 8,
          height: textBBox.height + 10
        });
        if (targetNode.linkTyp == "4") {
          group.addShape("path", {
            attrs: {
              path: [
                [
                  "M",
                  centerPoint.x - textBBox.width / 2,
                  centerPoint.y + textBBox.height / 2
                ],
                [
                  "L",
                  centerPoint.x + textBBox.width / 2,
                  centerPoint.y + textBBox.height / 2
                ]
              ],
              stroke: config.fontColor,
              lineWidth: 1
            },
            className: "edge-text-line"
          });
        }
      }
    },
    cmCallBack(item) {
      let node = this.currentItem;
      let model = node.getModel();
      let nodeType = model.type;
      switch (item.type) {
        case "addMenu":
          this.dialogTitle = "新增菜单";
          this.dialogType = "addMenu";
          this.dialogVisible = true;
          this.menuForm = {
            menuName: "",
            menuIcon: "",
            newTab: "1",
            menuUrl: "",
            sn: 0
          };
          break;
        case "addView":
          this.dialogTitle = "新增视角";
          this.dialogType = "addView";
          this.dialogVisible = true;
          this.viewForm = {
            viewName: ""
          };
          break;
        case "addViewMenu":
          this.dialogTitle = "新增视角菜单";
          this.dialogType = "addViewMenu";
          this.dialogVisible = true;
          this.viewCmptMenuForm = {
            viewCmptTyp: "1",
            viewCmptName: "",
            menuUrl: "",
            refreshFlg: "0",
            loadFlg: "0"
          };
          break;
        case "addViewBtn":
          this.dialogTitle = "新增视角按钮";
          this.dialogType = "addViewBtn";
          this.dialogVisible = true;
          this.viewCmptBtnForm = {
            viewCmptTyp: "2",
            viewCmptName: "",
            btnMthd: "",
            expr: ""
          };
          break;
        case "addModel":
          this.dialogTitle = "新增功能";
          this.dialogType = "addModel";
          this.dialogVisible = true;
          this.modelForm = {
            btnKey: "",
            mapperPath: "",
            modelName: "",
            modelTyp: "1",
            modelRmk: "",
            expr: ""
          };
          this.dataAuthTable = JSON.parse(JSON.stringify(dataAuthList));
          this.$nextTick(() => {
            this.$refs.dataAuthTable.clearSelection();
            this.urlTableData.modelId = "";
            this.$refs.urlList.refresh();
          });
          break;
        case "addModelLink":
          this.checkboxOption.value = [];
          api.findAllModelList(res => {
            let data = [];
            for (let item of res.list) {
              if (
                !findUpIsImport(this.graph, model, item.modelId) &&
                !findChildIsImport(this.graph, model, item.modelId)
              ) {
                data.push(item);
              }
            }
            this.checkboxOption.data = data;
            this.checkboxShow = true;
          });
          break;
        case "delete":
          this.$confirm(
            "此操作将永久删除该记录，及其所有节点，是否继续?",
            "提示",
            {
              type: "warning"
            }
          ).then(() => {
            // let datas = this.graph.save();
            // let datas = find(type, fn)
            let node = this.graph.find("node", node => {
              return node.get("model").id === model.id;
            });
            let obj = getDeleteIds(node.get("model"));
            api.deleteNodes(obj, res => {
              if (res.code == 0) {
                this.deleteNode();
              } else {
                this.$message.error(res.msg);
              }
            });
          });
          break;
        case "authentication":
          break;
      }
    },
    deleteNode() {
      let node = this.currentItem;
      let model = node.getModel();
      let id = model.id;
      this.graph.removeChild(id);
      let parentNode = node.getInEdges()[0].getSource();
      let parentNodeId = parentNode.get("id");
      let parentNodeData = this.graph.findDataById(parentNodeId);
      if (parentNodeData.children.length === 0) {
        parentNode
          .get("group")
          .findByClassName("collapse-icon")
          .destroy();
      }
    },
    openEdit() {
      let node = this.currentItem.getModel();
      switch (node.type) {
        case "menu":
          this.dialogTitle = "修改菜单";
          this.dialogType = "editMenu";
          this.menuForm = {
            menuName: node.text,
            menuIcon: node.icon,
            menuUrl: node.menuUrl,
            newTab: node.newTab,
            sn: node.sn
          };
          this.dialogVisible = true;
          break;
        case "view":
          this.dialogTitle = "编辑视角";
          this.dialogType = "editView";
          this.viewForm = {
            viewName: node.text
          };
          this.dialogVisible = true;
          break;
        case "viewMenu":
          this.dialogTitle = "编辑视角菜单";
          this.dialogType = "editViewMenu";
          this.viewCmptMenuForm = {
            viewCmptTyp: "1",
            viewCmptName: node.text,
            menuUrl: node.menuUrl,
            refreshFlg: node.refreshFlg,
            loadFlg: node.loadFlg
          };
          break;
        case "viewBtn":
          this.dialogTitle = "编辑视角按钮";
          this.dialogType = "editViewBtn";
          this.viewCmptBtnForm = {
            viewCmptTyp: "2",
            viewCmptName: node.text,
            btnMthd: node.btnMthd,
            expr: node.expr
          };
          this.dialogVisible = true;
          break;
        case "model":
          this.dialogTitle = "编辑功能";
          this.dialogType = "editModel";
          this.dialogVisible = true;
          this.modelForm = {
            btnKey: node.btnKey,
            mapperPath: node.mapperPath,
            modelName: node.text,
            modelTyp: node.modelTyp,
            modelRmk: node.modelRmk,
            expr: node.expr
          };
          if (node.dataAuth) {
            this.dataAuthTable = JSON.parse(node.dataAuth);
          }
          if (this.dataAuthTable.length == 0) {
            this.dataAuthTable = JSON.parse(JSON.stringify(dataAuthList));
          }
          this.$nextTick(() => {
            if (node.dataAuth) {
              for (let row of this.dataAuthTable) {
                if (row.selected) {
                  this.$refs.dataAuthTable.toggleRowSelection(row, true);
                  row.selected = false;
                }
              }
            }
            this.urlTableData.modelId = node.nodeId;
            this.$refs.urlList.refresh();
          });
          break;
      }
      this.dialogVisible = true;
    },
    openAuthEdit() {
      let node = this.currentItem.getModel();
      switch (node.type) {
        case "model":
          this.dataAuthTable = [];
          if (node.dataAuth) {
            let dataAuthList = JSON.parse(node.dataAuth);
            for (let item of dataAuthList) {
              if (item.selected) {
                this.dataAuthTable.push(item);
              }
            }
          }
          api.getDataAuthByModelId(node.nodeId, this.roleId, res => {
            if (res.code == 0) {
              this.dataAuthDialogVisible = true;
              if (!res.authData) {
                return;
              }
              this.$nextTick(() => {
                this.$refs.dataAuthConifgTable.clearSelection();
                this.dataAuthTableSelected = {};
                if (res.authData.authType) {
                  for (let row of this.dataAuthTable) {
                    // if(res.authData.authType == "0"){
                    //     if(res.authData.authField == JSON.stringify(row)){
                    //         this.$refs.dataAuthConifgTable.toggleRowSelection(row,true);
                    //         this.dataAuthTableSelected = row;
                    //     }
                    // }else{
                    if (res.authData.authType == row.id) {
                      this.$refs.dataAuthConifgTable.toggleRowSelection(
                        row,
                        true
                      );
                      this.dataAuthTableSelected = row;
                    }
                    // }
                  }
                }
              });
            } else {
              this.$message.error(res.msg);
            }
          });
          break;
      }
    },
    validateForm() {
      if (this.$refs.urlList) {
        this.$refs.urlList.validateForm(valid => {
          if (valid) {
            // console.log("获取表单全部数据",this.$refs.cusList.getFormValue())
            // console.log("获取表单第一条数据",this.$refs.cusList.getFormValue(0))
            // console.log("获取表单第一条数据中指定字段",this.$refs.cusList.getFormValue(0,"cusNo"))
            this.dialogSubmit();
          } else {
            return false;
          }
        });
      } else {
        this.dialogSubmit();
      }
    },
    dialogSubmit() {
      let node = this.currentItem;
      let group = node.get("group");
      let model = node.getModel();
      let nodeId = model.id;
      let parentData = this.graph.findDataById(nodeId);
      switch (this.dialogType) {
        case "addMenu":
        case "editMenu":
          this.$refs["menuForm"].validate(valid => {
            if (valid) {
              if (this.dialogType == "addMenu") {
                this.menuForm.upMenuId = model.nodeId;
                api.addMenu(this.menuForm, res => {
                  if (res.code == 0) {
                    if (
                      !parentData.children ||
                      parentData.children.length == 0
                    ) {
                      parentData.children = [];
                      this.createNodeMarker(group, model);
                    }
                    let menuData = {
                      id: uuid.v4(),
                      nodeId: res.menuId,
                      text: this.menuForm.menuName,
                      type: "menu",
                      collapsed: true,
                      icon: this.menuForm.menuIcon,
                      newTab: this.menuForm.newTab,
                      menuUrl: this.menuForm.menuUrl,
                      sn: this.menuForm.sn,
                      sts: "1",
                      children: []
                    };
                    parentData.children.push(menuData);
                    parentData.collapsed = false;
                    let icon = group.findByClassName("collapse-icon");
                    icon.attr("symbol", this.COLLAPSE_ICON);
                    this.graph.changeData();
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              } else if (this.dialogType == "editMenu") {
                this.menuForm.menuId = model.nodeId;
                api.updateMenu(this.menuForm, res => {
                  if (res.code == 0) {
                    let menuData = {
                      text: this.menuForm.menuName,
                      icon: this.menuForm.menuIcon,
                      menuUrl: this.menuForm.menuUrl,
                      sn: this.menuForm.sn,
                      newTab: this.menuForm.newTab
                    };
                    node.update(menuData);
                    let text = group.findByClassName("node-text");
                    text.attrs.text = menuData.text;
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              }
            } else {
              return false;
            }
          });
          break;
        case "addView":
        case "editView":
          this.$refs["viewForm"].validate(valid => {
            if (valid) {
              if (this.dialogType == "addView") {
                this.viewForm.upCmptId = model.nodeId;
                if (model.type == "menu") {
                  this.viewForm.upCmptTyp = "1";
                }
                api.addView(this.viewForm, res => {
                  if (res.code == 0) {
                    if (
                      !parentData.children ||
                      parentData.children.length == 0
                    ) {
                      parentData.children = [];
                      this.createNodeMarker(group, model);
                    }
                    let viewData = {
                      id: uuid.v4(),
                      nodeId: res.viewId,
                      upCmptId: this.viewForm.upCmptId,
                      text: this.viewForm.viewName,
                      type: "view",
                      collapsed: true,
                      sn: 1,
                      sts: "1",
                      children: []
                    };
                    parentData.children.push(viewData);
                    parentData.collapsed = false;
                    let icon = group.findByClassName("collapse-icon");
                    icon.attr("symbol", this.COLLAPSE_ICON);
                    this.graph.changeData();
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              } else if (this.dialogType == "editView") {
                this.viewForm.viewId = model.nodeId;
                api.updateView(this.viewForm, res => {
                  if (res.code == 0) {
                    let viewData = {
                      text: this.viewForm.viewName
                    };
                    node.update(viewData);
                    let text = group.findByClassName("node-text");
                    text.attrs.text = viewData.text;
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              }
            } else {
              return false;
            }
          });
          break;
        case "addViewMenu":
        case "editViewMenu":
          this.$refs["viewCmptMenuForm"].validate(valid => {
            if (valid) {
              if (this.dialogType == "addViewMenu") {
                if (model.type == "viewMenu") {
                  this.viewCmptMenuForm.viewId = model.viewId;
                  this.viewCmptMenuForm.upViewCmptId = model.nodeId;
                } else {
                  this.viewCmptMenuForm.viewId = model.nodeId;
                  this.viewCmptMenuForm.upViewCmptId = 0;
                }
                api.addViewCmpt(this.viewCmptMenuForm, res => {
                  if (res.code == 0) {
                    if (
                      !parentData.children ||
                      parentData.children.length == 0
                    ) {
                      parentData.children = [];
                      this.createNodeMarker(group, model);
                    }
                    let viewMenuData = {
                      id: uuid.v4(),
                      nodeId: res.viewCmptId,
                      viewId: this.viewCmptMenuForm.viewId,
                      menuUrl: this.viewCmptMenuForm.menuUrl,
                      upViewCmptId: this.viewCmptMenuForm.upViewCmptId,
                      viewCmptTyp: this.viewCmptMenuForm.viewCmptTyp,
                      refreshFlg: this.viewCmptMenuForm.refreshFlg,
                      loadFlg: this.viewCmptMenuForm.loadFlg,
                      text: this.viewCmptMenuForm.viewCmptName,
                      type: "viewMenu",
                      collapsed: true,
                      sn: 1,
                      sts: "1",
                      children: []
                    };
                    parentData.children.push(viewMenuData);
                    parentData.collapsed = false;
                    let icon = group.findByClassName("collapse-icon");
                    icon.attr("symbol", this.COLLAPSE_ICON);
                    this.graph.changeData();
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              } else if (this.dialogType == "editViewMenu") {
                this.viewCmptMenuForm.viewCmptId = model.nodeId;
                api.updateViewCmpt(this.viewCmptMenuForm, res => {
                  if (res.code == 0) {
                    let viewMenuData = {
                      text: this.viewCmptMenuForm.viewCmptName,
                      refreshFlg: this.viewCmptMenuForm.refreshFlg,
                      loadFlg: this.viewCmptMenuForm.loadFlg
                    };
                    node.update(viewMenuData);
                    let text = group.findByClassName("node-text");
                    text.attrs.text = viewMenuData.text;
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              }
            } else {
              return false;
            }
          });
          break;
        case "addViewBtn":
        case "editViewBtn":
          this.$refs["viewCmptBtnForm"].validate(valid => {
            if (valid) {
              if (this.dialogType == "addViewBtn") {
                this.viewCmptBtnForm.viewId = model.nodeId;
                this.viewCmptBtnForm.upViewCmptId = 0;
                api.addViewCmpt(this.viewCmptBtnForm, res => {
                  if (res.code == 0) {
                    if (
                      !parentData.children ||
                      parentData.children.length == 0
                    ) {
                      parentData.children = [];
                      this.createNodeMarker(group, model);
                    }
                    let viewBtnData = {
                      id: uuid.v4(),
                      nodeId: res.viewCmptId,
                      viewId: this.viewCmptMenuForm.viewId,
                      viewCmptTyp: this.viewCmptBtnForm.viewCmptTyp,
                      btnMthd: this.viewCmptBtnForm.btnMthd,
                      expr: this.viewCmptBtnForm.expr,
                      text: this.viewCmptBtnForm.viewCmptName,
                      type: "viewBtn",
                      collapsed: true,
                      sn: 1,
                      sts: "1",
                      children: []
                    };
                    parentData.children.push(viewBtnData);
                    parentData.collapsed = false;
                    let icon = group.findByClassName("collapse-icon");
                    icon.attr("symbol", this.COLLAPSE_ICON);
                    this.graph.changeData();
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              } else if (this.dialogType == "editViewBtn") {
                this.viewCmptBtnForm.viewCmptId = model.nodeId;
                api.updateViewCmpt(this.viewCmptBtnForm, res => {
                  if (res.code == 0) {
                    let viewBtnData = {
                      text: this.viewCmptBtnForm.viewCmptName,
                      btnMthd: this.viewCmptBtnForm.btnMthd,
                      expr: this.viewCmptBtnForm.expr
                    };
                    node.update(viewBtnData);
                    let text = group.findByClassName("node-text");
                    text.attrs.text = viewBtnData.text;
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              }
            } else {
              return false;
            }
          });
          break;
        case "addModel":
        case "editModel":
          this.$refs["modelForm"].validate(valid => {
            if (valid) {
              // let dataAuthSelectionStr = JSON.stringify(this.dataAuthTableSelection);
              for (let auth of this.dataAuthTable) {
                for (let item of this.dataAuthTableSelection) {
                  if (JSON.stringify(auth) == JSON.stringify(item)) {
                    auth.selected = true;
                    break;
                  }
                }
              }
              let dataAuthSelectionStr = JSON.stringify(this.dataAuthTable);
              this.modelForm.dataAuth = dataAuthSelectionStr;
              let allData = this.$refs.urlList.getFormValue().allData;
              if (allData && allData.length > 0) {
                this.modelForm.urlAuth = JSON.stringify(allData);
              } else {
                this.modelForm.urlAuth = null;
              }
              if (this.dialogType == "addModel") {
                this.modelForm.upCmptId = model.nodeId;
                api.addModel(this.modelForm, res => {
                  if (res.code == 0) {
                    if (
                      !parentData.children ||
                      parentData.children.length == 0
                    ) {
                      parentData.children = [];
                      this.createNodeMarker(group, model);
                    }
                    let modelData = {
                      id: uuid.v4(),
                      nodeId: res.modelId,
                      upCmptId: this.modelForm.upCmptId,
                      modelTyp: this.modelForm.modelTyp,
                      modelRmk: this.modelForm.modelRmk,
                      expr: this.modelForm.expr,
                      dataAuth: this.modelForm.dataAuth,
                      urlAuth: this.modelForm.urlAuth,
                      btnKey: this.modelForm.btnKey,
                      mapperPath: this.modelForm.mapperPath,
                      text: this.modelForm.modelName,
                      type: "model",
                      collapsed: true,
                      children: []
                    };
                    parentData.children.push(modelData);
                    parentData.collapsed = false;
                    let icon = group.findByClassName("collapse-icon");
                    icon.attr("symbol", this.COLLAPSE_ICON);
                    this.graph.changeData();
                    let config;
                    if (modelData.modelTyp == "1") {
                      config = this.nodeStyle("model");
                    } else {
                      config = this.nodeStyle("modelView");
                    }
                    let modelNode = this.graph.findById(modelData.id);
                    let nodeRect = modelNode
                      .get("group")
                      .findByClassName("node-rect");
                    nodeRect.attrs.fill = config.bgColor;
                    nodeRect.attrs.stroke = config.borderColor;
                    let nodeLeftLine = modelNode
                      .get("group")
                      .findByClassName("node-left-line");
                    nodeLeftLine.attrs.fill = config.borderColor;
                    let text = modelNode
                      .get("group")
                      .findByClassName("node-text");
                    text.attrs.fill = config.fontColor;
                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              } else if (this.dialogType == "editModel") {
                this.modelForm.modelId = model.nodeId;
                api.updateModel(this.modelForm, res => {
                  if (res.code == 0) {
                    let modelData = {
                      text: this.modelForm.modelName,
                      btnKey: this.modelForm.btnKey,
                      mapperPath: this.modelForm.mapperPath,
                      modelTyp: this.modelForm.modelTyp,
                      modelRmk: this.modelForm.modelRmk,
                      expr: this.modelForm.expr,
                      dataAuth: this.modelForm.dataAuth,
                      urlAuth: this.modelForm.urlAuth
                    };
                    node.update(modelData);

                    let config;
                    if (modelData.modelTyp == "1") {
                      config = this.nodeStyle("model");
                    } else {
                      config = this.nodeStyle("modelView");
                    }
                    let nodeRect = group.findByClassName("node-rect");
                    nodeRect.attrs.fill = config.bgColor;
                    nodeRect.attrs.stroke = config.borderColor;
                    let nodeLeftLine = group.findByClassName("node-left-line");
                    nodeLeftLine.attrs.fill = config.borderColor;
                    let text = group.findByClassName("node-text");
                    text.attrs.fill = config.fontColor;
                    text.attrs.text = modelData.text;

                    this.dialogVisible = false;
                  } else {
                    this.$message.error(res.msg);
                  }
                });
              }
            } else {
              return false;
            }
          });
          break;
      }
    },
    dataAuthDialogSubmit() {
      let node = this.currentItem;
      let model = node.getModel();
      let authData = this.dataAuthTableSelected;
      if (!authData.id) {
        this.dataAuthDialogVisible = false;
        return;
      }
      let data = {
        modelId: model.nodeId,
        authType: authData.id,
        roleId: this.roleId
      };
      // if(authData.id == "0"){
      //     data.authField = JSON.stringify(authData);
      // }
      // if(authData.type=="88"&&authData.brNos){
      //     data.authField = authData.brNos.join(",")
      // }else{
      //     data.authField = authData.field;
      // }
      api.addDataAuth(data, res => {
        if (res.code == 0) {
          let modelData = {
            authData
          };
          node.update(modelData);
          this.dataAuthDialogVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    checkboxFunc(values) {
      let node = this.currentItem.getModel();
      let importArr = [];

      for (let value of values) {
        let importData = {
          cmptId: value.modelId,
          upCmptId: node.nodeId,
          cmpt_typ: value.modelTyp
        };
        importArr.push(importData);
      }

      api.addImportModel({ importArr: importArr }, res => {
        if (res.code == 0) {
          this.initChart(() => {
            this.graph.read(this.rootNode);
            this.graph.fitView(20);
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    dataAuthTableSelectionChange(val) {
      this.dataAuthTableSelection = val;
    },
    dataAuthTableSelect(selection, row) {
      this.$refs.dataAuthConifgTable.clearSelection();
      this.$refs.dataAuthConifgTable.toggleRowSelection(row, true);
      this.dataAuthTableSelected = row;
    },
    handleAdd() {
      this.$refs.urlList.addData();
    },
    cancel(index, row) {
      this.$refs.urlList.editTable(index, false);
    },
    edit(index, row) {
      this.$refs.urlList.editTable(index, true);
    },
    delete(index, row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {});
    },
    editCorpAuth(row) {
      let brNos = [];
      if (row.row.brNos) {
        brNos = row.row.brNos;
      }
      this.pltBrTreeOption.value = brNos;
      this.pltBrTreeShow = true;
      this.authBrNoIndex = row.$index;
    },
    pltBrTreeFunc(values) {
      let brNos = [];
      for (let br of values) {
        brNos.push(br.brNo);
      }
      if (this.dataAuthTable.length > 0) {
        let dataAuth = this.dataAuthTable[this.authBrNoIndex];
        dataAuth.brNos = brNos;
      }
    },
    handleAddCustomAuth() {
      let data = {
        id: new Date().getTime(),
        field: "",
        text: ""
      };
      this.dataAuthTable.push(data);
    }
  }
};
</script>
<style>
.authDialog .el-dialog {
  width: 800px;
}

.authDialog .el-dialog__body {
  height: 500px;
  overflow: auto;
}

.authDialog .el-divider__text {
  font-size: 18px;
}

.authDialog .el-dropdown-DColumn {
  display: none;
}

.authDialog .dataAuthTable .el-table__body {
  width: 100% !important;
}

.dataAuthConifgTable > .el-table__header-wrapper .el-checkbox {
  display: none;
}

.g6-minimap {
  position: absolute;
  bottom: 10px;
  right: 10px;
  border: 1px solid #e2e2e2;
  background: #ffffffe0;
}

.g6-minimap-viewport {
  border: 1px solid rgb(25, 128, 255);
}
</style>
