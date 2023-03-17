<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">保存页面</div></el-header>
        <div class="mftcc-form-tips"><i class="el-icon-warning-outline"></i>说明：带*号的为必须项信息，请填写完整</div>
        <el-container>
        <el-row>
            <mftcc-form formId="sys/sysRoleUpdate" :parentVm="this" ref="sysRoleUpdateForm"></mftcc-form>
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
    name: 'sysRoleUpdate',
    title: '角色修改',
    data() {
        return {
        }
    },
    methods: {
        submitForm() {
            this.$refs.sysRoleUpdateForm.validateForm((valid) => {
                if (valid) {
                    let data = this.$refs.sysRoleUpdateForm.getFormValue();
                    api.update(data,reponse => {
                        if(reponse.code === 0){
                            this.$message({
                                type: reponse.msgType,
                                message: reponse.msg
                            })
                        }else{
                            this.$alert(reponse.msg, this.$alertTitle, {
                                type: reponse.msgType,
                                dangerouslyUseHTMLString: true,
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
        },
        findById(){
            let roleId = this.$route.query.roleId;
            api.findById(roleId,reponse => {
                if(reponse.code === 0){
                    var data = reponse.data;
                    this.$refs.sysRoleUpdateForm.setFormValue(data);
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

    },
    mounted() {
        this.findById();
    }
}
</script>
