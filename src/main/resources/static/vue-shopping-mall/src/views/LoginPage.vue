<template>
  <div class="login-box">
    <div class="title-container">
      <h3 class="head-title">顶尖数码产品商城用户登录</h3>
    </div>
    <div class="login">
      <el-form class="login-container" :model="loginForm" ref="loginForm">
        <h1 class="title">用户登录</h1>
        <el-form-item>
          <el-input
            type="text"
            v-model="loginForm.username"
            placeholder="用户名"
            autocomplete="on"
          ></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input
            :type="passwordType"
            v-model="loginForm.password"
            ref="password"
            placeholder="登录密码"
            autocomplete="on"
          ></el-input>
          <span class="show-pwd" @click="showPwd">
            <i class="el-icon-view"></i>
          </span>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            style="width: 100%"
            @click.native.prevent="doSubmit"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            type="info"
            style="width: 100%"
            @click="returnTo('loginForm')"
            >返回</el-button
          >
        </el-form-item>
        <el-row style="text-align: center; margin-top: -10px">
          <el-link type="primary" @click="gotoRegister()">用户注册</el-link>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
   
  <script>
import { setToken } from "@/utils/token";
import axios from "axios";
import { login } from "@/api/login";
export default {
  name: "login",
  components: { axios },
  data() {
    return {
      loginForm: {
        userID: "",
        username: "",
        password: "",
        email: "",
        address: "",
        phoneNumber: "",
        flag: "",
      },
      passwordType: "password",
    };
  },
  //页面跳转
  methods: {
    gotoRegister() {
      this.$router.push({
        path: "/register",
        name: "register",
        // query: {
        //   keywords: this.input,
        //   type: this.type,
        //   date: new Date().getTime(),
        // },
      });
    },
    async doSubmit() {
      try {
        const loginRes = await login(this.loginForm);
        if (loginRes.data.code === 1) {
          // 登录成功，保存 token
          setToken(loginRes.data.data);
          this.$message.success("登录成功！正在跳转......");

          // 获取用户信息并保存到 Vuex 中
          await this.$store.dispatch(
            "user/fetchUserInfo",
            this.loginForm.username
          );
          this.$router.push("/mall");
        } else {
          this.$message.error(loginRes.data.msg);
        }
      } catch (error) {
        // 处理请求失败情况
        console.error("Error during login:", error);
      }
    },
    //展示密码
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    returnTo(formName) {
      this.$refs[formName].resetFields();
      this.$router.push({
        path: "/mall",
        name: "mall",
      });
    },
  },
};
</script>
   
   
  <style scoped>
/* .login-box {
    background-color: #2d3a4b;
    min-height: 100%;
    width: 100%;
    overflow: hidden;
  } */

.login {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 2vh; /* 25% of the viewport height */
  background-repeat: no-repeat;
  background-position: center right;
  background-size: 100%;
}

.login-container {
  border-radius: 10px;
  width: 350px;
  padding: 30px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  text-align: left;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);
}

.title {
  margin: 0 auto 40px auto;
  text-align: center;
  color: #505458;
}

.head-title {
  margin: 100px auto 100px auto;
  text-align: center;
  color: #505458;
  font-size: 40px;
}
.show-pwd {
  position: absolute;
  right: 10px;
  font-size: 16px;
  color: #505458;
  cursor: pointer;
  user-select: none;
}
</style>