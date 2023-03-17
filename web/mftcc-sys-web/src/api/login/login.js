const { postJsonNoLoading, postJson } = $axios

const api = {
  login: (data, success, error) => {
    postJsonNoLoading("/" + $servers.sys + "/login", data, true, success, error);
  },
  loginOut: (data, success, error) => {
    postJsonNoLoading("/" + $servers.sys + "/loginOut", data, true, success, error);
  },
  validatePassword: (data, success, error) => {
    postJsonNoLoading("/" + $servers.sys + "/sys/sysUser/validatePassword", data, true, success, error);
  },
  validateNewPassword: (data, success, error) => {
    postJsonNoLoading("/" + $servers.sys + "/sys/sysSecAuditConfig/validatePWAjax", data, true, success, error);
  },
  updatePassword: (data, success, error) => {
    postJson("/" + $servers.sys + "/sys/sysUser/updatePassword", data, true, success, error);
  }
};

export default api;