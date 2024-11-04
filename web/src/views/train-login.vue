<template>
  <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="onFinish"
      @finishFailed="onFinishFailed"
  >
    <a-form-item
        label="Username"
        name="username"
        :rules="[{ required: true, message: 'Please input your username!' }]"
    >
      <a-input v-model:value="formState.username" />
    </a-form-item>

    <a-form-item
        label="Password"
        name="password"
        :rules="[{ required: true, message: 'Please input your password!' }]"
    >
      <a-input-password v-model:value="formState.password" />
    </a-form-item>

    <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
      <a-checkbox v-model:checked="formState.remember">Remember me</a-checkbox>
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">Submit</a-button>
    </a-form-item>
  </a-form>
</template>
<script setup>
import { reactive } from 'vue';  // 引入 Vue 的 reactive 方法，用于创建响应式数据

// 定义表单状态对象，使用 reactive 使其成为响应式
const formState = reactive({
  username: '',  // 用户名，初始为空字符串
  password: '',  // 密码，初始为空字符串
  remember: true,  // 记住我选项，初始为 true
});

// 表单提交成功时的处理函数
const onFinish = values => {
  console.log('Success:', values);  // 打印提交成功的数据
};

// 表单提交失败时的处理函数
const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);  // 打印提交失败的错误信息
};
</script>