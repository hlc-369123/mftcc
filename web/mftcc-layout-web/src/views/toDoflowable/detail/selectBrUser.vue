<template>
  <el-row :gutter="20" style="height: 350px; overflow: auto">
    <el-col :span="14">
      <select-br @current="setBr"></select-br>
    </el-col>
    <el-col
      :span="10"
      element-loading-text="加载中"
      style="height: 100%; overflow: auto"
    >
      <div class="select-list">
        <div
          v-for="user in userlist"
          :key="user.opNo"
          :class="{ selected: user.opNo == selectedOpNo }"
          @click="setUser(user)"
        >
          {{ user.opName }}
        </div>
      </div>
    </el-col>
  </el-row>
</template>
<script>
import selectBr from "./selectBr";
import api from "@/api/flowable/flowable";
export default {
  data() {
    return {
      brData: [],
      brText: null,
      expanded: [],
      userlist: [],
      selectedOpNo: null
    };
  },
  components: {
    selectBr
  },
  methods: {
    initData(id) {
      let data = {
        id: id
      };
      api.getUserData(data, res => {
        if (res.code == 0) {
          this.userlist = res.list;
        } else {
          this.$alert(res.msg, "错误", {
            type: "error",
            dangerouslyUseHTMLString: true
          });
        }
      });
    },
    setBr(data) {
      this.brText = data.label;
      this.initData(data.id);
    },
    setUser(data) {
      this.selectedOpNo = data.opNo;
      let currentData = this.clone(data);
      if (this.brText) {
        currentData.opName = this.brText + "/" + currentData.opName;
      }
      this.$emit("current", currentData);
    },
    clone(data) {
      return JSON.parse(JSON.stringify(data));
    }
  }
};
</script>
<style scoped>
.select-list > div {
  height: 35px;
  line-height: 35px;
  border-bottom: 1px solid #dcdfe6;
  padding: 0 8px;
}
.select-list > div:hover,
.select-list > div.selected {
  background: #f0f7ff;
}
</style>
