<template>
  <div id="pdView">
    <!-- 表格显示 -->
    <el-table :data="tableData">
      <el-table-column
        prop="productID"
        label="产品ID"
        width="80"
      ></el-table-column>
      <el-table-column
        prop="productName"
        label="产品名字"
        width="180"
      ></el-table-column>
      <el-table-column
        prop="price"
        label="销售价"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="stockQuantity"
        label="库存量"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="salesVolume"
        label="销量"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="description"
        label="产品简介"
        width="280"
      ></el-table-column>
      <el-table-column label="操作">
        <el-button type="primary" size="mini">编辑</el-button>
        <el-button type="danger" size="mini">删除</el-button>
      </el-table-column>
    </el-table>
    <!-- 分页栏显示 -->
    <!-- Pagination分页 -->
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
import axios from "axios";
export default {
  data() {
    return {
      background: true,
      //每页显示的条数
      pageSize: 10,
      //当前页码
      currentPage: 1,
      //条数总数
      totalCount: 100,
      // 添加数据对话框是否展示的标记
      dialogVisible: false,
      //存储返回的产品列表
      tableData: [],
    };
  },
  mounted() {
    this.page(); //当页面加载完成后，发送异步请求，获取数据
    axios
      .get("http://localhost:8080/products", {
        withCredentials: true,
      })
      .then((resp) => {
        console.log(resp.data.data);
        this.tableData = resp.data.data.products;
      });
  },
  methods: {
    // 查询分页数据
    page() {
      axios
        .get(
          "http://localhost:8080/products?currentPage=" +
            this.currentPage +
            "&pageSize=" +
            this.pageSize
        )
        .then((res) => {
          this.totalCount = res.data.data.totalItems;
          this.tableData = res.data.data.products;
        });
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
};
</script>

<style scoped>
#title {
  background-color: #dcdfe6;
  font-family: "微软雅黑";
  color: #303133;
  font-size: 25px;
  line-height: 60px;
}
</style>
