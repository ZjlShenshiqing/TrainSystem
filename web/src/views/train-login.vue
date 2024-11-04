<template>
  <div class="dynamic-background"></div>
  <a-row class="login" type="flex" justify="center" align="middle">
    <a-col :xs="22" :sm="18" :md="12" :lg="8" class="login-main">
      <h1 class="login-title"><rocket-two-tone />&nbsp;12306售票系统</h1>
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
      >
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号!' }]"
        >
          <a-input v-model:value="loginForm.mobile" placeholder="请输入您的手机号"/>
        </a-form-item>

        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
        >
          <a-input v-model:value="loginForm.code" placeholder="请输入验证码">
            <template #addonAfter>
              <a @click="sendCode" class="get-code-button">获取验证码</a>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block class="login-button" @click="login">登录</a-button>
        </a-form-item>

      </a-form>
    </a-col>
  </a-row>
</template>

<script>
import { defineComponent, reactive } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import { useRouter } from 'vue-router'
import store from "@/store";

export default defineComponent({
  name: "login-view",
  setup() {
    const router = useRouter();

    const loginForm = reactive({
      mobile: '',
      code: '',
    });

    const sendCode = () => {
      axios.post("/member/member/send-code", {
        mobile: loginForm.mobile
      }).then(response => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '发送验证码成功！' });
          loginForm.code = "8888";
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const login = () => {
      axios.post("/member/member/login", loginForm).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '登录成功！' });
          // 登录成功，跳到控台主页
          router.push("/welcome");
          store.commit("setMember", data.content);
        } else {
          notification.error({ description: data.message });
        }
      })
    };

    return {
      loginForm,
      sendCode,
      login
    };
  },
});
</script>

<style>
.login-main h1 {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.login-main {
  padding: 40px;
  border: 2px solid #e6e6e6;
  border-radius: 20px;
  background-color: #ffffff;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  z-index: 1;
  position: relative;
  overflow: hidden;
}

.login-button {
  transition: background-color 0.3s, transform 0.3s;
}

.login-button:hover {
  background-color: #40a9ff;
  transform: translateY(-2px);
}

.get-code-button {
  transition: color 0.3s;
}

.get-code-button:hover {
  color: #40a9ff;
}

.login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
  padding: 0;
  margin: 0;
  position: relative;
  overflow: hidden;
}

@keyframes moveBackground {
  0% { transform: translate(0, 0); }
  50% { transform: translate(-50px, -50px); }
  100% { transform: translate(0, 0); }
}

.dynamic-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(circle, #e0f7fa, #b2ebf2, #80deea, #4dd0e1, #26c6da);
  animation: moveBackground 20s infinite alternate;
  z-index: -1;
  filter: blur(100px);
}
</style>