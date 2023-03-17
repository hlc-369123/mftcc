const { postJsonNoLoading, postJson,get } = $axios;

const api = {
  getList: (data, success, error) => {
    postJsonNoLoading("/" + $servers.sys + "/sys/sysInitStyle/findList", data, true, success, error);

  },
  insert:(data,success,error) =>{
    postJsonNoLoading("/" + $servers.sys + "/sys/sysInitStyle/insert", data, true, success, error);
  }
};

export default api;
