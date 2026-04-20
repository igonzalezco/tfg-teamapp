import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '@/stores/auth'
import { isTokenExpired } from '@/utils/token'
import routes from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

router.beforeEach((to, from, next) => {
  const auth = authStore()
  const authenticated = auth.isLoggedIn && !isTokenExpired()

  if (!authenticated) {
    auth.logout()

    if (to.name === 'auth') {
      next()
    } else {
      next({ name: 'auth' })
    }
    return
  }

  if (to.path === '/auth' || (from.path === '/auth' && to.name !== 'dashboard')) {
    if (auth.hasTeams) {
      next({
        name: 'dashboard',
        params: {
          id: auth.getSelectedTeam.equipo.id,
        },
      })
    } else if (to.name !== 'initContent') {
      next({ name: 'initContent' })
    } else {
      next()
    }
    return
  }

  next()
})

export default router
