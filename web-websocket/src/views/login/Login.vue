<template>
  <div>
    <input
        type="text"
        v-model="loginForm.username"
        placeholder="请输入用户名" />
    <button @click="handleLoginButton()">登录</button>
  </div>
</template>

<script>

import {
  setUsername
} from "@/utils/username";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: ''
      }
    }
  },
  methods: {
    handleLoginButton() {
      const username = this.loginForm.username.trim()
      //验证用户名
      if (username.length<6){
        alert('请输入正确的用户名')
        return
      }
      //将用户名存入localStorage
      setUsername(username)
      //路由跳转
      this.$router.push({
        name: 'Home'
      })
    }
  },
}
</script>

<style scoped>

</style>
