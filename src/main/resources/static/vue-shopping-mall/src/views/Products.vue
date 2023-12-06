<template>
  <div>
    <el-container>
      <el-header>
        <div class="logo"></div>
        <div class="search">
          <el-input placeholder="请输入内容" v-model="input" class="input">
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="searchForProduct"
            ></el-button>
          </el-input>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-openeds="['1', '5']">
            <el-menu-item index="1" @click="fetchProductList">
              全部
            </el-menu-item>

            <el-menu-item index="2" @click="getHighPhone">
              销量最高
            </el-menu-item>
            <el-menu-item-group>
              <template slot="title">品牌</template>
              <el-menu-item index="3" @click="getHUAWEI">
                HUAWEI 华为 手机
              </el-menu-item>
              <el-menu-item index="4" @click="getAPPLE">
                APPLE 苹果 手机
              </el-menu-item>
            </el-menu-item-group>
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
              <el-card
                class="card"
                :body-style="{ padding: '0px' }"
                shadow="hover"
              >
                <img
                  :src="'imgs/productPics/' + product.imageURL"
                  class="image"
                />
                <!-- <img
                  src="../assets/productPics/Apple iPhone 13.jpg"
                  class="image"
                /> -->
                <div style="padding: 14px">
                  <span class="productName">{{ product.productName }}</span>
                  <div class="bottom clearfix">
                    <div class="description">{{ product.description }}</div>
                    <div class="price">{{ product.price }}元</div>
                    <el-button
                      type="text"
                      class="button"
                      @click="showDetailAndGetID(product)"
                      >查看详情</el-button
                    >
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
    <el-drawer
      title="商品详情"
      :visible.sync="prodVisible"
      direction="ltr"
      size="50%"
    >
      <img
        :src="'imgs/productPics/' + this.singleProd.prodImage"
        class="imageDetail"
      />
      <div style="padding: 14px">
        <span class="productName1">{{ this.singleProd.prodName }}</span>
        <div class="bottom clearfix">
          <div class="description1">{{ this.singleProd.prodDescrip }}</div>
          <div class="description1">已售：{{ this.singleProd.prodVolume }}</div>
          <div class="description1">还剩：{{ this.singleProd.prodStocks }}</div>
          <div class="price1">{{ this.singleProd.prodPrice }}元</div>
          <el-button type="danger" round class="button1" @click="addProduct"
            >加入购物车</el-button
          >
        </div>
        <!-- <div class="description">{{ product.description }}</div> -->
        <!-- <div class="price">价格：{{ product.price }}</div>
                  <el-button type="text" class="button">操作按钮</el-button> -->
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {
  allProducts,
  searchByKeywords,
  getHighPhone,
  selectById,
  getDetailById,
} from "@/api/prod";
import { getCartId, addProduct } from "@/api/cart";
export default {
  data() {
    return {
      input: "",
      productList: [],

      prodVisible: false,

      singleProd: {
        prodID: "",
        prodImage: "",
        prodName: "",
        prodDescrip: "",
        prodPrice: "",
        prodVolume: "",
        prodStocks: "",
      },
      // 构造 cartItem 对象
      cartItem: {
        cartItemID: 0,
        cartID: "",
        productID: "",
        quantity: 1,
      },
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
      return chunkedProducts;
    },
  },
  methods: {
    open4() {
      this.$message.error("请先进行登录操作！");
    },
    open1() {
      this.$notify({
        title: "添加成功！",
        message: "您已经成功添加一件商品到购物车！",
        type: "success",
      });
    },
    async showDetailAndGetID(product) {
      this.singleProd.prodID = product.productID;
      let res;
      try {
        // 获取到商品的id之后，下面发一个axios请求
        // 以此来触发检查登录的拦截器
        res = await getDetailById(product.productID);
        console.log(res.data);

        // 如果返回的是未登录，则跳转至登录界面
        // 实际上在axios添加了响应拦截器，已经提前跳转了
        // 此处是弹出提示框
        if (res.data.code == 0 && res.data.msg == "NOT_LOGIN_USER") {
          this.open4();
          return;
        } else {
          this.prodVisible = true;
          this.singleProd.prodImage = product.imageURL;
          this.singleProd.prodDescrip = product.description;
          this.singleProd.prodName = product.productName;
          this.singleProd.prodPrice = product.price;
          this.singleProd.prodStocks = product.stockQuantity;
          this.singleProd.prodVolume = product.salesVolume;
        }
      } catch (error) {
        // 处理请求失败的情况
        console.error("Error fetching details:", error);
        // 可以添加适当的错误处理逻辑
        return;
      }

      // 异步操作完成后执行以下代码

      // console.log(this.singleProd.prodImage);
      // 此处应该记录日志相关信息
    },
    async addProduct() {
      try {
        this.open1();
        // 从 Vuex 获取用户 ID
        const userId = this.$store.state.user.userInfo.userID;
        // 调用封装好的 axios 请求获取 cartId
        const cartId = await getCartId(userId);

        this.cartItem.cartID = cartId.data.data;
        this.cartItem.productID = this.singleProd.prodID;

        console.log(this.cartItem);
        // 调用封装好的 axios 请求添加商品到购物车
        await addProduct(this.cartItem);

        // 在这里可以处理成功添加商品到购物车的逻辑
        console.log("Product added to the cart successfully!");
      } catch (error) {
        // 在这里处理错误
        console.error("Error while adding product to the cart:", error);
      }
    },
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
    searchForProduct() {
      searchByKeywords(this.input)
        .then((response) => {
          console.log(response.data);
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
          console.error("搜索商品时出错:", error);
        });
      this.input = "";
    },
    getHighPhone() {
      getHighPhone()
        .then((response) => {
          console.log(response.data);
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
          console.error("无法获取:", error);
        });
    },
    getHUAWEI() {
      searchByKeywords("HUAWEI")
        .then((response) => {
          console.log(response.data);
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
          console.error("出错:", error);
        });
    },
    getAPPLE() {
      searchByKeywords("APPLE")
        .then((response) => {
          console.log(response.data);
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
          console.error("出错:", error);
        });
    },
  },
};
</script>

<style>
.imageDetail {
  width: 50%;
}
.row {
  margin-bottom: 20px;
}
.productName {
  display: -webkit-box;
  -webkit-line-clamp: 1; /* 指定行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.productName1 {
  display: -webkit-box;
  -webkit-line-clamp: 1; /* 指定行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: 50px;
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
.description1 {
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 指定行数 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 25px;
  height: 50px;
  font-size: 25px;
  color: #909399;
}
.price {
  margin-top: 25px;
  font-size: 30px;
  color: #f56c6c;
}
.price1 {
  margin-top: 25px;
  font-size: 50px;
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
.button1 {
  float: right;
  size: medium;
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

.input {
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