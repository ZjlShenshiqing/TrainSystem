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
      {
        path: 'station',
        component: () => import('../views/main/train-station.vue'),
      },
      {
        path: 'train',
        component: () => import('../views/main/train-train.vue'),
      },
      {
        path: 'train-station',
        component: () => import('../views/main/train-trainAndStation.vue'),
      },
      {
        path: 'train-carriage',
        component: () => import('../views/main/train-carriage.vue'),
      },
      {
        path: 'train-seat',
        component: () => import('../views/main/train-seat.vue'),
      },
      {
        path: 'batch/job',
        name: 'batch/job',
        component: () => import('../views/main/train-job.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
