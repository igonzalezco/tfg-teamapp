import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { i18n } from './i18n/i18n'
import './assets/css/main.css'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import es from 'element-plus/es/locale/lang/es'
import dayjs from 'dayjs'
import 'dayjs/locale/es'

import Notifications from '@kyvg/vue3-notification'

dayjs.locale('es')

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.use(ElementPlus, {
  locale: es,
})

app.use(Notifications)

app.mount('#app')
