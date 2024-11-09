<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="1">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="1">
        <router-link to="/welcome">
          <user-outlined /> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import { ref } from "vue";
// 定义响应式变量 selectedKeys2 和 openKeys
const selectedKeys2 = ref(['1']); // 默认选中项，例如 ['1']
const openKeys = ref(['sub1']); // 默认展开项，例如 ['sub1']
</script>

<style scoped>

</style>