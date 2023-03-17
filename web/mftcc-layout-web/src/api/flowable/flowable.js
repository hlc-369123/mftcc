const { get, postJson, postForm, del, putJson, findByPage } = $axios;
const api = {
  getBizList: (data, success) => {
    postJson($servers.flowable + "/appcenter/getBizList", data, true, success);
  },
  getColList: (bizMark, success) => {
    get(
      $servers.flowable + "/appcenter/getColList/" + bizMark,
      null,
      true,
      success
    );
  },
  getSysTaskInfo: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getSysTaskInfo",
      data,
      true,
      success
    );
  },
  getTimeLine: (data, success) => {
    postJson($servers.flowable + "/bpmn/getTimeLine", data, true, success);
  },
  simulationByTraceNo: (data, success) => {
    postJson(
      $servers.flowable + "/bpmn/simulationByTraceNo",
      data,
      true,
      success
    );
  },
  getApprovalDetail: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getApprovalDetail",
      data,
      true,
      success
    );
  },
  needOperated: (data, success) => {
    postForm($servers.flowable + "/flowable/needOperated", data, true, success);
  },
  doCommitDemo: (data, success) => {
    postForm($servers.flowable + "/flowable/doCommitDemo", data, true, success);
  },
  recallAndCancel: (data, success) => {
    postForm(
      $servers.flowable + "/flowable/recallAndCancel",
      data,
      true,
      success
    );
  },
  getFinishNodeList: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getFinishNodeList",
      data,
      true,
      success
    );
  },
  getFinishParentNodeList: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getFinishParentNodeList",
      data,
      true,
      success
    );
  },
  getNextAllNodeList: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getNextAllNodeList",
      data,
      true,
      success
    );
  },
  getCountForToDoTask: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getCountForToDoTask",
      data,
      true,
      success
    );
  },
  getCountListForToDoTask: (data, success) => {
    postJson(
      $servers.flowable + "/appcenter/getCountListForToDoTask",
      data,
      true,
      success
    );
  },
  designate: (data, success) => {
    postForm($servers.flowable + "/flowable/designate", data, true, success);
  },
  batchComplete: (data, success) => {
    postForm(
      $servers.flowable + "/flowable/batchComplete",
      data,
      true,
      success
    );
  },
  getBrTree: (data, success, error) => {
    postJson($servers.flowable + "/br/listForTree", data, true, success, error);
  },
  getUserData: (data, success, error) => {
    postJson(
      $servers.flowable + "/user/listByBrNo",
      data,
      true,
      success,
      error
    );
  },
  getNextUserList: (data, success) => {
    postJson($servers.flowable + "/user/pageByUserId", data, true, success);
  },
  findTaskById: (data, success) => {
    postForm($servers.flowable + "/flowable/findTaskById", data, true, success);
  },
  findTemplateByIds: (data, success) => {
    postJson($servers.flowable + "/bizconfig/template/findByIds", data, true, success);
  },
  //获取流程待办列表
  getTaskInfoList: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getTaskInfoListByOpNo",
      data,
      true,
      success
    );
  },
  //获取流程已办列表
  getHisTaskInfoList: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getHisTaskInfoListByOpNo",
      data,
      true,
      success
    );
  },
  //获取流程列表数据(条数)
  getTaskInfoListBytotal: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getHisTaskInfoListBytotal",
      data,
      true,
      success
    );
  },

  findById: (data, success, error) => {
    get(
      $servers.investment + "/wkf/wkfBusflowable/findById/" + data,
      null,
      true,
      success,
      error
    );
  },

  //修改待办置顶为1
  updateToppingOne: (data, success, error) => {
    putJson(
      $servers.investment + "/wkf/wkfBusflowable/updateToppingOne",
      data,
      true,
      success,
      error
    );
  },

  getStayTableDataStatistics: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getStayTableDataStatistics",
      data,
      true,
      success
    );
  },

  //获取待办任务列表数据(业务)
  getTaskDealInfoList: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getTaskDealInfoList",
      data,
      true,
      success
    );
  },
  //获取待办已办右侧年份搜索框
  getTaskYearCountByTime: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getTaskYearCountByTime",
      data,
      true,
      success
    );
  },

  //根据年份 月份 日期分组统计条数
  getTaskMonthCountByTime: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getTaskMonthCountByTime",
      data,
      true,
      success
    );
  },

  //获取已办任务列表数据(业务)
  getTaskDealHisInfoList: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getTaskDealHisInfoList",
      data,
      true,
      success
    );
  },
  //修改待办置顶为0
  updateToppingZero: (data, success, error) => {
    putJson(
      $servers.investment + "/wkf/wkfBusflowable/updateToppingZero",
      data,
      true,
      success,
      error
    );
  },

  //修改待办收藏为0
  updateCollectionZero: (data, success, error) => {
    putJson(
      $servers.investment + "/wkf/wkfBusflowable/updateCollectionZero",
      data,
      true,
      success,
      error
    );
  },
  //修改待办收藏为1
  updateCollectionOne: (data, success, error) => {
    putJson(
      $servers.investment + "/wkf/wkfBusflowable/updateCollectionOne",
      data,
      true,
      success,
      error
    );
  },
  //修改已办，待办表单名称
  updateWkfInvestment: (data, success, error) => {
    putJson($servers.investment + "/wkf/wkfBusflowable/updateWkfInvestment", data, true, success, error);
  },
  //获取关注任务列表数据(业务)
  getFocusOnTasksList: (data, success) => {
    postJson(
      $servers.investment + "/wkf/wkfBusflowable/getFocusOnTasksList",
      data,
      true,
      success
    );
  },
};

export default api;
