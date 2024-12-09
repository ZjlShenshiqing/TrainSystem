<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        :openKeys="['batch' , 'base' , 'business']"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined /> &nbsp; 关于
        </router-link>
      </a-menu-item>
      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined />
            后台任务管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <UnorderedListOutlined /> &nbsp; 任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined />
            基础数据
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <user-outlined /> &nbsp; 车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <user-outlined /> &nbsp; 车次管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-station">
          <router-link to="/base/train-station">
            <user-outlined /> &nbsp; 火车经停站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-carriage">
          <router-link to="/base/train-carriage">
            <user-outlined /> &nbsp; 火车车厢管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-seat">
          <router-link to="/base/train-seat">
            <user-outlined /> &nbsp; 火车座位管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedListOutlined />
            业务管理
          </span>
        </template>
        <a-menu-item key="/business/daily-train-ticket">
          <router-link to="/business/daily-train-ticket">
            <user-outlined /> &nbsp; 余票信息管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/confirm-order">
          <router-link to="/business/confirm-order">
            <user-outlined /> &nbsp; 订单信息管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train">
          <router-link to="/business/daily-train">
            <user-outlined /> &nbsp; 每日车次管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-station">
          <router-link to="/business/daily-train-station">
            <user-outlined /> &nbsp; 每日车次经停站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-carriage">
          <router-link to="/business/daily-train-carriage">
            <user-outlined /> &nbsp; 每日车次车厢管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-seat">
          <router-link to="/business/daily-train-seat">
            <user-outlined /> &nbsp; 每日车次座位管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import {ref, watch} from "vue";
import router from "@/router";
import 'ant-design-vue/dist/antd.css';  // 引入 Ant Design 样式

// 定义响应式变量 seletedKeys2 和 openKeys
const selectedKeys2 = ref([]); // 默认选中项
const openKeys = ref(['sub1']); // 默认展开项，例如 ['sub1']
// 监视当前路由的变化，当有变化就执行当前回调函数
watch(() => router.currentRoute.value.path, (newValue) => {
  console.log('watch', newValue);
  selectedKeys2.value = [];
  selectedKeys2.value.push(newValue);
},{immediate: true})
</script>

<style scoped>

</style>