<template>
    <div class="pt mftcc-list">
        <mftcc-table tableId="example/hxDataTable" :parentVm="this" ref="hxDataList" class="user-table"></mftcc-table>
    </div>
</template>

<script>
import api from '@/example/api/hxdemo/hxData'
export default {
  name: 'hxDataList',
  title: '列表',
  data() {
    return {
    }
  },
  methods: {
    query(index, row){
      this.$router
                .push({path: '/hxdemo/hxDataDetails', query: {id:row.id}});
    },
    insert(){
      this.$router
              .push({path: '/hxdemo/hxDataInsert'});
    },
    onSearch(){
      let formData = this.$refs.hxDataSearchForm.getFormValue();
      this.$refs.hxDataList.search(formData);
    },
    edit(index, row){
        this.$router
                .push({path: '/hxdemo/hxDataUpdate/', query: {id:row.id}});
    },

    subflow(index, row){
      this.$confirm('是否提交流程?', this.$alertTitle, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.startProcess({
          opNo:this.$store.getters.user.opNo,
          processMark:"221103",
          id:row.id
        },res => {
          if(res.code === 0){
            this.$message({
              type: res.msgType,
              dangerouslyUseHTMLString:true,
              message: res.msg
            })
            this.$refs.hxDataList.refresh();
          }else{
            this.$alert(res.msg, this.$alertTitle, {
              type: res.msgType,
              dangerouslyUseHTMLString: true
            });
          }
        });
      });
    },


    delete(index, row){
        this.$confirm('此操作将永久删除该记录, 是否继续?', this.$alertTitle, {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            api.deleteById(row.id,res => {
                if(res.code === 0){
                    this.$message({
                        type: res.msgType,
                        dangerouslyUseHTMLString:true,
                        message: res.msg
                    })
                    this.$refs.hxDataList.refresh();
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
