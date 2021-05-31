import VueRouter from "vue-router";
import Vue from "vue";
Vue.use(VueRouter)

const routes = [
  { //登录页
    path: '/',
    name: 'Login',
    component: () => import('@/views/login/Login')
  },
  { //首页
    path: '/home',
    name: 'Home',
    component: () => import('@/views/home/Home')
  }
]

const router = new VueRouter({
  routes
})

export default router
