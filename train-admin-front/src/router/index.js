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
    path: 'base/',
    children: [
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
    ],
   },
   // 分类: 批量任务相关
   {
    path: 'batch/',
    children: [
     {
      path: 'job',
      component: () => import('../views/main/train-job.vue'),
     },
    ],
   },
  ],
 },
];