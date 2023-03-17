<template>
  <div class="pt mftcc-list">
    <mftcc-table tableId="sys/demoLeaseAppProjectInitTable"></mftcc-table>
  </div>
</template>
<script>
import api from "@/api/demo/demoLeaseAppProjectInit";
export default {
  data() {
    return {};
  },
  created() {},
  methods: {
    initCallBack(table) {
      let arr = ["API202103HZ002209", "API202103HZ002205"];
      table.setSelectValue(arr);
    },
    handleAdd() {
      this.$router.push({ path: "/demo/CURD/insert" });
    },
    edit(index, row) {
      this.$router.push({ path: "/demo/CURD/update", query: { appId: row.appId } });
    },
    submit(index, row) {
      console.log(row);
      let data = {
        processDefinitionKey: "leaseApp",
        bizMark: "credit",
        traceNo: new Date().getTime() + "",
        // traceNo:"1614937228996",
        variables: JSON.stringify({
          bizMark: "pawn",
          traceNo: "c" + new Date().getTime(),
        }),
        opNo: "0000",
        approvalContents: "发起业务流程",
      };
      api.startProcessWithAppContents(data, (res) => {
        if (res.code === 0) {
          this.$message({
            type: res.msgType,
            dangerouslyUseHTMLString: true,
            message: "流程已启动",
          });
          this.$refs.demoLeaseAppProjectInitList.refresh();
        } else {
          this.$alert(res.msg, this.$alertTitle, {
            type: res.msgType,
            dangerouslyUseHTMLString: true,
          });
        }
      });
    },
    delete(index, row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", this.$alertTitle, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.deleteById(row.appId, (res) => {
          if (res.code === 0) {
            this.$message({
              type: res.msgType,
              dangerouslyUseHTMLString: true,
              message: res.msg,
            });
            this.$refs.demoLeaseAppProjectInitList.refresh();
          } else {
            this.$alert(res.msg, this.$alertTitle, {
              type: res.msgType,
              dangerouslyUseHTMLString: true,
            });
          }
        });
      });
    },
  },
};
</script>
