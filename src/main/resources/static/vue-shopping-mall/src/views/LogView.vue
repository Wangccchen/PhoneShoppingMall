<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="username" label="用户名" width="180">
      </el-table-column>
      <el-table-column prop="operTime" label="操作时间" width="180">
      </el-table-column>
      <el-table-column prop="operationType" label="操作详情"> </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :background="background"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="10"
      :current-page="currentPage"
      layout="sizes,prev, pager, next,jumper,total"
      :total="totalCount"
    >
    </el-pagination>
  </div>
</template>

<script>
import { logPage } from "@/api/Log";
import { selectById } from "@/api/user";

export default {
  data() {
    return {
      background: true,
      tableData: [],
      //每页显示的条数
      pageSize: 10,
      //当前页码
      currentPage: 1,
      //条数总数
      totalCount: 100,
    };
  },
  methods: {
    // 查询分页数据
    page() {
      console.log("执行了page刷新函数！");
      logPage(this.currentPage, this.pageSize).then(async (res) => {
        console.log(res.data);
        if (res.data.code === 0 && res.data.msg === "NOT_LOGIN") {
          console.log("未登录！已跳转至登录界面");
        } else {
          this.totalCount = res.data.data.totalItems;
          const originalTableData = res.data.data.logs;
          this.tableData = await this.enrichTableData(originalTableData);
        }
      });
    },
    async enrichTableData(originalTableData) {
      const enrichedTableData = [];

      for (const item of originalTableData) {
        const userRes = await selectById(item.userID);
        // console.log(userRes.data.data.username);
        const enrichedItem = { ...item, username: userRes.data.data.username };

        enrichedTableData.push(enrichedItem);
      }

      return enrichedTableData;
    },
    //分页
    handleSizeChange(val) {
      // 重新设置每页显示的条数
      this.pageSize = val;
      this.page();
    },

    handleCurrentChange(val) {
      // 重新设置当前页码
      this.currentPage = val;
      this.page();
    },
  },
  mounted() {
    this.page(); //当页面加载完成后，发送异步请求，获取数据
    console.log("page执行完毕！");
  },
};
</script>

<style>
</style>