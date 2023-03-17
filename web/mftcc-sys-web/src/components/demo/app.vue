<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">详情页面</div></el-header>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/demoLeaseAppProjectInitDetailsForm"
          :parentVm="this"
          ref="demoLeaseAppProjectInitDetailsForm"
        ></mftcc-form>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import api from "@/api/demo/demoLeaseAppProjectInit";
export default {
  name: "demoLeaseAppProjectInitDetails",
  title: "项目登记表详情",
  data() {
    return {};
  },
  methods: {
    findById() {
      let appId = "7fd547eb4465bf001509a85d572f37c3";
      api.findById(
        appId,
        (res) => {
          if (res.code === 0) {
            var data = res.data;
            this.$refs.demoLeaseAppProjectInitDetailsForm.setFormValue(data);
          } else {
            this.$alert(res.msg, this.$alertTitle, {
              type: res.msgType,
              dangerouslyUseHTMLString: true,
              callback: (action) => {
                this.$router.back(-1);
              },
            });
          }
        },
        (error) => {
          this.$router.back(-1);
        }
      );
    },
    getVariables(formData, callback) {
      let variables = {
        lawApproveFlag: "fca8ed32f8754f4366e0fad58cbffe8d",
      };

      return callback(variables);
    },
    doCommit(params, callback) {
      api.doCommitDemo(
        {
          //业务参数
          params, //必传
          variables: {
            // "UserTask_1i6mnio":"guohanchen,liupei",
            amt: "200",
            bbbb: "22222",
            lawApproveFlag: "fca8ed32f8754f4366e0fad58cbffe8d",
          },
          formData: "",
          opNo: this.$store.getters.user.opNo,
        },
        (res) => {
          if (res.code == 0) {
          } else {
            this.$alert(res.msg, "提示", {
              type: "error",
              dangerouslyUseHTMLString: true,
            });
          }
          callback(res);
        }
      );
    },
  },
  mounted() {
    this.findById();
  },
};
</script>
d
