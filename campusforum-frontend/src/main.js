import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/router.js'
import axios from "axios";
import { createPinia } from 'pinia'

import'element-plus/theme-chalk/dark/css-vars.css';

axios.defaults.baseURL = 'http://172.17.57.129:8080'

const app = createApp(App)

app.use(router)
app.use(createPinia())

app.mount('#app')
