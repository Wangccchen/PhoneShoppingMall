<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-page-header @back="goBack" content="结算页面"> </el-page-header>
      </el-header>
      <el-main style="background-color: #fff">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>购买配送信息</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="openDialog"
              >修改信息</el-button
            >
          </div>

          <el-descriptions title="用户信息">
            <el-descriptions-item label="用户名">{{
              userInfo ? userInfo.username : ""
            }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{
              userInfo ? userInfo.phoneNumber : ""
            }}</el-descriptions-item>
            <el-descriptions-item label="配送地址">{{
              userInfo ? userInfo.address : ""
            }}</el-descriptions-item>
            <!-- 根据需要添加其他 el-descriptions-items -->
            <el-descriptions-item label="邮箱地址">
              {{ userInfo ? userInfo.email : "" }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
        <el-table :data="checkoutData.products">
          <el-table-column label="商品图片" width="150">
            <template slot-scope="scope">
              <img
                :src="'imgs/productPics/' + scope.row.product.imageURL"
                alt="Product Image"
                style="width: 100%; height: auto"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="商品名"
            prop="product.productName"
          ></el-table-column>
          <el-table-column label="商品数量" prop="quantity"></el-table-column>
          <el-table-column label="商品总价" prop="totalPrice">
            <template slot-scope="scope">
              {{ (scope.row.product.price * scope.row.quantity).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer>
        <div style="float: left">
          总价：{{ checkoutData.totalPrice.toFixed(2) }}
        </div>
        <div style="float: right">
          <el-button type="primary" @click="goToPay">去支付</el-button>
        </div>
      </el-footer>
    </el-container>
    <el-dialog
      title="信息修改"
      :visible.sync="dialogVisible1"
      width="30%"
      @close="handleDialogClose"
    >
      <el-form :model="form" ref="form" label-position="top">
        <el-form-item label="配送地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phoneNumber"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDialogCancel">取消</el-button>
        <el-button type="primary" @click="handleDialogConfirm">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <el-result
        icon="success"
        title="成功支付购买"
        subTitle="请前往邮件查收订单消息"
      >
        <template slot="extra">
          <el-button type="primary" size="medium" @click="goBack"
            >返回主页</el-button
          >
        </template>
      </el-result>
    </el-dialog>
  </div>
</template>

<script>
import { checkout, cleanByID, getCartId, sendEmail } from "@/api/cart";
import { getToken } from "@/utils/token";
import { mapState, mapMutations, mapActions } from "vuex";
import { jwtDecode } from "jwt-decode";
import { getDetailById, getProductByIDForUpdate, updateNums } from "@/api/prod";
export default {
  data() {
    return {
      userID: 0,
      checkoutData: null,
      dialogVisible: false,
      dialogVisible1: false,
      form: {
        address: "",
        email: "",
        phoneNumber: "",
      },
    };
  },
  methods: {
    sendMsg() {
      this.$notify({
        title: "邮件发送成功",
        message: "邮件已发送，请注意查收订单！",
        type: "success",
      });
    },
    open1() {
      this.$notify({
        title: "成功",
        message: "成功修改相关信息！",
        type: "success",
        position: "top-left",
      });
    },
    open4() {
      this.$notify.error({
        title: "取消",
        message: "您取消了修改！",
        position: "top-left",
      });
    },
    goBack() {
      this.dialogVisible = false;
      this.$router.push("/mall");
    },
    ...mapMutations("user", ["updateUserInfo"]),
    ...mapActions("user", ["updateUser"]),
    openDialog() {
      // 打开弹窗时将 form 字段初始化为当前用户信息
      this.form = { ...this.userInfo };
      this.dialogVisible1 = true;
    },
    handleDialogCancel() {
      this.dialogVisible1 = false;
      this.handleDialogClose();
      this.open4();
    },
    handleDialogClose() {
      // 关闭弹窗时清空 form 字段
      this.form = {
        address: "",
        email: "",
        phoneNumber: "",
      };
    },
    async handleDialogConfirm() {
      // 确认修改时只更新两者共有的属性，关闭弹窗
      const updatedFields = {};
      Object.keys(this.form).forEach((key) => {
        if (key in this.userInfo) {
          updatedFields[key] = this.form[key];
        }
      });

      this.updateUserInfo(updatedFields);
      await this.updateUser(); // 调用 updateUser action 发送请求
      this.dialogVisible1 = false;
      this.handleDialogClose();
      this.open1();
    },
    async fetchCheckoutData() {
      const userId = this.userID;
      var cartId = 0;
      if (userId) {
        const response = await getCartId(userId);
        cartId = response.data.data;
      } else {
        console.error("User ID not available.");
      }
      console.log(cartId);
      if (cartId) {
        const response2 = await checkout(cartId);
        this.checkoutData = response2.data.data;
      } else {
        console.error("Cart ID not available.");
      }
    },
    openFullScreen2() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      setTimeout(() => {
        loading.close();
      }, 2000);
    },
    successPay() {
      this.dialogVisible = true;
    },
    async goToPay() {
      //邮件发送
      //获得相关信息
      const info = { ...this.userInfo };
      // 构造购物车信息对象
      let cartData = {
        email: info.email,
        address: info.address,
        cartItems: {},
        totalPrice: this.checkoutData.totalPrice,
        //下面这个字段是用于更新product库存的作用
        cartForUpdateStore: [],
      };

      // 遍历 checkoutData 中的产品，构造购物车商品列表
      this.checkoutData.products.forEach((item) => {
        const productName = item.product.productName;
        const quantity = item.quantity;
        const productID = item.product.productID;
        cartData.cartItems[productName] = quantity;
        //把对应id和数量存进数组
        const item1 = {
          productID: productID,
          quantity: quantity,
        };
        cartData.cartForUpdateStore.push(item1);
      });
      const response = await sendEmail(cartData);
      if (response.data.code == 1) {
        console.log("邮件成功发送！");
      } else {
        console.log("出现错误:" + response.data.msg);
      }

      //购买完，发送完邮件要做相关处理

      //首先清空购物车
      //要清除checkoutData的内容
      //并且要把cartitems表对应的内容删除
      //在这里可以获取到当前userid找到对应的cartid
      const userID = info.userID;
      console.log(userID);
      const response1 = await getCartId(userID);
      if (response1.data.code == 1) {
        console.log("成功获取cartid！");
      } else {
        console.log("出现错误:" + response1.data.msg);
      }
      const cartID = response1.data.data;
      const response2 = await cleanByID(cartID);
      if (response2.data.code == 1) {
        console.log("成功清空购物车！");
      } else {
        console.log("出现错误:" + response2.data.msg);
      }

      //并且更新对应商品的存量
      //此处可以根据前面构造的cartData对象，利用id来获取对应商品的行
      cartData.cartForUpdateStore.forEach((item) => {
        getProductByIDForUpdate(item.productID).then((res) => {
          let product = res.data.data;
          //然后更新stock和salevolumn字段
          console.log(product);
          product.stockQuantity -= item.quantity;
          product.salesVolume += item.quantity;
          //重新写回
          updateNums(product)
            .then((resp) => {
              if (response2.data.code == 1) {
                console.log("成功更新商品销售信息！");
              }
            })
            .catch((error) => {
              console.error("Error while updating product data:", error);
            });
        });
      });

      //下面进行付款接口的编写
      //此处就不实现真实付款接口，测试的时候太麻烦
      this.openFullScreen2();
      setTimeout(() => {
        this.successPay();
        this.sendMsg();
      }, 2000);
    },
  },
  computed: {
    ...mapState("user", ["userInfo"]),
  },
  mounted() {
    const storedToken = getToken();

    if (storedToken) {
      // 解析 token 为 JSON 对象
      const decodedToken = jwtDecode(storedToken);
      // 从解析后的 JSON 对象中获取用户名或其他信息
      this.userID = decodedToken.userid;
      this.fetchCheckoutData();
    } else {
      this.$router.push("/mall");
    }
  },
};
</script>

<style>
.header {
  justify-content: left !important;
  color: #fff !important;
}
.el-page-header__content {
  color: #fff;
}
.el-descriptions {
  font-size: 18px;
}
.el-descriptions__title {
  font-size: 22px;
}
.el-card__header {
  font-size: 22px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 99%;
}
</style>