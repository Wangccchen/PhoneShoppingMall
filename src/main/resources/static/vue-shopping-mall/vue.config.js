const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer : {
    port:90,
    //proxy: 'http://localhost:8080', // 后端API的地址
  }
})
