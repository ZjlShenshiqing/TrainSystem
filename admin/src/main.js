import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import './assets/js/enums';

const app = createApp(App)
app.use(store).use(router).mount('#app')

// 全局注册图标组件
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

// 请求拦截器
axios.interceptors.request.use(
    function (config) {
        console.log("请求参数：", config); // 打印请求的配置信息
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
        console.log("返回结果：", response); // 打印响应结果
        return response;
    },
    function (error) {
        console.log("返回错误：", error); // 打印错误信息

        // 检查 error.response 是否存在
        if (error.response) {
            const status = error.response.status;
            console.log("错误状态码：", status);
        } else {
            // 没有响应，可能是网络错误或服务器不可达
            console.error("请求失败，没有收到服务器响应");
            notification.error({ description: "网络错误，请检查您的网络连接" });
        }

        // 返回错误信息
        return Promise.reject(error);
    }
);
