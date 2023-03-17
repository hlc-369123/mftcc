const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  insert: (data, success, error) => {
    postJson("/" + $servers.sys + "/sys/sysSecAuditConfig/insert", data, true, success, error);
  },
  update: (data, success, error) => {
    putJson("/" + $servers.sys + "/sys/sysSecAuditConfig/update", data, true, success, error);
  },
  findById: (data, success, error) => {
    get("/" + $servers.sys + "/sys/sysSecAuditConfig/findById/" + data, null, true, success, error);
  },
  deleteById: (data, success, error) => {
    del("/" + $servers.sys + "/sys/sysSecAuditConfig/deleteById/" + data, null, true, success, error);
  }
};

export default api;