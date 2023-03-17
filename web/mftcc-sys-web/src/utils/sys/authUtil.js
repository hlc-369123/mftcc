
import uuid from 'uuid'
let modelImportData = {};
let convertAuthData = (menuList, viewList, viewCmptList, modelList) => {
    for(let item of menuList){
        item.nodeId = item.menuId;
        item.upNodeId = item.upMenuId;
        item.nodeType = "menu";
    }
    for(let item of viewList){
        item.nodeId = item.viewId;
        item.upNodeId = item.upCmptId;
        item.nodeType = "view";
    }
    for(let item of viewCmptList){
        item.nodeId = item.viewCmptId;
        if(item.upViewCmptId == 0){
            item.upNodeId = item.viewId;
        }else{
            item.upNodeId = item.upViewCmptId;
        }
        item.nodeType = "viewCmpt";
    }

    let map = {};
    modelImportData = {};
    for(let item of modelList){
        item.nodeId = item.modelId;
        item.upNodeId = item.upCmptId;
        item.nodeType = "model";

        if(map[item.nodeId]){
            if(modelImportData[item.nodeId]){
                modelImportData[item.nodeId] += 1;
            }else{
                modelImportData[item.nodeId] = 1;
            }
        }else{
            map[item.nodeId] = item;
        }
    }
    let data = menuList.concat(viewList).concat(viewCmptList).concat(modelList);
    data = commonConvertData(data);
    return data;
}

let commonConvertData = (data) => {
    let result = [];
    for (let obj of data) {
        if(isParent(obj.nodeId, data)){
            let item = createData(obj);
            let children = getchilds(item.nodeId, data);
            if (children.length > 0) {
                item.children = item.children.concat(children);
            }
            result.push(item);
        }
    }
    for(let i in result){
        let children = result[i].children;
        if(children.length>0){
            result[i].collapsed = false;
            break;
        }
    }
    return result;
}
//获取子节点
let getchilds = (id, array) => {
    let childs = [];
    for (let arr of array) {
        if (arr.upNodeId == id) {
            let item = createData(arr);
            let children = getchilds(item.nodeId, array);
            if (children.length > 0) {
                item.children = item.children.concat(children);
            }
            childs.push(item);
        }
    }
    return childs;
}

let createData = (obj) => {
    let type = obj.nodeType;
    let item = {};
    switch(type){
        case "menu":
            item = {
                id:uuid.v4(),
                nodeId: obj.menuId,
                text: obj.menuName,
                type: "menu",
                collapsed : true,
                icon: obj.menuIcon,
                menuUrl: obj.menuUrl,
                newTab: obj.newTab,
                selected: obj.selected,
                sn: obj.sn,
                sts: obj.sts,
                children: []
            };
            break;
        case "view":
            item = {
                id:uuid.v4(),
                nodeId: obj.viewId,
                upCmptId: obj.upCmptId,
                text: obj.viewName,
                type: "view",
                collapsed : true,
                selected: obj.selected,
                sn: obj.sn,
                sts: obj.sts,
                children: []
            };
            break;
        case "viewCmpt":
            if(obj.viewCmptTyp == "1"){
                item = {
                    id:uuid.v4(),
                    nodeId:obj.viewCmptId,
                    viewId: obj.viewId,
                    menuUrl: obj.menuUrl,
                    upViewCmptId: obj.upViewCmptId,
                    viewCmptTyp: obj.viewCmptTyp,
                    refreshFlg: obj.refreshFlg,
                    loadFlg: obj.loadFlg,
                    text: obj.viewCmptName,
                    type: "viewMenu",
                    collapsed :true,
                    selected: obj.selected,
                    sn: obj.sn,
                    sts: obj.sts,
                    children: []
                };
            }else if(obj.viewCmptTyp == "2"){
                item = {
                    id:uuid.v4(),
                    nodeId:obj.viewCmptId,
                    viewId: obj.viewId,
                    viewCmptTyp: obj.viewCmptTyp,
                    btnMthd: obj.btnMthd,
                    expr: obj.expr,
                    text: obj.viewCmptName,
                    type: "viewBtn",
                    collapsed :true,
                    selected: obj.selected,
                    sn: obj.sn,
                    sts: obj.sts,
                    children: []
                };
            }
            break;
        case "model":
            item = {
                id:uuid.v4(),
                nodeId:obj.modelId,
                upCmptId: obj.upCmptId,
                modelTyp: obj.modelTyp,
                modelRmk: obj.modelRmk,
                expr: obj.expr,
                dataAuth: obj.dataAuth,
                btnKey: obj.btnKey,
                mapperPath: obj.mapperPath,
                text: obj.modelName,
                type: "model",
                selected: obj.selected,
                collapsed :true,
                children: []
            };
            break;
    }
    return item;
}

let isParent = (id, data) => {
    let result = true;
    for(let item of data){
        if(item.nodeId == id){
            for(let itm of data){
                if(item.upNodeId == itm.nodeId){
                    result = false;
                    break;
                }else{
                    result = true;
                }
            }
            break;
        }
    }
    return result;
}

