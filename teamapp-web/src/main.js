import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { i18n } from './i18n/i18n'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import './assets/css/main.css'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import es from 'element-plus/es/locale/lang/es'

import Notifications from '@kyvg/vue3-notification'


const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.use(ElementPlus, {
    locale: es
})

app.use(Notifications)



app.mount('#app')
