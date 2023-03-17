<template>
  <div class="pt mftcc-list">
    <mftcc-table tableId="sys/sysRoleList" :parentVm="this" ref="sysRoleList"></mftcc-table>
  </div>
</template>

<script>


import api from '@/api/sys/sysRole'
export default {
  name: 'sysRoleList',
  title: '角色列表',
  data() {
    return {
      formEvents:{

      }
    }
  },
  methods: {
    handleAdd(){
      this.$router
              .push({path: '/sys/role/sysRoleInsert'});
    },
    onSearch(){
      let formData = this.$refs.sysRoleSearchForm.getFormValue();
      this.$refs.sysRoleList.search(formData);
    },
    edit(index, row){
      this.$router.push({path: '/sys/role/sysRoleUpdate/', query: {roleId:row.roleId}});
    },
    delete(index, row){
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
      }).then(() => {
          api.deleteById(row.roleId,reponse => {
              if(reponse.code === 0){
                    this.$message({
                        type: reponse.msgType,
                        message: reponse.msg
                    })
                    this.$refs.sysRoleList.refresh();
                }else{
                    this.$alert(reponse.msg, this.$alertTitle, {
                        type: reponse.msgType,
                        dangerouslyUseHTMLString: true,
                        callback: action => {
                        }
                    });
                }
          });
      });
    },
    auth(index, row){
      this.$router.push({path: '/sys/auth/authConfig/', query: {roleId:row.roleId}});
    }
  }
}
</script>