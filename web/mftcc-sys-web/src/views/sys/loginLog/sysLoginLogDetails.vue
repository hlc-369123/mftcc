<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">详情页面</div></el-header>
        <el-container>
            <el-row>
                <mftcc-form formId="sys/sysLoginLogDetailsForm" :parentVm="this" ref="sysLoginLogDetailsForm"></mftcc-form>
                <div class="mftcc-container-button">
                    <el-button v-if="$hasPerm('')" @click="back">返 回</el-button>
                </div>
            </el-row>
        </el-container>
    </div>
</template>
<script>
import api from '@/api/sys/sysLoginLog'
export default {
    name: 'sysLoginLogDetails',
    title: '系统登录日志详情',
    data() {
        return {
        }
    },
    methods: {
        findById(){
            let token = this.$route.query.token;
            api.findById(token,res => {
                if(res.code === 0){
                    var data = res.data;
                    this.$refs.sysLoginLogDetailsForm.setFormValue(data);
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
        },
        back(){
            this.$router.back(-1);
        }
    },
    mounted() {
        this.findById();
    }
}
</script>