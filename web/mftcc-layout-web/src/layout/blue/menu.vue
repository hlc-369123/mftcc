<template>
  <div class="mftcc-layout-menu"
       ref="container">
    <el-scrollbar>
      <el-menu :default-active="selectStatus"
               :collapse="iscollapse"
               class="menu"
               ref="menu"
               :style="'min-height:' + microAppHeight + 'px'">
        <template v-for="item in menuData">
          <el-submenu :key="item.id"
                      :index="item.id"
                      v-if="item.children && item.newTab != 0">
            <!-- <el-submenu :key="item.id" :index="item.id" v-if="item.children"> -->
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{ item.title }}</span>
              <div class="menu_bg"></div>
            </template>
            <el-menu-item v-for="citem in item.children"
                          :key="citem.id"
                          :index="citem.id"
                          @click="navRouter(citem)">
              <template slot="title">
                <span slot="title">{{ citem.title }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
          <el-menu-item v-else
                        :key="item.id"
                        :index="item.id"
                        @click="navRouter(item)">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{ item.title }}</span>
              <div class="menu_bg"></div>
            </template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
    <transition name="mftcc-multilevel-menu-transition">
      <div v-if="multilevelMenu"
           class="mftcc-multilevel-menu">
        <el-scrollbar>
          <div class="mftcc-multilevel-menu-item"
               v-for="(item, index) in menuItems"
               :key="index">
            <div class="second-menu-title">
              <template v-if="item.children !== undefined && item.children.length > 0">
                <span>{{ item.title }}</span>
                <div v-for="menuItem in item.children"
                     :key="menuItem.id"
                     class="three-menu-title"
                     @click="navRouterChildren(menuItem)">
                  <i class="el-icon-arrow-right"></i>
                  {{ menuItem.title }}
                </div>
              </template>
              <template v-else>
                <span @click="navRouterChildren(item)">
                  <i class="el-icon-arrow-right"></i>
                  {{ item.title }}
                </span>
              </template>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </transition>
    <div v-if="multilevelMenu"
         @click="maskClick"
         class="mftcc-multilevel-menu-mask"></div>
  </div>
