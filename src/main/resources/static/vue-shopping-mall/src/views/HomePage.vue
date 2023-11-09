<template>
  <div id="pdView">
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header id="title">小叶子手机商城后台管理系统</el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-openeds="['1', '4']">
            <el-submenu index="1">
              <template slot="title"
                ><i class="el-icon-message"></i>基本操作</template
              >

              <el-menu-item index="1-1">
                <router-link to="/products"> 商品清单 </router-link>
              </el-menu-item>
              <el-menu-item index="1-2">
                <router-link to="/users"> 用户信息 </router-link>
              </el-menu-item>
              <el-menu-item index="1-3">操作日志</el-menu-item>
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
import axios from "axios";
export default {
  data() {},
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