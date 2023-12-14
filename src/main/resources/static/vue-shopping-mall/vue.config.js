const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer : {
    port:80,
    // proxy:process.env.VUE_APP_BASEURL
    // proxy:"http://8.134.204.233:8080",
    // proxy: {
    //   '/api': {
    //     target:process.env.VUE_APP_BASEURL,
    //     // target:'http://localhost:8080/',
    //     ws: false,
    //     secure: false,
    //     changeOrigin: true,
    //     pathRewrite:{
    //       '^/api':''
    //     },
    //   }
    // },
  }
})
