<template>
  <a-layout id="components-layout-demo-top-side-2">
    <!-- Header 部分 -->
    <the-header-view></the-header-view>

    <!-- 主布局部分 -->
    <a-layout>
      <!-- Sider 侧边栏部分 -->
      <the-sider-view></the-sider-view>

      <!-- 内容布局 -->
      <a-layout style="padding: 0 24px 24px">
        <!-- 内容区域 -->
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <!-- 子路由页面会在这里渲染 -->
          <router-view></router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref } from 'vue';
import TheHeaderView from "@/components/the-header.vue";
import TheSiderView from "@/components/the-sider.vue";
const openKeys = ref(['sub1']);
</script>
<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>