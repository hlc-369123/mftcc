<template>
    <div  class="uni_entrance" >


            <el-row :gutter="24" class="row_space">
              <el-col :span="24" class="margin_top_block margin_first_block">
                <el-row class="grid-content" style="height: 213px">
                  <el-col :span="24" >
                    <el-row class="blockTip busHeight">
                      <el-col class="leftpart">
                        <el-row >
                          <el-col class="splitDiv"></el-col>
                          <el-col class="tipText">个人业务汇总</el-col>
                        </el-row>
                      </el-col>
                    </el-row>
                    <el-row>
                      <div class="margin_left_33">
                        <el-col :span="4">
                            <div class="div_floatLeft">
                              <img src="../../assets/image/cusCnt.png" />
                            </div>
                            <div class="div_floatLeft">
                              <div class="numDiv">
                                  {{ busSummaryData.cusCount }}
                              </div>
                              <div class="descDiv">
                                客户数(人)
                              </div>
                            </div>
                        </el-col>
                        <el-col :span="4">
                          <div class="div_floatLeft">
                            <img src="../../assets/image/newCusCnt.png" />
                          </div>
                          <div class="div_floatLeft">
                            <div class="numDiv">
                              {{ busSummaryData.cusCount }}
                            </div>
                            <div class="descDiv">
                              新增客户(人)
                            </div>
                          </div>
                        </el-col>
                        <el-col :span="4">
                          <div class="div_floatLeft">
                            <img src="../../assets/image/totalNum.png" />
                          </div>
                          <div class="div_floatLeft">
                            <div class="numDiv">
                              {{ busSummaryData.successBusCount }}
                            </div>
                            <div class="descDiv">
                              累计融资金额(万元)
                            </div>
                          </div>
                        </el-col>
                        <el-col :span="4">
                          <div class="div_floatLeft">
                            <img src="../../assets/image/curTotal.png" />
                          </div>
                          <div class="div_floatLeft">
                            <div class="numDiv">
                              {{ busSummaryData.successBusCount }}
                            </div>
                            <div class="descDiv">
                              新增融资金额(万元)
                            </div>
                          </div>
                        </el-col>
                        <el-col :span="4">
                          <div class="div_floatLeft">
                            <img src="../../assets/image/loanBal.png" />
                          </div>
                          <div class="div_floatLeft">
                            <div class="numDiv">
                             {{ busSummaryData.balanceAmt }}
                            </div>
                            <div class="descDiv">
                              融资余额(万元)
                            </div>
                          </div>
                        </el-col>
                        <el-col :span="4">
                          <div class="div_floatLeft">
                            <img src="../../assets/image/overAmt.png" />
                          </div>
                          <div class="div_floatLeft">
                            <div class="numDiv">
                              {{ busSummaryData.overdueAmt }}
                            </div>
                            <div class="descDiv">
                              逾期融资金额(万元)
                            </div>
                          </div>
                        </el-col>
                      </div>
                    </el-row>

                  </el-col>

                </el-row>
              </el-col>




              <!--消息信息块 -->
              <el-col :span="12" class="margin_top_block">
                <el-row class="grid-content msg">
                  <el-col :span="24" >
                    <el-row class="blockTip myMsg_bottom">
                      <el-col class="leftpart">
                        <el-row >
                          <el-col class="splitDiv"></el-col>
                          <el-col class="tipText">我的消息</el-col>
                          <el-col style="width: 75%;">
                                <el-menu style="height: 25px;" :default-active="activeIndexMsg">
                                    <el-menu-item index="0" class="el-submenu__title gua" @click.native="getMsgList('')">全部</el-menu-item>
                                    <el-menu-item :index='item.type' class="el-submenu__title gua"  v-for = '(item,index) in msgTypeList' :key='item.id' @click.native="getMsgList(item.type)"><el-badge :value='item.cnt' :max="99" class="item">{{item.name}}</el-badge></el-menu-item>
                                </el-menu>
                            </el-col>
                        </el-row>
                      </el-col>
                      <div @click="getMsgTable">
                        <el-col class="rightPart" >更多>></el-col>
                      </div>
                    </el-row>
                    <!-- 加载消息-->
                    <el-row class="sepratorLine"  v-for = '(item,index) in msgLists' :key='item.id'>
                      <el-col :span="24" class="content-height">
                        <div class="margin_top_10"  @click='clickDivMsgDetail(item.msgId,item.mainMsgId,item.msgLevel)'>
                          <div class="div_floatLeft circleWidth" >
                              <div  v-if="item.tmplType=='1'" class="circle_red"></div>
