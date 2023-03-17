<template>
  <appPc v-if="$model == 'pc' && show" :appData="appData"></appPc>
  <appMobile
    v-else-if="$model == 'mobile' && show"
    :appData="appData"
  ></appMobile>
</template>

<script>
import appPc from "../toDoflowable/appPage.vue";
import appMobile from "../toDoflowable/mobile/appMobilePage.vue";
import api from "@/api/flowable/flowable";
import apiForLogin from "@/api/login/login";
export default {
  data() {
    return {
      appData: {},
      show: false
    };
  },
  components: {
    appPc,
    appMobile
  },
  methods: {
    foreignLogin() {
      apiForLogin.foreignLogin(null, res => {
        if (res.code === 0) {
          let data = res.data;
          this.$store.commit("changeLogin", {
            user: data.sysUserInfo,
            buttonAuth: data.buttonAuth,
            token: data.token,
            refreshToken: data.refreshToken,
            buttons: data.buttons
            // routes: routes
          });
          this.create();
        } else {
          this.$alert(res.msg, this.$alertTitle, {
            type: res.msgType,
            dangerouslyUseHTMLString: true,
            callback: action => {
              this.$router.back(-1);
            }
          });
        }
      });
    },
    create() {
      let $query = this.$route.query;
      this.appData = {
        traceNo: $query.traceNo,
        taskDefId: $query.taskDefId,
        procId: $query.procId,
        bizMark: $query.bizMark,
        taskId: $query.taskId,
        finishType: $query.finishType
      };
      api.findTaskById({ taskId: this.appData.taskId }, res => {
        if (res.code == 0) {
          this.appData.finishType = "0";
        } else {
          this.appData.finishType = "1";
        }
        this.show = true;
      });
    }
  },

  mounted() {
    this.foreignLogin();
  }
};
</script>

<style></style>
