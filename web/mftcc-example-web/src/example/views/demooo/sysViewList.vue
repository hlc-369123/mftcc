<template>
    <div class="pt mftcc-list">
        <mftcc-table tableId="example/sysViewTable" :parentVm="this" ref="sysViewList" class="user-table"></mftcc-table>
    </div>
</template>

<script>
import api from '@/example/api/demooo/sysView'
export default {
  name: 'sysViewList',
  title: '综合视图定义列表',
  data() {
    return {
    }
  },
  methods: {
    query(index, row){
      this.$router
                .push({path: '/demooo/sysViewDetails', query: {viewId:row.viewId}});
    },
    insert(){
      this.$router
              .push({path: '/demooo/sysViewInsert'});
    },
    onSearch(){
      let formData = this.$refs.sysViewSearchForm.getFormValue();
      this.$refs.sysViewList.search(formData);
    },
    edit(index, row){
        this.$router
                .push({path: '/demooo/sysViewUpdate/', query: {viewId:row.viewId}});
    },
    delete(index, row){
        this.$confirm('此操作将永久删除该记录, 是否继续?', this.$alertTitle, {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            api.deleteById(row.viewId,res => {
                if(res.code === 0){
                    this.$message({
                        type: res.msgType,
                        dangerouslyUseHTMLString:true,
                        message: res.msg
                    })
                    this.$refs.sysViewList.refresh();
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