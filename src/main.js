// 捕获并忽略ResizeObserver循环错误
window.addEventListener('error', (e) => {
  if (e.message.includes('ResizeObserver loop completed with undelivered notifications')) {
    // 忽略ResizeObserver循环错误
    e.stopPropagation();
    e.preventDefault();
  }
});

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
