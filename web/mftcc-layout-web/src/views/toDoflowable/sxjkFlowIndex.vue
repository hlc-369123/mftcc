<!-- 待办页面 -->
<template>
  <div class="waiting_box">
    <el-container class="waiting_left_box" style="height: 100%;">
      <el-header class="header">
        <el-row :gutter="10" style="width:100%">
          <el-col
            class="col"
            :xs="6"
            :sm="4"
            :md="3"
            :lg="2"
            :xl="1"
            :offset="5"
            @click.native="getBizListButtonChange()"
            v-trigger
          >
            <span :class="nameAA">待办</span>
            <span class="data">({{ taskInfoListBySize }})</span>
          </el-col>
          <el-col
            class="col"
            :xs="6"
            :sm="4"
            :md="3"
            :lg="2"
            :xl="1"
            @click.native="getHisTaskButtonChange()"
            v-clickDown
          >
            <span :class="nameBB">已办</span>
            <span class="data">({{ histaskInfoListBySize }})</span>
          </el-col>
          <el-col class="col" :xs="6" :sm="4" :md="3" :lg="2" :xl="1">
            <span :class="nameCC">投票任务</span>
            <span class="data">(15)</span>
          </el-col>
          <el-col class="col" :xs="6" :sm="4" :md="3" :lg="2" :xl="1">
            <span :class="nameDD">提醒</span>
            <span class="data">(15)</span>
          </el-col>
          <el-col class="col" :xs="6" :sm="4" :md="3" :lg="2" :xl="1" @click.native="getFocusTaskButtonChange()">
            <span :class="nameEE">关注任务</span>
            <span class="data">({{ focusOnTasksInfoListBySize }})</span>
          </el-col>
          <el-col class="col" :xs="6" :sm="4" :md="3" :lg="2" :xl="1">
            <span :class="nameFF">预警</span>
            <span class="data">(15)</span>
          </el-col>
        </el-row>
      </el-header>
      <el-container style="height: 100%;padding-bottom:90px">
        <el-main class="elMain" style="height: 100%;">
          <div
            class="waiting_item_box"
            v-for="(item, index) in dataList"
            :key="index"
            @click.prevent="doFlowView(index, item)"
          >
            <img
              class="icon"
              :src="require('../../assets/home/awaiting-' + '1' + '.png')"
            />
            <div class="title">{{ item.FLOW_NAME }}</div>
            <div class="desc">{{ item.FLOW_CONTENT }}</div>
            <div class="dateTime">{{ item.CREATE_TIME }}</div>
            <img
              class="collection"
              @click.stop="doCollection(index, item)"
              v-if="collectionShow"
              :src="require('../../assets/home/collection-' + '1' + '.png')"
            />
            <img
              class="top"
              :src="require('../../assets/home/top-' + '1' + '.png')"
              @click.stop="doTop(index, item)"
              v-if="topShow"
            />

            <div
              class="revoke"
              @click.stop="doFlowCancel(index, item)"
              v-if="revokeShow"
            >
              <div class="revoke_box">
                <div class="revoke_name">撤销</div>
                <img
                  class="revoke_icon"
                  :src="require('../../assets/home/chexiao.png')"
                />
              </div>
            </div>

            <div
              class="recall"
              @click.stop="doFlowRecall(index, item)"
              v-if="recallShow"
            >
              <div class="recall_box">
                <div class="recall_name">召回</div>
                <img
                  class="recall_icon"
                  :src="require('../../assets/home/zhaohui.png')"
                />
              </div>
            </div>

            <div
              class="assign"
              @click.stop="doFlowDesign(index, item)"
              v-if="assignShow"
            >
              <div class="assign_box">
                <div class="assign_name">指派</div>
                <img
                  class="assign_icon"
                  :src="require('../../assets/home/zhipai.png')"
                />
              </div>
            </div>

            <div class="line"></div>
          </div>
        </el-main>
        <el-aside class="aside" width="200px">
          <el-select v-model="yearKey" placeholder="请选择" @change="changeDateRefresh(yearKey)">
            <el-option
              v-for="item in options"
              :key="item.yearKey"
              :label="item.label"
              :value="item.yearKey"
            ></el-option>
          </el-select>
          <div style="height: auto;padding-left:40px;margin-top:32px;">
            <el-steps direction="vertical" :active="100" :space="40">
              <el-step title icon="el-icon-time"></el-step>

              <el-step
                v-for="(item, index) in stepData"
                :key="index"
                class="mftcc_step"
                v-show="item.show"
              >
                <template slot="icon" v-if="item.isMonth">
                  <div class="point">
                    <div class="point1"></div>
                  </div>
                </template>

                <template slot="icon" v-if="item.isDay">
                  <div class="point2"></div>
                </template>

                <template slot="title" v-if="item.isMonth">
                  <div class="step_title" @click="showDays(item.month)">
                    {{ item.month }}月份({{ item.data }}条)
                    <i class="el-icon-arrow-down"></i>
                  </div>
                </template>
                <template slot="title" v-if="item.isDay">
                  <div class="step_title" @click="dateDayClick(item.day)">
                    {{ item.day }}({{ item.data }}条)
                  </div>
                </template>
              </el-step>
              <el-step title icon="el-icon-time"></el-step>
            </el-steps>
          </div>
        </el-aside>
      </el-container>
    </el-container>
    <sxjk-flow-app @getBizList="getBizList" ref="sxjkFlowApp"></sxjk-flow-app>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from '《组件路径》';
