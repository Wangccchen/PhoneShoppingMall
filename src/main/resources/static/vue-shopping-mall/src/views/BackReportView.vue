<template>
  <!-- 图表区域 -->
  <div>
    <el-card>
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
              >
                <template slot="prefix">
                  <i class="el-icon-s-flag" style="color: red"></i>
                </template>
                <template slot="suffix">
                  <i class="el-icon-s-flag" style="color: blue"></i> </template
              ></el-statistic>
            </div>
          </el-col>
          <!-- <el-col :span="6">
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
          </el-col> -->
        </el-row>
      </div>
    </el-card>

    <div class="chart-container">
      <el-card class="pics"><div class="chart" ref="pieChart"></div></el-card>
      <el-card class="pics"><div class="chart" ref="barChart"></div></el-card>
    </div>
    <el-card><div class="chart1" ref="lineChart"></div></el-card>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { searchByKeywords, allProducts } from "@/api/prod";
import { getNums } from "@/api/user";
export default {
  data() {
    return {
      userNums: 0,
      //   fisrtPhone: "",
      totalPrice: 0,
      totalNums: 0,
    };
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
    initCharts() {
      // 初始化饼图
      this.initPieChart();

      // 初始化柱状图
      this.initBarChart();

      // 初始化折线图
      this.initLineChart();
    },
    getTotalSales(list) {
      let count = 0;
      list.forEach((element) => {
        count += element.salesVolume;
      });
      return count;
    },
    async getUserNums() {
      const response = await getNums();
      if (response.data.code === 1) {
        this.userNums = response.data.data;
      } else {
        console.error(response.data.msg);
      }
    },
    async initPieChart() {
      const pieChart = echarts.init(this.$refs.pieChart);
      try {
        const huaweiResponse = await searchByKeywords("HUAWEI");
        const appleResponse = await searchByKeywords("APPLE");
        const allProductsResponse = await allProducts();

        const handleResponse = (response, list) => {
          if (response.data.code === 1) {
            list.push(...response.data.data);
          } else {
            console.error(response.data.msg);
          }
        };

        const listHuawei = [];
        handleResponse(huaweiResponse, listHuawei);

        const listApple = [];
        handleResponse(appleResponse, listApple);

        const listAll = [];
        handleResponse(allProductsResponse, listAll);

        const pieData = [
          {
            value: this.getTotalSales(listHuawei) / this.getTotalSales(listAll),
            name: "华为",
          },
          {
            value: this.getTotalSales(listApple) / this.getTotalSales(listAll),
            name: "苹果",
          },
          {
            value:
              (this.getTotalSales(listAll) -
                this.getTotalSales(listHuawei) -
                this.getTotalSales(listApple)) /
              this.getTotalSales(listAll),
            name: "其他",
          },
        ];

        const pieOption = {
          title: {
            text: "销售比例",
            left: "center",
          },
          tooltip: {
            trigger: "item",
            formatter: "{b}:   {d}%", // 设置百分数格式
          },
          legend: {
            orient: "vertical",
            left: "left",
          },
          series: [
            {
              // name: "Access From",
              type: "pie",
              radius: "50%",
              data: pieData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: "rgba(0, 0, 0, 0.5)",
                },
              },
            },
          ],
        };

        pieChart.setOption(pieOption);
      } catch (error) {
        console.error("出错:", error);
      }
    },
    async initBarChart() {
      const barChart = echarts.init(this.$refs.barChart);

      try {
        const huaweiResponse = await searchByKeywords("HUAWEI");
        const appleResponse = await searchByKeywords("APPLE");
        const allProductsResponse = await allProducts();

        const handleResponse = (response, list) => {
          if (response.data.code === 1) {
            list.push(...response.data.data);
          } else {
            console.error(response.data.msg);
          }
        };

        const listHuawei = [];
        handleResponse(huaweiResponse, listHuawei);

        const listApple = [];
        handleResponse(appleResponse, listApple);

        const listAll = [];
        handleResponse(allProductsResponse, listAll);
        const barData = [
          this.getTotalSales(listHuawei),
          this.getTotalSales(listApple),
          this.getTotalSales(listAll) -
            this.getTotalSales(listHuawei) -
            this.getTotalSales(listApple),
        ];
        const minSales =
          Math.min(
            this.getTotalSales(listHuawei),
            this.getTotalSales(listApple),
            this.getTotalSales(listAll) -
              this.getTotalSales(listHuawei) -
              this.getTotalSales(listApple)
          ) / 2;

        const barOption = {
          title: {
            text: "品牌销售数量",
            left: "center",
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
            },
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true,
          },
          xAxis: {
            type: "category",
            data: ["华为", "苹果", "其他"],
          },
          yAxis: {
            type: "value",
            name: "营业额(元)",
            min: minSales,
            axisTick: {
              inside: true,
            },
            scale: true,
            axisLabel: {
              margin: 2,
              formatter: function (value, index) {
                if (value >= 10000 && value < 10000000) {
                  value = value / 10000 + "万";
                } else if (value >= 10000000) {
                  value = value / 10000000 + "千万";
                }
                return value;
              },
            },
          },
          series: [
            {
              barWidth: "60%",
              type: "bar",
              data: barData,
            },
          ],
        };
        barChart.setOption(barOption);
      } catch (error) {
        console.error("出错:", error);
      }
    },

    async initLineChart() {
      const lineChart = echarts.init(this.$refs.lineChart);
      try {
        // 获取整个产品列表
        const allProductsResponse = await allProducts();
        const result = allProductsResponse.data;
        if (result.code === 1) {
          const allProducts = result.data;
          //计算总销量和销售额
          let totalPrice = 0;
          let totalNums = 0;
          allProducts.forEach((item) => {
            totalNums += item.salesVolume;
            totalPrice += item.salesVolume * item.price;
          });
          this.totalPrice = totalPrice;
          this.totalNums = totalNums;
          // 按销量降序排序
          const sortedProducts = allProducts.sort(
            (a, b) => b.salesVolume - a.salesVolume
          );

          // 选择销量最高的前十款产品
          const topProducts = sortedProducts.slice(0, 10);
          // //取得销冠
          // this.firstPhone = topProducts[0].productName;
          // 生成折线图数据
          const lineData = topProducts.map((product) => ({
            name: product.productName,
            value: product.salesVolume,
          }));

          const lineOption = {
            // ECharts 折线图配置项
            title: {
              text: "销售量前十排行榜",
              left: "center",
            },
            xAxis: {
              type: "category",
              data: topProducts.map((product) => product.productName),
              axisLabel: {
                interval: 0,
                rotate: 30,
              },
            },
            yAxis: {
              type: "value",
              name: "营业额(元)",
            },
            series: [
              {
                type: "line",
                data: lineData,
              },
            ],
          };

          lineChart.setOption(lineOption);
        } else {
          console.error(result.msg);
        }
      } catch (error) {
        console.error("出错:", error);
      }
    },
  },
  mounted() {
    this.getUserNums();
    this.initCharts();
    console.log("page执行完毕！");
    console.log(this.fisrtPhone);
  },
};
</script>

<style scoped>
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
.pics {
  width: 50%; /* 设置图表占据 el-card 的一半宽度 */
  display: flex;
  justify-content: center;
}
</style>