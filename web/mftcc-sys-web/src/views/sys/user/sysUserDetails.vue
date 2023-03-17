<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">详情页面</div></el-header>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/sysUserDetails"
          :parentVm="this"
          ref="sysUserDetailsForm"
        ></mftcc-form>
        <div class="mftcc-container-button">
          <el-button @click="pageBack()">取消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import api from "@/api/sys/sysUser";
export default {
  name: "sysUserDetails",
  title: "用户详情",
  data() {
    return {};
  },
  methods: {
    findById() {
      let userId = this.$route.query.userId;
      api.findById(
        userId,
        reponse => {
          if (reponse.code === 0) {
            var data = reponse.data;
            this.$refs.sysUserDetailsForm.setFormValue(data);
            // var roleList = data.sysRoleList;
            // let dicItem = []
            // for (let i in roleList) {
            //     dicItem.push({'optCode': roleList[i].roleNo, 'optName': roleList[i].roleName})
            // }
            // this.$refs.sysUserDetailsForm.attr('roleNo', 'option', dicItem)
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
    },
    pageBack() {
      this.$router.back(-1);
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
