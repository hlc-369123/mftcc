<template>
  <el-container style="background: #EFF4FB;">
    <div class="main-bg"></div>
    <el-header height="55px"
               style="z-index:1;"
               v-if="isShowHeader">
      <mftcc-header @fixedMenuFunc="fixedMenuFunc"
                    @menuFunc="menuFunc"
                    @updatePassword="updatePassword"
                    @signOut="signOut"
                    @cancelSelect="cancelSelect"
                    @openCusSearch="openSearchDialog"
                    :userName="opName"
                    :brName="brName"
                    :phone="phone"
                    :userImg="userImg"
                    :roleName="roleName"
                    :approvalNum="approvalNum"
                    :noticeNum="noticeNum"
                    ref="headerMenu"></mftcc-header>
    </el-header>
    <el-container class="mftcc-main-el-containner"
                  ref="container">
      <el-aside width="215px"
                v-if="isShowMenu">
        <mftcc-menu :iscollapse="iscollapse"
                    @cancelSelect="cancelSelect"
                    @getSelection="getSelection"
                    :user="$store.getters.user"
                    :userImg="userImg"
                    ref="menu">
        </mftcc-menu>
      </el-aside>
      <el-container class="zq-border"
                    style="background:#FFF">
        <el-main class="center-content"
                 style="white-space: nowrap;">
          <!-- <transition name="fade-transform" mode="out-in"> -->
          <!-- <template
            v-if="
              $route.path.startsWith('/mftcc') || $route.path.startsWith('/vue')
            "
          > -->
          <el-scrollbar v-show="
              $route.path.startsWith('/mftcc') || $route.path.startsWith('/vue')
            ">
            <div id="microApp"
                 ref="microApp"
                 :style="'min-height:' + microAppHeight + 'px'"></div>
          </el-scrollbar>
          <!-- </template> -->
          <!-- <template > -->
          <keep-alive v-if="isRouterAlive"
                      v-show="
              !$route.path.startsWith('/mftcc') &&
                !$route.path.startsWith('/vue')
            ">
            <router-view :key="$route.path"> </router-view>
          </keep-alive>
          <!-- </template> -->
          <!-- </transition> -->
        </el-main>

        <el-drawer :visible.sync="drawer"
                   :withHeader="true"
                   :show-close="false"
                   custom-class="mftcc-main-drawer"
                   :modal="false"
                   size="60"
                   :destroy-on-close="true"
                   class="mftcc-fixed-drawer"
                   :before-close="handleClose">
          <div calss="mftcc-fixed-menu-mark"
               slot="title">
            <!--            <transition name="mftcc-fixed-menu-transition">-->
            <div class="mftcc-fixed-menu"
                 v-if="drawer">
              <el-row class="blockTip">
                <el-col class="leftpart height-50">
                  <el-row>
                    <el-col class="splitDiv"></el-col>
                    <el-col class="tipText">快捷入口</el-col>
                  </el-row>
                </el-col>
              </el-row>
              <!--                快捷入口菜单-->
              <el-col>
                <div class="quick-position">
                  <ul>
                    <li class="mftcc-fixed-menu-item quick-position-li"
                        v-for="menu of showIconList"
                        :key="menu.id"
                        @click="bindItemClick(menu)">
                      <img :class="menu.itemUrl" />
                      <span> {{ menu.itemName }}</span>
                    </li>
                  </ul>
                </div>
              </el-col>
              <el-row>
                <div v-if="fixMenuList.length > 8"
                     style="text-align: center;">
                  <span v-if="more">
                    <img src="@/assets/image/down.png" /><span @click="changeState"
                          class="more">全部</span>
                  </span>
                  <span v-if="!more">
                    <img src="@/assets/image/up.png" /><span @click="changeState"
                          class="more">收起</span>
                  </span>
                </div>
              </el-row>

              <!--                其他工具-->
              <el-row class="blockTip margin-top-50">
                <el-col class="leftpart height-50">
                  <el-row>
                    <el-col class="splitDiv"></el-col>
                    <el-col class="tipText">微金小工具</el-col>
                  </el-row>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
                  <div class="tool-quick-position">
                    <ul class="margin-right-30">
                      <li class="mftcc-fixed-menu-item tool-quick-position"
                          v-for="menu of toolMenuList"
                          :key="menu.id"
                          @click="bindItemClick(menu)">
                        <img :class="menu.itemUrl" />
                        <span class="margin-top-5"> {{ menu.itemName }}</span>
                      </li>
                    </ul>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!--            </transition>-->
          </div>
        </el-drawer>
      </el-container>
    </el-container>
    <el-dialog :visible.sync="updatePasswordFormShow"
               width="50%"
               :close-on-click-modal="false"
               :center="true">
      <div slot="title"
           class="dialog-header">
        <label>修改密码</label>
      </div>
      <div class="dialog-content">
        <el-form :model="updatePasswordForm"
                 :rules="rules"
                 ref="updatePasswordForm"
                 label-width="100px">
          <el-form-item label="登录账号"
                        prop="opNo"
                        v-show="false">
            <el-input ref="opNo"
                      type="text"
                      v-model="updatePasswordForm.opNo"
                      placeholder="请输入登录账号"
                      :disabled="true">
            </el-input>
          </el-form-item>
          <el-form-item label="用户名"
                        prop="opName"
                        v-show="true">
            <el-input ref="opName"
                      type="text"
                      v-model="updatePasswordForm.opName"
                      placeholder="请输入用户名"
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
        </el-form>
      </div>
      <div slot="footer"
           class="dialog-footer">
        <el-button type="primary"
                   @click="updatePasswordFunc">确 定</el-button>
        <el-button @click="updatePasswordFormShow = false">取 消</el-button>
      </div>
    </el-dialog>
    <searchDialog v-if="searchDialog"
                  :inputValue="inputValue"></searchDialog>
  </el-container>
