<template>
  <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">详情页面</div></el-header>
        <el-container>
            <el-row>
                <mftcc-form formId="sys/sysDeptDetails" :parentVm="this" ref="sysDeptDetailsForm"></mftcc-form>
                <div class="mftcc-container-button">
                    <el-button @click="pageBack()">取消</el-button>
                </div>
            </el-row>
        </el-container>
    </div>
</template>
<script>
  
  import api from '@/api/sys/sysDept'
  export default {
    name: 'sysDeptDetails',
    title: '机构详情',
    data() {
      return {
      }
    },
    methods: {
      findById(){
        let brId = this.$route.query.brId;
        api.findById(brId,reponse => {
            if(reponse.code === 0){
                var data = reponse.data;
                this.$refs.sysDeptDetailsForm.setFormValue(data);
            }else{
                this.$alert(reponse.msg, this.$alertTitle, {
                type: reponse.msgType,
                callback: action => {
                    this.$router.back(-1);
                }
              });
            }
        },error => {
            this.$router.back(-1);
        });
      },
      pageBack(){
          this.$router.back(-1);
      }
    },
    mounted() {
      this.findById();
    }
  }
</script>
