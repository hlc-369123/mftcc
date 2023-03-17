<template>
  <div>
    <el-dialog
      :visible.sync="visible"
      v-if="visible"
      title="查询结果"
      class="mftcc-table-dialog"
      :center="true"
      top="25vh"
      :close-on-click-modal="false"
      width="1000px"
    >
      <div class="dialog-content" style="height: 450px">
        <mftcc-table :initSearchData="initSearchData" tableId="cus/searchCusTable" :parentVm="this" class="user-table"></mftcc-table>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'searchDialog',
  props:["inputValue"],
  data () {
    return {
      visible : false,
      initSearchData:{"cusName":this.inputValue}
    };
  },
  methods: {
    query(index, row){
      let parm = {};
      parm.cusBaseType = row.cusBaseType;
      parm.cusType = row.cusType;
      parm.id = row.id;
      this.$router.pushMain({path: "/mftcc-cus-web/detail/cusDetail", query: parm});
      this.visible = false;
    },
  },
  mounted(){
      this.visible = true;
  }
};
</script>
