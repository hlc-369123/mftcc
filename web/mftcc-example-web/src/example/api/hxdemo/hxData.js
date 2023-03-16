const {get,postJson,del,putJson,findByPage} = $axios

const api = {
    insert: (data, success,error) => {
        postJson($servers.example+"/hxdemo/hxData/insert",data,true,success,error);
    },
    update: (data, success,error) => {
        putJson($servers.example+"/hxdemo/hxData/update",data,true,success,error);
    },
    findById: (data, success,error) => {
        get($servers.example+"/hxdemo/hxData/findById/"+data,null,true,success,error);
    },
    deleteById: (data, success,error) => {
        del($servers.example+"/hxdemo/hxData/deleteById/"+data,null,true,success,error);
    },
    startProcess: (data, success,error) => {
        postJson($servers.example+"/hxdemo/hxData/startProcess",data,true,success,error);
    },
    doCommit: (data, success,error) => {
        postJson($servers.example+"/hxdemo/hxData/doCommit",data,true,success,error);
    }
};

export default api;
