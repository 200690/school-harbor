import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/index.scss'
import { createPinia } from 'pinia'

const pinia = createPinia()

createApp(App).use(router).use(ElementPlus, {
  // 全局配置
  message: {
    duration: 1500 // 设置消息显示时间为 1.5 秒
  }
}).use(pinia).mount('#app')
