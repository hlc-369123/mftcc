<template>
  <el-dialog
    :visible.sync="visible"
    @close="$emit('update:show', false)"
    :close-on-click-modal="false"
    :show="show"
    class="wkf-user-Dialog"
  >
    <div slot="title" class="dialog-header">
      <label>选择人员</label>
    </div>
    <div class="dialog-content">
      <el-select
        v-model="userCheck"
        :multiple="type === 'single' ? false : true"
        placeholder="点击下方选择"
        clearable
        @visible-change="userChange"
        ref="userSelect"
        @change="userCheckedChange"
      >
        <el-option
          v-for="user in allUserArray"
          :key="user.id"
          :label="user.title"
          :value="user.id"
        >
        </el-option>
      </el-select>
      <el-row>
        <el-col :span="16" class="br-tree">
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
              style="padding: 20px 0px 0 20px;"
            >
            </el-tree>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="user-group">
            <template v-if="type === 'single'">
              <el-radio-group v-model="userCheck">
                <el-radio
                  v-for="user in userArray"
                  :key="user.id"
                  :label="user.id"
                  >{{ user.title }}
                </el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-checkbox
                :indeterminate="isIndeterminate"
                v-model="checkAll"
                @change="userCheckAllChange"
                >全选</el-checkbox
              >
              <el-checkbox-group
                @change="userCheckedChange"
                v-model="userCheck"
              >
                <el-checkbox
                  v-for="user in userArray"
                  :key="user.id"
                  :label="user.id"
                >
                  {{ user.title }}
                </el-checkbox>
              </el-checkbox-group>
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
    userCheckAllChange(val) {
      for (let user of this.userArray) {
        let index = this.userCheck.indexOf(user.id);
        if (index === -1 && val) {
          this.userCheck.push(user.id);
        } else if (index != -1 && !val) {
          this.userCheck.splice(index, 1);
        }
      }
      this.isIndeterminate = false;
    },
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
      this.checkAll = false;
      this.isIndeterminate = false;
      let data = this.searchData || {};
      postJson(
        "/mftcc-flowable-server/br/listForTreeAll",
        data,
        true,
        res => {
          this.treeData = res.data;
          this.allUserArray = this.unique(this.getUsers(res.data));
          if (this.type === "single") {
            if (typeof this.initData == "string") {
              this.userCheck = this.initData;
            } else {
              this.userCheck = "";
            }
          } else if (this.initData instanceof Array) {
            this.userCheck = JSON.parse(JSON.stringify(this.initData));
          } else {
            this.userCheck = [];
          }
        },
        error => {
          this.userCheck = [];
        }
      );
    },
    submit() {
      let result = [];
      for (let user of this.allUserArray) {
        if (this.type === "single") {
          if (user.id === this.userCheck) {
            result.push(user);
          }
        } else {
          for (let userCheck of this.userCheck) {
            if (user.id === userCheck) {
              result.push(user);
            }
          }
        }
      }
      this.$emit("callback", result);
      this.visible = false;
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
  width: 100px;
  padding: 20px 0px 0 20px;
}
</style>