<!--                              <div  v-if="item.titleType=='2'" class="circle_green"></div>-->
                              <div  v-else="item.tmplType=='3'" class="circle_bule"></div>
                          </div>
                          <div class="div_floatLeft contentWidth">
                            <div class="msgTitle">
                              {{ item.msgTitle }}
                            </div>

                          </div>
                          <div class="msgTime">
                            <span>
                              {{ item.createTime|times }}
                            </span>
                            <div style="clear:both;"></div>

                          </div>
                        </div>
                        <div class="msgContent"  :title=item.msgContent>
                          {{ item.msgContent |subStr }}
                        </div>

                      </el-col>
                    </el-row>
                  </el-col>

                </el-row>
              </el-col>
                <!--大屏报表展示块 -->

              <!--<el-col :span="24" class="margin_top_block">
                <el-row class="grid-content">
                  <el-col :span="24" >
                    <el-row class="blockTip">
                      <el-col class="leftpart">
                        <el-row >
                          <el-col class="splitDiv"></el-col>
                          <el-col class="tipText">客户分布图</el-col>
                        </el-row>
                      </el-col>
                    </el-row>
                    <el-row class="reportLine">
                      <el-col :span="20"  :offset="2" style="height:352px;">
                        <img  @click="bindItemClick('cusPng')"  src="../../assets/image/lineReport.png" />
                      </el-col>
                    </el-row>

                  </el-col>

                </el-row>

              </el-col>-->

              <!-- </el-row>
              <el-row :gutter="12" class="row_space"> -->
                <!-- 列表信息块 -->

                <!-- 折线图信息块 -->
                <el-col :span="12" class="margin_top_block">
                    <el-row class="grid-content">
                        <el-col :span="24" >
                            <el-row class="blockTip">
                                <el-col class="leftpart">
                                    <el-row >
                                    <el-col class="splitDiv"></el-col>
                                    <el-col class="tipText">大屏报表展示</el-col>
                                    </el-row>
                                </el-col>
                            </el-row>
                             <el-row class="report" >
                               <el-col>
                                 <div class="block"  >
                                   <el-carousel autoplay="false" indicator-position="outside" arrow="always" height="360px">
                                     <el-carousel-item >
                                       <!--运营大屏-->
                                       <img @click = "bindItemClick('bigPng1')" src="../../assets/image/guarantee-operation.png" />
                                     </el-carousel-item>
                                     <el-carousel-item  >
                                       <!--风险大屏-->
                                       <img @click = "bindItemClick('bigPng2')" src="../../assets/image/guarantee-risk.png" />
                                     </el-carousel-item>
                                     <el-carousel-item >
                                       <!--客户大屏-->
                                       <img @click = "bindItemClick('bigPng3')" src="../../assets/image/guarantee-customer.png" />
                                     </el-carousel-item>
                                   </el-carousel>
                                 </div>
                               </el-col>
                            </el-row>

                        </el-col>

                    </el-row>

                </el-col>



              <el-col :span="24" class="margin_top_block">
                <el-row class="grid-content">
                  <el-col :span="24" >
                    <el-row class="blockTip">
                      <el-col class="leftpart">
                        <el-row >
                          <el-row>
                            <el-col class="splitDiv"></el-col>
                            <el-col class="tipText">应收账款预警</el-col>
                            <el-col style="width: 75%;">
                              <el-menu style="height: 25px;" :default-active="activeIndex">
                                <el-menu-item index="1" class="el-submenu__title gua" @click.native="getBusInfoByCusType('')">全部</el-menu-item>
                                <el-menu-item index="3" class="el-submenu__title gua" @click.native="getBusInfoByCusType('2')">个人客户</el-menu-item>
                                <el-menu-item index="4" class="el-submenu__title gua" @click.native="getBusInfoByCusType('1')">企业客户</el-menu-item>
                              </el-menu>
                            </el-col>
                          </el-row>
                        </el-row>
                      </el-col>
                      <el-col class="rightPart" onclick="window.open('/mftcc-guarantee-web/expire/guaranteeExpireQueryList','_self')">{{ busSummaryData.expireCount }}条>></el-col>
                    </el-row>
                    <el-row  style="margin: 0px 16px">
                      <el-col :span="24">
                        <mftcc-table  :initSearchData="initSearchDataGua" :key="freshKey" tableId="guarantee/guaranteeExpireQueryMini" :parentVm="this" ref="guaranteeExpireQueryList" class="user-table" ></mftcc-table>
                      </el-col>
                    </el-row>

                  </el-col>

                </el-row>
              </el-col>

            </el-row>

            <el-dialog
            :visible.sync="msgDetailFormShow"
            width="75%"
            :close-on-click-modal="false"
            :center="true"
            >
            <div slot="title" class="dialog-header">
                <label>消息详情</label>
            </div>
            <div class="dialog-content">
                <mftcc-form formId="msg/msgDetailForm" :parentVm="this" ref="msgDetailForm"></mftcc-form>

              <div v-if="$hasPerm('reply_msg_open')">
                <div style="padding: 9px;height: 25px;margin-left: 947px;">
                  <a @click="clickReplyInputFlag" title="回复"><i class="el-icon-s-comment"></i>&nbsp;({{ this.replyList.length }})回复</a>
                </div>
                <div v-if="replyInputFlag">
                  <el-input type="text" v-model="replyContent" placeholder="请输入回复内容">
                    <el-button style="padding-right:10px" slot="suffix" type="text" @click="sendReplyMsg">回复</el-button>
                  </el-input>
                </div>

                <div>
                  <el-row class="sepratorLine"  v-for = '(item,index) in replyList' :key='item.id'>
                    <el-col :span="24" class="content-height">
                      <div class="margin_top_10">
                        <div class="div_floatLeft contentWidth">
                          <div class="msgReplyTitle">
                            {{ item.sendName }}
                            <span STYLE="font-weight: bold; color: #666666;" > 回复 </span>
                            {{ item.receiverName }}
                          </div>
                        </div>
                        <div class="msgReplyTime">
                          {{ item.createTime|times }}
                          <div style="clear:both;"></div>
                        </div>
                      </div>

                      <div class="msgReplyContentUnread" v-if="msgDetail.msgId == item.msgId">
                        <i class="el-icon-message"></i>{{ item.msgContent}}
                      </div>
                      <div class="msgReplyContent" v-else>
                        {{ item.msgContent}}
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <span v-if="msgDetailFormTableShow">
                  <el-button type="primary" @click="findNextById">下一条</el-button>
                </span>
                <span v-else>
                  <el-button type="primary" @click="findNextUnreadById">下一条</el-button>
                </span>
                <el-button @click="msgDetailFormShow = false">关 闭</el-button>
            </div>
            </el-dialog>

            <el-dialog :visible.sync="msgTableShow" width="90%" :close-on-click-modal="false" :center="true">
            <div slot="title" class="dialog-header">
                <label>消息列表</label>
            </div>
            <div class="dialog-content" style="height:680px;">
                <div class="pt mftcc-list">
                    <mftcc-table tableId="msg/msgMessagesTablePro" :parentVm="this" ref="msgMessagesTablePro" class="user-table" :initSearchData="initSearchData"></mftcc-table>
                </div>
            </div>
            </el-dialog>

    </div>
