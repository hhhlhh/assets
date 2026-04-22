import { createRouter, createWebHistory } from 'vue-router'
import AssetManagement from '../views/AssetManagement.vue'
import Statistics from '../views/Statistics.vue'

const routes = [
  {
    path: '/',
    name: 'AssetManagement',
    component: AssetManagement,
    meta: {
      title: '资产管理'
    }
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics,
    meta: {
      title: '统计报表'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard to update page title
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 固定资产管理系统'
  }
  next()
})

export default router