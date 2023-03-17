<!--  -->
<template>
  <div class="mftcc_home_box">
    <!--头部信息-->
    <div class="mftcc_home_header">
      <div class="heder_title">个人业务汇总</div>
      <div class="heder_item">
        <div class="item_box">
          <!--循环输出头部列表块-->
          <div class="bus_item" v-for="(item,index) in busItemList" :key="index">
            <!-- <div class="bus_item_icon" :style="{background: 'url(' + item.busItemIcon + ')'}"></div> -->
            <img  class="bus_item_icon" :src="require('../../assets/home/'+item.busItemIcon +'.png')"                  />
            <div class="bus_item_data">{{item.busItemData}}</div>
            <div class="bus_item_desc">{{item.busItemDesc}}</div>
          </div>
          <!--循环输出头部列表块-->
        </div>
      </div>
    </div>
    <!--头部信息-->
    <el-row :gutter="12" style="margin-top:12px">
      <el-col :span="12">
        <div class="list_box_item">
          <div class="list_title" style="background:#F07235">我的待办</div>
          <div class="list_link_box">
            <!-- <el-link class="link_item" type="primary">
              <span class="link_name_select">全部</span>
            </el-link> -->
            <el-link class="link_item" :type="stayShow == 'need_deal' ? 'primary' :'info'">
              <span :class="stayShow == 'need_deal' ? 'link_name_select':'link_name'" :key= 'need_deal'  @click="getStayTableData('need_deal')">待办</span>
              <span class="link_data">({{taskInfoListBySize}})</span>
            </el-link>
            <el-link class="link_item" :type="stayShow == 'already_done' ? 'primary' :'info'">
              <span :class="stayShow == 'already_done' ? 'link_name_select':'link_name'" :key= 'already_done'  @click="getStayTableData('already_done')">已办</span>
              <span class="link_data">({{histaskInfoListBySize}})</span>
            </el-link>
            <el-link class="link_item" type="info">
              <span class="link_name">提醒</span>
              <span class="link_data">(16)</span>
            </el-link>
            <el-link class="link_item" :type="stayShow == 'focus_deal' ? 'primary' :'info'">
              <span :class="stayShow == 'focus_deal' ? 'link_name_select':'link_name'" :key= 'focus_deal'  @click="getStayTableData('focus_deal')">关注</span>
              <span class="link_data">({{focusOnTasksInfoListBySize}})</span>
            </el-link>
            <el-link class="link_item" type="info">
              <span class="link_name">预警</span>
              <span class="link_data">(16)</span>
            </el-link>
          </div>
          <div class="list_link_more">
            <el-link class="link_item" style="text-align: right;" type="info">
              <span class="link_more_name" @click="linkToStalyTableList()">更多>></span>
            </el-link>
          </div>

          <div class="list_data_box">
            <div v-for="(item ,index) in waitingList" :key="index" @click.prevent="doFlowView(index,item)">
              <div class="data_item">
                <div class="dataIcon"></div>
                <div class="data_title">{{item.FLOW_NAME}}</div>

                <el-tooltip
                  :disabled="showTip(item.FLOW_CONTENT)"
                  effect="dark"
                  :content="item.FLOW_CONTENT"
                  placement="bottom"
                >
                  <div ref="dataDesc" class="data_des">{{item.FLOW_CONTENT}}</div>
                </el-tooltip>
                <div class="data_date">{{item.CREATE_TIME}}</div>
              </div>
              <div class="line"></div>
            </div>
          </div>
          <sxjk-flow-app @getBizList="getBizList" ref="sxjkFlowApp"></sxjk-flow-app>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="list_box_item">
          <div class="list_title" style="background:#409EFF">项目视图</div>
          <div class="list_link_box">
            <el-link class="link_item" :type="hoverShow == 'all' ? 'primary' :'info'">
              <span :class="hoverShow == 'all' ? 'link_name_select':'link_name'" :key= 'all' @click="getProjectData('all')">全部</span>
            </el-link>
            <el-link class="link_item" :type="hoverShow == 'equity' ? 'primary' :'info'">
              <span :class="hoverShow == 'equity' ? 'link_name_select':'link_name'" :key= 'equity' @click="getProjectData('equity')">股权</span>
            </el-link>
            <el-link class="link_item" :type="hoverShow == 'creditor' ? 'primary' :'info'">
              <span :class="hoverShow == 'creditor' ? 'link_name_select':'link_name'" :key= 'creditor'  @click="getProjectData('creditor')">债权</span>
            </el-link>
            <el-link class="link_item" :type="hoverShow == 'projectQin' ? 'primary' :'info'">
              <span :class="hoverShow == 'projectQin' ? 'link_name_select':'link_name'"  :key= 'projectQin' @click="getProjectData('projectQin')">秦创原</span>
            </el-link>
          </div>
          <div class="list_link_more">
            <el-link class="link_item" style="text-align: right;" type="info">
              <span class="link_more_name" @click="linkToTableList()">更多>></span>
            </el-link>
          </div>
          <div class="tble_box">
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="project_name" label="项目名称"></el-table-column>
              <el-table-column prop="company_name" label="企业名称"></el-table-column>
              <el-table-column prop="projectLabel" label="项目标签"></el-table-column>
              <el-table-column prop="create_user_name" label="经办人"></el-table-column>
              <el-table-column prop="entryData" label="录入日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="12" style="margin-top:12px">
      <el-col :span="12">
        <div class="list_box_item">
          <div class="list_title" style="background:#FFBA39">数据与分析</div>
          <div class="line_bar_box">
            <div id="line" style="width:100%;height:100%">3434</div>
          </div>
          <div class="pie_bar_box">
            <div id="pie" style="width:100%;height:100%">3434</div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="list_box_item">
          <div class="list_title" style="background:#2BD7D1">咨询</div>
          <div class="list_link_box">
            <el-link class="link_item" type="primary">
              <span class="link_name_select">投资咨询</span>
            </el-link>
            <el-link class="link_item" type="info">
              <span class="link_name">产品政策</span>
            </el-link>
            <el-link class="link_item" type="info">
              <span class="link_name">国资监管</span>
            </el-link>
          </div>
          <div class="list_link_more">
            <el-link class="link_item" style="text-align: right;" type="info">
              <span class="link_more_name">更多>></span>
            </el-link>
          </div>
          <div class="list_data_box">
            <div v-for="(item ,index) in waitingList" :key="index" style="height:45px">
              <div class="data_item" style="height:40px">
                <el-tooltip
                  :disabled="showTip(item.FLOW_CONTENT)"
                  effect="dark"
                  :content="item.FLOW_CONTENT"
                  placement="bottom"
                >
                  <div
                    ref="dataDesc"
                    class="data_des"
                    style="left:27px;padding-right: 100px;min-height: 35px;"
                  >{{item.FLOW_CONTENT}}</div>
                </el-tooltip>
                <div class="data_date" style="top:24px">{{item.CREATE_TIME}}</div>
              </div>
              <div class="line"></div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from '《组件路径》';
