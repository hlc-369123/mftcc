const {get,postJson,del,putJson,findByPage} = $axios

const api = {
    insert: (data, success,error) => {
        postJson($servers.example+"/demooo/sysView/insert",data,true,success,error);
    },
    update: (data, success,error) => {
        putJson($servers.example+"/demooo/sysView/update",data,true,success,error);
    },
    findById: (data, success,error) => {
        get($servers.example+"/demooo/sysView/findById/"+data,null,true,success,error);
    },
    deleteById: (data, success,error) => {
        del($servers.example+"/demooo/sysView/deleteById/"+data,null,true,success,error);
    }
};

export default api;