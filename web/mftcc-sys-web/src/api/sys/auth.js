const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  getAll: (success) => {
    get("/" + $servers.sys + "/sys/sysAuth/getAll", null, true, success);
  },
  addMenu: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysMenu/insert", data, true, success);
  },
  updateMenu: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysMenu/update", data, true, success);
  },
  deleteNodes: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysAuth/deleteNodes", data, true, success);
  },
  addView: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysView/insert", data, true, success);
  },
  updateView: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysView/update", data, true, success);
  },
  addViewCmpt: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysViewCmpt/insert", data, true, success);
  },
  updateViewCmpt: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysViewCmpt/update", data, true, success);
  },
  addModel: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysModel/insert", data, true, success);
  },
  updateModel: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysModel/update", data, true, success);
  },
  findAllModelList: (success) => {
    get("/" + $servers.sys + "/sys/sysModel/findAllList", null, true, success);
  },
  addImportModel: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysImport/insertModel", data, true, success);
  },
  setAuth: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysAuth/auth", data, true, success);
  },
  getAuthByRole: (roleId, success) => {
    get("/" + $servers.sys + "/sys/sysAuth/getAuthByRole/" + roleId, null, true, success);
  },
  addDataAuth: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysDataAuth/insert", data, true, success);
  },
  getDataAuthByModelId: (modelId, roleId, success) => {
    get("/" + $servers.sys + "/sys/sysDataAuth/getDataAuthByModelId/" + modelId + "/" + roleId, null, true, success);
  }
};

export default api;