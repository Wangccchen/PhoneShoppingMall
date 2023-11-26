<template>
  <div>
    <el-container>
      <el-header>
        <div class="logo"></div>
        <div class="search">
          <el-input placeholder="请输入内容" v-model="input" class="input">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-openeds="['1', '5']">
            <el-menu-item index="1"> 全部 </el-menu-item>
            <el-menu-item index="2"> 智能手机 </el-menu-item>
            <el-menu-item index="3"> 平板电脑 </el-menu-item>
            <el-menu-item index="4">笔记本电脑</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <el-row
            v-for="(productArray, index) in filteredProductList"
            :key="index"
            class="row"
          >
            <el-col
              :span="4"
              v-for="(product, index) in productArray"
              :key="index"
              :offset="index % 5 === 0 ? 0 : 1"
            >
              <el-card class="card" :body-style="{ padding: '0px' }">
                <!-- <img :src="product.imageURL" class="image" /> -->
                <img
                  src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                  class="image"
                />
                <div style="padding: 14px">
                  <span class="productName">{{ product.productName }}</span>
                  <div class="bottom clearfix">
                    <div class="description">{{ product.description }}</div>
                    <div class="price">{{ product.price }}元</div>
                    <el-button type="text" class="button">操作按钮</el-button>
                  </div>
                  <!-- <div class="description">{{ product.description }}</div> -->
                  <!-- <div class="price">价格：{{ product.price }}</div>
                  <el-button type="text" class="button">操作按钮</el-button> -->
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { allProducts } from "@/api/prod";
export default {
  data() {
    return {
      input: "",
      productList: [],
    };
  },
  mounted() {
    // 在组件挂载后发送异步请求获取商品信息
    this.fetchProductList();
  },
  computed: {
    filteredProductList() {
      // 将商品列表切割为每行五个商品的数组
      const chunkedProducts = [];
      const chunkSize = 5;

      for (let i = 0; i < this.productList.length; i += chunkSize) {
        chunkedProducts.push(this.productList.slice(i, i + chunkSize));
      }
      console.log(chunkedProducts);
      return chunkedProducts;
    },
  },
  methods: {
    fetchProductList() {
      allProducts()
        .then((response) => {
          const result = response.data;
          if (result.code === 1) {
            console.log(response.data);
            // 更新商品列表数据
            this.productList = result.data;
          } else {
            console.error(result.msg);
          }
        })
        .catch((error) => {
          console.error("获取商品列表时出错:", error);
        });
    },
  },
};
</script>

<style>
.row {
  margin-bottom: 20px;
}
.productName {
  display: -webkit-box;
  -webkit-line-clamp: 1; /* 指定行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.description {
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 指定行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 18px;
  height: 36px;
  font-size: 12px;
  color: #909399;
}

.price {
  margin-top: 25px;
  font-size: 30px;
  color: #f56c6c;
}

.card {
  text-align: left;
  /* line-height: 0px; */
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.logo {
  width: 50px;
  height: 50px;
  background-image: url("../assets/20231125021856.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: absolute;
  left: 25%;
  margin-left: -25px;
}
.search {
  left: 5%;
  position: relative;
  align-items: center;
  width: auto;
}

.el-input {
  width: 35rem;
  margin-left: auto;
  margin-right: 25%;
}

.input .el-input-group__prepend {
  background-color: #fff;
}
.el-header {
  display: flex;
  justify-content: space-around;
  color: #333;
  text-align: center;
  align-items: center;
  line-height: 60px;
  background-color: #409eff;
}

.el-aside {
  color: #333;
  text-align: center;
  line-height: 100%;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  line-height: auto;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>