import * as echarts from 'echarts'
import api from '@/api/apply/sxjkEntrance'
import flowapi from "@/api/flowable/flowable";
import SxjkFlowApp  from "@/views/toDoflowable/sxjkFlowApp";


export default {
  // import引入的组件需要注入到对象中才能使用
  components: {
    SxjkFlowApp,
  },
  data() {
    // 这里存放数据
    return {
      // 头部的业务统计信息
      busItemList: [],
      taskInfoListBySize:"", //获取当前审批人代办条数
      histaskInfoListBySize:"", //获取当前审批人已办条数
      focusOnTasksInfoListBySize: "", //获取当前审批人关注条数
      // 待办数据源
      waitingList: [
        {
          dataIcon: '',
          FLOW_NAME: '',
          FLOW_CONTENT:  '',
          CREATE_TIME: '',
        }
        // {
        //   dataIcon: 'data_icon_2',
        //   dataTitle: '贷款预期',
        //   dataDes: '客户黄奕然-有欠款总额390,201.0',
        //   dataDate: '2021-10-28',
        // },
        // {
        //   dataIcon: 'data_icon_3',
        //   dataTitle: '押品预期',
        //   dataDes: '客户黄奕然-有欠款总额390,201.0',
        //   dataDate: '2021-10-28',
        // },
        // {
        //   dataIcon: 'data_icon_3',
        //   dataTitle: '合同签订预期',
        //   dataDes:
        //     '客户黄奕然-有欠款总额390,201.0是的发送到发送到发送到发送打发sdfasdfasdfasdfasd收到发送到发手动阀收到发送到发手动阀是的发',
        //   dataDate: '2021-10-28',
        // },
        // {
        //   dataIcon: 'data_icon_3',
        //   dataTitle: '贷款预期',
        //   dataDes: '客户黄奕然-有欠款总额390,201.0',
        //   dataDate: '2021-10-28',
        // },
      ],
      // 项目视图
      tableData: [
        {
          project_name: '',
          company_name: '',
          projectLabel: '',
          create_user_name: '',
          entryData: '',
        },


      ],
      // 饼图
      pieOption: {
        legend: {
          top: 'bottom',
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: false },
            dataView: { show: false, readOnly: false },
            restore: { show: false },
            saveAsImage: { show: false },
          },
        },
        series: [
          {
            name: 'Nightingale Chart',
            type: 'pie',
            radius: [10, 80],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8,
            },
            data: [
              { value: 40, name: 'rose 1' },
              { value: 38, name: 'rose 2' },
              { value: 32, name: 'rose 3' },
              { value: 30, name: 'rose 4' },
              { value: 28, name: 'rose 5' },
              { value: 26, name: 'rose 6' },
              { value: 22, name: 'rose 7' },
              { value: 18, name: 'rose 8' },
            ],
          },
        ],
      },
      //  折线和柱状
      hoverShow:"all",
      stayShow:"need_deal",  //我的待办
      tableParm:'',
      staytableParm:'',
      dataParam:''
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    //首页统计项目数
    getProjectDataStatistics(){
            api.getProjectDataStatistics('',res => {
                if(res.code === 0){

                    this.busItemList = res.data;
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
      getProjectData(parm){
       api.getProjectTableDataStatistics(parm,res => {
                if(res.code === 0){
                   this.hoverShow = parm;
                   this.tableParm = parm;
                   this.tableData = res.data;
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

    //获取首页我的待办数据
    getStayTableData(parm){
      let  data = {};
      data.parm = parm;
      data.ASSIGNEE=this.$store.getters.user.opNo;
      flowapi.getStayTableDataStatistics(data,res => {
                if(res.code === 0){debugger
                  this.stayShow = parm;
                  this.staytableParm = parm;
                  this.waitingList = res.data.list;
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

    //获取流程列表数据(条数)
    getTaskInfoListBytotal(){
      flowapi.getTaskInfoListBytotal(
        {
          opNo: this.$store.getters.user.opNo,
        },
        res => {
          if (res.code == 0) {
            this.taskInfoListBySize=res.data.taskInfoListBySize;
            this.histaskInfoListBySize=res.data.histaskInfoListBySize;
            this.focusOnTasksInfoListBySize=res.data.focusOnTasksInfoListBySize;
          } else {
            this.$message.error(res.msg);
          }
        }
      );
    },

    //点击已办不需审批页面
    doHisFlowView(index, item) {
      this.$refs.sxjkFlowApp.apphist(index,item);
    },

    //跳转列表详情
    linkToTableList(index, row){
      this.$router
                .push({path: '/apply/projectDataStaticsList', query: {param:this.tableParm}});
    },
    //跳转待办列表详情
    linkToStalyTableList(index, row){
      this.$router
                .push({path: '/toDoflowable/sxjkFlowIndex', query: {param:this.staytableParm}});
    },

      back(){
        this.$router.back(-1);
      },
    //处理标题展示
    showTip(value) {
      if(typeof(value)!="undefined" && typeof(value)!=""){
          return value.length < 50;
      }
    },
    initLintChar() {
      let sData = [
        ['2020-04-09 02:00:00', 36],
        ['2020-04-09 02:30:00', 73],
        ['2020-04-09 03:00:00', 35],
        ['2020-04-09 03:30:00', 36],
        ['2020-04-09 04:00:00', 36],
        ['2020-04-09 04:30:00', 35],
        ['2020-04-09 05:00:00', 33],
        ['2020-04-09 05:30:00', 37],
        ['2020-04-09 06:00:00', 43],
        ['2020-04-09 06:30:00', 63],
      ]
      let sData1 = [
        ['2020-04-09 02:00:00', 32],
        ['2020-04-09 02:30:00', 33],
        ['2020-04-09 03:00:00', 53],
        ['2020-04-09 03:30:00', 53],
        ['2020-04-09 04:00:00', 53],
        ['2020-04-09 04:30:00', 33],
        ['2020-04-09 05:00:00', 63],
        ['2020-04-09 05:30:00', 53],
        ['2020-04-09 06:00:00', 86],
        ['2020-04-09 06:30:00', 23],
      ]
      // option
      let option = {
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['legend11', 'legend22'],
          icon: 'circle',
          bottom: 'bottom',
          selectedMode: false,
        },
        xAxis: [
          {
            type: 'time',
            axisPointer: {
              type: 'shadow',
            },
            axisLine: {
              // 轴线
              show: false,
            },
            axisTick: {
              // 刻度线
              show: false,
            },
            axisLabel: {
              // 刻度数值
              color: '#000',
            },
            splitLine: {
              show: false,
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '系列名1',
            axisLabel: {
              formatter: '{value}',
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              // y轴线不显示
              show: false,
            },
            scale: true,
          },
          {
            type: 'value',
            name: '系列名2',
            axisLabel: {
              formatter: '{value}',
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              // y轴线的颜色以及宽度
              show: false,
            },
            scale: true,
          },
        ],
        dataZoom: [
          {
            type: 'inside',
            // minSpan: 5, // 这个最好根据数据条数定
            minValueSpan: 3600000, // 时间轴左侧到右侧最多可以缩放到一小时
          },
        ],
        series: [
          {
            name: 'legend11',
            type: 'bar',
            data: sData,
            barWidth: '20%',
            itemStyle: {
              normal: {
                // 设置柱子的渐变色
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: '#409EFF',
                  },
                  {
                    offset: 1,
                    color: '#606DFF',
                  },
                ]),
                barBorderRadius: 2,
              },
            },
          },
          {
            name: 'legend22',
            type: 'line',
            yAxisIndex: 1,
            data: sData1,
            lineStyle: {
              normal: {
                width: 1,
                color: '#40DAFF',
              },
            },
            itemStyle: {
              normal: {
                color: '#40DAFF',
                borderWidth: 2,
                borderColor: '#40DAFF',
              },
            },
            smooth: false,
            showSymbol: false,
          },
        ],
      }
      let lineDom = document.getElementById('line')
      var lineChart = echarts.init(lineDom)
      lineChart.setOption(option)
    },
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {
    let that = this
    this.$nextTick(() => {
      let s1 = this.$refs.dataDesc
      let chartDom = document.getElementById('pie')
      var myChart = echarts.init(chartDom)
      myChart.setOption(this.pieOption)

      that.initLintChar()
    })
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    //查询后台
    let queryType = this.$route.query.queryType;
    if(queryType != undefined){
        this.hoverShow = queryType
    }
    this.getProjectData(this.hoverShow);
    this.getStayTableData(this.stayShow);
    this.getTaskInfoListBytotal();
    this.getProjectDataStatistics();
  },
  beforeCreate() {}, // 生命周期 - 创建之前
  beforeMount() {}, // 生命周期 - 挂载之前
  beforeUpdate() {}, // 生命周期 - 更新之前
  updated() {}, // 生命周期 - 更新之后
  beforeDestroy() {}, // 生命周期 - 销毁之前
  destroyed() {}, // 生命周期 - 销毁完成
  activated() {}, // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style>
.mftcc_home_box {
  overflow: hidden;
  overflow-y: auto;
  width: 100%;
  height: 100%;
  background-color: #eef3f7;
}
.mftcc_home_header {
  position: relative;
  width: auto;
  height: 200px;
  margin-top: 12px;
  margin-left: 12px;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(207, 222, 244, 0.1);
  border-radius: 8px;
}
.heder_title {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 194px;
  height: 42px;
  padding-left: 16px;
  background: #6d6bdb;
  border-radius: 8px 0px 100px 0px;
  font-size: 18px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #ffffff;
  line-height: 42px;
}
.heder_item {
  position: absolute;
  top: 42px;
  height: 158px;
  width: 100%;
}

.item_box {
  padding-top: 31px;
  display: flex;
  justify-content: space-around;
}

.bus_item {
  position: relative;
  width: 204px;
  height: 102px;
  background: #ffffff;
  box-shadow: 1px 3px 6px 0px rgba(64, 158, 255, 0.2);
  border-radius: 8px;
}
.bus_item_icon {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 62px;
  height: 57px;
}
.bus_item_data {
  position: absolute;
  top: 15px;
  left: 71px;
  width: 58px;
  height: 45px;
  font-size: 32px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #141414;
  line-height: 45px;
}
.bus_item_desc {
  position: absolute;
  top: 66px;
  left: 78px;
  width: 66px;
  height: 20px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  white-space: nowrap;
  line-height: 20px;
}

.list_box_item {
  position: relative;
  width: 100%;
  height: 508px;
  margin-left: 13px;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(207, 222, 244, 0.1);
  border-radius: 8px;
}
.list_title {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 158px;
  height: 42px;
  border-radius: 8px 0px 100px 0px;
  padding-left: 16px;
  font-size: 18px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #ffffff;
  line-height: 42px;
}

.list_link_more {
  position: absolute;
  top: 62px;
  right: 36px;
}
.link_more_name {
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #409eff;
  line-height: 22px;
}

.list_link_box {
  position: absolute;

  top: 62px;
}

.link_item {
  margin-left: 17px;
}

.link_name_select {
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #409eff;
  line-height: 22px;
}
.link_name {
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #141414;
  line-height: 22px;
}
.link_item .link_data {
  color: #409eff;
}
.tble_box {
  position: absolute;
  top: 96px;
  left: 16px;
  width: 100%;
  height: 100%;
}

.list_data_box {
  position: absolute;
  width: 100%;
  top: 104px;
}
.data_item {
  position: relative;
  width: 100%;
  min-height: 46px;
  height: auto;
}

.dataIcon {
  position: absolute;
  width: 36px;
  height: 36px;
  top: 0px;
  left: 16px;
  background-size: cover;
  background-image: url('../../assets/home/awaiting-1.png');
}

.data_icon_1 {
  position: absolute;
  width: 36px;
  height: 36px;
  top: 0px;
  left: 16px;
  background-size: cover;
  background-image: url('../../assets/home/awaiting-1.png');
}
.data_icon_2 {
  position: absolute;
  width: 36px;
  height: 36px;
  top: 0px;
  left: 16px;
  background-size: cover;
  background-image: url('../../assets/home/awaiting-2.png');
}
.data_icon_3 {
  position: absolute;
  width: 36px;
  height: 36px;
  top: 0px;
  left: 16px;background-size: cover;

  background-image: url('../../assets/home/awaiting-3.png');
}
.data_title {
  position: absolute;
  top: 0px;
  left: 69px;
  width: 180px;
  height: 22px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 22px;
}
.data_des {
  position: absolute;
  top: 24px;
  left: 69px;
  width: 90%;
  height: 20px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 20px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: break-all;
}
.data_date {
  position: absolute;
  top: 0px;
  right: 16px;
  width: 100px;
  height: 20px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #333333;
  line-height: 20px;
  right: 60px;
  top: 20px;
}
.line {
  background-color: #ececec;
  height: 1px;
  width: 100%;
  margin-left: 16px;
  margin-right: 16px;
  margin-bottom: 11px;
  margin-top: 11px;
}

.line_bar_box {
  position: absolute;
  top: 80px;
  left: 16px;
  width: 50%;
  width: 308px;
  height: 308px;
}
.pie_bar_box {
  position: absolute;
  top: 80px;
  left: 50%;
  width: 308px;
  height: 308px;
}
</style>
