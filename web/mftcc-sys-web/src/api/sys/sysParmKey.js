const { get, postJson, del, putJson, findByPage, downloadFile } = $axios

const api = {
  findByPage: (data, success) => {
    findByPage("/" + $servers.sys + "/sys/sysParmKey/findByPage", data, true, res => {
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
    postJson("/" + $servers.sys + "/sys/sysParmKey/insert", data, true, success);
  },
  update: (data, success) => {
    putJson("/" + $servers.sys + "/sys/sysParmKey/update", data, true, success);
  },
  findById: (data, success) => {
    get("/" + $servers.sys + "/sys/sysParmKey/findById/" + data, null, true, success);
  },
  deleteById: (data, success) => {
    del("/" + $servers.sys + "/sys/sysParmKey/deleteById/" + data, null, true, success);
  },
  downloadFile: (data) => {
    downloadFile("/" + $servers.sys + "/sys/sysParmKey/getParmDicFile", data, true);
  },
  findMoleId: (data, success) => {
    postJson("/" + $servers.sys + "/sys/sysParmDic/findMoleId", data, true, success);
  }
};

export default api;