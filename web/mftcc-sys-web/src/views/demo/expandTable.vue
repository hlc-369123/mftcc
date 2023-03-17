<template>
  <div class="pt mftcc-list">
    <mftcc-table
      tableId="sys/demoExpandTable"
      :parentVm="this"
      ref="demoExpandTable"
    ></mftcc-table>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    edit(index, row) {
      this.$router.push({ path: "/demo/CURD/update", query: { appId: row.appId } });
    },
  },
};
</script>
<style>
.el-table__expanded-cell td,
.el-table__expanded-cell tr,
.el-table__expanded-cell .el-table {
  background: transparent !important;
}
.el-table__expanded-cell {
  background: #f0f7fa;
  pointer-events: none;
}
</style>
