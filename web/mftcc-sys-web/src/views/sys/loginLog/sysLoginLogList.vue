<template>
  <div class="pt mftcc-list">
    <mftcc-table
      tableId="sys/sysLoginLogList"
      :parentVm="this"
      ref="sysLoginLogList"
    ></mftcc-table>
  </div>
</template>

<script>
import api from "@/api/sys/sysLoginLog";
export default {
  name: "sysLoginLogList",
  title: "系统登录日志列表",
  data() {
    return {};
  },
  methods: {
    handleAdd() {
      this.$router.push({ path: "/plt/sys/sysLoginLogInsert" });
    },
    onSearch() {
      let formData = this.$refs.sysLoginLogSearchForm.getFormValue();
      this.$refs.sysLoginLogList.search(formData);
    },
    edit(index, row) {
      this.$router.push({
        path: "/plt/sys/sysLoginLogUpdate/",
        query: { token: row.token }
      });
    },
    delete(index, row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", this.$alertTitle, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        api.deleteById(row.token, res => {
          if (res.code === 0) {
            this.$message({
              type: res.msgType,
              dangerouslyUseHTMLString: true,
              message: res.msg
            });
            this.$refs.sysLoginLogList.refresh();
          } else {
            this.$alert(res.msg, this.$alertTitle, {
              type: res.msgType,
              dangerouslyUseHTMLString: true
            });
          }
        });
      });
    }
  }
};
</script>
<style>
.mftcc-list .search-button {
  text-align: right;
}
</style>
