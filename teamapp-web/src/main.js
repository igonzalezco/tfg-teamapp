import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import es from 'element-plus/es/locale/lang/es'

import Notifications from '@kyvg/vue3-notification'



const app = createApp(App)

app.use(createPinia())
app.use(router)

app.use(ElementPlus, {
    locale: es
})

app.use(Notifications)

app.mount('#app')
