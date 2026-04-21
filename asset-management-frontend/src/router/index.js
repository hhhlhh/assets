import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/assets',
    name: 'AssetList',
    component: () => import('../views/AssetList.vue')
  },
  {
    path: '/assets/add',
    name: 'AddAsset',
    component: () => import('../views/AddAsset.vue')
  },
  {
    path: '/assets/edit/:id',
    name: 'EditAsset',
    component: () => import('../views/EditAsset.vue'),
    props: true
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: () => import('../views/Statistics.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router