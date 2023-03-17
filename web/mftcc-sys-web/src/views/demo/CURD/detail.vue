<template>
  <div class="mftcc-container">
    <el-header><div class="mftcc-form-header">详情页面</div></el-header>
    <el-container>
      <el-row>
        <mftcc-form
          formId="sys/demoLeaseAppProjectInitDetailsForm"
          :parentVm="this"
          ref="demoLeaseAppProjectInitDetailsForm"
        ></mftcc-form>
        <div class="mftcc-container-button">
          <el-button v-if="$hasPerm('')" @click="back">取 消</el-button>
        </div>
      </el-row>
    </el-container>
  </div>
</template>
<script>
import api from "@/api/demo/demoLeaseAppProjectInit";
export default {
  name: "demoLeaseAppProjectInitDetails",
  title: "项目登记表详情",
  data() {
    return {};
  },
  methods: {
    findById() {
      let appId = this.$route.query.appId;
      api.findById(
        appId,
        (res) => {
          if (res.code === 0) {
            var data = res.data;
            this.$refs.demoLeaseAppProjectInitDetailsForm.setFormValue(data);
          } else {
            this.$alert(res.msg, this.$alertTitle, {
              type: res.msgType,
              dangerouslyUseHTMLString: true,
              callback: (action) => {
                this.$router.back(-1);
              },
            });
          }
        },
        (error) => {
          this.$router.back(-1);
        }
      );
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
