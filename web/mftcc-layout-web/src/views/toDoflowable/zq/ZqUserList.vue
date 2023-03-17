<template>
  <div class="zq-user-lists">
    <ul v-if="tableData.length>0"
        class="zq-user-list">
      <li class="zq-user-item"
          v-for="item in tableData"
          :key="item.id">
        <div class="zq-user-desc">{{ item['APP_DSC'] }}</div>
        <div class="zq-user-time">
          <i class="el-icon-time"></i>{{ item['CREATE_TIME'] }}
        </div>
        <!-- <div :class="'zq-user-status status-' + item.status">
          <i :class="'zq-user-status-icon status-icon-' + item.status"></i>{{ item['TASK_NAME'] }}
        </div> -->
        <div :class="'zq-user-status status-1'">
          <i :class="'zq-user-status-icon status-icon-1'"></i>{{ item['TASK_NAME'] }}
        </div>
        <div class="zq-user-btns">
          <template v-if="type == 'finished'">
            <span class="zq-user-btn"
                  @click="view({ type: 'query', item })">查看</span>
            <el-dropdown size="mini"
                         trigger="click"
                         placement="bottom"
                         @command="handleCommand">
              <span class="zq-user-btn el-icon-more"></span>
              <el-dropdown-menu slot="dropdown"
                                class="zq-dropdown">
                <el-dropdown-item :command="{ type: 'flowCancel', item }">撤销</el-dropdown-item>
                <el-dropdown-item :command="{ type: 'flowRecall', item }">召回</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

          </template>
          <template v-else>
            <span class="zq-user-btn"
                  @click="view({ type: 'app', item })">审批</span>
            <el-dropdown size="mini"
                         trigger="click"
                         placement="bottom"
                         @command="handleCommand">
              <span class="zq-user-btn el-icon-more"></span>
              <el-dropdown-menu slot="dropdown"
                                class="zq-dropdown">
                <el-dropdown-item :command="{ type: 'design', item }">指派</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </div>
      </li>
    </ul>
    <el-empty v-else
              description="暂无数据"></el-empty>
    <el-pagination :hide-on-single-page="singlePageHide"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="pageNo"
                   :page-sizes="pageSizes"
                   :page-size="pageSize"
                   layout="total, sizes, prev, pager, next"
                   :total="pageTotal">
    </el-pagination>
  </div>
</template>
<script>
  import api from "@/api/flowable/flowable";
  export default {
    name: "ZqUserList",
    title: "人员列表",
    data() {
      return {
        tableData: [],
        pageNo: 1,
        pageSize: 10,
        pageTotal: 0,
        singlePageHide: true,
        pageSizes: [10, 20, 50, 100]
      };
    },
    inject: ["dynamicQuery"],
    props: {
      type: {
        type: String,
        default: ""
      }
    },
    created() {
      this.getTableData();
    },
    methods: {
      refresh() {
        this.getTableData();
      },
      getTableData() {
        let queryType = "task";
        if (this.type == "finished") {
          queryType = "hisTask";
        }
        let data = {
          bizMark: "pocPreInfo",
          dynamicQuery: this.dynamicQuery,
          opNo: this.$store.getters.user.opNo,
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          queryType: queryType
        };
        api.getSysTaskInfo(data, (res) => {
          if (res.code == 0) {
            this.tableData = res.list.records;
            this.pageNo = res.list.current; // 当前页数
            this.pageSize = res.list.size; // 每页数量
            this.pageTotal = res.list.total; // 页面总数
          } else {
            this.$message.error(res.msg);
          }
        });
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getTableData();
      },
      handleCurrentChange(val) {
        this.pageNo = val;
        this.getTableData();
      },
      handleCommand(command) {
        this.$emit("command", command);
      },
      view(command) {
        this.$emit("command", command);
      }
    }
  };
</script>

<style scoped>
  .zq-user-lists {
    margin: 0;
    background-color: #ffffff;
    padding: 0 10px;
    width: 100%;
  }
  .zq-user-list {
    margin: 0;
    padding: 0;
    list-style: none;
  }
  .zq-user-item {
    border: 1px solid #ddd;
    background-color: #f9fafc;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-content: center;
    align-items: center;
    justify-content: flex-start;
    font-size: 12px;
    height: 54px;
    margin: 10px 0;
    border-radius: 5px;
  }
  .zq-user-item:hover {
    border: 1px solid #0a7cff;
  }
  .zq-user-desc {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin: 0 10px;
    width: 100%;
  }
  .zq-user-item:hover .zq-user-desc {
    color: #0a7cff;
    font-weight: bold;
  }
  .zq-user-time {
    width: 180px;
    flex-shrink: 0;
    padding: 0 10px;
    margin: 0 5px;
    text-align: center;
    border-left: 1px solid #ddd;
    border-right: 1px solid #ddd;
    cursor: pointer;
  }

  .zq-user-time > i {
    margin-right: 6px;
  }

  .zq-user-status {
    width: 150px;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    border-right: 1px solid #ddd;
  }
  .zq-user-status.status-0 {
    color: #0cb1df;
  }
  .zq-user-status.status-1 {
    color: #0a7cff;
  }
  .zq-user-status.status-2 {
    color: #ed6f7a;
  }
  .zq-user-status.status-3 {
    color: #ff8a48;
  }
  .zq-user-status.status-4 {
    color: #6dd400;
  }
  .zq-user-status-icon {
    width: 6px;
    height: 6px;
    border-radius: 50%;

    display: block;
    margin: 0 10px 0 20px;
  }
  .zq-user-status-icon.status-icon-0 {
    background-color: #0cb1df;
    outline: 3px solid #0cb1df5e;
  }
  .zq-user-status-icon.status-icon-1 {
    background-color: #0a7cff;
    outline: 3px solid #409eff7d;
  }
  .zq-user-status-icon.status-icon-2 {
    background-color: #ed6f7a;
    outline: 3px solid #ed6f7965;
  }
  .zq-user-status-icon.status-icon-3 {
    background-color: #ff8a48;
    outline: 3px solid #ff8b486e;
  }
  .zq-user-status-icon.status-icon-4 {
    background-color: #6dd400;
    outline: 3px solid #6dd4005e;
  }
  .zq-user-btns {
    flex-shrink: 0;
    padding-left: 15px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
  }
  .zq-user-btn {
    border: 1px solid #ddd;
    padding: 10px;
    color: #0a7cff;
    padding: 7px 7px;
    color: #0a7cff;
    border-radius: 5px;
    height: 30px;
    margin-right: 10px;
    cursor: pointer;
    user-select: none;
  }
  .zq-user-btn.view {
    padding: 7px 10px;
  }
  .zq-user-btn:hover {
    border: 1px solid #0a7cff;
  }
  .el-icon-more {
    transform: rotate(90deg);
    height: 28px;
    padding: 6px 7px;
  }
</style>
