import { createApp } from 'vue'
import App from './App.vue'
// 导入router文件夹下index.js
import router from "./router";
// 导入store文件夹下index.js
import store from "./store";
// 引入Element Plus组件库
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 导入全局样式表
import "./assets/css/global.css";
// 引入axios
import axios from "axios";
import Cookies from "js-cookie";
// 给axios组件设置全局api地址
axios.defaults.baseURL = "http://localhost:8888/mas/";
// 请求拦截器
axios.interceptors.request.use(config => {
  // axios通过headers传参，设置请求头token
  config.headers['token'] = Cookies.get('token');
  return config;
});

const app = createApp(App)
// 挂载axios
app.config.globalProperties.$axios = axios
//app.$axios = axios;
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(store).use(router).use(ElementPlus);
app.provide('$axios',axios);
app.provide('$router',router);
app.mount("#app");

