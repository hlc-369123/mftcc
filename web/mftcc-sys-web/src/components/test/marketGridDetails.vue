<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">详情页面</div></el-header>
    <el-container>
      <el-row>
        <mftcc-form
          formId="demo/marketGridDetailsForm"
          :parentVm="this"
          ref="marketGridDetailsForm"
        ></mftcc-form>
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
    return {};
  },
  props: ["id"],
  methods: {
    findById() {
      // let id = this.$route.query.id;
      console.log("详情页面id=" + this.id);
    },
    back() {
      this.$router.back(-1);
    },
  },
  mounted() {
    this.findById();
  },
};
</script>
