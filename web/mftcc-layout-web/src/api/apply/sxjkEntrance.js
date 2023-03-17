const {get,postJson,del,putJson,findByPage} = $axios

const api = {
    getProjectDataStatistics: (data, success) => {
        get($servers.investment + "/projectSelection/busProcessProjectEquity/getProjectDataStatistics/" + data,null,true,success);
    },

     getProjectTableDataStatistics: (data, success) => {
        get($servers.investment + "/projectSelection/busProcessProjectEquity/getProjectTableDataStatistics/" + data,null,true,success);
    },
   
};

export default api;
