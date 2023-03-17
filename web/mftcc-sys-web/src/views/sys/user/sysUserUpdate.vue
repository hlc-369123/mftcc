<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">保存页面</div></el-header>
    <div class="mftcc-form-tips">
      <i class="el-icon-warning-outline"></i
      >说明：带*号的为必须项信息，请填写完整
    </div>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/sysUserUpdate"
          :parentVm="this"
          ref="sysUserUpdateForm"
        ></mftcc-form>
        <div class="mftcc-container-button">
          <el-button type="primary" @click="submitForm()">保存</el-button>
          <el-button type="primary" @click="pageBack()">取消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import api from "@/api/sys/sysUser";
// import sysRoleApi from "@/api/sys/sysRole";
// import sysDeptApi from "@/api/sys/sysDept";
export default {
  name: "sysUserUpdate",
  title: "用户修改",
  data() {
    return {};
  },
  methods: {
    // passwordChkBlur(res) {
    //   let passwordVal = this.$refs.sysUserUpdateForm.getFormValue("password");
    //   if (!passwordVal) {
    //     this.$message({
    //       message: "请先录入密码",
    //       type: "info" // 主题 success/warning/info/error 默认info
    //     });
    //     this.$refs.sysUserUpdateForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    //   let passwordChkVal = this.$refs.sysUserUpdateForm.getFormValue(
    //     "passwordChk"
    //   );
    //   if (!passwordChkVal) {
    //     this.$refs.sysUserUpdateForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    //   if (passwordVal != passwordChkVal) {
    //     this.$message({
    //       message: "确认密码与密码不相符，请重新录入",
    //       type: "info" // 主题 success/warning/info/error 默认info
    //     });
    //     this.$refs.sysUserUpdateForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    // },
    submitForm() {
      this.$refs.sysUserUpdateForm.validateForm(valid => {
        if (valid) {
          let data = this.$refs.sysUserUpdateForm.getFormValue();
          api.update(data, reponse => {
            if (reponse.code === 0) {
              this.$message({
                type: reponse.msgType,
                message: reponse.msg
              });
            } else {
              this.$alert(reponse.msg, this.$alertTitle, {
                type: reponse.msgType,
                dangerouslyUseHTMLString: true,
                callback: action => {}
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    pageBack() {
      this.$router.back(-1);
    },
    findById() {
      let userId = this.$route.query.userId;
      api.findById(
        userId,
        reponse => {
          if (reponse.code === 0) {
            var data = reponse.data;
            this.$refs.sysUserUpdateForm.setFormValue(data);
          } else {
            this.$alert(reponse.msg, this.$alertTitle, {
              type: reponse.msgType,
              callback: action => {
                this.$router.back(-1);
              }
            });
          }
        },
        error => {
          this.$router.back(-1);
        }
      );
    }
  },
  mounted() {
    this.findById();
  }
};
</script>
<style>
.el-tag.el-tag--info.el-tag--small.el-tag--light {
  height: 25px;
}
</style>
