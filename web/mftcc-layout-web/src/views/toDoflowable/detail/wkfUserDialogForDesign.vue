<template>
  <el-dialog
    :visible.sync="visible"
    @close="$emit('update:show', false)"
    :show="show"
    :close-on-click-modal="false"
    class="wkf-user-Dialog"
  >
    <div slot="title" class="dialog-header">
      <label>选择人员</label>
    </div>
    <div class="dialog-content">
      <el-row>
        <el-col :span="12" class="br-tree">
          <div class="content">
            <el-tree
              :data="treeData"
              node-key="id"
              highlight-current
              check-strictly
              :default-expanded-keys="expandedKeys"
              :show-checkbox="false"
              :props="defaultProps"
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              @current-change="handleCheck"
              ref="tree"
              style="padding: 20px 0px 0 20px"
            >
            </el-tree>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="user-group">
            <template>
              <el-radio-group v-model="userCheck">
                <el-radio
                  v-for="user in userArray"
                  :key="user.id"
                  :label="user.id"
                  >{{ user.title }}
                </el-radio>
              </el-radio-group>
            </template>
          </div>
        </el-col>
      </el-row>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
const { postJson } = $axios;
export default {
  data() {
    return {
      title: "选择人员",
      visible: this.show,
      defaultProps: {
        children: "children",
        label: "title"
      },
      expandedKeys: [],
      treeData: [],
      userArray: [],
      userCheck: [],
      allUserArray: [],
      checkAll: false,
      isIndeterminate: false
    };
  },
  props: ["show", "initData", "type", "searchData"],
  watch: {
    show() {
      this.visible = this.show;
      if (this.visible === true) {
        this.getTreeData();
      }
    }
  },
  created() {
    this.getTreeData();
  },
  methods: {
    userCheckedChange() {
      let checkedCount = 0;
      for (let user of this.userArray) {
        if (this.userCheck.indexOf(user.id) != -1) {
          checkedCount += 1;
        }
      }
      this.checkAll =
        checkedCount > 0 && checkedCount === this.userArray.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.userArray.length;
    },
    userChange(value) {
      if (value === true) {
        this.$refs.userSelect.blur();
      }
    },
    getUsers(data, users) {
      if (!(users instanceof Array)) {
        users = [];
      }
      for (let treeNode of data) {
        if (treeNode.user instanceof Array) {
          users = users.concat(treeNode.user);
        }
        if (treeNode.children instanceof Array) {
          users = this.getUsers(treeNode.children, users);
        }
      }
      return users;
    },
    getTreeData() {
      this.userArray = [];
      this.treeData = [];
      this.isIndeterminate = false;
      let data = this.searchData || {};
      postJson(
        "/mftcc-flowable-server/br/listForTreeAll",
        data,
        true,
        res => {
          this.searchData.pageNo = "1";
          this.searchData.pageSize = "-1";
          postJson(
            "/mftcc-sys-server/sys/sysUser/findByPage",
            this.searchData,
            true,
            result => {
              if (result.code == 0) {
                for (var i = 0; i < res.data.length; i++) {
                  var showData = res.data[i];
                  showData.user = [];
                  for (
                    var j = 0, len = result.list.records.length;
                    j < len;
                    j++
                  ) {
                    if (result.list.records[j].brNo == showData.id) {
                      var user = {};
                      user["id"] = result.list.records[j].opNo.toString();
                      user["title"] = result.list.records[j].opName.toString();
                      showData.user.push(user);
                    }
                  }
                }
                this.treeData = res.data;
                this.allUserArray = this.unique(this.getUsers(res.data));
                if (typeof this.initData == "string") {
                  this.userCheck = this.initData;
                } else {
                  this.userCheck = "";
                }
              }
            }
          );
        },
        error => {
          this.userCheck = [];
        }
      );
    },
    submit() {
      if (this.userCheck != null && this.userCheck != "") {
        let result = [];
        for (let user of this.allUserArray) {
          if (user.id === this.userCheck) {
            result.push(user);
          }
        }

        this.$emit("callback", result);
        this.visible = false;
      } else {
        this.$message("请选择审批人");
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data[dp.label].indexOf(value) !== -1;
    },
    unique(arr1) {
      const res = new Map();
      return arr1.filter(a => !res.has(a.id) && res.set(a.id, 1));
    },
    handleCheck(currentObj, treeStatus) {
      if (currentObj.user instanceof Array) {
        this.userArray = this.unique(currentObj.user);
      } else {
        this.userArray = [];
      }
      this.userCheckedChange();
    }
  }
};
</script>
<style>
.wkf-user-Dialog .el-tree {
  height: 300px;
  overflow: auto;
}
.wkf-user-Dialog .el-dialog {
  width: 500px;
}
.wkf-user-Dialog .dialog-header {
  display: flex;
  justify-content: flex-end;
}
.wkf-user-Dialog .dialog-header label {
  position: absolute;
  left: 20px;
}
.wkf-user-Dialog .dialog-content .el-row {
  border: 1px solid rgb(235, 235, 235);
}
.wkf-user-Dialog .dialog-content .el-row .br-tree {
  border-right: 1px solid rgb(235, 235, 235);
}
.wkf-user-Dialog .dialog-content .el-row .user-group {
  padding: 20px 0px 0 20px;
  width: 209px;
  height: 300px;
  overflow-y: auto;
}
.el-tree-node__label {
  font-size: 14px;
}
.user-group .el-radio {
  width: 75%;
  margin: 10px;
}
</style>
