<template>
  <div>
    <table>
      <thead>
      <tr>
        <th>消息编号</th>
        <th>发送者</th>
        <th>发送时间</th>
        <th>发送内容</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in messageList" :key="item.time">
        <td>{{ item.id }}</td>
        <td>{{ item.username }}</td>
        <td>{{ new Date(item.time).toLocaleTimeString() }}</td>
        <td>{{ item.message }}</td>
      </tr>
      </tbody>
    </table>
    <input
        type="text"
        v-model="sendMessage"
        placeholder="请输入你要发送的消息">
    <button @click="handleSendButton()">发送</button>
    <button @click="handleLogoutButton()">退出</button>
  </div>
</template>

<script>

import {
  getUsername,
  removeUsername
} from "@/utils/username";

export default {
  name: "Home",
  data() {
    return {
      webSocketObject: null,
      username: '',
      messageList: [

      ],
      sendMessage: ''
    }
  },
  created() {
    //从localStorage中获得username
    this.username = getUsername()
    //如果username不存在返回到登录页面
    if (!this.username){
      this.$router.push({
        name: 'Login'
      })
    }
    //初始化WebSocket
    this.webSocketInit()
  },
  beforeDestroy() {
    this.webSocketObject.close();//在该组件销毁时关闭该连接以节约资源
  },
  methods: {
    webSocketInit(){
      const webSocketUrl = 'ws://localhost:8000/websocket/'+this.username
      this.webSocketObject = new WebSocket(webSocketUrl);
      this.webSocketObject.onopen = this.webSocketOnOpen
      this.webSocketObject.onmessage = this.webSocketOnMessage
      this.webSocketObject.onerror = this.webSocketOnError
      this.webSocketObject.onclose = this.webSocketOnClose
    },
    webSocketOnOpen(e){
      console.log('与服务端连接打开->',e)
    },
    webSocketOnMessage(e){
      console.log('来自服务端的消息->',e)
      const receiveMessage = JSON.parse(e.data);
      this.messageList.push(receiveMessage)
    },
    webSocketOnError(e){
      console.log('与服务端连接异常->',e)
    },
    webSocketOnClose(e){
      console.log('与服务端连接关闭->',e)
    },
    handleSendButton() {
      const username = this.username
      const message = this.sendMessage
      this.webSocketObject.send(JSON.stringify({
        id: 1,
        message,
        username,
        time: new Date().getTime()
      }))
      this.sendMessage = ''
    },
    handleLogoutButton(){
      removeUsername() //清除username然后断开连接
      this.webSocketObject.close();
      this.$router.push({
        name: 'Login'
      })
    }
  },
}
</script>

<style scoped>

</style>
