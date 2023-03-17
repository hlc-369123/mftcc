<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">新增页面</div></el-header>
    <div class="mftcc-form-tips"><i class="el-icon-warning-outline"></i>说明：带*号的为必须项信息，请填写完整</div>
    <el-container>
      <el-row>
        <mftcc-form formId="sys/sysRoleInsert" :parentVm="this" ref="sysRoleInsertForm"></mftcc-form>
        <div class="mftcc-container-button">
          <el-button type="primary" @click="submitForm()">保存</el-button>
          <el-button @click="pageBack()">取消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
  
  import api from '@/api/sys/sysRole'
  export default {
    name: 'sysRoleInsert',
    title: '角色新增',
    data () {
      return {
      }
    },
    methods: {
      submitForm () {
        this.$refs.sysRoleInsertForm.validateForm((valid) => {
          if (valid) {
            let data = this.$refs.sysRoleInsertForm.getFormValue();
            api.insert(data, reponse => {
              if(reponse.code === 0){
                  this.$message({
                      type: reponse.msgType,
                      message: reponse.msg
                  })
                  this.$router.back(-1);
              }else{
                  this.$alert(reponse.msg, this.$alertTitle, {
                    type: reponse.msgType,
                    callback: action => {
                    }
                  });
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      pageBack(){
          this.$router.back(-1);
      }
  },
  }
</script>
