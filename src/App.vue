<template>
  <div id="app">
    <!-- 用户被封禁提示 -->
    <div v-if="isUserBanned" class="banned-notice">
      <el-result icon="error" title="该用户已封禁" sub-title="您的账号已被封禁，请联系管理员">
        <template #extra>
          <el-button type="primary" @click="goToLogin">重新登录</el-button>
        </template>
      </el-result>
    </div>
    
    <!-- 导航栏 - 404页面、封禁用户和封禁详情页面不显示 -->
    <AppNavbar v-if="!isNotFoundPage && !isUserBanned && !isBannedDetailPage" />
    
    <!-- 页面过渡动画和组件缓存 -->
    <transition name="fade" mode="out-in">
      <!-- 用户未被封禁时显示页面内容 -->
      <div v-if="!isUserBanned">
        <router-view />
      </div>
    </transition>
    
    <!-- 页脚 - 404页面、封禁用户和封禁详情页面不显示 -->
    <AppFooter v-if="!isNotFoundPage && !isUserBanned && !isBannedDetailPage" />
    
    <!-- 返回顶部按钮 - 封禁用户和封禁详情页面不显示 -->
    <BackToTop v-if="!isUserBanned && !isBannedDetailPage" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AppNavbar from './components/Navbar.vue'
import AppFooter from './components/Footer.vue'
import BackToTop from './components/BackToTop.vue'

const router = useRouter()
const route = useRoute()
const isUserBanned = ref(false)
const isPublisherBannedPage = ref(false)

const handleUserBanned = () => {
  console.log('[App.vue] 收到用户被封禁事件')
  isUserBanned.value = true
  console.log('[App.vue] isUserBanned 设置为:', isUserBanned.value)
}

const handlePublisherBanned = () => {
  console.log('[App.vue] 收到服务提供者被封禁事件')
  isPublisherBannedPage.value = true
  console.log('[App.vue] isPublisherBannedPage 设置为:', isPublisherBannedPage.value)
}

const handlePublisherUnbanned = () => {
  console.log('[App.vue] 收到服务提供者解封事件')
  isPublisherBannedPage.value = false
  console.log('[App.vue] isPublisherBannedPage 设置为:', isPublisherBannedPage.value)
}

const goToLogin = () => {
  router.push('/user/user/login')
}

const isNotFoundPage = computed(() => {
  return route.name === 'not-found'
})

// 检查是否是详情页面且处于封禁模式
const isBannedDetailPage = computed(() => {
  const path = route.path
  // 检查是否是兼职详情页面或二手交易详情页面
  const isDetailPage = path.includes('/item/') || path.includes('/second-hand/detail/')
  return isDetailPage && isPublisherBannedPage.value
})

// 检查用户是否被封禁
const checkIfUserBanned = () => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  console.log('[App.vue] 检查用户封禁状态:', { token: !!token, userInfo: !!userInfo })
  
  // 如果没有token或用户信息，说明用户已注销或未登录，不显示封禁提示
  if (!token || !userInfo) {
    console.log('[App.vue] 未检测到token或用户信息，重置封禁状态')
    isUserBanned.value = false
    console.log('[App.vue] isUserBanned 设置为:', isUserBanned.value)
  }
  // 如果有token和userInfo，保持当前状态（可能已被user-banned事件设置为true）
}

// 监听localStorage变化
const handleStorageChange = (event) => {
  if (event.key === 'token' || event.key === 'userInfo' || event.key === null) {
    console.log('[App.vue] localStorage变化:', event)
    checkIfUserBanned()
  }
}

onMounted(() => {
  console.log('[App.vue] 组件挂载，添加事件监听')
  window.addEventListener('user-banned', handleUserBanned)
  window.addEventListener('storage', handleStorageChange)
  window.addEventListener('publisher-banned', handlePublisherBanned)
  window.addEventListener('publisher-unbanned', handlePublisherUnbanned)
  checkIfUserBanned()
})

onUnmounted(() => {
  console.log('[App.vue] 组件卸载，移除事件监听')
  window.removeEventListener('user-banned', handleUserBanned)
  window.removeEventListener('storage', handleStorageChange)
  window.removeEventListener('publisher-banned', handlePublisherBanned)
  window.removeEventListener('publisher-unbanned', handlePublisherUnbanned)
})

// 监听路由变化
watch(
  () => route.path,
  () => {
    checkIfUserBanned()
    // 当路由变化时，重置服务提供者封禁状态
    const isDetailPage = route.path.includes('/item/') || route.path.includes('/second-hand/detail/')
    if (!isDetailPage) {
      isPublisherBannedPage.value = false
    }
    
    // 当导航到主页、兼职、二手交易这三个主页面时，清除所有子组件的缓存
    const mainPages = ['/', '/index', '/part-time', '/second-hand']
    if (mainPages.includes(route.path)) {
      console.log('[App.vue] 导航到主页面，清除所有子组件缓存')
      clearAllCaches()
    }
  }
)

// 清除所有子组件的缓存和状态
const clearAllCaches = () => {
  // 清除localStorage中的缓存数据（保留token和userInfo）
  const keysToKeep = ['token', 'userInfo']
  const allKeys = Object.keys(localStorage)
  allKeys.forEach(key => {
    if (!keysToKeep.includes(key)) {
      localStorage.removeItem(key)
    }
  })
  
  // 触发自定义事件，通知所有子组件清除缓存
  window.dispatchEvent(new CustomEvent('clear-cache'))
  
  console.log('[App.vue] 已清除所有子组件缓存')
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

.banned-notice {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.banned-notice :deep(.el-result) {
  color: #fff;
}

.banned-notice :deep(.el-result__title) {
  color: #fff;
}

.banned-notice :deep(.el-result__subtitle) {
  color: rgba(255, 255, 255, 0.9);
}

.banned-notice :deep(.el-result__icon) {
  color: #fff;
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
