<template>
  <div style="width:700px">
    <time-line
      :timeLineShow="timeLineShow"
      :traceNo="traceNo"
      :bizMark="bizMark"
    ></time-line>
  </div>
</template>

<script>
export default {
  name: "bizTimeLineDemo",
  title: "bizTimeLineDemo",
  data() {
    return {
      timeLineShow: true,
      traceNo: "66666666666",
      procId: "",
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
  }
};
</script>
