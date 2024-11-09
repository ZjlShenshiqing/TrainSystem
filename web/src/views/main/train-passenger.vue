<template>
  <div class="welcome">
    <h1>常用旅客</h1>
    <a-button type="primary" @click="showModal">新增</a-button>
    <!-- visible 设置为true，代表着模态框要弹出来 -->
    <a-modal v-model:visible="visible" title="乘车人" @ok="handleOk">
      <p></p>
      <p></p>
      <p></p>
    </a-modal>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 初始值为false,不弹出模态框
const visible = ref(false)

// 当调用 showModal 时，设置为 true，这表示模态框应该显示
const showModal = () => {
  visible.value = true;
}

// 点击ok,关闭模态框
const handleOk = e => {
  console.log(e);
  visible.value = false;
}

</script>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 20px;
}
</style>