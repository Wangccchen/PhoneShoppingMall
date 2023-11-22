<template>
  <div class="login-box">
    <div class="title-container">
      <h3 class="head-title">手机商城后台管理登陆系统</h3>
    </div>
    <h1 class="title">管理员注册</h1>
    <div class="login">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="登录用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="配送地址" prop="address">
          <el-input v-model="ruleForm.address"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" prop="phoneNumber">
          <el-input v-model="ruleForm.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm('ruleForm')"
            :loading="loading"
            >立即创建</el-button
          >
          <el-button type="danger" @click="resetForm('ruleForm')"
            >重置</el-button
          >
          <el-button type="info" @click="returnTo('ruleForm')">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { add } from "../api/user";
export default {
  name: "backRegister",
  components: { axios },
  data() {
    return {
      loading: false,
      ruleForm: {
        username: "",
        password: "",
        email: "",
        address: "",
        phoneNumber: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名称", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "change" },
          {
            min: 6,
            max: 16,
            message: "长度在6到16个英文或数字",
            trigger: "blur",
          },
        ],
        email: [{ required: true, message: "请输入邮箱地址", trigger: "blur" }],
        address: [
          { required: true, message: "请输入配送地址", trigger: "blur" },
        ],
        phoneNumber: [
          { required: true, message: "请输入手机号", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true;
          //调用后端接口进行创建
          add(this.ruleForm).then((res) => {
            if (res.data.code == 1) {
              alert("创建成功!即将跳转至登录页面...");
              this.$router.push("/back/login");
            } else {
              alert("未知原因创建失败！请联系管理员");
              this.loading = false;
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    returnTo(formName) {
      this.$refs[formName].resetFields();
      this.$router.push({
        path: "/back/login",
        name: "backLogin",
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