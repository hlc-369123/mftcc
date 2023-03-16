<template>
    <div class="pt mftcc-list">
        <mftcc-table tableId="example/sysSecUserMarkInfoTable" :parentVm="this" ref="sysSecUserMarkInfoList" class="user-table"></mftcc-table>
    </div>
</template>

<script>
import api from '@/example/api/aaaa/sysSecUserMarkInfo'
export default {
  name: 'sysSecUserMarkInfoList',
  title: '用户记录信息列表',
  data() {
    return {
    }
  },
  methods: {
    query(index, row){
      this.$router
                .push({path: '/aaaa/sysSecUserMarkInfoDetails', query: {opNo:row.opNo}});
    },
    insert(){
      this.$router
              .push({path: '/aaaa/sysSecUserMarkInfoInsert'});
    },
    onSearch(){
      let formData = this.$refs.sysSecUserMarkInfoSearchForm.getFormValue();
      this.$refs.sysSecUserMarkInfoList.search(formData);
    },
    edit(index, row){
        this.$router
                .push({path: '/aaaa/sysSecUserMarkInfoUpdate/', query: {opNo:row.opNo}});
    },
    delete(index, row){
        this.$confirm('此操作将永久删除该记录, 是否继续?', this.$alertTitle, {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            api.deleteById(row.opNo,res => {
                if(res.code === 0){
                    this.$message({
                        type: res.msgType,
                        dangerouslyUseHTMLString:true,
                        message: res.msg
                    })
                    this.$refs.sysSecUserMarkInfoList.refresh();
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true
                    });
                }
            });
        });
    }
  }
}
</script>