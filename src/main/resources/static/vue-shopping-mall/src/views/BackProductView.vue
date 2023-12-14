<template>
  <div id="pdView">
    <!--按钮-->
    <el-row>
      <el-button type="danger" size="medium" @click="deleteByIds">
        - 批量删除</el-button
      >
      <el-button @click="toggleSelection()">取消选择</el-button>
      <el-button
        type="primary"
        size="medium"
        @click="
          dialogFormVisible = true;
          singleProd = {
            description: '',
            imageURL: '',
            price: '',
            productID: '',
            productName: '',
            salesVolume: 0,
            stockQuantity: 0,
          };
        "
        >+ 上架产品</el-button
      >
    </el-row>
    <!-- 图表区域 -->
    <!-- <div>
      <div class="report">
        <el-row :gutter="20">
          <el-col :span="6">
            <div>
              <el-statistic
                group-separator=","
                :value="userNums"
                title="注册用户数量"
              ></el-statistic>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-statistic
                group-separator=","
                :value="totalNums"
                title="总销售量"
              ></el-statistic>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-statistic
                group-separator=","
                :precision="2"
                :value="totalPrice"
                title="总销售额"
              ></el-statistic>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-statistic :value="fisrtPhone" title="销冠">
                <template slot="prefix">
                  <i class="el-icon-s-flag" style="color: red"></i>
                </template>
                <template slot="suffix">
                  <i class="el-icon-s-flag" style="color: blue"></i>
                </template>
              </el-statistic>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="chart-container">
        <div class="chart" ref="pieChart"></div>
        <div class="chart" ref="barChart"></div>
      </div>
      <div class="chart1" ref="lineChart"></div>
    </div> -->

    <!-- 表格显示 -->
    <el-table
      ref="multipleTable"
      :data="tableData"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
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
        width="500"
      ></el-table-column>
      <!-- 操作按钮显示 -->
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="update(scope.row.productID)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="deleteById(scope.row.productID)"
            >删除</el-button
          >
        </template>
      </el-table-column>
      <!-- 对话框显示 -->
      <el-dialog
        title="产品信息"
        :visible.sync="dialogFormVisible"
        :append-to-body="true"
      >
        <el-form :model="singleProd">
          <el-form-item label="产品名字" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.productName"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品价格" :label-width="formLabelWidth">
            <el-input v-model="singleProd.price" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="产品描述" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.description"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品库存" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.stockQuantity"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品销量" :label-width="formLabelWidth">
            <el-input
              v-model="singleProd.salesVolume"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-upload
              class="avatar-uploader"
              action="http://8.134.204.233:8080/backsystem4up/upload"
              :on-success="handleUploadSuccess"
            >
              <el-button type="primary" icon="el-icon-upload"
                >上传图片</el-button
              >
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="add">确 定</el-button>
        </div>
      </el-dialog>
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
import * as echarts from "echarts";
import {
  page,
  add,
  update,
  deleteById,
  selectById,
  deleteByIds,
  searchByKeywords,
  allProducts,
} from "@/api/prod";
import { getNums } from "@/api/user";
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
      //存储单个product
      singleProd: {
        description: "",
        imageURL: "",
        price: "",
        productID: "",
        productName: "",
        salesVolume: 0,
        stockQuantity: 0,
      },
      //表单相关数据
      dialogFormVisible: false,
      formLabelWidth: "120px",
      //被选中的列表
      multipleSelection: [],
      // 被选中的id数组
      selectedIds: [],
      userNums: 0,
      fisrtPhone: "",
      totalPrice: 0,
      totalNums: 0,
    };
  },
  mounted() {
    this.page(); //当页面加载完成后，发送异步请求，获取数据
    // this.getUserNums();
    // this.getFirst();
    // this.initCharts();
    // console.log("page执行完毕！");
    // console.log(this.fisrtPhone);
  },

  methods: {
    // async getFirst() {
    //   const allProductsResponse = await allProducts();
    //   const result = allProductsResponse.data;
    //   if (result.code === 1) {
    //     const allProducts = result.data;
    //     // 按销量降序排序
    //     const sortedProducts = allProducts.sort(
    //       (a, b) => b.salesVolume - a.salesVolume
    //     );
    //     //取得销冠
    //     this.fisrtPhone = sortedProducts[0].productName;
    //   }
    // },
    // initCharts() {
    //   // 初始化饼图
    //   this.initPieChart();

    //   // 初始化柱状图
    //   this.initBarChart();

    //   // 初始化折线图
    //   this.initLineChart();
    // },
    // getTotalSales(list) {
    //   let count = 0;
    //   list.forEach((element) => {
    //     count += element.salesVolume;
    //   });
    //   return count;
    // },
    // async getUserNums() {
    //   const response = await getNums();
    //   if (response.data.code === 1) {
    //     this.userNums = response.data.data;
    //   } else {
    //     console.error(response.data.msg);
    //   }
    // },
    // async initPieChart() {
    //   const pieChart = echarts.init(this.$refs.pieChart);
    //   try {
    //     const huaweiResponse = await searchByKeywords("HUAWEI");
    //     const appleResponse = await searchByKeywords("APPLE");
    //     const allProductsResponse = await allProducts();

    //     const handleResponse = (response, list) => {
    //       if (response.data.code === 1) {
    //         list.push(...response.data.data);
    //       } else {
    //         console.error(response.data.msg);
    //       }
    //     };

    //     const listHuawei = [];
    //     handleResponse(huaweiResponse, listHuawei);

    //     const listApple = [];
    //     handleResponse(appleResponse, listApple);

    //     const listAll = [];
    //     handleResponse(allProductsResponse, listAll);

    //     const pieData = [
    //       {
    //         value: this.getTotalSales(listHuawei) / this.getTotalSales(listAll),
    //         name: "华为",
    //       },
    //       {
    //         value: this.getTotalSales(listApple) / this.getTotalSales(listAll),
    //         name: "苹果",
    //       },
    //       {
    //         value:
    //           (this.getTotalSales(listAll) -
    //             this.getTotalSales(listHuawei) -
    //             this.getTotalSales(listApple)) /
    //           this.getTotalSales(listAll),
    //         name: "其他",
    //       },
    //     ];

    //     const pieOption = {
    //       title: {
    //         text: "销售比例",
    //         left: "center",
    //       },
    //       tooltip: {
    //         trigger: "item",
    //         formatter: "{b}:   {d}%", // 设置百分数格式
    //       },
    //       legend: {
    //         orient: "vertical",
    //         left: "left",
    //       },
    //       series: [
    //         {
    //           // name: "Access From",
    //           type: "pie",
    //           radius: "50%",
    //           data: pieData,
    //           emphasis: {
    //             itemStyle: {
    //               shadowBlur: 10,
    //               shadowOffsetX: 0,
    //               shadowColor: "rgba(0, 0, 0, 0.5)",
    //             },
    //           },
    //         },
    //       ],
    //     };

    //     pieChart.setOption(pieOption);
    //   } catch (error) {
    //     console.error("出错:", error);
    //   }
    // },
    // async initBarChart() {
    //   const barChart = echarts.init(this.$refs.barChart);

    //   try {
    //     const huaweiResponse = await searchByKeywords("HUAWEI");
    //     const appleResponse = await searchByKeywords("APPLE");
    //     const allProductsResponse = await allProducts();

    //     const handleResponse = (response, list) => {
    //       if (response.data.code === 1) {
    //         list.push(...response.data.data);
    //       } else {
    //         console.error(response.data.msg);
    //       }
    //     };

    //     const listHuawei = [];
    //     handleResponse(huaweiResponse, listHuawei);

    //     const listApple = [];
    //     handleResponse(appleResponse, listApple);

    //     const listAll = [];
    //     handleResponse(allProductsResponse, listAll);
    //     const barData = [
    //       this.getTotalSales(listHuawei),
    //       this.getTotalSales(listApple),
    //       this.getTotalSales(listAll) -
    //         this.getTotalSales(listHuawei) -
    //         this.getTotalSales(listApple),
    //     ];
    //     const minSales =
    //       Math.min(
    //         this.getTotalSales(listHuawei),
    //         this.getTotalSales(listApple),
    //         this.getTotalSales(listAll) -
    //           this.getTotalSales(listHuawei) -
    //           this.getTotalSales(listApple)
    //       ) / 2;

    //     const barOption = {
    //       title: {
    //         text: "品牌销售数量",
    //         left: "center",
    //       },
    //       tooltip: {
    //         trigger: "axis",
    //         axisPointer: {
    //           type: "shadow",
    //         },
    //       },
    //       grid: {
    //         left: "3%",
    //         right: "4%",
    //         bottom: "3%",
    //         containLabel: true,
    //       },
    //       xAxis: {
    //         type: "category",
    //         data: ["华为", "苹果", "其他"],
    //       },
    //       yAxis: {
    //         type: "value",
    //         name: "营业额(元)",
    //         min: minSales,
    //         axisTick: {
    //           inside: true,
    //         },
    //         scale: true,
    //         axisLabel: {
    //           margin: 2,
    //           formatter: function (value, index) {
    //             if (value >= 10000 && value < 10000000) {
    //               value = value / 10000 + "万";
    //             } else if (value >= 10000000) {
    //               value = value / 10000000 + "千万";
    //             }
    //             return value;
    //           },
    //         },
    //       },
    //       series: [
    //         {
    //           barWidth: "60%",
    //           type: "bar",
    //           data: barData,
    //         },
    //       ],
    //     };
    //     barChart.setOption(barOption);
    //   } catch (error) {
    //     console.error("出错:", error);
    //   }
    // },

    // async initLineChart() {
    //   const lineChart = echarts.init(this.$refs.lineChart);
    //   try {
    //     // 获取整个产品列表
    //     const allProductsResponse = await allProducts();
    //     const result = allProductsResponse.data;
    //     if (result.code === 1) {
    //       const allProducts = result.data;
    //       //计算总销量和销售额
    //       let totalPrice = 0;
    //       let totalNums = 0;
    //       allProducts.forEach((item) => {
    //         totalNums += item.salesVolume;
    //         totalPrice += item.salesVolume * item.price;
    //       });
    //       this.totalPrice = totalPrice;
    //       this.totalNums = totalNums;
    //       // 按销量降序排序
    //       const sortedProducts = allProducts.sort(
    //         (a, b) => b.salesVolume - a.salesVolume
    //       );

    //       // 选择销量最高的前十款产品
    //       const topProducts = sortedProducts.slice(0, 10);
    //       // //取得销冠
    //       // this.firstPhone = topProducts[0].productName;
    //       // 生成折线图数据
    //       const lineData = topProducts.map((product) => ({
    //         name: product.productName,
    //         value: product.salesVolume,
    //       }));

    //       const lineOption = {
    //         // ECharts 折线图配置项
    //         title: {
    //           text: "销售量前十排行榜",
    //           left: "center",
    //         },
    //         xAxis: {
    //           type: "category",
    //           data: topProducts.map((product) => product.productName),
    //           axisLabel: {
    //             interval: 0,
    //             rotate: 30,
    //           },
    //         },
    //         yAxis: {
    //           type: "value",
    //           name: "营业额(元)",
    //         },
    //         series: [
    //           {
    //             type: "line",
    //             data: lineData,
    //           },
    //         ],
    //       };

    //       lineChart.setOption(lineOption);
    //     } else {
    //       console.error(result.msg);
    //     }
    //   } catch (error) {
    //     console.error("出错:", error);
    //   }
    // },
    // 上传成功回调
    handleUploadSuccess(response) {
      console.log(response);
      if (response.code === 1) {
        this.singleProd.imageURL = response.data;
        this.$message.success("上传成功");
        console.log(11);
      } else {
        this.$message.error("上传失败");
        console.log(22);
      }
    },
    //更新被选中的列表
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //取消选择
    toggleSelection() {
      this.$refs.multipleTable.clearSelection();
    },
    // 查询分页数据
    page() {
      console.log("执行了page刷新函数！");
      page(this.currentPage, this.pageSize).then((res) => {
        console.log(res.data);
        if (res.data.code === 0 && res.data.msg === "NOT_LOGIN") {
          console.log("未登录！已跳转至登录界面");
        } else {
          this.totalCount = res.data.data.totalItems;
          this.tableData = res.data.data.products;
        }
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

    // 添加数据
    add() {
      if (this.singleProd.productID) {
        //修改
        update(this.singleProd).then((resp) => {
          console.log(this.singleProd);
          if (resp.data.code == 1) {
            this.dialogFormVisible = false;
            this.page();
            this.$message({ message: "恭喜你，保存成功", type: "success" });
            this.singleProd = {
              description: "",
              imageURL: "",
              price: "",
              productID: "",
              productName: "",
              salesVolume: 0,
              stockQuantity: 0,
            };
          } else {
            this.$message.error(resp.data.msg);
          }
        });
      } else {
        //添加
        add(this.singleProd).then((resp) => {
          console.log(this.singleProd);
          if (resp.data.code == 1) {
            this.dialogFormVisible = false;
            this.page();
            this.$message({ message: "恭喜你，保存成功", type: "success" });
            this.singleProd = {
              description: "",
              imageURL: "",
              price: "",
              productID: "",
              productName: "",
              salesVolume: 0,
              stockQuantity: 0,
            };
          } else {
            this.$message.error(resp.data.msg);
          }
        });
      }
    },

    //删除信息
    deleteById(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteById(id).then((resp) => {
            if (resp.data.code == 1) {
              //删除成功
              this.$message.success("恭喜你，删除成功");
              this.page();
            } else {
              this.$message.error(resp.data.msg);
            }
          });
        })
        .catch(() => {
          //用户点击取消按钮
          this.$message.info("已取消删除");
        });
    },
    //批量删除
    deleteByIds() {
      // 弹出确认提示框
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //点击确认按钮
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.selectedIds[i] = this.multipleSelection[i].productID;
          }

          deleteByIds(this.selectedIds).then((resp) => {
            if (resp.data.code == 1) {
              //删除成功
              this.$message.success("恭喜你，删除成功");
              this.page();
            } else {
              this.$message.error(resp.data.msg);
            }
          });
        })
        .catch(() => {
          //用户点击取消按钮
          this.$message.info("已取消删除");
        });
    },
    //编辑界面
    update(id) {
      //显示窗口
      this.dialogFormVisible = true;
      console.log(id);
      //发送修改请求
      selectById(id).then((res) => {
        if (res.data.code == 1) {
          //console.log(res.data.data);
          this.singleProd = res.data.data;
          console.log(this.singleProd);
        }
      });
    },
    beforeUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      if (!isJPG) {
        this.$message.error("只能上传 JPG/PNG 格式的图片");
        return false;
      }

      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("图片大小不能超过 2MB");
        return false;
      }

      return true;
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
.chart-container {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.chart {
  width: 400px;
  height: 300px;
}
.chart1 {
  height: 500px;
}
.like {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}
</style>
