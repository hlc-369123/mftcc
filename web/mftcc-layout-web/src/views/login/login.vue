<template>
  <div class="login-bg"
       :style="getLoginBg()"
       v-if="show">
    <div class="dev-login-dev">
      <div class="dev-login-img"
           :style="getLogin()"></div>
      <el-form ref="loginForm"
               :model="form"
               :rules="rules"
               @keyup.enter.native="onSubmit"
               v-show="updatePasswordFlag === false"
               label-width="80px"
               class="login-box"
               label-position="top">
        <p class="login-title">欢迎登录</p>
        <p class="login-title2">欢迎登录</p>
        <el-form-item label=""
                      prop="opNo">
          <el-input type="text"
                    class="aaa"
                    placeholder="请输入账号"
                    v-model="form.opNo">
          </el-input>
        </el-form-item>
        <el-form-item label=""
                      prop="password">
          <el-input type="password"
                    placeholder="请输入密码"
                    v-model="form.password">
          </el-input>
        </el-form-item>

        <el-form-item v-if="showIdentifyCode"
                      label=""
                      prop="code">
          <el-input type="text"
                    placeholder="图片验证码"
                    v-model="form.code"
                    class="identify">
            <template slot="append">
              <div @click="refreshIdentifyCode"
                   title="点击刷新"
                   style="cursor: pointer">
                <mftcc-identify :identifyOption="identifyOption"
                                ref="identify"></mftcc-identify>
              </div>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     v-on:click="onSubmit()"
                     :style="{
              color: formData.buttonTextColor
                ? formData.buttonTextColor
                : '#ffffff',
              background: formData.buttonColor
                ? formData.buttonColor
                : '#6FB1FF'
            }"
                     class="dev-login-btn">登录
          </el-button>
        </el-form-item>
      </el-form>

      <el-form :model="updatePasswordForm"
               :rules="updatePasswordRules"
               ref="updatePasswordForm"
               label-width="80px"
               @keyup.enter.native="updatePasswordFunc"
               v-show="updatePasswordFlag"
               class="login-box update-pw"
               label-position="top">
        <p class="login-title">修改密码</p>
        <p class="login-title2 update-pw">首次登陆需修改密码</p>
        <el-form-item label="登录账号"
                      prop="opNo"
                      v-show="true">
          <el-input ref="opNo"
                    type="text"
                    v-model="updatePasswordForm.opNo"
                    placeholder="请输入登录账号"
                    :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="原密码"
                      prop="password"
                      v-show="true">
          <el-input ref="password"
                    type="password"
                    autocomplete="off"
                    v-model="updatePasswordForm.password"
                    placeholder="请输入原密码">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码"
                      prop="newPassword"
                      v-show="true">
          <el-input ref="newPassword"
                    type="password"
                    autocomplete="off"
                    v-model="updatePasswordForm.newPassword"
                    placeholder="请输入新密码">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码"
                      prop="newPasswordChk"
                      v-show="true">
          <el-input ref="newPasswordChk"
                    type="password"
                    autocomplete="off"
                    v-model="updatePasswordForm.newPasswordChk"
                    placeholder="确认密码">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="update-login-btn"
                     type="primary"
                     v-on:click="updatePasswordFunc()">确认</el-button>
          <el-button class="update-login-btn"
                     v-on:click="updatePasswordFlag = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import api from "@/api/login/login";
import apiPage from "@/api/pageconfig/pageconfig";

