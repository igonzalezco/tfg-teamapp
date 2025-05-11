import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: {
        name: 'auth'
      }
    },
    {
      path: '/auth',
      name: 'auth',
      component: () => import('@/views/AuthView.vue'),
    },
    {
      path: '/content',
      name: 'content',
      component: () => import('@/views/ContentView.vue'),
      redirect: {
        name: 'initContent'
      },
      meta: {
        requiresAuth: true
      },
      children: [
        {
          path: '/content/initContent',
          name: 'initContent',
          component: () => import('@/views/InitView.vue')
        }
      ]
    }
  ],
})

export default router