</template>
<script>
export default {
  inject: ["reload"],
  data() {
    return {
      menuData: [],
      multilevelMenu: false,
      clickIndex: null,
      activeIndex: null,
      menuItems: [],
      headerMenuIndex: null,
      selectStatus: "",
      microAppHeight: 0
    };
  },
  props: ["iscollapse"],
  created() {
    this.initMenu();
    let data = this.menuData;
    this.selectStatus = this.getParentNode(this.$route.fullPath, data);
    window.$menu = this;
  },
  // watch: {
  //   $route(val, old) {
  //     if (val) {
  //       let data = this.menuData;
  //       this.selectStatus = this.getParentNode(val.fullPath, data);
  //     }
  //   }
  // },
  mounted() {
    /* 如果是新页签则默认打开第一个菜单的路由 */
    if (this.$route.query.menuId) {
      let menuData = this.menuData;
      for (let i = 0; i < menuData.length; i++) {
        if (!menuData[i].children && menuData[i].url) {
          this.navRouter(menuData[i]);
          break;
        } else if (menuData[i].children) {
          if (this.getChildNode(menuData[i].children)) {
            break;
          }
        }
      }
    }
    this.microAppHeight = document.body.clientHeight - 55;
  },
  methods: {
    /* 根据菜单ID设置菜单选中的效果，给业务使用 */
    setMenuStatus(menuId) {
      this.selectStatus = menuId;
    },
    navRouter(item) {
      let url = item.url;
      let children = item.children;
      let newTab = item.newTab;
      this.$emit("getSelection", "headerMenu", headerMenuIndex => {
        if (headerMenuIndex !== null) {
          this.headerMenuIndex = headerMenuIndex;
        }
      });
      if (newTab == 0 && children != undefined) {
        let routeUrl = this.$router.resolve({
          path: "/main", //新页面地址
          query: { menuId: item.id } //携带的参数
        });
        window.open(routeUrl.href);
        this.multilevelMenu = false;
        return;
      } else if (url == "" && children == undefined) {
        this.multilevelMenu = false;
        return;
      }
      this.$emit("cancelSelect", "headerMenu", null);
      if (url && children == undefined && newTab != 0) {
        this.multilevelMenu = false;
        singleSpaNavigate(url);
        this.activeIndex = this.$refs.menu.activeIndex;
        this.headerMenuIndex = null;
        // this.selectStatus = this.getParentNode(url, this.menuData);
        this.reload();
      } else if (
        url &&
        children == undefined &&
        newTab == 0 &&
        this.$route.query.menuId
      ) {
        this.multilevelMenu = false;
        singleSpaNavigate(url);
        this.activeIndex = this.$refs.menu.activeIndex;
        this.reload();
      } else if (
        url &&
        children == undefined &&
        newTab == 0 &&
        !this.$route.query.menuId
      ) {
        this.multilevelMenu = false;
        return;
      } else if (this.multilevelMenu === true) {
        if (this.clickIndex !== item.id) {
          this.multilevelMenu = false;
          this.menuItems = children;
          this.$nextTick(() => {
            setTimeout(() => {
              this.multilevelMenu = true;
            }, 300);
          });
        } else {
          this.maskClick();
        }
      } else {
        this.menuItems = children;
        this.multilevelMenu = true;
      }
      this.clickIndex = this.$refs.menu.activeIndex;
    },
    navRouterChildren(item) {
      this.multilevelMenu = false;
      if (singleSpaNavigate) {
        singleSpaNavigate(item.url);
      } else {
        this.$router.push(item.url);
      }
      this.activeIndex = this.$refs.menu.activeIndex;
      this.headerMenuIndex = null;
      this.reload();
    },
    initMenu() {
      let menuId = sessionStorage.getItem("mId");
      let menuData = this.$store.getters.menu;
      if (this.$route.query.menuId) {
        let mId = this.$route.query.menuId;
        sessionStorage.setItem("mId", mId);
        this.getMenuData(mId, menuData);
      } else if (menuId) {
        this.getMenuData(menuId, menuData);
      } else {
        this.menuData = this.$store.getters.menu;
      }
    },
    getMenuData(mId, menuData) {
      menuData.forEach(item => {
        if (item.id == mId) {
          this.menuData = item.children;
        } else if (item.children) {
          item.children.forEach(item1 => {
            if (item1.id == mId) {
              this.menuData = [];
              this.menuData = item1.children;
            } else {
              if (item1.children != undefined) {
                this.getMenuData(mId, item1.children);
              }
            }
          });
        }
      });
    },
    getParentNode(url, data) {
      url = url.split("?")[0];
      for (let index in data) {
        if (data[index].url == url) {
          return data[index].id;
        } else if (data[index].children) {
          let data1 = data[index].children;
          for (let inx in data1) {
            if (data1[inx].url == url) {
              return data1[inx].id;
            } else {
              if (data1[inx].children != undefined) {
                this.getMenuData(url, data1[inx].children);
              }
            }
          }
        }
      }
    },
    getChildNode(menuData) {
      for (let i = 0; i < menuData.length; i++) {
        if (!menuData[i].children && menuData[i].url) {
          this.navRouter(menuData[i]);
          return true;
        } else if (menuData[i].children) {
          if (this.getChildNode(menuData[i].children)) {
            return true;
          }
        }
      }
    },
    cancelSelect() {
      this.multilevelMenu = false;
      this.$refs.menu.activeIndex = null;
      this.clickIndex = null;
      this.activeIndex = null;
    },
    maskClick() {
      this.multilevelMenu = false;
      this.$refs.menu.activeIndex = this.activeIndex;
      this.clickIndex = this.activeIndex;
      this.$emit("cancelSelect", "headerMenu", this.headerMenuIndex);
    }
  }
};
</script>
