const { get, postJson, del, putJson, findByPage } = $axios

const api = {
  findByPage: (data, success) => {
    findByPage("/" + $servers.sys + "/sys/sysMsgMold/findByPage", data, true, res => {
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
    postJson("/" + $servers.sys + "/sys/sysMsgMold/insert", data, true, success);
  },
  update: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysMsgMold/update", data, true, success);
  },
  findById: (data, success) => {
    get("/" + $servers.sys + "/sys/sysMsgMold/findById/" + data, null, true, success);
  },
  deleteById: (data, success) => {
    del("/" + $servers.sys + "/sys/sysMsgMold/deleteById/" + data, null, true, success);
  },
  delBatch: (data, success) => {
    del("/" + $servers.sys + "/sys/sysMsgMold/delBatch/" + data, null, true, success);
  },
  getList: (data, success) => {
    get("/" + $servers.sys + "/sys/sysMsgMold/getList/" + data, null, true, success);
  }
};

export default api;