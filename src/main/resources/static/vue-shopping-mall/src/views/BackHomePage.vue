<template>
  <div id="pdView">
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header
        id="title"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <span>手机商城后台管理系统</span>
        <el-button type="danger" @click="exit">退出</el-button>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-openeds="['1', '5']">
            <el-submenu index="1">
              <template slot="title"
                ><i class="el-icon-message"></i>基本操作</template
              >

              <el-menu-item index="1-1">
                <router-link to="/backsystem/products"> 商品清单 </router-link>
              </el-menu-item>
              <el-menu-item index="1-2">
                <router-link to="/backsystem/users"> 用户信息 </router-link>
              </el-menu-item>
              <el-menu-item index="1-3">
                <router-link to="/backsystem/logs"> 操作日志 </router-link>
              </el-menu-item>
              <el-menu-item index="1-4">
                <router-link to="/backsystem/reports"> 销售图表 </router-link>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <!-- 主页面显示 -->
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
  
  <script>
import { removeToken } from "@/utils/token";
import Router from "@/router";
export default {
  data() {
    return {};
  },
  methods: {
    exit() {
      removeToken();
      Router.replace("/back/login");
      console.log("跳转至登陆界面！");
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