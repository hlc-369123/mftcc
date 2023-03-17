<template>
  <div class="pt mftcc-list">
    <mftcc-table
      tableId="sys/demoQueryModelTable"
      :parentVm="this"
      ref="queryModelTable"
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
