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
      :visible.sync="dialogVisible"
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
  </div>
</template>

<script>
import { checkout, getCartId, sendEmail } from "@/api/cart";
import { getToken } from "@/utils/token";
import { mapState, mapMutations, mapActions } from "vuex";
import { jwtDecode } from "jwt-decode";
export default {
  data() {
    return {
      userID: 0,
      checkoutData: null,
      dialogVisible: false,
      form: {
        address: "",
        email: "",
        phoneNumber: "",
      },
    };
  },
  methods: {
    sendMsg() {
      this.$message("邮件已发送，请注意查收订单！");
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
      this.$router.push("/mall");
    },
    ...mapMutations("user", ["updateUserInfo"]),
    ...mapActions("user", ["updateUser"]),
    openDialog() {
      // 打开弹窗时将 form 字段初始化为当前用户信息
      this.form = { ...this.userInfo };
      this.dialogVisible = true;
    },
    handleDialogCancel() {
      this.dialogVisible = false;
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
      this.dialogVisible = false;
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
      this.$alert("您已成功购买！请登录邮箱查看邮件订单信息！", "通知", {
        confirmButtonText: "返回主页",
        showClose: false,
      });
    },
    async goToPay() {
      //下面进行付款接口的编写
      //此处就不实现真实付款接口，测试的时候太麻烦
      this.openFullScreen2();
      this.successPay();
      //邮件发送
      //获得相关信息
      const info = { ...this.userInfo };
      // 构造购物车信息对象
      let cartData = {
        email: info.email,
        address: info.address,
        cartItems: {},
        totalPrice: this.checkoutData.totalPrice,
      };

      // 遍历 checkoutData 中的产品，构造购物车商品列表
      this.checkoutData.products.forEach((item) => {
        const productName = item.product.productName;
        const quantity = item.quantity;
        cartData.cartItems[productName] = quantity;
      });
      const response = await sendEmail(cartData);
      if (response.data.code == 1) {
        console.log("邮件成功发送！");
      } else {
        console.log("出现错误:" + response.data.msg);
      }
      this.sendMsg();

      //购买完，发送完邮件要清空购物车
      //并且更新对应商品的存量
      //首先清空购物车
      
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