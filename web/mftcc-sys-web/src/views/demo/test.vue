<template>
    <div class="mftcc-container">
        <el-header><div class="mftcc-form-header">复杂表单页面</div></el-header>
        <el-container>
            <el-row>
                <mftcc-form formId="sys/demotest" :parentVm="this" ref="demoInsertForm"></mftcc-form>
            </el-row>
        </el-container>
    </div>
</template>
<script>
export default {
    data() {
        return {
        }
    },
    mounted(){
        this.$refs.demoInsertForm.setFormValue("finType","3");
    }
}
</script>