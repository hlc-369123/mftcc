<template>
  <div class="mftcc-layout-header">
    <div class="outer-west-logo">
      <!--      <div class="layout-logo">-->
      <img class="layout-logo" style="width: 40px;width: 55px" :src="logo" />
      <span>综合业务管理平台</span>
      <!--      </div>-->
      <img class="layout-title" :src="logoTitle" />
    </div>
    <div class="middle-north-east" @click="fixedMenu">
      <i class="el-icon-menu"></i>
    </div>
    <div class="middle-north-center">
      <div class="head-search-div" v-if="$hasPerm('headrSearch')">
        <input
          v-model="inputValue"
          class="input_content"
          type="text"
          @keyup.enter="searchClick()"
          placeholder="请输入客户名称"
        />
        <div class="search-icon-div">
          <i class="el-icon-search search-icon-i" @click="searchClick"></i>
        </div>
      </div>

      <el-menu
        mode="horizontal"
        @select="headerMenuFunc"
        class="mftcc-header-menu"
        ref="headerMenu"
      >
        <!--        <el-menu-item index="search" v-if="searchShow">-->
        <!--          <i class="el-icon-search"></i>-->
        <!--          <span>搜索</span>-->
        <!--        </el-menu-item>-->
        <el-menu-item index="notice">
          <el-badge
            :value="noticeNum"
            :max="99"
            class="item menu-notice-num"
            type="danger"
          >
            <div>
              <i class="el-icon-chat-dot-round"></i>
            </div>
          </el-badge>
          <span>消息</span>
        </el-menu-item>

        <el-menu-item index="approval">
          <el-badge
            :value="approvalNum"
            class="menu-approval-num"
            type="danger"
          >
            <i class="el-icon-bell"></i>
          </el-badge>
          <span>待办</span>
        </el-menu-item>
        <el-menu-item index="user" class="menu-user">
          <el-popover
            placement="bottom-end"
            title=""
            trigger="hover"
            popper-class="menu-user-content"
            :offset="70"
          >
            <div class="menu-user-title" @click="headerMenuFunc('user')">
              <div class="menu-user-message">
                <div>
                  <img :src="userImg" />
                </div>
                <div>
                  <div class="width-270">
                    <div class="head-username-div">
                      <span>{{ userName }}</span>
                    </div>
                    <div class="codeImage">
                      <img src="@/assets/image/code.png" />
                    </div>
                    <div></div>
                  </div>

                  <div class="head-br-div">
                    <span>{{ brName }}(部门)</span>
                  </div>
                  <div class="head-role-div">
                    <div class="float-left" style="width: 135px">
                      <div class="img-div">
                        <img src="@/assets/image/role.png" />
                      </div>
                      <div>
                        {{ roleName }}
                      </div>
                    </div>
                    <div class="float-left" style="width: 125px">
                      <div class="img-div">
                        <img src="@/assets/image/phone.png" />
                      </div>
                      <div>
                        {{ phone }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="menu-user-button">
              <el-button type="primary" @click="updatePassword"
                >修改密码
              </el-button>
              <el-divider direction="vertical"></el-divider>
              <el-button type="primary" @click="signOut">退出系统 </el-button>
            </div>
            <div class="menu-user-info" @click="navRouter" slot="reference">
              <img :src="userImg" />
              <span>欢迎您，{{ userName }}</span>
            </div>
          </el-popover>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>
<script>
// import apiPage from "@/api/pageconfig/pageconfig";
export default {
  data() {
    return {
      searchShow: false,
      formData: {},
      inputValue: "",
      logoTitle: "/" + $productName + "/static/images/header/logo_title.png",
      logo: "/" + $productName + "/static/images/header/logo_wj.png"
    };
  },
  props: {
    userName: String,
    approvalNum: String,
    noticeNum: Number,
    brName: String,
    phone: String,
    userImg: String,
    roleName: String
  },
  created() {
    // this.getData();
  },
  methods: {
    navRouter() {
      this.$router.pushMain({ path: this.$index_router });
    },
    //获取数据
    // getData() {
    //   let _this = this;
    //   let data = {};
    //   data["companyId"] = window.$productName;
    //   apiPage.getList(data, res => {
    //     if (res.data.length > 0) {
    //       let formData = res.data[0];
    //       if (formData.sysLogo) {
    //         this.sysLogoImg = formData.sysLogo;
    //       }
    //       if (formData.slogan) {
    //         this.sloganImg = formData.slogan;
    //       }
    //     }
    //   });
    // },
    signOut() {
      this.$emit("signOut");
    },
    searchClick() {
      this.$emit("openCusSearch", this.inputValue);
    },
    updatePassword() {
      this.$emit("updatePassword");
    },
    headerMenuFunc(index, indexPath) {
      this.$emit("cancelSelect", "menu", null);
      this.$emit("menuFunc", index);
    },
    fixedMenu() {
      this.$emit("fixedMenuFunc");
    },
    cancelSelect(index) {
      this.$refs.headerMenu.activeIndex = index;
    },
    getSelection() {
      return this.$refs.headerMenu.activeIndex;
    }
  }
};
</script>
<style scoped>
.layout-logo {
  /*background: url(/mftcc-base-web/static/libs/base/images/logo.png) no-repeat center;*/
  height: 40px;
  background-size: 100% 100%;
  width: 182px;
}

.layout-title {
  /*background: url(/mftcc-base-web/static/libs/base/images/logo_title.png) no-repeat center;*/
  height: 60%;
  background-size: 100% 100%;
  margin-left: 20px;
  margin-top: 12px;
}
</style>

<style scoped src="@/assets/css/newHead.css" />
