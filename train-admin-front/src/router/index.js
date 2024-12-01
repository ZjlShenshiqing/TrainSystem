import { createRouter, createWebHistory } from 'vue-router'

const routes = [
 {
  path: '',
  redirect: '/welcome', // 根路径重定向到 welcome
 },
 {
  path: '/',
  component: () => import('../views/train-main.vue'),
  children: [
   {
    path: 'welcome',
    component: () => import('../views/main/train-welcome.vue'),
   },
   {
    path: 'about',
    component: () => import('../views/main/train-about.vue'),
   },
   // 分类: 基础设置相关
   {
    path: 'base/', // 将基础设置相关的路径统一放入 base 路由下
    children: [
     {
      path: 'station',
      component: () => import('../views/main/base/train-station.vue'),
     },
     {
      path: 'train',
      component: () => import('../views/main/base/train-train.vue'),
     },
     {
      path: 'train-station',
      component: () => import('../views/main/base/train-trainAndStation.vue'),
     },
     {
      path: 'train-carriage',
      component: () => import('../views/main/base/train-carriage.vue'),
     },
     {
      path: 'train-seat',
      component: () => import('../views/main/base/train-seat.vue'),
     },
    ],
   },
   // 分类: 批量任务相关
   {
    path: 'batch/', // 将批量任务相关的路径统一放入 batch 路由下
    children: [
     {
      path: 'job',
      component: () => import('../views/main/batch/train-job.vue'),
     },
    ],
   },
   // 业务相关：每日生成车次等数据
   {
    path: 'business/',
    children: [
     {
      path: 'daily-train',
      component: () => import('../views/main/business/daily-train.vue'),
     },
     {
      path: 'daily-train-station',
      component: () => import('../views/main/business/daily-train-station.vue'),
     },
    ],
   },
  ],
 },
];

const router = createRouter({
 history: createWebHistory(process.env.BASE_URL),
 routes,
});

export default router;