</template>
<script>
import api from '@/api/apply/unifiedEntrance'
export default {
    name: 'unifiedEntrance',
    title: '统一入口',
    data() {
        return {
            opNo:'',
            reportUrl:'',
            activeIndex:"1",
            activeIndexMsg:"0",
            personalCenter:{},
            busSummaryData:{},
            msgDetailFormShow: false,
            msgDetailFormTableShow: false,
            replyList:[],//消息回复列表
            replyContent:"",//回复表单内容
            replyInputFlag:false,//回复表单是否显示
            msgTableShow: false,
            msgLists:[],
            msgTypeList:[],
            msgDetail:{},
            initSearchData:{},
            initSearchDataGua:{},
            phone:'',
            tel:'',
        }
    },
    filters:{
      times: function (value){
        return value.substring(0,19).replace('T','  ');
      },
      subStr (value) {
        if (!value) return ''
        if (value.length > 30) {
          return value.slice(0, 30) + '...'
        }
        return value
      }
    },
    methods: {
        //获取操作员信息
        getPersonalCenter(){
            api.findUserByOpNo(this.opNo,res => {
                if(res.code === 0){
                    this.personalCenter = res.data;
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true,
                        callback: action => {
                            this.$router.back(-1);
                        }
                    });
                }
            });
        },

        //获取个人业务汇总信息
        getBusSummaryData(){
            api.getBusSummaryData(this.opNo,res => {
                if(res.code === 0){
                    this.busSummaryData = res.data;
                    this.reportUrl = res.data.reportUrl;
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true,
                        callback: action => {
                            this.$router.back(-1);
                        }
                    });
                }
            });
        },

        back(){
            this.$router.back(-1);
        },
        //根据客户类型刷新担保到期列表
        getBusInfoByCusType(cusType){
            //     let searchData = {
            //         dynamicQuery: this.dynamicQuery,
            //         bizMark: this.bizMark,
            //         cusBaseType: cusType,
            //         opNo: this.$store.getters.user.opNo,
            //     }
            //     this.$refs.guaranteeExpireQueryList.search(searchData);
            //     this.$refs.guaranteeExpireQueryList.refresh();
            if(cusType==''){
                this.initSearchDataGua = {cusBaseType:null};
                this.$refs.guaranteeExpireQueryList.initSearchData = this.initSearchDataGua;
            }else{
                this.initSearchDataGua = {cusBaseType:cusType};
                this.$refs.guaranteeExpireQueryList.initSearchData = this.initSearchDataGua;
            }
            this.$refs.guaranteeExpireQueryList.refresh();
        },
        //功能入口
        bindItemClick(id){
          debugger;
            switch (id){
            //大屏报表
            case "bigPng1" :
                window.open(this.reportUrl+'/RDP-SERVER/obddpshow/show/b1d8fda44fb04b9a1e753226f7b82a7c');
                break;
              case "bigPng2":
                window.open(this.reportUrl+'/RDP-SERVER/obddpshow/show/3d9ffdc1a21bbf48af2e7f2264f6d5b6');
                break;
              case "bigPng3":
                window.open(this.reportUrl+'/RDP-SERVER/obddpshow/show/03169318aa7a15e2cec5202893b94e09');
                break;
            //客户分布图
            case "cusPng" :
                window.open(this.reportUrl+'/RDP-SERVER/rdppage/main/63e786ab5c09dbd7afed0e28b2241597');
                break;
            default :
                this.$alert("功能建设中，敬请期待......", this.$alertTitle, {
                                type: "success",
                                dangerouslyUseHTMLString: true
                            });
                break;

            }
        },
        //展示隐藏回复输入框
        clickReplyInputFlag(){
          this.replyInputFlag = !this.replyInputFlag;
        },
        //回复保存
        sendReplyMsg(){
            if(msgContent == null || msgContent == ''){
                return;
            }
            let msgId = this.msgDetail.msgId;
            let data = {"mainMsgId":msgId,"msgContent":this.replyContent};
            api.sendReplyMsg(data,res => {
              if(res.code === 0){
                this.replyContent = "";
                //刷新回复列表
                api.findMsgAndRepayListById(msgId,res => {
                  if(res.code === 0){
                    this.msgDetail = res.data.msgMessages;
                    this.replyList = res.data.replyList;
                  }
                });
                this.$message({ type: res.msgType, dangerouslyUseHTMLString:true,message: res.msg})
              }else{
                this.$alert(res.msg, this.$alertTitle, {
                  type: res.msgType,
                  dangerouslyUseHTMLString: true
                });
              }
            })
        },
        //列表点击查看消息详情
        clickListMsgDetail(index, row) {
          this.findMsgAndRepayListById(row.msgId,row.mainMsgId,row.msgLevel);
          this.msgDetailFormTableShow = true;
        },
        //详情块点击查看消息详情
        clickDivMsgDetail(msgId,mainMsgId,msgLevel) {
          this.findMsgAndRepayListById(msgId,mainMsgId,msgLevel);
          this.msgDetailFormTableShow = false;
        },
        //查询表单、回复列表，处理已读，刷新列表状态
        findMsgAndRepayListById(msgId,mainMsgId,msgLevel) {
          let id = msgId;
          //回复类消息展示主消息内容
          if(msgLevel == '2'){
            id = mainMsgId;
          }
          this.msgDetailFormShow = true;
          api.findMsgAndRepayListById(id,res => {
            if(res.code === 0){
              this.replyList = res.data.replyList;
              //给当前消息详情赋值
              if(msgLevel == '1'){
                this.msgDetail = res.data.msgMessages;
              }else {
                for(let data of this.replyList){
                  if(msgId == data.msgId){
                    this.msgDetail = data;
                  }
                }
              }
              this.$nextTick(()=>{
                this.$refs.msgDetailForm.setFormValue(res.data.msgMessages);
              });
              if('0'==this.msgDetail.msgReadStatus){
                this.msgBeanRead(this.msgDetail.msgId);
                this.$refs.msgMessagesTablePro.refresh();
              }
            }else{
              this.$alert(res.msg, this.$alertTitle, {
                type: res.msgType,
                dangerouslyUseHTMLString: true
              });
            }
          })
        },
        //下一条未读
        findNextUnreadById() {
            var msgId = this.msgDetail.msgId;
            api.findNextUnreadById(this.opNo+'/'+msgId,res => {
                if(res.code === 0){
                    this.findMsgAndRepayListById(res.data.msgId,res.data.mainMsgId,res.data.msgLevel);
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true
                    });
                }
            })
        },
        //列表弹窗按钮下一条
        findNextById() {
            var msgId = this.msgDetail.msgId;
            api.findNextById(this.opNo+'/'+msgId,res => {
                if(res.code === 0){
                  this.findMsgAndRepayListById(res.data.msgId,res.data.mainMsgId,res.data.msgLevel);
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true
                    });
                }
            })
        },
        //消息类型切换消息列表
        getMsgList(type){
          api.findLatestUnreadList(this.opNo+'/4?type='+type,res => {
                if(res.code === 0){
                    this.msgLists = res.list;
                }
            })
        },
        //消息列表弹窗
        getMsgTable(){
          this.msgTableShow = true;
        },
        //获取消息列表
        findLatestUnreadList() {
            api.findLatestUnreadList(this.opNo+'/4',res => {
                if(res.code === 0){
                    this.msgLists = res.list;
                }
            });
            api.queryUnreadMsgCountsByType(this.opNo,res => {
                if(res.code === 0){
                    this.msgTypeList = res.list;
                }
            });
        },
        //消息状态改为已读
        msgBeanRead(msgId) {
            api.msgBeanRead(msgId,res => {
                if(res.code === 0){
                   this.findLatestUnreadList();
                   this.activeIndexMsg ="0";
                }else{
                    this.$alert(res.msg, this.$alertTitle, {
                        type: res.msgType,
                        dangerouslyUseHTMLString: true
                    });
                }
            })
        },

    },
    mounted() {
        let user = this.$store.getters['user'];
        this.opNo = user.opNo;
        this.initSearchData = {receiverId:this.opNo};
        //this.getPersonalCenter();
        this.getBusSummaryData();
        this.findLatestUnreadList();
    }
}
</script>
<style scoped src="@/assets/css/unifiedEntrance.css"/>
