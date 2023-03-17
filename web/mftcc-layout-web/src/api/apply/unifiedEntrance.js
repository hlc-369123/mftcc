const {get,postJson,del,putJson,findByPage} = $axios

const api = {
    findUserByOpNo: (data, success) => {
        get($servers.guarantee + "/apply/guaranteeSummary/findUserByOpNo/" + data,null,true,success);
    },
    getBusSummaryData: (data, success) => {
        get($servers.guarantee + "/apply/guaranteeSummary/getBusSummaryData/" + data,null,true,success);
    },
    findLatestUnreadList: (data, success) => {
        get($servers.msg + "/messages/msgMessages/findLatestUnreadList/" + data,null,true,success);
    },
    queryUnreadMsgCountsByType: (data, success) => {
        get($servers.msg + "/messages/msgMessages/queryUnreadMsgCountsByType/" + data,null,true,success);
    },
    findNextById: (data, success) => {
        get($servers.msg + "/messages/msgMessages/findNextById/" + data,null,true,success);
    },
    findById: (data, success) => {
      get($servers.msg + "/messages/msgMessages/findById/" + data,null,true,success);
    },
    findMsgAndRepayListById: (data, success) => {
        get($servers.msg + "/messages/msgMessages/findMsgAndRepayListById/" + data,null,true,success);
    },
    sendReplyMsg: (data, success,error) => {
      postJson($servers.msg + "/messages/msgMessages/sendReplyMsg",data,true,success,error);
    },
    findNextUnreadById: (data, success) => {
        get($servers.msg + "/messages/msgMessages/findNextUnreadById/" + data,null,true,success);
    },
    msgBeanRead: (data, success) => {
        get($servers.msg + "/messages/msgMessages/msgBeanRead/" + data,null,true,success);
    },
    getPledgeReceivableWarningData: (data, success) => {
        postJson($servers.pledge + "/receivable/pledgeReceivable/getReceivableWarningByPage",data,true,success);
    },
};

export default api;