</template>

<script>
  import mftccMenu from "./menu";
  import mftccHeader from "./header";
  import searchDialog from "@/views/apply/searchDialog";
  import flowable from "@/api/flowable/flowable";
  const { getNoLoading, postJsonNoLoading, postJson } = $axios;
  export default {
    name: "App",
    components: {
      mftccMenu,
      mftccHeader,
      searchDialog: searchDialog
    },
    data() {
      return {
        more: true,
        searchDialog: false,
        inputValue: "",
        toolMenuList: {},
        iscollapse: false,
        drawer: false,
        isShowMenu: true,
        isShowHeader: true,
        //用户基本信息
        opNo: this.$store.getters.user.opNo,
        opName: this.$store.getters.user.opName,
        brName: this.$store.getters.user.brName,
        phone: this.$store.getters.user.tel,
        roleName: this.$store.getters.user.roleName,
        approvalNum: "0",
        noticeNum: 0,
        updatePasswordFormShow: false,
        updatePasswordFormShow: false,
        updatePasswordForm: {
          opNo: "",
          opName: "",
          password: "",
          newPassword: "",
          newPasswordChk: ""
        },
        rules: {
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
        isRouterAlive: true,
        microAppHeight: 0
      };
    },
    provide() {
      return {
        reload: this.reload
      };
    },
    props: {
      userImg: {
        type: String,
        default() {
          return require("@/theme/default/images/op_user.png");
        }
      },
      fixMenuList: {
        type: Array,
        default() {
          return [];
        }
      },
      url: {
        type: Object,
        default() {
          return {
            signOut: "/" + $servers.sys + "/loginOut",
            validatePassword: "/" + $servers.sys + "/sys/sysUser/validatePassword",
            validateNewPassword: "/" + $servers.sys + "/sys/sysSecAuditConfig/validatePWAjax",
            updatePassword: "/" + $servers.sys + "/sys/sysUser/updatePassword"
          };
        }
      }
    },
    created() {},
    mounted() {
      this.getShow();
      this.filter();
      this.microAppHeight = document.body.clientHeight;
      if (this.isShowHeader) {
        this.microAppHeight -= 70;
      }
      if (!this.isShowHeader) {
        return;
      }
      this.initWebSocket();
      this.initMsgCut();
      this.$emit("init");
      this.initCountForToDoTask();
      this.initCountForUnreadMsg();
    },
    watch: {
      "$route.path": {
        handler(val, oldVal) {
          this.isShowHeader = true;
          this.isShowMenu = true;
          this.getShow();
          this.filter();
          this.microAppHeight = document.body.clientHeight;
          if (this.isShowHeader) {
            this.microAppHeight -= 70;
          }
        }
      }
    },
    computed: {
      showIconList: {
        get: function () {
          if (this.more) {
            if (this.fixMenuList.length < 9) {
              return this.fixMenuList;
            }
            let newArr = [];
            for (var i = 0; i < 8; i++) {
              let item = this.fixMenuList[i];
              newArr.push(item);
            }
            return newArr;
          }
          return this.fixMenuList;
        },
        set: function (val) {
          this.fixMenuList = val;
        }
      }
    },
    methods: {
      initCountForToDoTask() {
        // 首页打开待办的话，此处不用单独查询待办条数。
        this.$nextTick(() => {
          this.getCountForToDoTask();
        });
        /** 此事件监听应废弃 */
        this.$bus.$on("getCountForToDoTask", () => {
          this.getCountForToDoTask();
        });
        this.$bus.$on("setCountForToDoTask", (count) => {
          this.setCountForToDoTask(count + "");
        });
      },
      initCountForUnreadMsg() {
        // TODO 如果首页跳转消息列表，应该在消息组件内处理条数、外部刷新显示数字。
        if (this.$index_router != "/mftcc-msg-web/detail/msgDetails") {
        }
        this.$bus.$on("setCountForUnreadMsg", (count) => {
          this.noticeNum = count;
        });
      },
      changeState() {
        this.more = !this.more;
      },
      handleClose(done) {
        done();
      },
      bindItemClick(item) {
        if (item.ifComplete == "1") {
          if (item.openFlag == "2") {
            window.open(item.reqUrl);
          } else {
            this.$router.pushMain({ path: item.reqUrl });
            this.drawer = false;
          }
        } else {
          const h = this.$createElement;
          this.$notify({
            title: item.itemName,
            message: h("i", { style: "color: teal" }, "正在建设中，敬请期待。")
          });
        }
      },
      getShow() {
        if (this.$route.path == this.$index_router && !this.$index_menu_show) {
          this.isShowMenu = false;
        }
      },
      initMsgCut() {
        if (window.config.servers.msg) {
          getNoLoading(
            "/" +
              window.config.servers.msg +
              "/messages/msgMessages/queryUnreadMsgCounts/" +
              this.$store.getters.user.opNo,
            {},
            true,
            (reponse) => {
              if (reponse.code === 0) {
                this.noticeNum = reponse.unreadMsgCount;
              }
            },
            (error) => {
              new Error("消息数量查询失败！");
            }
          );
        }
      },
      initQuickMenu() {
        postJsonNoLoading(
          "/" + $servers.config + "/quick/configQuickEntrance/findList",
          {},
          true,
          (reponse) => {
            if (reponse.code === 0) {
              this.fixMenuList = reponse.data.quickMenuList;
              this.toolMenuList = reponse.data.toolMenuList;
            }
          },
          (error) => {}
        );
      },
      //初始化weosocket
      initWebSocket() {
        if (window.config.mq) {
          let splicWebSocketUlr =
            "ws://" +
            window.config.mq.host +
            ":" +
            window.config.mq.port +
            "/mqWebsocket/" +
            this.$store.getters.user.opNo;
          this.websock = new WebSocket(splicWebSocketUlr);
          this.websock.onmessage = this.websocketonmessage;
          this.websock.onerror = this.websocketonerror;
        }
      },
      //连接建立失败重连
      websocketonerror() {
        this.initWebSocket();
      },
      //数据接收
      websocketonmessage(e) {
        const redata = JSON.parse(JSON.parse(e.data));
        this.$notify({
          title: redata.tmpTypeName,
          message: redata.body,
          position: "bottom-right",
          duration: 4500,
          iconClass: "el-icon-s-opportunity"
        });
        this.approvalNum++;
      },
      reload() {
        this.isRouterAlive = false;
        this.$nextTick(() => (this.isRouterAlive = true));
      },
      signOut() {
        let data = {
          opNo: this.opNo
        };
        postJsonNoLoading(
          this.url.signOut,
          data,
          true,
          (reponse) => {
            this.$store.commit("clearLogin");
            this.$router.push("/login");
          },
          (error) => {}
        );
      },
      //修改密码事件回调
      updatePassword() {
        this.updatePasswordFormShow = true;
        this.updatePasswordForm.opNo = this.opNo;
        this.updatePasswordForm.opName = this.opName;
      },
      validatePassword(rule, value, callback) {
        let data = {
          opNo: this.updatePasswordForm.opNo,
          password: this.updatePasswordForm.password
        };
        postJsonNoLoading(
          this.url.validatePassword,
          data,
          true,
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
        postJsonNoLoading(
          this.url.validateNewPassword,
          data,
          true,
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
            postJson(this.url.updatePassword, this.updatePasswordForm, true, (reponse) => {
              if (reponse.code === 0) {
                this.$message({
                  type: reponse.msgType,
                  dangerouslyUseHTMLString: true,
                  message: reponse.msg
                });
                this.signOut();
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
      //悬浮菜单点击事件回调
      fixMenuClick(type) {
        this.$emit("fixMenuClick", type);
      },
      //右上菜单点击事件回调
      menuFunc(type) {
        this.$emit("menuClick", type);
      },
      fixedMenuFunc() {
        if (this.drawer === true) {
          this.drawer = false;
        } else {
          this.drawer = true;
          //获取数据
          this.initQuickMenu();
        }
      },
      cancelSelect(rel, index) {
        // if (this.$refs[rel]) {
        //   this.$refs[rel].cancelSelect(index);
        // }
      },
      getSelection(rel, callback) {
        // callback(this.$refs[rel].getSelection());
      },
      setCountForToDoTask(num) {
        this.approvalNum = num;
      },
      getCountForToDoTask() {
        let data = {
          opNo: this.$store.getters.user.opNo
        };
        flowable.getCountForToDoTask(data, (res) => {
          if (res.code == 0) {
            this.setCountForToDoTask(res.count + "");
          }
        });
      },
      openSearchDialog(parm) {
        this.searchDialog = false;
        this.$nextTick(() => ((this.inputValue = parm), (this.searchDialog = true)));
      },
      filter() {
        let path = this.$route.path;
        let list = window.config.router_standalone_list;
        if (list) {
          for (let i in list) {
            if (path === list[i]) {
              this.isShowHeader = false;
              this.isShowMenu = false;
              break;
            }
          }
        }
      }
    }
  };
</script>
<style scoped src="@/assets/css/newHead.css" />
<style scoped src="@/assets/css/unifiedEntrance.css" />
