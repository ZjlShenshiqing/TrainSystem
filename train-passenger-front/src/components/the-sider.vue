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
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined /> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <user-outlined /> &nbsp; 车票查询
        </router-link>
      </a-menu-item>
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