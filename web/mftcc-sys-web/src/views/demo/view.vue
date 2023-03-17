<template>
  <div class="plt" id="testView">
    <mftcc-layout
      layoutId="sys/view001"
      :parentVm="this"
      ref="layout"
      :paramData="paramData"
    >
    </mftcc-layout>
    <mftcc-dialog-form
      :show.sync="editDialogShow"
      :parentVm="parentVm"
      :option="editOption"
      ref="editForm"
      @callback="editSubmit"
    ></mftcc-dialog-form>
  </div>
</template>
<script>
import api from "@/api/demo/demoCusInfo";
export default {
  data() {
    return {
      editDialogShow: false,
      editOption: {},
      cusFormData: {},
      cusNo: "10001",
      cusForm: {},
      paramData: {
        test1: "111",
        test2: "222",
        test3: "333",
      },
    };
  },
  mounted() {
    // setTimeout(() => {
    //   this.html2pdf("testView", "PDF的名字");
    // }, 5000);
  },
  methods: {
    demoCusInfoCallBack(form) {
      this.cusForm = form;
      this.findById();
    },
    findById() {
      api.demoCusInfoFindById(this.cusNo, (reponse) => {
        if (reponse.code === 0) {
          this.cusFormData = reponse.data;
          this.cusForm.setFormValue(this.cusFormData);
        } else {
          this.$alert(reponse.msg, this.$alertTitle, {
            type: reponse.msgType,
          });
        }
      });
    },
    editFormM(form) {
      this.editOption = {
        title: "修改客户信息",
        confirmButtonText: "提交修改",
        formId: "sys/demoCusInfoUpdate",
        width: "1200px",
      };
      this.editDialogShow = true;
      this.$refs.editForm.getFormRef((editForm) => {
        editForm.setFormValue(this.cusFormData);
      });
    },
    editSubmit(formData) {
      api.demoCusInfoUpdate(formData, (reponse) => {
        if (reponse.code === 0) {
          this.$message({
            type: reponse.msgType,
            message: reponse.msg,
          });
          this.editDialogShow = false;
          this.findById();
        } else {
          this.$alert(reponse.msg, this.$alertTitle, {
            type: reponse.msgType,
          });
        }
      });
    },
    qiehuan() {
      this.$bus.$emit("changeDemoHeadRightData");
    },
  },
};
</script>
