<template>
    <div class="pt mftcc-list">
         <!-- 统一菜单tab -->
        <div class="mftcc-config-web">
            <mftccListTabs entranceType="1" jumpEntr="2"></mftccListTabs>
        </div>
        <mftcc-table :tableId="tableId"  :initSearchData="tableInitSearchData"  :parentVm="this" ref="projectDataStaticsList" class="user-table"></mftcc-table>
    </div>
</template>
<script>
import api from '@/api/apply/sxjkEntrance'
export default {
   components: {},
  name: 'projectDataStaticsList',
  title: '项目数据列表',
  data() {
    return {
       tableId:'investment/busEquityProjectTable',
       tableInitSearchData:{
            
        },
        queryType:this.$route.query.param
    }
  },
  components: {
        mftccListTabs :config["/mftcctabs/mftccListTabs"]
    },
  methods: {
    
   linkToTableList(){
     let param = this.$route.query.param;
        if('all' == param ){
          this.tableId = 'investment/busCreditorEquityProjectTable';
        
        }else if('equity' == param){
          this.tableId = 'investment/busEquityProjectTable';
          this.tableInitSearchData ={rightsType:'1'}
        }else if('creditor' == param){
          this.tableId = 'investment/busCreditorProjectTable';
          this.tableInitSearchData ={rightsType:'2'}
        }else if('projectQin' == param ){
           this.tableId = 'investment/busCreditorEquityProjectTable';
           this.tableInitSearchData ={projectLabel:'1'}
        }
        
    },
    callBack(){
       this.$router.back(-1);
       this.$router
                .push({path: '/apply/sxjkEntrance', query: {queryType:this.queryType}});
    }

    
  },
   mounted() {
   this.linkToTableList()
  },
}
</script>