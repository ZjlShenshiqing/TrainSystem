<template>
  <a-layout-header class="header">
    <div class="logo" />
    <div style="float: right; color: white;">
      您好：{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login" style="color: white">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
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
  </a-layout-header>
</template>

<script setup>
import { ref } from "vue";
import store from "@/store";
  // 使用 ref 定义响应式数据
  const selectedKeys1 = ref(['1']); // 默认选中第一项
  // 不用设置成响应式变量，因为不会修改member
  let member = store.state.member;
</script>

<style scoped>

</style>
