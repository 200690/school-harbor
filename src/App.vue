<template>
  <div id="app">
    <!-- 导航栏 - 404页面不显示 -->
    <AppNavbar v-if="!isNotFoundPage" />
    
    <!-- 页面过渡动画 -->
    <transition name="fade" mode="out-in">
      <router-view />
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
