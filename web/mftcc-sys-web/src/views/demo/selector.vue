<template>
  <div class="selector">
    <el-row>
      <el-divider content-position="left">多选选择器</el-divider>
      <el-button type="primary" @click="checkBox1">默认的多选</el-button>
      <el-button type="primary" @click="checkBox2">带分组的多选</el-button>
      <el-button type="primary" @click="checkBox3">带查询的多选</el-button>
      <el-button type="primary" @click="checkBox4">带新增的多选</el-button>
      <el-button type="primary" @click="checkBox5">数据结构不同的多选</el-button>
      <el-button type="primary" @click="checkBox6">带初值的多选</el-button>
      <el-divider content-position="left">单选选择器</el-divider>
      <el-button type="primary" @click="radioBox1">默认的单选</el-button>
      <el-button type="primary" @click="radioBox2">带分组的单选</el-button>
      <el-button type="primary" @click="radioBox3">带查询的单选</el-button>
      <el-button type="primary" @click="radioBox4">带新增的单选</el-button>
      <el-button type="primary" @click="radioBox5">数据结构不同的单选</el-button>
      <el-button type="primary" @click="radioBox6">带初值的单选</el-button>
      <el-divider content-position="left">树形选择器</el-divider>
      <el-button type="primary" @click="treeList1">单选的树形</el-button>
      <el-button type="primary" @click="treeList2">多选的树形</el-button>
      <el-button type="primary" @click="treeList3">带查询的树形</el-button>
      <el-button type="primary" @click="treeList5">数据结构不同的树形</el-button>
      <el-button type="primary" @click="treeList6">带初值的树形</el-button>
      <el-divider content-position="left">库中选择</el-divider>
      <el-form :model="libraryForm" label-width="80px">
        <el-form-item label="单选">
          <mftcc-library-select
            v-model="libraryForm.select"
            :url="'/' + $servers.sys + '/demo/sysArea/findByUpLev'"
            realField="areaNo"
            showField="areaName"
            :multiple="false"
            placeholder="请选择用户"
          >
          </mftcc-library-select>
        </el-form-item>
        <el-form-item label="多选">
          <mftcc-library-select
            v-model="libraryForm.multiple"
            :url="'/' + $servers.sys + '/demo/sysArea/findByUpLev'"
            realField="areaNo"
            showField="areaName"
            :multiple="true"
            placeholder="请选择用户"
          >
          </mftcc-library-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getValue">获取选择值</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <mftcc-dialog-checkbox
      @callback="checkboxFunc"
      :show.sync="checkboxShow"
      :option="checkboxOption"
    ></mftcc-dialog-checkbox>
    <mftcc-dialog-radio
      @callback="radioFunc"
      :show.sync="radioShow"
      :option="radioOption"
    ></mftcc-dialog-radio>
    <mftcc-dialog-tree
      @callback="treeFunc"
      :show.sync="treeShow"
      :option="treeOption"
    ></mftcc-dialog-tree>
  </div>
