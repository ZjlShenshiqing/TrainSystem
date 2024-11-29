<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
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
            <UnorderedListOutLined />
            后台任务管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <UnorderedListOutLined /> &nbsp; 任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu>
        <template>
          <span>
            <UnorderedListOutLined /> &nbsp; 基础数据
          </span>
        </template>
        <a-menu-item key="/station">
          <router-link to="/station">
            <user-outlined /> &nbsp; 车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train">
          <router-link to="/train">
            <user-outlined /> &nbsp; 车次管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train-station">
          <router-link to="/train-station">
            <user-outlined /> &nbsp; 火车经停站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train-carriage">
          <router-link to="/train-carriage">
            <user-outlined /> &nbsp; 火车车厢管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train-seat">
          <router-link to="/train-seat">
            <user-outlined /> &nbsp; 火车座位管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import {ref, watch} from "vue";
import router from "@/router";
// 定义响应式变量 selectedKeys2 和 openKeys
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