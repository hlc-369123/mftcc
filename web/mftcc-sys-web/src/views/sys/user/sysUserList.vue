<template>
  <div class="pt mftcc-list">
    <mftcc-table
      tableId="sys/sysUserList"
      :parentVm="this"
      ref="sysUserList"
    ></mftcc-table>
  </div>
</template>

<script>
import api from "@/api/sys/sysUser";
import sysRoleApi from "@/api/sys/sysRole";
export default {
  name: "sysUserList",
  title: "用户列表",
  data() {
    return {
      formEvents: {},
    };
  },
  methods: {
    handleAdd() {
      this.$router.push({ path: "/sys/user/sysUserInsert" });
    },
    edit(index, row) {
      this.$router.push({
        path: "/sys/user/sysUserUpdate/",
        query: { userId: row.userId },
      });
    },
    delete(index, row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.deleteById(row.userId, (reponse) => {
          if (reponse.code === 0) {
            this.$message({
              type: reponse.msgType,
              message: reponse.msg,
            });
            this.$refs.sysUserList.refresh();
          } else {
            this.$alert(reponse.msg, this.$alertTitle, {
              type: reponse.msgType,
              dangerouslyUseHTMLString: true,
              callback: (action) => {},
            });
          }
        });
      });
    },
    start(index, row) {
      this.$confirm("是否确认启用?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.updateStart(row.userId, (reponse) => {
          if (reponse.code === 0) {
            this.$refs.sysUserList.refresh();
          } else {
            this.$message.error(reponse.msg);
          }
        });
      });
    },
    logout(index, row) {
      this.$confirm("是否确认注销?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.updateLogout(row.userId, (reponse) => {
          if (reponse.code === 0) {
            this.$refs.sysUserList.refresh();
          } else {
            this.$message.error(reponse.msg);
          }
        });
      });
    },
    resetPassword(index, row) {
      this.$confirm("是否确认重置密码?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.resetPassword({ opNo: row.opNo }, (reponse) => {
          if (reponse.code === 0) {
            this.$message.success("操作成功");
          } else {
            this.$alert(reponse.msg, this.$alertTitle, {
              type: reponse.msgType,
              dangerouslyUseHTMLString: true,
            });
          }
        });
      });
    },
    unlock(index, row) {
      this.$confirm("是否确认解除锁定?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        api.unlock({ opNo: row.opNo }, (reponse) => {
          if (reponse.code === 0) {
            this.$message.success("操作成功");
          } else {
            this.$alert(reponse.msg, this.$alertTitle, {
              type: reponse.msgType,
              dangerouslyUseHTMLString: true,
            });
          }
        });
      });
    },
    getRoles() {
      sysRoleApi.findAll(
        "",
        (reponse) => {
          if (reponse.code === 0) {
            var roleList = reponse.list;
            let dicItem = [];
            for (let i in roleList) {
              dicItem.push({
                optCode: roleList[i].roleNo,
                optName: roleList[i].roleName,
              });
            }
            this.$refs.sysUserSearchForm.attr("roleNo", "option", dicItem);
          } else {
            this.$alert(reponse.msg, "提示", {
              type: "error",
              callback: (action) => {
                this.$router.back(-1);
              },
            });
          }
        },
        (error) => {
          this.$alert("获取失败", "提示", {
            type: "error",
            callback: (action) => {
              this.$router.back(-1);
            },
          });
        }
      );
    },
  },
  mounted() {
    console.log("mounted", this.$refs.sysUserList.option);
    // this.getRoles();
  },
};
</script>
