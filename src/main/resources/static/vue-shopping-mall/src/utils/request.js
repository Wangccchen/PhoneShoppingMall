import axios from "axios";
import Router from '@/router'
import { getToken,removeToken } from "./token";

// axios.defaults.baseURL = process.env.VUE_APP_BASEURL;

const service = axios.create({
    baseURL:process.env.VUE_APP_BASEURL,
    // baseURL:"http://8.134.204.233:8080",
    // baseURL: process.env.VUE_APP_BASE_API ,
    // baseURL:'/api',
    withCredentials: true,
    timeout: 500000 
  })

//axios请求拦截器，添加头部
service.interceptors.request.use(
  config=>{
    let token = getToken();
    if(token){
      //为请求添加token给后端识别
      config.headers['token'] = token;
    }
    return config;
  },
  error=>{
    console.log(error);
    return Promise.reject(error);
  }
)

//axios响应拦截器
service.interceptors.response.use(res=>{
  if (res.data.code === 0 && res.data.msg === 'NOT_LOGIN_USER') {// 返回登录页面
    console.log('用户未登录!');
    removeToken();
    Router.replace('/login');
    console.log("跳转至用户登录界面！");
    return res;
  } else if(res.data.code === 0 && res.data.msg === 'NOT_LOGIN'){
    console.log('管理员未登录!');
    removeToken();
    Router.replace('/back/login');
    console.log("跳转至后台登录界面！");
    return res;
  }else{
    return res;
  }
},
error => {
  console.log('err:' + error)
  let { message } = error;
  if (message == "Network Error") {
    message = "后端接口连接异常";
  }
  else if (message.includes("timeout")) {
    message = "系统接口请求超时";
  }
  else if (message.includes("Request failed with status code")) {
    message = "系统接口" + message.substr(message.length - 3) + "异常";
  }
  return Promise.reject(error)

})

export default service