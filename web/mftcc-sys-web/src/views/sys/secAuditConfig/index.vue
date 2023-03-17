<template>
    <div class="sec-audit-config">
        <el-row :gutter="20" class="sec-full-screen">
            <el-col :span="12" class="sec-full-screen">
                <div class="sec-border">
                    <div class="sec-title">密码检验规则</div>
                    <div class="sec-list">
                        <mftcc-edit-table 
                            tableId="sys/sysSecAuditConfigTable" 
                            :initSearchData="{codeType:'PR'}" 
                            :parentVm="this" 
                            ref="sysSecPasswordRulesList"
                            @callback="initCallback('sysSecPasswordRulesList')"></mftcc-edit-table>
                    </div>
                </div>
            </el-col>
            <el-col :span="12" class="sec-full-screen">
                <el-row class="sec-options">
                    <div class="sec-border">
                        <div class="sec-title">安全选项设置</div>
                        <div class="sec-list">
                            <mftcc-edit-table 
                                tableId="sys/sysSecAuditConfigTable" 
                                :initSearchData="{codeType:'SL'}" 
                                :parentVm="this" 
                                ref="sysSecOptionsList"
                                @callback="initCallback('sysSecOptionsList')"></mftcc-edit-table>
                        </div>
                    </div>
                </el-row>
                <el-row class="sec-password-error">
                    <div class="sec-border">
                        <div class="sec-title">密码错误设置</div>
                        <div class="sec-list">
                            <mftcc-edit-table 
                                tableId="sys/sysSecAuditConfigTable" 
                                :initSearchData="{codeType:'SF'}" 
                                :parentVm="this" 
                                ref="sysSecPasswordErrorList"
                                @callback="initCallback('sysSecPasswordErrorList')"></mftcc-edit-table>
                        </div>
                    </div>
                </el-row>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import api from '@/api/sys/sysSecAuditConfig'
export default {
    name: 'sysSecAuditConfig',
    title: '安全审计',
    data() {
        return {
            
        }
    },
    methods: {
        initCallback(ref){
            let tableData = this.$refs[ref].option.tableData;
            for(let i = 0;i < tableData.length;i++){
                if(tableData[i].isEdit === "1"){
                    this.$refs[ref].editTable(i,true);
                }
            }
        },
        edit(index,row){
            let ref = "";
            if(row.codeType === 'PR'){
                ref = 'sysSecPasswordRulesList'
            }else if(row.codeType === 'SL'){
                ref = 'sysSecOptionsList'
            }else if(row.codeType === 'SF'){
                ref = 'sysSecPasswordErrorList'
            }else{
                this.$message({
                    type: "error",
                    dangerouslyUseHTMLString:true,
                    message: "不支持的格则类型"
                })
                return false;
            }
            let data = {}
            data.itemNo = row.itemNo
            data.itemValues = this.$refs[ref].getFormValue(index,"itemValues")
            api.update(data,res => {
                if(res.code === 0){
                    this.$message({
                        type: res.msgType,
                        dangerouslyUseHTMLString:true,
                        message: res.msg
                    })
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true
                    });
                }
            },)
        },
        userChange(el,index,row){
            let data = {}
            data.itemNo = row.itemNo
            data.isUse = el
            api.update(data,res => {
                if(res.code === 0){
                    this.$message({
                        type: res.msgType,
                        dangerouslyUseHTMLString:true,
                        message: res.msg
                    })
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true,
                        callback: action => {
                            if(el == '1'){
                                row.isUse = '0'
                            }else{
                                row.isUse = '1'
                            }
                        }
                    });
                }
            },error => {
                if(el == '1'){
                    row.isUse = '0'
                }else{
                    row.isUse = '1'
                }
            })
        }
    }
}
</script>
<style>
.sec-audit-config{
    height: 100%;
    padding: 20px;
}
.sec-audit-config .sec-full-screen{
    height: 100%;
}
.sec-audit-config .sec-options{
    height: calc(50% - 5px);
}
.sec-audit-config .sec-password-error{
    height: calc(50% - 5px);
    margin-top: 10px;
}
.sec-audit-config .sec-border{
    border: solid 1px #ddd; 
    border-radius: 4px;
    height: 100%;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}
.sec-audit-config .sec-title{
    font-size: 14px;
    padding: 10px;
    color: #606266;
}
.sec-audit-config .sec-list{
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}

.sec-audit-config  .el-form-item--small.el-form-item {
    margin: 0px !important;
}
</style>