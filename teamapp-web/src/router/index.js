import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '@/stores/auth';
import { isTokenExpired } from '@/utils/token';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
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

router.beforeEach((to, from, next) => {
  const auth = authStore();
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (auth.isLoggedIn && !isTokenExpired()) {
      next();
      return;
    }
    auth.logout;
    next('/auth')
  } else {
    if (from.path == "/" && to.path == "/auth" && auth.isLoggedIn && !isTokenExpired()) {
      next('/content/initContent');
    } else {
      next();
    }
  }
})

export default router
