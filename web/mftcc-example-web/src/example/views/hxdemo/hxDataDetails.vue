<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">详情页面</div></el-header>
        <el-container>
            <el-row>
                <mftcc-form formId="example/hxDataDetailsForm" :parentVm="this" ref="hxDataDetailsForm"></mftcc-form>
                <div class="mftcc-container-button">
                    <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
                </div>
            </el-row>
        </el-container>
    </div>
</template>
<script>
import api from '@/example/api/hxdemo/hxData'
export default {
    name: 'hxDataDetails',
    title: '详情',
    data() {
        return {
        }
    },
    methods: {
        findById(){
            let id = this.$route.query.id;
            api.findById(id,res => {
                if(res.code === 0){
                    var data = res.data;
                    this.$refs.hxDataDetailsForm.setFormValue(data);
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