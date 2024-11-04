import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import Antd from 'ant-design-vue';
import store from './store';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';

const app = createApp(App); // 正确地创建应用实例

// 使用 Ant Design Vue、Vuex Store 和 Vue Router
app.use(Antd).use(store).use(router);

// 全局注册图标组件
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

// 挂载到 #app 元素
app.mount('#app');
