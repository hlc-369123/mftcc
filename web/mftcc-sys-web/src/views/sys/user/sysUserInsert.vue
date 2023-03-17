<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">新增页面</div></el-header>
    <div class="mftcc-form-tips">
      <i class="el-icon-warning-outline"></i
      >说明：带*号的为必须项信息，请填写完整
    </div>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/sysUserInsert"
          :parentVm="this"
          ref="sysUserInsertForm"
        ></mftcc-form>
        <div class="mftcc-container-button">
          <el-button type="primary" @click="submitForm()">新增</el-button>
          <el-button type="primary" @click="submitFormEnable()"
            >新增并启用</el-button
          >
          <el-button @click="pageBack()">取消</el-button>
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
  name: "sysUserInsert",
  title: "用户新增",
  data() {
    return {};
  },
  methods: {
    // brNoClick(res) {
    //   let brNoVal = this.$refs.sysUserInsertForm.getFormValue("brNo");
    //   if (!brNoVal) {
    //     brNoVal = "";
    //   }
    //   sysDeptApi.getList(
    //     "",
    //     (reponse) => {
    //       if (reponse.code === 0) {
    //         let deptData = reponse.list;

    //         this.treeShow = true;
    //         this.treeOption = {
    //           search: true,
    //           insert: false,
    //           type: "single", // single,multiple
    //           data: deptData,
    //           defaultProps: {
    //             children: "children",
    //             label: "brName",
    //           },
    //         };
    //       } else {
    //         this.$alert(reponse.msg, "提示", {
    //           type: "error",
    //           callback: (action) => {
    //             this.$router.back(-1);
    //           },
    //         });
    //       }
    //     },
    //     (error) => {
    //       this.$alert("获取失败", "提示", {
    //         type: "error",
    //         callback: (action) => {
    //           this.$router.back(-1);
    //         },
    //       });
    //     }
    //   );
    // },
    // treeFunc(nodes) {
    //   this.$refs.sysUserInsertForm.setFormValue("brNo", nodes[0].brNo);
    // },
    // passwordChkBlur(res) {
    //   let passwordVal = this.$refs.sysUserInsertForm.getFormValue("password");
    //   if (!passwordVal) {
    //     this.$message({
    //       message: "请先录入密码",
    //       type: "info", // 主题 success/warning/info/error 默认info
    //     });
    //     this.$refs.sysUserInsertForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    //   let passwordChkVal = this.$refs.sysUserInsertForm.getFormValue("passwordChk");
    //   if (!passwordChkVal) {
    //     this.$refs.sysUserInsertForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    //   if (passwordVal != passwordChkVal) {
    //     this.$message({
    //       message: "确认密码与密码不相符，请重新录入",
    //       type: "info", // 主题 success/warning/info/error 默认info
    //     });
    //     this.$refs.sysUserInsertForm.setFormValue("passwordChk", "");
    //     return;
    //   }
    // },
    submitForm() {
      this.$refs.sysUserInsertForm.validateForm(valid => {
        if (valid) {
          let data = this.$refs.sysUserInsertForm.getFormValue();
          api.insert(data, reponse => {
            if (reponse.code === 0) {
              this.$alert("新增成功", "提示", {
                type: "info",
                callback: action => {
                  this.$router.back(-1);
                }
              });
            } else {
              this.$alert(reponse.msg, "提示", {
                type: "error",
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
    submitFormEnable() {
      this.$refs.sysUserInsertForm.validateForm(valid => {
        if (valid) {
          let data = this.$refs.sysUserInsertForm.getFormValue();
          api.insertEnable(data, reponse => {
            if (reponse.code === 0) {
              this.$message({
                type: reponse.msgType,
                message: reponse.msg
              });
              this.$router.back(-1);
            } else {
              this.$alert(reponse.msg, this.$alertTitle, {
                type: reponse.msgType,
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
    }
  }
};
</script>
<style>
.el-tag.el-tag--info.el-tag--small.el-tag--light {
  height: 25px;
}
</style>
