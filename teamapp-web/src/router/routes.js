const routes = [
  {
    path: '/',
    redirect: {
      name: 'auth',
    },
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
      name: 'initContent',
    },
    meta: {
      requiresAuth: true,
    },
    children: [
      {
        path: 'initContent',
        name: 'initContent',
        label: 'lang.menu.initContent',
        icon: 'House',
        permissions: [],
        visible: true,
        component: () => import('@/views/InitView.vue'),
      },
      {
        path: 'equipos/crear',
        name: 'createTeam',
        visible: false,
        component: () => import('@/views/CreateTeamView.vue'),
      },
      {
        path: 'equipos/:id/dashboard',
        name: 'dashboard',
        label: 'lang.menu.dashboard',
        icon: 'DataBoard',
        permissions: [],
        visible: true,
        component: () => import('@/views/DashboardView.vue'),
        props: true,
      },
      {
        path: 'equipos/:id/eventos',
        name: 'events',
        label: 'lang.menu.events',
        icon: 'Calendar',
        permissions: [],
        visible: true,
        component: () => import('@/views/EventView.vue'),
        props: true,
        meta: {
          requiresAuth: true,
        },
      },
      {
        path: 'equipos/:id/eventos/crear',
        name: 'createEvent',
        component: () => import('@/views/CreateEventView.vue'),
        props: true,
        meta: {
          requiresAuth: true,
        },
      },
    ],
  },
]

export default routes