import SxjkFlowApp from "@/views/toDoflowable/sxjkFlowApp";
import api from "@/api/flowable/flowable";
export default {
  // import引入的组件需要注入到对象中才能使用
  components: {
    SxjkFlowApp
  },
  data() {
    // 这里存放数据
    return {
      nameAA: "name", //标题
      nameBB: "",
      nameCC: "",
      nameDD: "",
      nameEE: "",
      nameFF: "",
      taskFlag: "need_deal", //待办按钮标识
      taskInfoListBySize: "", //获取当前审批人代办条数
      histaskInfoListBySize: "", //获取当前审批人已办条数
      focusOnTasksInfoListBySize: "", //获取当前审批人关注任务条数
      queryType: this.$route.query.param,
      options:[],
      yearKey:"",
      dataList: [],
      stepData: [],
      year:"",
      month:"",
      day:"",
    };
  },
  // 自动触发点击事件
  directives: {
    col: {
      inserted(el) {
        // console.log("自动触发事件")
        el.click();
      }
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    showDays(month) {
      let that = this;
      that.stepData.filter(item => {
        if (item.isDay) {
          if (item.pareatMonth === month) {
            item.show = true;
          } else {
            item.show = false;
          }
        }
      });
    },
    //按钮变更时重新刷新右侧树
    getBizListButtonChange(){
       //按钮类型变更之后将年、月、日赋值为空
       this.year="";
       //月份
       this.month="";
       //日期
       this.day="";
       //执行获取流程待办接口
       this.getBizList();
       //重新刷新按钮标识的数目
       this.getTaskInfoListBytotalDate();
    },

    //获取流程接口(业务待办)
    getBizList() {
      if ((this.nameAA = "name")) {
        //隐藏召回和撤销
        this.recallShow = this.revokeShow = false;
        this.collectionShow = this.topShow = this.assignShow = true;
        //处理顶部待办已办按钮的颜色
        this.nameBB = this.nameCC = this.nameDD = this.nameEE = this.nameFF =
          "name";
        this.nameAA = "nameColor";
      }
      let data = {};
      data.ASSIGNEE = this.$store.getters.user.opNo;
      data.year=this.year;
      data.month=this.month;
      data.day=this.day;
      api.getTaskDealInfoList(data, res => {
        if (res.code == 0) {
          this.$nextTick(() => {
            this.dataList = res.data.list;
            this.taskInfoListBySize = res.data.taskInfoListBySize;
            this.taskFlag = "need_deal";
            //获取右边的搜索树(点击搜索树的日期时搜索树不重新刷新)
            if(this.month==""||this.day==""){
               this.getTaskYearCountByTime(this.taskFlag);
            }
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    //按钮变更时重新刷新右侧树
    getHisTaskButtonChange(){
       //按钮类型变更之后将年、月、日赋值为空
       this.year="";
       //月份
       this.month="";
       //日期
       this.day="";
       //执行获取流程已办接口
       this.getHisTask();
       //重新刷新按钮标识的数目
       this.getTaskInfoListBytotalDate();
    },

    //获取已办接口
    getHisTask() {
      if ((this.nameBB = "name")) {
        //处理顶部待办已办按钮的颜色
        this.nameAA = this.nameCC = this.nameDD = this.nameEE = this.nameFF =
          "name";
        this.nameBB = "nameColor";
        //隐藏指派，收藏，置顶
        this.recallShow = this.revokeShow = true;
        this.collectionShow = this.topShow = this.assignShow = false;
      }
      let data = {};
      data.ASSIGNEE = this.$store.getters.user.opNo;
      data.year=this.year;
      data.month=this.month;
      data.day=this.day;
      api.getTaskDealHisInfoList(data, res => {
        if (res.code == 0) {
          this.$nextTick(() => {
            this.dataList = res.data.list;
            this.taskFlag = "already_done";
            this.histaskInfoListBySize = res.data.histaskInfoListBySize;
            //获取右边的搜索树(点击搜索树的日期时搜索树不重新刷新)
            if(this.month==""||this.day==""){
              //获取右边的搜索数
              this.getTaskYearCountByTime(this.taskFlag);
            }
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    //按钮变更时重新刷新右侧树
    getFocusTaskButtonChange(){
       //按钮类型变更之后将年、月、日赋值为空
       this.year="";
       //月份
       this.month="";
       //日期
       this.day="";
       //执行获取流程关注接口
       this.getFocusTask();
       //重新刷新按钮标识的数目
       this.getTaskInfoListBytotalDate();
    },

    //获取关注任务
    getFocusTask() {
      if ((this.nameEE = "name")) {
        //隐藏召回和撤销
        this.recallShow = this.revokeShow = false;
        this.collectionShow = this.topShow = this.assignShow = true;
        //处理顶部待办已办按钮的颜色
        this.nameAA = this.nameBB = this.nameCC = this.nameDD = this.nameEE = this.nameFF =
          "name";
        this.nameEE = "nameColor";
      }
      let data = {};
      data.ASSIGNEE = this.$store.getters.user.opNo;
      data.year=this.year;
      data.month=this.month;
      data.day=this.day;
      api.getFocusOnTasksList(data, res => {
        if (res.code == 0) {
          this.$nextTick(() => {
            this.dataList = res.data.list;
            this.focusOnTasksInfoListBySize = res.data.focusOnTasksInfoListBySize;
            this.taskFlag = "focus_deal";
            //获取右边的搜索树(点击搜索树的日期时搜索树不重新刷新)
            if(this.month==""||this.day==""){
              //获取右边的搜索数
              this.getTaskYearCountByTime(this.taskFlag);
            }
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    //获取流程列表数据(条数)
    getTaskInfoListBytotal() {
      api.getTaskInfoListBytotal(
        {
          opNo: this.$store.getters.user.opNo
        },
        res => {
          if (res.code == 0) {
            this.taskInfoListBySize = res.data.taskInfoListBySize;
            this.histaskInfoListBySize = res.data.histaskInfoListBySize;
            this.focusOnTasksInfoListBySize=res.data.focusOnTasksInfoListBySize;
          } else {
            this.$message.error(res.msg);
          }
        }
      );
      if (this.queryType == "need_deal") {
        //获取待办(从首页跳转)
        this.getBizList();
      } else if (this.queryType == "already_done") {
        //获取已办(从首页跳转)
        this.getHisTask();
      } else if (this.queryType == "focus_deal") {
        //获取关注任务(从首页跳转)
        this.getFocusTask();
      } else {
        //获取待办(默认)
        this.getBizList();
      }
    },
    //点击审批打开审批页面
    doFlowView(index, item) {
      if (this.taskFlag == "need_deal") {
        this.$refs.sxjkFlowApp.app(index, item);
      } else if (this.taskFlag == "already_done") {
        this.doHisFlowView(index, item);
      }
    },

    //获取任务年份搜索框的值
    getTaskYearCountByTime(titleType){
      let data = {};
      data.ASSIGNEE = this.$store.getters.user.opNo;
      data.titleType=titleType;
      api.getTaskYearCountByTime(data, res => {
        if (res.code == 0) {
          this.$nextTick(() => {
             //下拉选择框的数据源
             this.options=res.data.options;
             if(this.year==""){
               //默认年份
               this.yearKey=res.data.yearKey;
             }
             data.year=this.yearKey;
             this.getTaskMonthCountByTime(data);
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },

   //改变年份时重新刷新右侧数据以及搜索数
   changeDateRefresh(yearKey){
       //日期切换年份之后,年份重新赋值、将月、日同时清空
       this.year=yearKey;
       //月份
       this.month="";
       //日期
       this.day="";
       if (this.taskFlag == "need_deal") {
        //获取待办(选择年份)
        this.getBizList();
      } else if (this.taskFlag == "already_done") {
        //获取已办(选择年份)
        this.getHisTask();
      } else if (this.taskFlag == "focus_deal") {
        //获取关注任务(选择年份)
        this.getFocusTask();
      } else {
        //获取待办(选择年份)
        this.getBizList();
      };
      //右侧时间树发生变化时上层的按钮数据变更
      this.getTaskInfoListBytotalDate();
   },

  //点击日期改变左侧数据
  dateDayClick(day){
    debugger;
    let arrDate=day.split('/');
    this.month=arrDate[0];
    this.day=arrDate[1];
    if (this.taskFlag == "need_deal") {
        //获取待办(选择年份)
        this.getBizList();
      } else if (this.taskFlag == "already_done") {
        //获取已办(选择年份)
        this.getHisTask();
      } else if (this.taskFlag == "focus_deal") {
        //获取关注任务(选择年份)
        this.getFocusTask();
      } else {
        //获取待办(选择年份)
        this.getBizList();
      };
      //右侧时间树发生变化时上层的按钮数据变更
      this.getTaskInfoListBytotalDate();
  },

   //右侧时间树发生变化时上层的按钮数据变更
   getTaskInfoListBytotalDate() {
      api.getTaskInfoListBytotal(
        {
          opNo: this.$store.getters.user.opNo,
          year:this.year,
          month:this.month,
          day:this.day
        },
        res => {
          if (res.code == 0) {
            this.taskInfoListBySize = res.data.taskInfoListBySize;
            this.histaskInfoListBySize = res.data.histaskInfoListBySize;
            this.focusOnTasksInfoListBySize=res.data.focusOnTasksInfoListBySize;
          } else {
            this.$message.error(res.msg);
          }
        }
      );
    },


   //根据年份 月份 日期分组统计条数
   getTaskMonthCountByTime(data){
       api.getTaskMonthCountByTime(data, res => {
        if (res.code == 0) {
          this.$nextTick(() => {
             //根据年份 月份 日期分组统计条数
             this.stepData=res.data;
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    //获取收藏接口
    doCollection(index, item) {
      api.updateCollectionOne(item, res => {
        if (res.code === 0) {
          this.getBizList();
        } else {
          this.$alert(res.msg, this.$alertTitle, {
            type: res.msgType,
            dangerouslyUseHTMLString: true
          });
        }
      });
    },

    //获取置顶接口
    doTop(index, item) {
      api.updateToppingOne(item, res => {
        if (res.code === 0) {
          this.getBizList();
        } else {
          this.$alert(res.msg, this.$alertTitle, {
            type: res.msgType,
            dangerouslyUseHTMLString: true
          });
        }
      });
    },
    callBack() {
      this.$router.back(-1);
      this.$router.push({
        path: "/apply/sxjkEntrance",
        query: { queryType: this.queryType }
      });
    },

    //点击已办不需审批页面
    doHisFlowView(index, item) {
      this.$refs.sxjkFlowApp.apphist(index, item);
    },
    //指派(审批)
    doFlowDesign(index, item) {
      this.$refs.sxjkFlowApp.design(index, item);
    },
    //召回
    doFlowRecall(index, item) {
      this.$refs.sxjkFlowApp.flowRecall(index, item);
    },
    //撤销
    doFlowCancel(index, item) {
      this.$refs.sxjkFlowApp.flowCancel(index, item);
    }
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //加载审批列表
    // this.getBizList();
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.getTaskInfoListBytotal();
  },
  beforeCreate() {}, // 生命周期 - 创建之前
  beforeMount() {}, // 生命周期 - 挂载之前
  beforeUpdate() {}, // 生命周期 - 更新之前
  updated() {}, // 生命周期 - 更新之后
  beforeDestroy() {}, // 生命周期 - 销毁之前
  destroyed() {}, // 生命周期 - 销毁完成
  activated() {} // 如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style>
.waiting_box {
  width: 100%;
  height: 100%;
  background-color: #f0f2f5;
  overflow: hidden;
}
.header {
  background-color: #b3c0d1;
  text-align: center;
  line-height: 60px;

  height: 60px;
  background: #ffffff;
  margin-bottom: 16px;
}

.col > .name {
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #3d3d3d;
}
.col > .nameColor {
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #409eff;
}
.col > .data {
  color: #409eff;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
}

.aside {
  background-color: #ffffff;
  margin-left: 16px;
  margin-right: 16px;
}

.elMain {
  width: 100%;
  height: 100%;
  background-color: #ffffff;
  margin-left: 16px;
  overflow-y: auto;
}
::-webkit-scrollbar {
  width: 0px !important;
  height: 0px !important;
}
.waiting_item_box {
  position: relative;
  width: 100%;
  height: 130px;
}
.waiting_item_box > .icon {
  position: absolute;
  width: 74px;
  height: 74px;
  top: 20px;
  left: 32px;
}
.waiting_item_box > .title {
  position: absolute;
  top: 34px;
  left: 124px;

  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
}
.waiting_item_box > .desc {
  position: absolute;
  top: 60px;
  left: 124px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 20px;
}
.waiting_item_box > .dateTime {
  position: absolute;
  top: 36px;
  right: 42px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #333333;
}
.waiting_item_box > .collection {
  position: absolute;
  top: 95px;
  right: 90px;
  width: 24px;
  height: 23px;
  cursor: pointer;
}
.waiting_item_box > .top {
  position: absolute;
  top: 95px;
  right: 42px;
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.waiting_item_box > .line {
  position: absolute;

  top: 131px;
  margin-left: 12px;
  width: 100%;
  height: 1px;
  background: #e8e8e8;
}

.waiting_item_box > .revoke {
  position: absolute;
  top: 97px;
  right: 45px;
  cursor: pointer;
}

.revoke_box {
  position: relative;
  width: 52px;
  height: 22px;
}

.revoke_name {
  position: absolute;
  top: 0px;
  left: 20px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #399cff;
}

.revoke_icon {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 20px;
  height: 20px;
}

.waiting_item_box > .recall {
  position: absolute;
  top: 97px;
  right: 125px;
  cursor: pointer;
}

.recall_box {
  position: relative;
  width: 52px;
  height: 22px;
}

.recall_name {
  position: absolute;
  top: 0px;
  left: 20px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #399cff;
}

.recall_icon {
  position: absolute;
  top: 2px;
  left: 0px;
  width: 20px;
  height: 20px;
}

.waiting_item_box > .assign {
  position: absolute;
  top: 97px;
  right: 135px;
  cursor: pointer;
}

.assign_box {
  position: relative;
  width: 52px;
  height: 22px;
}

.assign_name {
  position: absolute;
  top: 0px;
  left: 20px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #399cff;
}

.assign_icon {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 20px;
  height: 20px;
}

.point {
  position: relative;
  width: 16px;
  height: 16px;
  background: rgba(64, 158, 255, 0.26);
  border-radius: 8px;
}
.point1 {
  position: absolute;
  width: 10px;
  height: 10px;
  background: #409eff;
  border-radius: 5px;
  left: 3px;
  top: 3px;
}
.point2 {
  width: 10px;
  height: 10px;
  background: #409eff;
  border-radius: 5px;
}
.step_title {
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #409eff;
  line-height: 20px;
  cursor: pointer;
}

.mftcc_step .is-text {
  border: 0px;
}
.waiting_box .el-main {
  display: -webkit-box;
}
</style>
