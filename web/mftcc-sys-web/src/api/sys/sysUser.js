const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  findByPage: (data, success) => {
    findByPage("/" + $servers.sys + "/sys/sysUser/findByPage", data, true, res => {
      if (res.code == 0) {
        let list = res.list;
        let total = list.total; // 页面总数
        let size = list.size; // 每页数量
        let current = list.current;// 当前页
        let tableData = list.records;
        success(tableData, current, size, total);
      }
    });
  },
  insert: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysUser/insert", data, true, success);
  },
  insertEnable: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysUser/insertEnable", data, true, success);
  },
  update: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysUser/update", data, true, success);
  },
  updateStart: (data, success) => {
    get("/" + $servers.sys + "/sys/sysUser/updateStart/" + data, null, true, success);
  },
  updateLogout: (data, success) => {
    get("/" + $servers.sys + "/sys/sysUser/updateLogout/" + data, null, true, success);
  },
  findById: (data, success) => {
    get("/" + $servers.sys + "/sys/sysUser/findById/" + data, null, true, success);
  },
  deleteById: (data, success) => {
    del("/" + $servers.sys + "/sys/sysUser/deleteById/" + data, null, true, success);
  },
  resetPassword: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysUser/resetPassword", data, true, success);
  },
  unlock: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysSecUserMarkInfo/unlock", data, true, success);
  }
};

export default api;