<template>
  <el-container>
    <el-header style="height: auto">
      <el-page-header @back="$router.back(-1)" content="权限配置">
      </el-page-header>
    </el-header>
    <!-- <el-main class="center-content"> -->
    <auth-def
      :editAuth="true"
      :roleId="roleId"
      ref="authDef"
      style="height: calc(100% - 101px);"
    ></auth-def>
    <!-- </el-main> -->
    <el-footer style="margin:0 auto;">
      <el-button type="primary" @click="saveAuth">保存</el-button>
    </el-footer>
  </el-container>
</template>
<script>
import authDef from "@/views/sys/auth/authDef";
import api from "@/api/sys/auth";
export default {
  name: "authConfig",
  title: "功能权限配置",
  data() {
    return {
      flag: false,
      roleId: ""
    };
  },
  components: {
    authDef
  },
  created() {
    this.roleId = this.$route.query.roleId;
  },
  methods: {
    saveAuth() {
      let graph = this.$refs.authDef.graph;
      let rootData = graph.findDataById("0");
      let menuData = [];
      let viewData = [];
      let modelData = [];
      this.save(rootData.children, menuData, viewData, modelData);
      let authData = {};
      authData.menu = menuData;
      authData.view = viewData;
      authData.model = modelData;
      authData.roleId = this.roleId;
      api.setAuth(authData, res => {
        if (res.code == 0) {
          this.$message.success("操作成功！");
        } else {
          this.$message.error(res.msg);
        }
      });

      /* let nodes = graph.findAllByState('node', 'selected');
            let authData = {};
            let menuData = [];
            let viewData = [];
            let modelData = [];
            let roleId = this.roleId;
            for(let node of nodes){
                let model = node.getModel();
                let type = model.type;
                let nodeId = model.nodeId;
                let data = {};
                switch(type){
                    case "menu":
                        data = {
                            menuId: nodeId,
                            roleId:roleId
                        };
                        menuData.push(data);
                        break;
                    case "view":
                        data = {
                            viewId: nodeId,
                            roleId:roleId
                        };
                        viewData.push(data);
                        break
                    case "viewMenu":
                    case "viewBtn":
                        data = {
                            viewId: model.viewId,
                            viewCmptId: nodeId,
                            roleId:roleId
                        };
                        viewData.push(data);
                        break;
                    case "model":
                        data = {
                            modelId: nodeId,
                            roleId: roleId
                        };
                        modelData.push(data);
                        break;
                }
            }
            authData.menu = menuData;
            authData.view = viewData;
            authData.model = modelData;
            authData.roleId = roleId;
            api.setAuth(authData, res => {
                if(res.code == 0){
                    this.$message.success("操作成功！");
                }else{
                    this.$message.error(res.msg);
                }
            }); */
    },
    save(children, menuData, viewData, modelData) {
      for (let model of children) {
        if (!model.selected) {
          continue;
        }
        let type = model.type;
        let nodeId = model.nodeId;
        let data = {};
        switch (type) {
          case "menu":
            data = {
              menuId: nodeId,
              roleId: this.roleId
            };
            menuData.push(data);
            break;
          case "view":
            data = {
              viewId: nodeId,
              roleId: this.roleId
            };
            viewData.push(data);
            break;
          case "viewMenu":
          case "viewBtn":
            data = {
              viewId: model.viewId,
              viewCmptId: nodeId,
              roleId: this.roleId
            };
            viewData.push(data);
            break;
          case "model":
            data = {
              modelId: nodeId,
              roleId: this.roleId
            };
            modelData.push(data);
            break;
        }
        this.save(model.children, menuData, viewData, modelData);
      }
    }
  }
};
</script>
