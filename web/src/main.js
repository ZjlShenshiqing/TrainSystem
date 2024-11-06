import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import Antd from 'ant-design-vue';
import store from './store';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

// 请求拦截器
axios.interceptors.request.use(
    function (config) {
        console.log("请求参数：", config); // 打印请求的配置信息
        // 可以在这里添加 token 或者修改请求头
        return config;
    },
    function (error) {
        // 请求错误处理
        console.log("请求错误：", error);
        return Promise.reject(error);
    }
);

// 响应拦截器
axios.interceptors.response.use(
    function (response) {
        console.log("返回结果：", response); // 打印响应的结果
        return response;
    },
    function (error) {
        console.log("返回错误：", error); // 打印错误信息
        return Promise.reject(error);
    }
);

const app = createApp(App);

// 使用 Ant Design Vue、Vuex Store 和 Vue Router
app.use(Antd).use(store).use(router);

// 全局注册图标组件
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

// 将 axios 挂载到全局，以便组件中可以通过 this.$axios 访问
app.config.globalProperties.$axios = axios;

app.mount('#app');

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('环境：', process.env.NODE_ENV);
console.log('环境：', process.env.VUE_APP_SERVER);