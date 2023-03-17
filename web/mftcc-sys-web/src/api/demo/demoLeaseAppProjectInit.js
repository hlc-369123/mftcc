const { get, postJson, del, putJson, postForm } = $axios

const api = {
  insert: (data, success, error) => {
    postJson($servers.sys + "/demo/demoLeaseAppProjectInit/insert", data, true, success, error);
  },
  update: (data, success, error) => {
    putJson($servers.sys + "/demo/demoLeaseAppProjectInit/update", data, true, success, error);
  },
  findById: (data, success, error) => {
    get($servers.sys + "/demo/demoLeaseAppProjectInit/findById/" + data, null, true, success, error);
  },
  deleteById: (data, success, error) => {
    del($servers.sys + "/demo/demoLeaseAppProjectInit/deleteById/" + data, null, true, success, error);
  },
  startProcessWithAppContents: (data, success) => {
    postForm("/mftcc-flowable-server/flowable/startProcessWithAppContentsDemo", data, true, success);
  },
  doCommitDemo: (data, success) => {
    postForm("/mftcc-flowable-server/flowable/doCommit4Test", data, true, success);
  },
  sendMessage: (data, success) => {
    postJson($servers.sys + "/demo/demoLeaseAppProjectInit/sendMessage", data, true, success);
  },
};

export default api;