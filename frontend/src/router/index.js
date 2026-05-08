import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/quick-check',
    name: 'QuickCheck',
    component: () => import('../views/QuickCheck.vue')
  },
  {
    path: '/history',
    name: 'ExerciseHistory',
    component: () => import('../views/ExerciseHistory.vue')
  },
  {
    path: '/goals',
    name: 'MyGoals',
    component: () => import('../views/MyGoals.vue')
  },
  {
    path: '/calendar',
    name: 'Calendar',
    component: () => import('../views/Calendar.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
