(window.webpackJsonpmftcc_layout_web=window.webpackJsonpmftcc_layout_web||[]).push([[7],{"5mVP":function(n,e,t){"use strict";t.r(e);var A=t("IJdV"),s={name:"ZqUserList",title:"人员列表",data:function(){return{tableData:[],pageNo:1,pageSize:10,pageTotal:0,singlePageHide:!0,pageSizes:[10,20,50,100]}},inject:["dynamicQuery"],props:{type:{type:String,default:""}},created:function(){this.getTableData()},methods:{refresh:function(){this.getTableData()},getTableData:function(){var n=this,e="task";"finished"==this.type&&(e="hisTask");var t={bizMark:"pocPreInfo",dynamicQuery:this.dynamicQuery,opNo:this.$store.getters.user.opNo,pageNo:this.pageNo,pageSize:this.pageSize,queryType:e};A.a.getSysTaskInfo(t,(function(e){0==e.code?(n.tableData=e.list.records,n.pageNo=e.list.current,n.pageSize=e.list.size,n.pageTotal=e.list.total):n.$message.error(e.msg)}))},handleSizeChange:function(n){this.pageSize=n,this.getTableData()},handleCurrentChange:function(n){this.pageNo=n,this.getTableData()},handleCommand:function(n){this.$emit("command",n)},view:function(n){this.$emit("command",n)}}},i=(t("iQVM"),t("KHd+")),o=Object(i.a)(s,(function(){var n=this,e=n.$createElement,t=n._self._c||e;return t("div",{staticClass:"zq-user-lists"},[n.tableData.length>0?t("ul",{staticClass:"zq-user-list"},n._l(n.tableData,(function(e){return t("li",{key:e.id,staticClass:"zq-user-item"},[t("div",{staticClass:"zq-user-desc"},[n._v(n._s(e.APP_DSC))]),n._v(" "),t("div",{staticClass:"zq-user-time"},[t("i",{staticClass:"el-icon-time"}),n._v(n._s(e.CREATE_TIME)+"\n      ")]),n._v(" "),t("div",{class:"zq-user-status status-1"},[t("i",{class:"zq-user-status-icon status-icon-1"}),n._v(n._s(e.TASK_NAME)+"\n      ")]),n._v(" "),t("div",{staticClass:"zq-user-btns"},["finished"==n.type?[t("span",{staticClass:"zq-user-btn",on:{click:function(t){return n.view({type:"query",item:e})}}},[n._v("查看")]),n._v(" "),t("el-dropdown",{attrs:{size:"mini",trigger:"click",placement:"bottom"},on:{command:n.handleCommand}},[t("span",{staticClass:"zq-user-btn el-icon-more"}),n._v(" "),t("el-dropdown-menu",{staticClass:"zq-dropdown",attrs:{slot:"dropdown"},slot:"dropdown"},[t("el-dropdown-item",{attrs:{command:{type:"flowCancel",item:e}}},[n._v("撤销")]),n._v(" "),t("el-dropdown-item",{attrs:{command:{type:"flowRecall",item:e}}},[n._v("召回")])],1)],1)]:[t("span",{staticClass:"zq-user-btn",on:{click:function(t){return n.view({type:"app",item:e})}}},[n._v("审批")]),n._v(" "),t("el-dropdown",{attrs:{size:"mini",trigger:"click",placement:"bottom"},on:{command:n.handleCommand}},[t("span",{staticClass:"zq-user-btn el-icon-more"}),n._v(" "),t("el-dropdown-menu",{staticClass:"zq-dropdown",attrs:{slot:"dropdown"},slot:"dropdown"},[t("el-dropdown-item",{attrs:{command:{type:"design",item:e}}},[n._v("指派")])],1)],1)]],2)])})),0):t("el-empty",{attrs:{description:"暂无数据"}}),n._v(" "),t("el-pagination",{attrs:{"hide-on-single-page":n.singlePageHide,"current-page":n.pageNo,"page-sizes":n.pageSizes,"page-size":n.pageSize,layout:"total, sizes, prev, pager, next",total:n.pageTotal},on:{"size-change":n.handleSizeChange,"current-change":n.handleCurrentChange}})],1)}),[],!1,null,"76608667",null);e.default=o.exports},IMVD:function(n,e,t){(n.exports=t("JPst")(!0)).push([n.i,"\n.zq-user-lists[data-v-76608667] {\n  margin: 0;\n  background-color: #ffffff;\n  padding: 0 10px;\n  width: 100%;\n}\n.zq-user-list[data-v-76608667] {\n  margin: 0;\n  padding: 0;\n  list-style: none;\n}\n.zq-user-item[data-v-76608667] {\n  border: 1px solid #ddd;\n  background-color: #f9fafc;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: horizontal;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: row;\n          flex-direction: row;\n  -ms-flex-wrap: nowrap;\n      flex-wrap: nowrap;\n  -ms-flex-line-pack: center;\n      align-content: center;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: start;\n      -ms-flex-pack: start;\n          justify-content: flex-start;\n  font-size: 12px;\n  height: 54px;\n  margin: 10px 0;\n  border-radius: 5px;\n}\n.zq-user-item[data-v-76608667]:hover {\n  border: 1px solid #0a7cff;\n}\n.zq-user-desc[data-v-76608667] {\n  overflow: hidden;\n  text-overflow: ellipsis;\n  white-space: nowrap;\n  margin: 0 10px;\n  width: 100%;\n}\n.zq-user-item:hover .zq-user-desc[data-v-76608667] {\n  color: #0a7cff;\n  font-weight: bold;\n}\n.zq-user-time[data-v-76608667] {\n  width: 180px;\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  padding: 0 10px;\n  margin: 0 5px;\n  text-align: center;\n  border-left: 1px solid #ddd;\n  border-right: 1px solid #ddd;\n  cursor: pointer;\n}\n.zq-user-time > i[data-v-76608667] {\n  margin-right: 6px;\n}\n.zq-user-status[data-v-76608667] {\n  width: 150px;\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  border-right: 1px solid #ddd;\n}\n.zq-user-status.status-0[data-v-76608667] {\n  color: #0cb1df;\n}\n.zq-user-status.status-1[data-v-76608667] {\n  color: #0a7cff;\n}\n.zq-user-status.status-2[data-v-76608667] {\n  color: #ed6f7a;\n}\n.zq-user-status.status-3[data-v-76608667] {\n  color: #ff8a48;\n}\n.zq-user-status.status-4[data-v-76608667] {\n  color: #6dd400;\n}\n.zq-user-status-icon[data-v-76608667] {\n  width: 6px;\n  height: 6px;\n  border-radius: 50%;\n\n  display: block;\n  margin: 0 10px 0 20px;\n}\n.zq-user-status-icon.status-icon-0[data-v-76608667] {\n  background-color: #0cb1df;\n  outline: 3px solid #0cb1df5e;\n}\n.zq-user-status-icon.status-icon-1[data-v-76608667] {\n  background-color: #0a7cff;\n  outline: 3px solid #409eff7d;\n}\n.zq-user-status-icon.status-icon-2[data-v-76608667] {\n  background-color: #ed6f7a;\n  outline: 3px solid #ed6f7965;\n}\n.zq-user-status-icon.status-icon-3[data-v-76608667] {\n  background-color: #ff8a48;\n  outline: 3px solid #ff8b486e;\n}\n.zq-user-status-icon.status-icon-4[data-v-76608667] {\n  background-color: #6dd400;\n  outline: 3px solid #6dd4005e;\n}\n.zq-user-btns[data-v-76608667] {\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  padding-left: 15px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: horizontal;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: row;\n          flex-direction: row;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: start;\n      -ms-flex-pack: start;\n          justify-content: flex-start;\n}\n.zq-user-btn[data-v-76608667] {\n  border: 1px solid #ddd;\n  padding: 10px;\n  color: #0a7cff;\n  padding: 7px 7px;\n  color: #0a7cff;\n  border-radius: 5px;\n  height: 30px;\n  margin-right: 10px;\n  cursor: pointer;\n  -webkit-user-select: none;\n     -moz-user-select: none;\n      -ms-user-select: none;\n          user-select: none;\n}\n.zq-user-btn.view[data-v-76608667] {\n  padding: 7px 10px;\n}\n.zq-user-btn[data-v-76608667]:hover {\n  border: 1px solid #0a7cff;\n}\n.el-icon-more[data-v-76608667] {\n  -webkit-transform: rotate(90deg);\n          transform: rotate(90deg);\n  height: 28px;\n  padding: 6px 7px;\n}\n","",{version:3,sources:["D:/工作/微服务项目/web/mftcc-layout-web/src/views/toDoflowable/zq/ZqUserList.vue","ZqUserList.vue"],names:[],mappings:";AAwIA;EACE,SAAA;EACA,yBAAA;EACA,eAAA;EACA,WAAA;ACCF;ADCA;EACE,SAAA;EACA,UAAA;EACA,gBAAA;ACCF;ADCA;EACE,sBAAA;EACA,yBAAA;EACA,oBAAA;EAAA,oBAAA;EAAA,aAAA;EACA,8BAAA;EAAA,6BAAA;MAAA,uBAAA;UAAA,mBAAA;EACA,qBAAA;MAAA,iBAAA;EACA,0BAAA;MAAA,qBAAA;EACA,yBAAA;MAAA,sBAAA;UAAA,mBAAA;EACA,uBAAA;MAAA,oBAAA;UAAA,2BAAA;EACA,eAAA;EACA,YAAA;EACA,cAAA;EACA,kBAAA;ACYF;ADVA;EACE,yBAAA;ACYF;ADVA;EACE,gBAAA;EACA,uBAAA;EACA,mBAAA;EACA,cAAA;EACA,WAAA;ACYF;ADVA;EACE,cAAA;EACA,iBAAA;ACYF;ADVA;EACE,YAAA;EACA,oBAAA;MAAA,cAAA;EACA,eAAA;EACA,aAAA;EACA,kBAAA;EACA,2BAAA;EACA,4BAAA;EACA,eAAA;ACaF;ADVA;EACE,iBAAA;ACYF;ADTA;EACE,YAAA;EACA,oBAAA;MAAA,cAAA;EACA,oBAAA;EAAA,oBAAA;EAAA,aAAA;EACA,yBAAA;MAAA,sBAAA;UAAA,mBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,cAAA;ACgBF;ADdA;EACE,cAAA;ACgBF;ADdA;EACE,cAAA;ACgBF;ADdA;EACE,cAAA;ACgBF;ADdA;EACE,cAAA;ACgBF;ADdA;EACE,UAAA;EACA,WAAA;EACA,kBAAA;;EAEA,cAAA;EACA,qBAAA;ACgBF;ADdA;EACE,yBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,yBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,yBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,yBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,yBAAA;EACA,4BAAA;ACgBF;ADdA;EACE,oBAAA;MAAA,cAAA;EACA,kBAAA;EACA,oBAAA;EAAA,oBAAA;EAAA,aAAA;EACA,8BAAA;EAAA,6BAAA;MAAA,uBAAA;UAAA,mBAAA;EACA,yBAAA;MAAA,sBAAA;UAAA,mBAAA;EACA,uBAAA;MAAA,oBAAA;UAAA,2BAAA;AC0BF;ADxBA;EACE,sBAAA;EACA,aAAA;EACA,cAAA;EACA,gBAAA;EACA,cAAA;EACA,kBAAA;EACA,YAAA;EACA,kBAAA;EACA,eAAA;EACA,yBAAA;KAAA,sBAAA;MAAA,qBAAA;UAAA,iBAAA;AC6BF;AD3BA;EACE,iBAAA;AC6BF;AD3BA;EACE,yBAAA;AC6BF;AD3BA;EACE,gCAAA;UAAA,wBAAA;EACA,YAAA;EACA,gBAAA;AC8BF",file:"ZqUserList.vue?vue&type=style&index=0&id=76608667&scoped=true&lang=css&",sourcesContent:["\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n.zq-user-lists {\n  margin: 0;\n  background-color: #ffffff;\n  padding: 0 10px;\n  width: 100%;\n}\n.zq-user-list {\n  margin: 0;\n  padding: 0;\n  list-style: none;\n}\n.zq-user-item {\n  border: 1px solid #ddd;\n  background-color: #f9fafc;\n  display: flex;\n  flex-direction: row;\n  flex-wrap: nowrap;\n  align-content: center;\n  align-items: center;\n  justify-content: flex-start;\n  font-size: 12px;\n  height: 54px;\n  margin: 10px 0;\n  border-radius: 5px;\n}\n.zq-user-item:hover {\n  border: 1px solid #0a7cff;\n}\n.zq-user-desc {\n  overflow: hidden;\n  text-overflow: ellipsis;\n  white-space: nowrap;\n  margin: 0 10px;\n  width: 100%;\n}\n.zq-user-item:hover .zq-user-desc {\n  color: #0a7cff;\n  font-weight: bold;\n}\n.zq-user-time {\n  width: 180px;\n  flex-shrink: 0;\n  padding: 0 10px;\n  margin: 0 5px;\n  text-align: center;\n  border-left: 1px solid #ddd;\n  border-right: 1px solid #ddd;\n  cursor: pointer;\n}\n\n.zq-user-time > i {\n  margin-right: 6px;\n}\n\n.zq-user-status {\n  width: 150px;\n  flex-shrink: 0;\n  display: flex;\n  align-items: center;\n  border-right: 1px solid #ddd;\n}\n.zq-user-status.status-0 {\n  color: #0cb1df;\n}\n.zq-user-status.status-1 {\n  color: #0a7cff;\n}\n.zq-user-status.status-2 {\n  color: #ed6f7a;\n}\n.zq-user-status.status-3 {\n  color: #ff8a48;\n}\n.zq-user-status.status-4 {\n  color: #6dd400;\n}\n.zq-user-status-icon {\n  width: 6px;\n  height: 6px;\n  border-radius: 50%;\n\n  display: block;\n  margin: 0 10px 0 20px;\n}\n.zq-user-status-icon.status-icon-0 {\n  background-color: #0cb1df;\n  outline: 3px solid #0cb1df5e;\n}\n.zq-user-status-icon.status-icon-1 {\n  background-color: #0a7cff;\n  outline: 3px solid #409eff7d;\n}\n.zq-user-status-icon.status-icon-2 {\n  background-color: #ed6f7a;\n  outline: 3px solid #ed6f7965;\n}\n.zq-user-status-icon.status-icon-3 {\n  background-color: #ff8a48;\n  outline: 3px solid #ff8b486e;\n}\n.zq-user-status-icon.status-icon-4 {\n  background-color: #6dd400;\n  outline: 3px solid #6dd4005e;\n}\n.zq-user-btns {\n  flex-shrink: 0;\n  padding-left: 15px;\n  display: flex;\n  flex-direction: row;\n  align-items: center;\n  justify-content: flex-start;\n}\n.zq-user-btn {\n  border: 1px solid #ddd;\n  padding: 10px;\n  color: #0a7cff;\n  padding: 7px 7px;\n  color: #0a7cff;\n  border-radius: 5px;\n  height: 30px;\n  margin-right: 10px;\n  cursor: pointer;\n  user-select: none;\n}\n.zq-user-btn.view {\n  padding: 7px 10px;\n}\n.zq-user-btn:hover {\n  border: 1px solid #0a7cff;\n}\n.el-icon-more {\n  transform: rotate(90deg);\n  height: 28px;\n  padding: 6px 7px;\n}\n","\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n.zq-user-lists {\n  margin: 0;\n  background-color: #ffffff;\n  padding: 0 10px;\n  width: 100%;\n}\n.zq-user-list {\n  margin: 0;\n  padding: 0;\n  list-style: none;\n}\n.zq-user-item {\n  border: 1px solid #ddd;\n  background-color: #f9fafc;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: horizontal;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: row;\n          flex-direction: row;\n  -ms-flex-wrap: nowrap;\n      flex-wrap: nowrap;\n  -ms-flex-line-pack: center;\n      align-content: center;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: start;\n      -ms-flex-pack: start;\n          justify-content: flex-start;\n  font-size: 12px;\n  height: 54px;\n  margin: 10px 0;\n  border-radius: 5px;\n}\n.zq-user-item:hover {\n  border: 1px solid #0a7cff;\n}\n.zq-user-desc {\n  overflow: hidden;\n  text-overflow: ellipsis;\n  white-space: nowrap;\n  margin: 0 10px;\n  width: 100%;\n}\n.zq-user-item:hover .zq-user-desc {\n  color: #0a7cff;\n  font-weight: bold;\n}\n.zq-user-time {\n  width: 180px;\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  padding: 0 10px;\n  margin: 0 5px;\n  text-align: center;\n  border-left: 1px solid #ddd;\n  border-right: 1px solid #ddd;\n  cursor: pointer;\n}\n.zq-user-time > i {\n  margin-right: 6px;\n}\n.zq-user-status {\n  width: 150px;\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  border-right: 1px solid #ddd;\n}\n.zq-user-status.status-0 {\n  color: #0cb1df;\n}\n.zq-user-status.status-1 {\n  color: #0a7cff;\n}\n.zq-user-status.status-2 {\n  color: #ed6f7a;\n}\n.zq-user-status.status-3 {\n  color: #ff8a48;\n}\n.zq-user-status.status-4 {\n  color: #6dd400;\n}\n.zq-user-status-icon {\n  width: 6px;\n  height: 6px;\n  border-radius: 50%;\n\n  display: block;\n  margin: 0 10px 0 20px;\n}\n.zq-user-status-icon.status-icon-0 {\n  background-color: #0cb1df;\n  outline: 3px solid #0cb1df5e;\n}\n.zq-user-status-icon.status-icon-1 {\n  background-color: #0a7cff;\n  outline: 3px solid #409eff7d;\n}\n.zq-user-status-icon.status-icon-2 {\n  background-color: #ed6f7a;\n  outline: 3px solid #ed6f7965;\n}\n.zq-user-status-icon.status-icon-3 {\n  background-color: #ff8a48;\n  outline: 3px solid #ff8b486e;\n}\n.zq-user-status-icon.status-icon-4 {\n  background-color: #6dd400;\n  outline: 3px solid #6dd4005e;\n}\n.zq-user-btns {\n  -ms-flex-negative: 0;\n      flex-shrink: 0;\n  padding-left: 15px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: horizontal;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: row;\n          flex-direction: row;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: start;\n      -ms-flex-pack: start;\n          justify-content: flex-start;\n}\n.zq-user-btn {\n  border: 1px solid #ddd;\n  padding: 10px;\n  color: #0a7cff;\n  padding: 7px 7px;\n  color: #0a7cff;\n  border-radius: 5px;\n  height: 30px;\n  margin-right: 10px;\n  cursor: pointer;\n  -webkit-user-select: none;\n     -moz-user-select: none;\n      -ms-user-select: none;\n          user-select: none;\n}\n.zq-user-btn.view {\n  padding: 7px 10px;\n}\n.zq-user-btn:hover {\n  border: 1px solid #0a7cff;\n}\n.el-icon-more {\n  -webkit-transform: rotate(90deg);\n          transform: rotate(90deg);\n  height: 28px;\n  padding: 6px 7px;\n}\n"]}])},TQNl:function(n,e,t){var A=t("IMVD");"string"==typeof A&&(A=[[n.i,A,""]]),A.locals&&(n.exports=A.locals);(0,t("SZ7m").default)("b95ac09a",A,!0,{})},iQVM:function(n,e,t){"use strict";t("TQNl")}}]);