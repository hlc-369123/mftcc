const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  demoCusInfoFindById: (cusno, success, error) => {
    get("/" + $servers.sys + "/demo/demoCusInfo/findById/" + cusno, null, true, success, error);
  },
  demoCusInfoUpdate: (data, success, error) => {
    postJson("/" + $servers.sys + "/demo/demoCusInfo/update", data, true, success, error);
  },
};

export default api;