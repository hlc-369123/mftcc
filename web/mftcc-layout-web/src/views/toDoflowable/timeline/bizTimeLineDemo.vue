<template>
  <div style="width:700px">
    <biz-time-line
      :traceNo="traceNo"
      :bizMark="bizMark"
      @getTask="getTask"
    ></biz-time-line>
    <!-- <biz-time-line :traceNo="traceNo2" :bizMark="bizMark"></biz-time-line> -->
    <!-- <biz-time-line :option="option"></biz-time-line> -->
  </div>
</template>

<script>
export default {
  name: "bizTimeLineDemo",
  title: "bizTimeLineDemo",
  data() {
    return {
      traceNo: "1637653605697",
      // traceNo2: "1605851943835",
      bizMark: "leave",
      option: [
        {
          traceNo: "1610428856221", //业务号
          bizMark: "credit" //业务标识
        },
        {
          traceNo: "1610429373261", //业务号
          bizMark: "credit" //业务标识
        },
        {
          procDefKey: "loan_main_due_apply_flow", //流程标识
          variable: {
            //判断分支流转所需变量
            doDueApprovalFlag: "1"
          }
        }
      ]
    };
  },
  methods: {
    getTask(task) {
      debugger;
    }
  }
};
</script>
