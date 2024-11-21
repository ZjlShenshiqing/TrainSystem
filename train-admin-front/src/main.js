import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
import './assets/js/enums';

/**
 * Axios 拦截器
 */
// 请求拦截器
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});

// 响应拦截器
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

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