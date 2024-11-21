<template>
  <a-layout-header class="header">
    <div class="logo" />
    <div style="float: right; color: white;">
        欢迎使用后台管理控制台
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
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
    </a-menu>
  </a-layout-header>
</template>

<script setup>
import { ref, watch } from "vue";
import store from "@/store";
import router from "@/router";
// 使用 ref 定义响应式数据
const selectedKeys1 = ref([]); // 默认选中第一项
// 不用设置成响应式变量，因为不会修改member
let member = store.state.member;
// 监视当前路由的变化，当有变化就执行当前回调函数
watch(() => router.currentRoute.value.path, (newValue) => {
  console.log('watch', newValue);
  selectedKeys1.value = [];
  selectedKeys1.value.push(newValue);
},{immediate: true})
</script>

<style scoped>

</style>

