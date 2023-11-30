<template>
  <div class="header">
    <div class="nav-topbar">
      <div class="container">
        <div class="top-menu">
          <a href="javascript:;">顶尖智能手机商城</a>
          <a href="javascript:;">WC-PhoneMall</a>
          <a href="javascript:;">其他服务</a>
          <a href="javascript:;">协议规则</a>
        </div>
        <div class="top-user">
          <el-dropdown
            class="userNav"
            trigger="click"
            v-if="username != ''"
            @command="handleCommand"
          >
            <span class="el-dropdown-link">
              欢迎您： {{ username
              }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              <el-dropdown-item command="delete">注销用户</el-dropdown-item>
              <el-dropdown-item command="gotoCart">我的购物车</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <a href="javascript:;" v-if="!username" @click="login">登录</a>
          <a href="javascript:;" v-if="!username" @click="register">注册</a>
          <a href="javascript:;" v-if="!username" @click="goToBack"
            >后台管理系统</a
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getToken, removeToken } from "@/utils/token";
import { jwtDecode } from "jwt-decode";
import { cancel } from "@/api/user";
// const jwt_decode = require("jwt-decode");
export default {
  name: "nav-header",
  data() {
    return {
      username: "",
    };
  },

  mounted() {
    setTimeout(() => {
      const storedToken = getToken();

      if (storedToken) {
        // 解析 token 为 JSON 对象
        const decodedToken = jwtDecode(storedToken);
        // 将解析后的用户信息存储到 Vuex 中
        this.$store.dispatch("user/setUserInfo", decodedToken);
        // 从解析后的 JSON 对象中获取用户名或其他信息
        this.username = decodedToken.username;
      }
    }, 0);
  },
  methods: {
    handleCommand(command) {
      if (command == "logout") {
        this.logout();
      } else if (command == "cancel") {
        this.cancelUser();
      } else if (command == "gotoCart") {
        this.gotoCart();
      }
    },
    login() {
      this.$router.push("/login");
    },
    register() {
      this.$router.push("/register");
    },
    goToBack() {
      this.$router.push("/back/login");
      // Router.replace("/back/login");
    },
    async cancelUser() {
      try {
        // 从 Vuex 获取用户 ID
        const userId = this.$store.state.user.userInfo.userID;

        // 发送取消删除用户的 Axios 请求
        const response = await cancel(userId);

        // 处理响应，可以根据后端返回的信息进行进一步的操作
        console.log("注销成功! :", response.data);
        //注销后退出登录
        this.logout();
      } catch (error) {
        console.error("注销用户发现错误!:", error);
      }
    },
    logout() {
      // 清空 Vuex 中的用户信息和本地存储的 token
      this.$store.dispatch("user/clearUserInfo");
      removeToken();
      // 重新加载页面
      window.location.reload();
    },
    gotoCart() {
      this.$router.push("/mall/cart");
    },
  },
};
</script>

<style lang="scss">
@import "./../assets/scss/base.scss";
@import "./../assets/scss/mixin.scss";
@import "./../assets/scss/config.scss";

.userNav {
  font-size: 17px;
}
.el-dropdown-link {
  cursor: pointer;
  color: #b0b0b0;
}
.el-icon-arrow-down {
  font-size: 12px;
}

.header {
  .nav-topbar {
    height: 39px;
    line-height: 39px;
    background-color: #333333;
    color: #b0b0b0;

    .container {
      @include flex();

      a {
        display: inline-block;
        color: #b0b0b0;
        margin-right: 17px;

        &:hover {
          color: #ffffff;
        }
      }
    }
  }

  .nav-header {
    .container {
      height: 112px;
      @include flex();
      position: relative;

      .header-logo {
        display: inline-block;
        width: 55px;
        height: 55px;
        background-color: $colorA;

        a {
          display: inline-block;
          width: 110px;
          height: 55px;

          &:before {
            content: " "; //给图片留白,一定要留白占位置
            transition: margin 0.2s;
          }

          &:after {
            content: " ";
          }

          //鼠标移上去的时候切换图片，前面的图片向左移动55px
          &:hover:before {
            margin-left: -55px;
            transition: margin 0.2s; //过渡动画效果
          }
        }
      }

      .header-menu {
        display: inline-block;
        padding-left: 209px;
        width: 643px;

        .item-menu {
          display: inline-block;
          line-height: 112px;
          margin-right: 15px;
          color: #333333;
          font-size: 16px;
          font-weight: bold; //加粗字体

          span {
            cursor: pointer; //鼠标放上去就会出现一个小手
          }

          &:hover {
            color: $colorA;

            .children {
              height: 220px;
              opacity: 1;
            }
          }

          .children {
            position: absolute;
            top: 112px;
            left: 0px;
            width: 1226px;
            border-top: 1px solid #e5e5e5;
            box-shadow: 0px 7px 6px 0px rgba(0, 0, 0, 0.11);
            z-index: 10;
            height: 0;
            opacity: 0; //透明度为0
            overflow: hidden; //内容会被修剪，并且其余内容是不可见的。
            transition: all 0.5s;
            background-color: #ffffff;

            .product {
              float: left;
              height: 220px;
              width: 16.6%;
              font-size: 12px;
              line-height: 12px;
              text-align: center;
              position: relative;

              a {
                display: inline-block;
              }

              img {
                width: auto;
                height: 111px;
                margin-top: 26px;
              }

              .product-img {
                height: 137px;
              }

              .product-name {
                font-weight: bold; //字体加粗
                margin-top: 19px;
                margin-bottom: 8px;
                color: $colorB;
              }

              .product-price {
                color: $colorA;
              }

              &:before {
                content: " ";
                position: absolute;
                top: 28px;
                right: 0px;
                border-right: 1px solid $colorF;
                height: 100px;
                width: 1px;
              }

              &:last-child::before {
                display: none; //最后一个before元素不显示
              }
            }
          }
        }
      }

      .header-search {
        width: 319px;
        display: flex;
        flex-direction: row;
        align-items: center;
        height: 50px;
        border: 1px solid #e0e0e0;

        input {
          box-sizing: border-box; //设置内边距包括在总宽度
          width: 264px;
          height: 50px;
          border: none;
          border-right: 1px solid #e0e0e0;
          padding-left: 14px;
        }

        a {
          margin-left: 17px;
        }
      }
    }
  }
}
</style>