</template>
<script>
export default {
  name: "layerSelector",
  text: "选择器示例",
  data() {
    return {
      checkboxShow: false,
      checkboxOption: {},
      radioShow: false,
      radioOption: {},
      treeShow: false,
      treeOption: {},

      libraryForm: {
        select: "",
        multiple: [],
      },
    };
  },
  created() {},
  methods: {
    selectionChange(a) {
      console.log(a);
    },
    currentChange(a, b) {
      console.log(a, b);
    },
    getValue() {
      this.$message(JSON.stringify(this.libraryForm));
    },
    checkBox1() {
      this.checkboxShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.checkboxOption = {
        search: false,
        insert: false,
        group: false,
        width: "600px",
        height: "600px",
        data: demoData,
        value: [],
      };
    },
    checkBox2() {
      this.checkboxShow = true;

      let demoData = [];
      let groupData1 = {
        name: "分组一",
        data: [
          { value: "value1", text: "选项1" },
          { value: "value3", text: "选项3" },
          { value: "value5", text: "选项5" },
          { value: "value7", text: "选项7" },
          { value: "value9", text: "选项9" },
        ],
      };
      let groupData2 = {
        name: "分组二",
        data: [
          { value: "value2", text: "选项2" },
          { value: "value4", text: "选项4" },
          { value: "value6", text: "选项6" },
          { value: "value8", text: "选项8" },
          { value: "value10", text: "选项10" },
        ],
      };
      demoData.push(groupData1);
      demoData.push(groupData2);
      this.checkboxOption = {
        search: false,
        insert: false,
        group: true,
        data: demoData,
        value: [[], []],
      };
    },
    checkBox3() {
      this.checkboxShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.checkboxOption = {
        search: true,
        insert: false,
        group: false,
        data: demoData,
        value: [],
      };
    },
    checkBox4() {
      this.checkboxShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.checkboxOption = {
        search: false,
        insert: true,
        insertPath: "/" + $servers.sys + "/dialog/insertDialogCheckbox",
        group: false,
        data: demoData,
        value: [],
      };
    },
    checkBox5() {
      this.checkboxShow = true;

      let demoData = [];
      demoData.push({ val: "value1", name: "选项1" });
      demoData.push({ val: "value2", name: "选项2" });
      demoData.push({ val: "value3", name: "选项3" });
      demoData.push({ val: "value4", name: "选项4" });
      demoData.push({ val: "value5", name: "选项5" });
      demoData.push({ val: "value6", name: "选项6" });
      demoData.push({ val: "value7", name: "选项7" });
      demoData.push({ val: "value8", name: "选项8" });
      demoData.push({ val: "value9", name: "选项9" });
      demoData.push({ val: "value10", name: "选项10" });

      this.checkboxOption = {
        search: true,
        insert: false,
        group: false,
        realField: "val",
        showField: "name",
        data: demoData,
        value: [],
      };
    },
    checkBox6() {
      this.checkboxShow = true;

      let demoData = [];
      demoData.push({ val: "value1", name: "选项1" });
      demoData.push({ val: "value2", name: "选项2" });
      demoData.push({ val: "value3", name: "选项3" });
      demoData.push({ val: "value4", name: "选项4" });
      demoData.push({ val: "value5", name: "选项5" });
      demoData.push({ val: "value6", name: "选项6" });
      demoData.push({ val: "value7", name: "选项7" });
      demoData.push({ val: "value8", name: "选项8" });
      demoData.push({ val: "value9", name: "选项9" });
      demoData.push({ val: "value10", name: "选项10" });

      this.checkboxOption = {
        search: true,
        insert: false,
        group: false,
        realField: "val",
        showField: "name",
        data: demoData,
        value: ["value1", "value3", "value5"],
      };
    },
    checkBox7() {
      this.checkboxShow = true;

      this.checkboxOption = {
        search: true,
        insert: false,
        group: false,
        data: [],
        initPath: "/" + $servers.sys + "/dialog/initDialogData",
        value: [],
      };
    },
    checkboxFunc(value) {
      console.log(value);
    },
    radioBox1() {
      this.radioShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.radioOption = {
        search: false,
        insert: false,
        group: false,
        width: "600px",
        height: "600px",
        data: demoData,
        value: "",
      };
    },
    radioBox2() {
      this.radioShow = true;

      let demoData = [];
      let groupData1 = {
        name: "分组一",
        data: [
          { value: "value1", text: "选项1" },
          { value: "value3", text: "选项3" },
          { value: "value5", text: "选项5" },
          { value: "value7", text: "选项7" },
          { value: "value9", text: "选项9" },
        ],
      };
      let groupData2 = {
        name: "分组二",
        data: [
          { value: "value2", text: "选项2" },
          { value: "value4", text: "选项4" },
          { value: "value6", text: "选项6" },
          { value: "value8", text: "选项8" },
          { value: "value10", text: "选项10" },
        ],
      };
      demoData.push(groupData1);
      demoData.push(groupData2);
      this.radioOption = {
        search: false,
        insert: false,
        group: true,
        data: demoData,
        value: ["", ""],
      };
    },
    radioBox3() {
      this.radioShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.radioOption = {
        search: true,
        insert: false,
        group: false,
        data: demoData,
        value: "",
      };
    },
    radioBox4() {
      this.radioShow = true;

      let demoData = [];
      demoData.push({ value: "value1", text: "选项1" });
      demoData.push({ value: "value2", text: "选项2" });
      demoData.push({ value: "value3", text: "选项3" });
      demoData.push({ value: "value4", text: "选项4" });
      demoData.push({ value: "value5", text: "选项5" });
      demoData.push({ value: "value6", text: "选项6" });
      demoData.push({ value: "value7", text: "选项7" });
      demoData.push({ value: "value8", text: "选项8" });
      demoData.push({ value: "value9", text: "选项9" });
      demoData.push({ value: "value10", text: "选项10" });

      this.radioOption = {
        search: false,
        insert: true,
        insertPath: "/" + $servers.sys + "/dialog/insertDialogRadio",
        insertParamKey: "demoKey",
        group: false,
        data: demoData,
        value: "",
      };
    },
    radioBox5() {
      this.radioShow = true;

      let demoData = [];
      demoData.push({ val: "value1", name: "选项1" });
      demoData.push({ val: "value2", name: "选项2" });
      demoData.push({ val: "value3", name: "选项3" });
      demoData.push({ val: "value4", name: "选项4" });
      demoData.push({ val: "value5", name: "选项5" });
      demoData.push({ val: "value6", name: "选项6" });
      demoData.push({ val: "value7", name: "选项7" });
      demoData.push({ val: "value8", name: "选项8" });
      demoData.push({ val: "value9", name: "选项9" });
      demoData.push({ val: "value10", name: "选项10" });

      this.radioOption = {
        search: true,
        insert: false,
        group: false,
        realField: "val",
        showField: "name",
        data: demoData,
        value: "",
      };
    },
    radioBox6() {
      this.radioShow = true;

      let demoData = [];
      demoData.push({ val: "value1", name: "选项1" });
      demoData.push({ val: "value2", name: "选项2" });
      demoData.push({ val: "value3", name: "选项3" });
      demoData.push({ val: "value4", name: "选项4" });
      demoData.push({ val: "value5", name: "选项5" });
      demoData.push({ val: "value6", name: "选项6" });
      demoData.push({ val: "value7", name: "选项7" });
      demoData.push({ val: "value8", name: "选项8" });
      demoData.push({ val: "value9", name: "选项9" });
      demoData.push({ val: "value10", name: "选项10" });

      this.radioOption = {
        search: true,
        insert: false,
        group: false,
        realField: "val",
        showField: "name",
        data: demoData,
        value: "value2",
      };
    },
    radioBox7() {
      this.radioShow = true;

      this.radioOption = {
        search: true,
        insert: false,
        group: false,
        data: [],
        initPath: "/" + $servers.sys + "/dialog/initDialogData",
        value: "",
      };
    },
    radioFunc(value) {
      console.log(value);
    },
    treeList1() {
      this.treeShow = true;
      this.treeOption = {
        search: false,
        insert: false,
        width: "600px",
        height: "600px",
        type: "single", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
      };
    },
    treeList2() {
      this.treeShow = true;
      this.treeOption = {
        search: false,
        insert: false,
        type: "multiple", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
      };
    },
    treeList3() {
      this.treeShow = true;
      this.treeOption = {
        search: true,
        insert: false,
        type: "multiple", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
      };
    },
    treeList4() {
      this.treeShow = true;
      this.treeOption = {
        search: false,
        insert: true,
        insertPath: "/dialog/insertDialogTree",
        insertParamKey: "demoKey",
        type: "multiple", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
      };
    },
    treeList5() {
      this.treeShow = true;
      this.treeOption = {
        search: true,
        insert: true,
        type: "multiple", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
        defaultProps: {
          children: "child",
          label: "value",
        },
      };
    },
    treeList6() {
      this.treeShow = true;
      this.treeOption = {
        search: true,
        insert: true,
        type: "multiple", //single,multiple
        data: [
          {
            id: "1",
            label: "一级 1",
            children: [
              {
                id: "4",
                label: "二级 1-1",
                children: [
                  {
                    id: "9",
                    label: "三级 1-1-1",
                  },
                  {
                    id: "10",
                    label: "三级 1-1-2",
                  },
                ],
              },
            ],
          },
        ],
        value: ["10"],
      };
    },
    treeList7() {
      this.treeShow = true;
      this.treeOption = {
        search: true,
        insert: true,
        type: "multiple", //single,multiple
        data: [],
        initPath: "/" + $servers.sys + "/dialog/initDialogTreeData",
      };
    },
    treeFunc(nodes) {
      console.log(nodes);
    },
  },
};
</script>
<style>
.mftcc-checkbox-dialog .el-checkbox-group {
  padding: 20px;
}
.mftcc-checkbox-dialog .el-checkbox-group .el-checkbox {
  margin-bottom: 10px;
}
.mftcc-checkbox-dialog .dialog-header {
  align-items: center;
  width: 100%;
}
.mftcc-checkbox-dialog .dialog-header .el-input.el-input--suffix {
  margin: 0px 65px 0px 0px !important;
}
.mftcc-checkbox-dialog .dialog-header .el-button.el-popover__reference {
  margin: 0px 65px 0px 0px !important;
}
.mftcc-radio-dialog .el-radio-group {
  padding: 20px;
}
.mftcc-radio-dialog .el-radio-group .el-radio {
  margin-bottom: 10px;
}
.mftcc-radio-dialog .dialog-header {
  align-items: center;
  width: 100%;
}
.mftcc-radio-dialog .dialog-header .el-input.el-input--suffix {
  margin: 0px 65px 0px 0px !important;
}
.mftcc-radio-dialog .dialog-header .el-button.el-popover__reference {
  margin: 0px 65px 0px 0px !important;
}

.treeDialog .el-dialog__body {
  padding: 20px;
}
.treeDialog .dialog-header {
  align-items: center;
  width: 100%;
}
.treeDialog .dialog-header .el-input.el-input--suffix {
  margin: 0px 65px 0px 0px !important;
}
.treeDialog .dialog-header .el-button.el-popover__reference {
  margin: 0px 65px 0px 0px !important;
}
</style>
