<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">保存页面</div></el-header>
        <div class="mftcc-form-tips"><i class="el-icon-warning-outline"></i>说明：带*号的为必须项信息，请填写完整</div>
        <el-container>
            <el-row>
                <mftcc-form formId="example/hxDataUpdateForm" :parentVm="this" ref="hxDataUpdateForm"></mftcc-form>
                <div class="mftcc-container-button">
                    <el-button type="primary" v-if="$hasPerm('')" @click="submitForm()">保 存</el-button>
                    <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
                </div>
            </el-row>
        </el-container>
    </div>
</template>
<script>
import api from '@/example/api/hxdemo/hxData'
export default {
    name: 'hxDataUpdate',
    title: '修改',
    data() {
        return {
        }
    },
    methods: {
        submitForm() {
            this.$refs.hxDataUpdateForm.validateForm((valid) => {
                if (valid) {
                    let data = this.$refs.hxDataUpdateForm.getFormValue();
                    api.update(data,res => {
                        if(res.code === 0){
                            this.$message({
                                type: res.msgType,
                                dangerouslyUseHTMLString:true,
                                message: res.msg
                            })
                          this.$router.back(-1);
                        }else{
                            this.$alert(res.msg, this.$alertTitle, {
                                type: res.msgType,
                                dangerouslyUseHTMLString: true
                            });
                        }
                    })
                } else {
                    return false;
                }
            });
        },
        back(){
            this.$router.back(-1);
        },
        findById(){
            let id = this.$route.query.id;
            api.findById(id,res => {
                if(res.code === 0){
                    var data = res.data;
                    this.$refs.hxDataUpdateForm.setFormValue(data);
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true,
                        callback: action => {
                            this.$router.back(-1);
                        }
                    });
                }
            },error => {
                this.$router.back(-1);
            });
        }
    },
    mounted() {
        this.findById();
    }
}
</script>
