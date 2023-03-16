const {get,postJson,del,putJson,findByPage} = $axios

const api = {
    insert: (data, success,error) => {
        postJson($servers.example+"/aaaa/sysSecUserMarkInfo/insert",data,true,success,error);
    },
    update: (data, success,error) => {
        putJson($servers.example+"/aaaa/sysSecUserMarkInfo/update",data,true,success,error);
    },
    findById: (data, success,error) => {
        get($servers.example+"/aaaa/sysSecUserMarkInfo/findById/"+data,null,true,success,error);
    },
    deleteById: (data, success,error) => {
        del($servers.example+"/aaaa/sysSecUserMarkInfo/deleteById/"+data,null,true,success,error);
    }
};

export default api;