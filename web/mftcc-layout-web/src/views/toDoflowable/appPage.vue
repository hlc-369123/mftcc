<template>
  <app-detail-page :appData="appPageData" :query="query"></app-detail-page>
</template>

<script>
export default {
  data() {
    return {
      appPageData: {},
      query: false
    };
  },
  props: ["appData"],
  created() {
    let $query = this.$route.query;
    if (this.appData) {
      this.appPageData = {
        TRACE_NO: this.appData.traceNo,
        TASK_DEF_ID: this.appData.taskDefId,
        PROC_INST_ID: this.appData.procId,
        BIZ_ID: this.appData.bizMark,
        TASK_ID: this.appData.taskId,
        finishType: this.appData.finishType
      };
    } else {
      this.appPageData = {
        TRACE_NO: $query.traceNo,
        TASK_DEF_ID: $query.taskDefId,
        PROC_INST_ID: $query.procId,
        BIZ_ID: $query.bizMark,
        TASK_ID: $query.taskId,
        finishType: $query.finishType
      };
    }
    if (this.appPageData.finishType == "0") {
      this.query = false;
    } else {
      this.query = true;
    }
  }
};
</script>