export default {
  name: "login",
  data() {
    return {
      login_bg: "/" + $productName + "/static/images/login/login_bg.png",
      login: "/" + $productName + "/static/images/login/login.png",
      formData: [],
      imgUrl: "",
      imgLogo: "/" + $productName + "/static/images/header/logo.png",
      inputAreaColor: "",
      sysTitle: "",
      titleColor: "",
      loadingImg: "",
      inputTextColor: "#6FB1FF",
      inputColor: "#6FB1FF",
      form: {
        opNo: "",
        password: "",
        code: ""
      },
      // 表单验证，需要在 el-form-item 元素中增加 prop 属性
      rules: {
        opNo: [{ required: true, message: "账号不可为空", trigger: "blur" }],
        password: [{ required: true, message: "密码不可为空", trigger: "blur" }]
      },
      updatePasswordForm: {
        opNo: "",
        password: "",
        newPassword: "",
        newPasswordChk: ""
      },
      updatePasswordRules: {
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          {
            message: "密码错误",
            trigger: "blur",
            validator: this.validatePassword
          }
        ],
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { message: "", trigger: "blur", validator: this.validateNewPassword }
        ],
        newPasswordChk: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          {
            message: "两次输入不同",
            trigger: "blur",
            validator: this.validateNewPasswordChk
          }
        ]
      },
      updatePasswordFlag: false,
      loginFlag: true,
      identifyOption: {
        length: 4, // 图片验证码位数
        typeMix: true, // 数字和字母混合
        numLength: 2, // 混合类型下的数字个数
        capsLookMix: true, //字母大小写混合
        uupperLength: 1, //大写字母的个数
        time: 30000 //有效时间(毫秒) 默认 10 min
      },
      showIdentifyCode: false,
      show: false
    };
  },
  created() {
    if (window.config.host.sso_sts) {
      if (this.$store.getters.token) {
        this.$router.pushMain({ path: this.$index_router });
      } else {
        this.$sso.login();
      }
    } else {
      this.show = true;
    }
  },
  methods: {
    //获取配置数据
    // getData() {
    //   let data = {};
    //   data["companyId"] = window.$productName;
    //   apiPage.getList(
    //     data,
    //     res => {
    //       if (res.data.length > 0) {
    //         this.formData = res.data[0];
    //       }
    //       this.getLoginBack();
    //       this.getLogo();
    //       this.getInputAreaColor();
    //       this.getTitle();
    //       this.getTitleColor();
    //       this.getLoadingImg();
    //       this.getInputColor();
    //       this.getInputTextColor();
    //     },
    //     error => {
    //       //防止请求失败后不加载图片等等
    //       this.getLoginBack();
    //       this.getLogo();
    //       this.getInputAreaColor();
    //       this.getTitle();
    //       this.getTitleColor();
    //       this.getLoadingImg();
    //       this.getInputColor();
    //       this.getInputTextColor();
    //     }
    //   );
    // },
    //获取输入框背景颜色
    getInputColor() {
      if (this.formData.inputColor) {
        this.inputColor = this.formData.inputColor;
      } else {
        this.inputColor = "#000000";
      }
    },
    //获取输入框文字颜色
    getInputTextColor() {
      if (this.formData.inputTextColor) {
        this.inputTextColor = this.formData.inputTextColor;
      } else {
        return (this.inputTextColor = "#0074EF");
      }
    },
    //获取动图loadingImg
    getLoadingImg() {
      // if (this.formData.loadingImg) {
      //   this.loadingImg = this.formData.loadingImg;
      // } else {
      //   return (this.loadingImg = require("../../assets/login/login_bg_zx.png"));
      // }
    },
    //整体登录页背景
    getLoginBg() {
      return "background: url(" + this.login_bg + ") no-repeat center;background-size: 100% 100%;";
      // if (this.formData.loginBackImg) {
      //   this.imgUrl = this.formData.loginBackImg;
      // } else {
      //   return (this.imgUrl = require("../../assets/login/login_bg_zx.png"));
      // }
    },
    //登录页图片
    getLogin() {
      return "background: url(" + this.login + ") no-repeat center;";
      // if (this.formData.loginBackImg) {
      //   this.imgUrl = this.formData.loginBackImg;
      // } else {
      //   return (this.imgUrl = require("../../assets/login/login_bg_zx.png"));
      // }
    },
    //获取logo
    getLogo() {
      return "background: url(" + this.imgLogo + ") no-repeat center;";
      // if (this.formData.loginLogo) {
      //   this.imgLogo = this.formData.loginLogo;
      // } else {
      //   return (this.imgLogo = require("../../assets/login/logo_zx.png"));
      // }
    },
    //获取输入域背景颜色
    getInputAreaColor() {
      if (this.formData.inputAreaColor) {
        this.inputAreaColor = this.formData.inputAreaColor;
      } else {
        this.inputAreaColor = "#FAFDFF";
      }
    },
    //获取系统标题
    getTitle() {
      if (this.formData.title) {
        this.sysTitle = this.formData.title;
      } else {
        this.sysTitle = "欢迎登录";
      }
    },
    //获取系统标题颜色
    getTitleColor() {
      if (this.formData.titleColor) {
        this.titleColor = this.formData.titleColor;
      } else {
        this.titleColor = "#6FB1FF";
      }
    },
    onSubmit() {
      // 为表单绑定验证功能
      if (this.loginFlag == true) {
        this.$refs["loginForm"].validate((valid) => {
          if (valid) {
            if (this.showIdentifyCode) {
              if (new Date().getTime() > this.$store.getters.identify.endTime) {
                this.$alert("验证码已失效，请重新输入", this.$alertTitle, {
                  type: "warning",
                  callback: (res) => {
                    this.form.code = "";
                    this.$refs.identify.refreshCode();
                  }
                });
                return false;
              } else if (this.form.code.toLowerCase() != this.$store.getters.identify.identifyCode.toLowerCase()) {
                this.$alert("验证码错误", this.$alertTitle, {
                  type: "error",
                  callback: (res) => {
                    this.$refs.identify.refreshCode();
                  }
                });
                return false;
              }
            }
            const loading = this.$loading({
              fullscreen: true,
              lock: true,
              text: "拼命加载中",
              background: "rgba(0, 0, 0, 0.4)"
            });
            this.loginFlag = false;
            api.login(
              this.form,
              (reponse) => {
                loading.close();
                if (reponse.code == "0") {
                  var routes = [];
                  let data = reponse.data;
                  let menuData = this.initMenuData(data.sysMenuList, routes);
                  this.$store.commit("changeLogin", {
                    user: data.sysUserInfo,
                    buttonAuth: data.buttonAuth,
                    token: data.token,
                    refreshToken: data.refreshToken,
                    buttons: data.buttons,
                    menu: menuData
                    // routes: routes
                  });
                  // mainRouter.children = routes;
                  // this.$router.addRoutes([mainRouter]);
                  // this.$router.push("/main");
                  let user = this.$store.getters["user"];
                  this.$notify({
                    title: "首次登录",
                    message: "欢迎您，" + user.opName,
                    position: "bottom-right"
                  });
                  this.$router.pushMain({ path: this.$index_router });
                } else if (reponse.code == "300") {
                  this.$confirm(reponse.msg, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(() => {
                    this.updatePasswordForm.opNo = this.form.opNo;
                    this.updatePasswordForm.password = "";
                    this.updatePasswordForm.newPassword = "";
                    this.updatePasswordForm.newPasswordChk = "";
                    this.updatePasswordFlag = true;
                  });
                } else {
                  this.$alert(reponse.msg, this.$alertTitle, {
                    type: "error",
                    callback: (res) => {
                      if (reponse.identify) {
                        if (!this.showIdentifyCode) {
                          this.showIdentifyCode = true;
                        } else {
                          this.$refs.identify.refreshCode();
                        }
                        // this.identifyCode = reponse.identify;
                      }
                    }
                  });
                }
                this.loginFlag = true;
              },
              (error) => {
                loading.close();
                this.loginFlag = true;
              }
            );
          } else {
            return false;
          }
        });
      }
    },
    initMenuData(menuData, routers) {
      var result = [];
      for (let menu of menuData) {
        if (menu.upMenuId == null) {
          //判断是否为顶层节点
          var item = {
            id: menu.menuId.toString(),
            title: menu.menuName,
            icon: menu.menuIcon,
            url: menu.menuUrl,
            newTab: menu.newTab
          };
          let children = this.getchilds(menu.menuId.toString(), menuData, routers, 1);
          if (children.length > 0) {
            item.children = children;
          }
          result.push(item);
        }
      }

      return result;
    },
    //获取子节点
    getchilds(menuId, array, routers, i) {
      var childs = [];
      for (let arr of array) {
        if (arr !== undefined && arr.upMenuId == menuId) {
          var child = {
            id: arr.menuId.toString(),
            title: arr.menuName,
            url: arr.menuUrl,
            icon: arr.menuIcon,
            newTab: arr.newTab
          };
          i++;
          let children = this.getchilds(arr.menuId.toString(), array, routers, i);
          if (children.length > 0) {
            child.children = children;
          }
          childs.push(child);
        }
      }
      return childs;
    },
    validatePassword(rule, value, callback) {
      let opNo = this.updatePasswordForm.opNo;
      let password = this.updatePasswordForm.password;
      let data = {
        opNo: opNo,
        password: password
      };
      api.validatePassword(
        data,
        (reponse) => {
          if (reponse.code === 0) {
            callback();
          } else {
            callback(new Error(reponse.msg));
          }
        },
        (error) => {
          callback(new Error("连接异常，密码校验失败"));
        }
      );
    },
    validateNewPassword(rule, value, callback) {
      let opNo = this.updatePasswordForm.opNo;
      let newPassword = this.updatePasswordForm.newPassword;
      let data = {
        opNo: opNo,
        password: newPassword
      };
      api.validateNewPassword(
        data,
        (reponse) => {
          if (reponse.code === 0) {
            if (reponse.data.flag == "success") {
              callback();
            } else {
              callback(new Error(reponse.data.msg));
            }
          } else {
            callback(new Error(reponse.msg));
          }
        },
        (error) => {
          callback(new Error("连接异常，密码校验失败"));
        }
      );
    },
    validateNewPasswordChk(rule, value, callback) {
      let newPassword = this.updatePasswordForm.newPassword;
      let newPasswordChk = this.updatePasswordForm.newPasswordChk;
      if (newPasswordChk !== "" && newPassword !== newPasswordChk) {
        callback(new Error("两次输入不同"));
      } else {
        callback();
      }
    },
    updatePasswordFunc() {
      this.$refs.updatePasswordForm.validate((valid) => {
        if (valid === true) {
          api.updatePassword(this.updatePasswordForm, (reponse) => {
            if (reponse.code === 0) {
              this.$message({
                type: reponse.msgType,
                dangerouslyUseHTMLString: true,
                message: reponse.msg
              });
              this.updatePasswordFlag = false;
            } else {
              this.$alert(reponse.msg, this.$alertTitle, {
                type: reponse.msgType,
                dangerouslyUseHTMLString: true
              });
            }
          });
        }
      });
    },
    refreshIdentifyCode() {
      this.$refs.identify.refreshCode();
    }
  }
};
</script>
<style>
.login-bg .login-box {
  width: 50%;
  padding: 100px 85px;
  box-sizing: border-box;
  overflow: hidden;
  position: relative;
}
.login-bg .login-box .login-banner {
  background-color: #0e81be;
  border-radius: 50%;
  width: 1000px;
  height: 310px;
  position: absolute;
  top: -170px;
  left: -269px;
  z-index: 1;
}
.login-bg .login-box.update-pw {
  padding: 40px 85px;
}

