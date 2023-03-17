<template>
  <div class="mftcc-layout-header">
    <div class="outer-west-logo">
      <img class="layout-logo"
           :src="logo" />
    </div>
    <div class="outer-east">
      <el-tabs class="header-menu"
               v-model="activeName"
               @tab-click="handleClick">
        <el-tab-pane v-for="item in menuData"
                     :key="item.id"
                     :label="item.title"
                     :name="item.id"></el-tab-pane>
      </el-tabs>
      <div class="header-line"></div>
      <el-input class="header-search-input"
                placeholder="请输入需要查询的内容···"
                v-model="searchInputValue">
        <i slot="prefix"
           class="el-input__icon el-icon-search"
           @click="doSearch"></i>
      </el-input>
      <el-badge v-if="approvalNum>0"
                :value="approvalNum"
                :max="99"
                class="menu-approval-num"
                type="danger">
        <i class="header-opt-icon el-icon-date"
           @click="headerMenuFunc('approval')"></i>
      </el-badge>
      <i v-else
         class="header-opt-icon el-icon-date"
         @click="headerMenuFunc('approval')"></i>

      <i class="header-opt-icon el-icon-bell"
         @click="headerMenuFunc('notice')"></i>
      <el-dropdown size="small"
                   @command="handleCommand"
                   trigger="click">
        <i class="header-opt-icon el-icon-setting"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="signOut">退出系统</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
  </div>
</template>
<script>
  // import apiPage from "@/api/pageconfig/pageconfig";
  export default {
    data() {
      return {
        activeName: "",
        lastMid: "",
        menuData: [],
        searchInputValue: "",
        logo: "/" + $productName + "/static/images/header/logo.png"
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
      this.menuData = this.$store.getters.menu;
      if (this.menuData) {
        this.activeName = this.menuData[0].id;
        this.lastMid = this.activeName;
      }
    },
    mounted() {
      this.refreshMenu(this.lastMid);
    },
    methods: {
      handleClick(el) {
        let mId = this.menuData[el.index].id;
        if (mId != this.lastMid) {
          this.lastMid = mId;
          this.refreshMenu(mId);
        }
      },
      refreshMenu(mId) {
        this.$parent.$parent.$children[1].$children[0].$children[0].initMenu(mId);
      },
      doSearch() {
        console.log(this.searchInputValue);
      },
      handleCommand(command) {
        if (command == "signOut") {
          this.$emit("signOut");
        }
      },
      headerMenuFunc(type) {
        this.$emit("menuFunc", type);
      }
    }
  };
</script>