import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import Antd, {notification} from 'ant-design-vue';
import store from './store';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

// 请求拦截器
axios.interceptors.request.use(
    function (config) {
        console.log("请求参数：", config); // 打印请求的配置信息
        // 添加token
        const token = store.state.member.token;
        // 通过有值，就会通过判断
        if (token) {
            config.headers.token = token;
            // config 是 Axios 在发送请求时的配置信息对象，其中包含请求的 URL、方法、请求头、请求参数等信息。
            console.log("请求headers增加token", token);
        }
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
        // 针对401错误做特殊的处理
        const response = error.response;
        const status = response.status;
        if (status == 401) {
            console.log("未登录或超时，跳到登录页");
            // 将token置空
            store.commit("setMember", {});
            // 弹窗，给用户提示
            notification.error({ description: "未登录或超时" });
            router.push('/login');
        }
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