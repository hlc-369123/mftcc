<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">详情页面</div></el-header>
    <el-container>
      <el-row>
        <!-- <mftcc-form formId="market/marketGridDetailsForm" :parentVm="this" ref="marketGridDetailsForm"></mftcc-form> -->
        <mftcc-layout
          v-if="layoutShow"
          layoutId="demo/marketGridDetailsLayout"
          :parentVm="this"
          :ref="marketGridDetailsLayout"
          :paramDat="paramData"
        ></mftcc-layout>
        <div class="mftcc-container-button">
          <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
export default {
  name: "marketGridDetails",
  title: "网格表详情",
  data() {
    return {
      paramData: {},
      layoutShow: false,
    };
  },
  methods: {
    ceshi() {
      let id = this.$route.query.id;
      this.paramData.id = id;
      this.layoutShow = true;
      console.log("paramData.id==", this.paramData.id);
    },

    // findById(){
    //     let id = this.$route.query.id;
    //     api.findById(id,res => {
    //         if(res.code === 0){
    //             var data = res.data;
    //             this.paramData.id = id;
    //             this.layoutShow = true;
    //             // this.$refs.marketGridDetailsLayout.setFormValue(data);
    //         }else{
    //             this.$alert(res.msg, this.$alertTitle, {
    //                 type: res.msgType,
    //                 dangerouslyUseHTMLString: true,
    //                 callback: action => {
    //                     this.$router.back(-1);
    //                 }
    //             });
    //         }
    //     },error => {
    //         this.$router.back(-1);
    //     });
    // },
    back() {
      this.$router.back(-1);
    },
  },
  mounted() {
    this.ceshi();
  },
};
</script>
