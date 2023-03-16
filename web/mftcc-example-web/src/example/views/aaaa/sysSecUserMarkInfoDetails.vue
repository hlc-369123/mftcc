<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">详情页面</div></el-header>
        <el-container>
            <el-row>
                <mftcc-form formId="example/sysSecUserMarkInfoDetailsForm" :parentVm="this" ref="sysSecUserMarkInfoDetailsForm"></mftcc-form>
                <div class="mftcc-container-button">
                    <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
                </div>
            </el-row>
        </el-container>
    </div>
</template>
<script>
import api from '@/example/api/aaaa/sysSecUserMarkInfo'
export default {
    name: 'sysSecUserMarkInfoDetails',
    title: '用户记录信息详情',
    data() {
        return {
        }
    },
    methods: {
        findById(){
            let opNo = this.$route.query.opNo;
            api.findById(opNo,res => {
                if(res.code === 0){
                    var data = res.data;
                    this.$refs.sysSecUserMarkInfoDetailsForm.setFormValue(data);
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