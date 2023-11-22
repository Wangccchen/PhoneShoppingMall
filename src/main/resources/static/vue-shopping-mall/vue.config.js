const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer : {
    port:39,
    proxy: {
      '/api': {
        target:'http://localhost:8080/',
        ws: false,
        secure: false,
        changeOrigin: true,
        pathRewrite:{
          '^/api':''
        },
      }
    },
    //proxy: 'http://localhost:8080', // 后端API的地址
  }
})
