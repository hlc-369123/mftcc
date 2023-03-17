<template>
  <div style="padding: 20px">
    <el-row :gutter="20">
      <el-col :span="12">
        用户编号：
        <el-input placeholder="请输入用户编号" v-model="opNo"> </el-input>
      </el-col>
      <el-col :span="12">
        消息内容：
        <el-input
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 6 }"
          placeholder="请输入内容"
          v-model="msg"
        ></el-input>
      </el-col>
    </el-row>
    <el-button style="margin-top: 20px" type="primary" @click="sendMessage"
      >发送消息</el-button
    >
  </div>
</template>
<script>
import api from "@/api/demo/demoLeaseAppProjectInit";
export default {
  data() {
    return {
      opNo: "",
      msg: "",
    };
  },
  methods: {
    sendMessage() {
      api.sendMessage({ opNo: this.opNo, msg: this.msg }, (res) => {
        if (res.code === 0) {
          this.$message({
            type: res.msgType,
            dangerouslyUseHTMLString: true,
            message: "发送成功",
          });
        } else {
          this.$alert(res.msg, this.$alertTitle, {
            type: res.msgType,
            dangerouslyUseHTMLString: true,
          });
        }
      });
    },
  },
};
</script>