.login-bg .login-title {
  font-size: 30px;
  font-weight: bold;
  color: #1a1a1a;
  line-height: 30px;
  margin: 0;
  height: 24px;
  width: 350px;
}
.login-bg .login-title2 {
  font-size: 14px;
  font-weight: 300;
  color: #999999;
  margin-top: 15px;
  margin-bottom: 60px;
}
.login-bg .login-title2.update-pw {
  margin-bottom: 30px;
}
.login-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  background-size: 100% 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-bg .dev-login-dev {
  width: 1000px;
  height: 560px;
  background: #ffffff;
  box-shadow: 0px 0px 20px 0px rgb(68 67 67 / 52%);
  border-radius: 12px;
  display: flex;
}
.login-bg .dev-login-img {
  width: 50%;
  height: 100%;
  background: #463f3c;
  border-radius: 12px;
  background-size: 100% 100%;
}
.login-bg .el-form-item__label {
  font-size: 12px;
  font-weight: 300;
  color: #999999;
  line-height: 15px;
  height: 15px;
  padding: 0;
}
.login-bg .el-form-item {
  margin-bottom: 40px;
}
.login-bg .el-input__inner {
  border: none;
  border-bottom: 1px solid #dedede;
  font-size: 16px;
  color: #1a1a1a;
  line-height: 30px;
  border-radius: 1px;
  padding: 0;
}
.login-bg
  .el-form-item.is-required:not(.is-no-asterisk)
  > .el-form-item__label:before {
  display: none;
}
.login-bg .dev-login-btn {
  width: 100%;
  height: 42px !important;
  background: #409eff !important;
  margin: 0 !important;
}
.login-bg .dev-login-btn:hover {
  opacity: 0.8;
}
.login-bg .update-login-btn {
  width: 45%;
}
</style>
