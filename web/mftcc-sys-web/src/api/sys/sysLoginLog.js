const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  insert: (data, success, error) => {
    postJson("/" + $servers.sys + "/sys/sysLoginLog/insert", data, true, success, error);
  },
  update: (data, success, error) => {
    putJson("/" + $servers.sys + "/sys/sysLoginLog/update", data, true, success, error);
  },
  findById: (data, success, error) => {
    get("/" + $servers.sys + "/sys/sysLoginLog/findById/" + data, null, true, success, error);
  },
  deleteById: (data, success, error) => {
    del("/" + $servers.sys + "/sys/sysLoginLog/deleteById/" + data, null, true, success, error);
  }
};

export default api;