let findUpIsImport = (graph, model,importId) => {
    let flag = false;
    if(model.type == "model"){
        if(model.nodeId == importId || model.upCmptId == importId){
            flag = true;
        }else{
            let edge = graph.find('edge', node => {
                let targetNode = node.get('model').target.getModel();
                if(targetNode.id === model.id){
                    return true;
                }
            });
            let parentModel = edge.getModel().source.getModel();
            flag = findUpIsImport(graph,parentModel,importId);
        }
    }else{
        flag = false;
    }
    return flag;
}

let findChildIsImport = (graph, model, importId) => {
    let flag = false;
    if(model.nodeId == importId){
        return true;
    }
    let importNode = graph.find('node', node => {
        return node.get('model').nodeId === importId;
    });
    for(let item of importNode.get('model').children){
        flag = findChildIsImport(graph, model, item.nodeId);
        if(flag){
            return flag;
        }
    }
    return flag;
}

let getDeleteIds = (node) => {
    let nodes = [];
    let imports = [];
    if(modelImportData[node.nodeId] && modelImportData[node.nodeId]!=0){
        imports.push(node.nodeId+","+node.upCmptId);
        modelImportData[node.nodeId] -= 1;
        if(modelImportData[node.nodeId] == 0){
            delete modelImportData[node.nodeId];
        }
    }else{
        nodes.push(node);
        for(let item of node.children){
            let obj = getDeleteIds(item);
            nodes = nodes.concat(obj.nodes);
            imports = imports.concat(obj.imports);
        }
    }
    return {
        nodes:nodes,
        imports:Array.from(new Set(imports))
    }
}

let childSelected = (_this,modelData,state) => {
    let node = _this.graph.findById(modelData.id);
    if(node){
        let checkbox = node.get("group").findByClassName('checkbox-image');
        checkbox.attr('img', state ? _this.SELECTED_ICON : _this.UNSELECTED_ICON);
    }
    modelData.selected = state;
    for(let childNode of modelData.children){
        childSelected(_this,childNode,state);
    }
}

let parentSelected = (_this,modelData,state) => {
    if(modelData.id == "0"){
        return;
    }
    let node = _this.graph.findById(modelData.id);
    let checkbox = node.get("group").findByClassName('checkbox-image');
    checkbox.attr('img', state ? _this.SELECTED_ICON : _this.UNSELECTED_ICON);
    modelData.selected = state;
    let edge = node.getInEdges();
    if(edge.length>0){
        modelData = _this.graph.findDataById(edge[0].getModel().source);
        parentSelected(_this,modelData,state);
    }
}

let setAuthByRole = (_this,res,data) => {
    // res.menuList,res.viewList,res.viewCmptList,res.modelList;
    for(let menu of data.menuList){
        for(let resMenu of res.menuList){
            if(menu.menuId == resMenu.menuId){
                resMenu.selected = true;
                break;
            }
        }
    }
    for(let model of data.modelList){
        for(let resModel of res.modelList){
            if(model.modelId == resModel.modelId){
                resModel.selected = true;
                break;
            }
        }
    }
    for(let view of data.viewList){
        for(let resView of res.viewList){
            if(view.viewId == resView.viewId){
                resView.selected = true;
                break;
            }
        }
        for(let resViewCmpt of res.viewCmptList){
            if(view.viewCmptId == resViewCmpt.viewCmptId){
                resViewCmpt.selected = true;
                break;
            }
        }
    }
    /* for(let menu of data.menuList){
        let node = _this.graph.find('node', node => {
            return node.get('model').nodeId === menu.menuId && node.get('model').type == "menu";
        });
        node.setState("selected",true);
        let checkbox = node.get("group").findByClassName('checkbox-image');
        checkbox.attr('img', _this.SELECTED_ICON);
    }
    for(let model of data.modelList){
        let node = _this.graph.find('node', node => {
            return node.get('model').nodeId === model.modelId && node.get('model').type == "model";
        });
        node.setState("selected",true);
        let checkbox = node.get("group").findByClassName('checkbox-image');
        checkbox.attr('img', _this.SELECTED_ICON);
    }
    for(let view of data.viewList){
        let node = _this.graph.find('node', node => {
            if(node.get('model').nodeId === view.viewId && node.get('model').type == "view"){
                return true;
            }else if(node.get('model').nodeId === view.viewCmptId && (node.get('model').type == "viewMenu" || node.get('model').type == "viewBtn")){
                return true;
            }else{
                return false;
            }
        });
        node.setState("selected",true);
        let checkbox = node.get("group").findByClassName('checkbox-image');
        checkbox.attr('img', _this.SELECTED_ICON);
    } */

}

let dataAuthList = [
    {
        id:"1",
        text:"本人",
        field:"reg_op_no"
    },
    {
        id:"4",
        text:"本级",
        field:"reg_br_no"
    },
    {
        id:"7",
        text:"本级及下级",
        field:"reg_br_no"
    },
    {
        id:"10",
        text:"本法人",
        field:"corp_id"
    },
    {
        id:"99",
        text:"全部",
        field:""
    }
]

export {
    convertAuthData,
    findUpIsImport,
    findChildIsImport,
    getDeleteIds,
    childSelected,
    parentSelected,
    setAuthByRole,
    dataAuthList
}
