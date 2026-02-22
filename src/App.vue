<template>
  <div id="app">
    <!-- 导航栏 - 404页面不显示 -->
    <AppNavbar v-if="!isNotFoundPage" />
    
    <!-- 页面过渡动画和组件缓存 -->
    <transition name="fade" mode="out-in">
      <keep-alive :include="['HomeView']">
        <router-view />
      </keep-alive>
    </transition>
    
    <!-- 页脚 - 404页面不显示 -->
    <AppFooter v-if="!isNotFoundPage" />
    
    <!-- 返回顶部按钮 -->
    <BackToTop />
  </div>
</template>

<script>
import AppNavbar from './components/Navbar.vue'
import AppFooter from './components/Footer.vue'
import BackToTop from './components/BackToTop.vue'

export default {
  name: 'App',
  components: {
    AppNavbar,
    AppFooter,
    BackToTop
  },
  computed: {
    isNotFoundPage() {
      return this.$route.name === 'not-found'
    }
  },
  mounted() {
    // 监听localStorage变化
    window.addEventListener('storage', (event) => {
      console.log('[App.vue] Storage事件:', event)
      console.log('[App.vue] Key:', event.key)
      console.log('[App.vue] OldValue:', event.oldValue)
      console.log('[App.vue] NewValue:', event.newValue)
      
      if (event.key === 'token' && event.newValue === null) {
        console.error('[App.vue] Token被清除！')
        console.error('[App.vue] 清除时间:', new Date().toISOString())
        console.error('[App.vue] 当前URL:', window.location.href)
      }
      
      if (event.key === 'userInfo' && event.newValue === null) {
        console.error('[App.vue] UserInfo被清除！')
        console.error('[App.vue] 清除时间:', new Date().toISOString())
        console.error('[App.vue] 当前URL:', window.location.href)
      }
    })
    
    // 定期检查token是否存在
    setInterval(() => {
      const token = localStorage.getItem('token')
      const userInfo = localStorage.getItem('userInfo')
      
      if (!token && userInfo) {
        console.error('[App.vue] 检测到token不存在但userInfo存在！')
        console.error('[App.vue] 检查时间:', new Date().toISOString())
      }
      
      if (token && !userInfo) {
        console.error('[App.vue] 检测到token存在但userInfo不存在！')
        console.error('[App.vue] 检查时间:', new Date().toISOString())
      }
    }, 30000) // 每30秒检查一次
  }
}
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text-color-primary);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
