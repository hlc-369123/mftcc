const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  findByPage: (data, success) => {
    findByPage("/" + $servers.sys + "/sys/sysParmDic/findByPage", data, true, res => {
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
  findByKeyName: (data, success) => {
    get("/" + $servers.sys + "/sys/sysParmDic/findByKeyName/" + data, null, true, success);
  },
  insert: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysParmDic/insert", data, true, success);
  },
  update: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysParmDic/update", data, true, success);
  },
  findById: (data, success) => {
    get("/" + $servers.sys + "/sys/sysParmDic/findById/" + data, null, true, success);
  },
  deleteById: (data, success) => {
    del("/" + $servers.sys + "/sys/sysParmDic/deleteById/" + data, null, true, success);
  },
  initCaChe: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysParmDic/initCaChe", data, true, success);
  }
};

export default api;