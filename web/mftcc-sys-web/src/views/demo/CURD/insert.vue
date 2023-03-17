<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">新增页面</div></el-header>
    <div class="mftcc-form-tips">
      <i class="el-icon-warning-outline"></i>说明：带*号的为必须项信息，请填写完整
    </div>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/demoLeaseAppProjectInitInsertForm"
          :parentVm="this"
          ref="demoLeaseAppProjectInitInsertForm"
        ></mftcc-form>
        <div class="mftcc-container-button">
          <el-button type="primary" v-if="$hasPerm('')" @click="submitForm()"
            >保 存</el-button
          >
          <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import api from "@/api/demo/demoLeaseAppProjectInit";
export default {
  name: "demoLeaseAppProjectInitInsert",
  title: "项目登记表新增",
  data() {
    return {};
  },
  methods: {
    submitForm() {
      this.$refs.demoLeaseAppProjectInitInsertForm.validateForm((valid) => {
        if (valid) {
          let data = this.$refs.demoLeaseAppProjectInitInsertForm.getFormValue();
          data.appNo = new Date().getTime();
          data.appSts = "0";
          data.regNo = "zhangyk";
          data.regName = "张云柯";
          data.orgNo = "FT18";
          data.orgName = "华东业务部";
          data.regData = "20210312";
          data.corpId = "1000000002";
          api.insert(data, (res) => {
            if (res.code === 0) {
              this.$message({
                type: res.msgType,
                dangerouslyUseHTMLString: true,
                message: res.msg,
              });
              this.back();
            } else {
              this.$alert(res.msg, this.$alertTitle, {
                type: res.msgType,
                dangerouslyUseHTMLString: true,
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    back() {
      this.$router.back(-1);
    },
  },
};
</script>
