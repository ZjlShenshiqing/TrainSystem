import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue';
import store from './store'
import 'ant-design-vue/dist/antd.css'; // 导入样式
import * as Icons from '@ant-design/icons-vue'

const app = createApp(app);
app.use(Antd).use(store).use(router).mount('#app')

// 全局使用图标
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}
