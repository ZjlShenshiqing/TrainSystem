import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/train-login.vue')
  },
  {
    path: '/welcome',
    name: 'welcome',
    component: () => import('../views/train-welcome.vue')
  },
  {
    path: '/',
    name: 'main',
    component: () => import('../views/train-main.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
