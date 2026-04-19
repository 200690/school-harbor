<template>
  <el-header height="60px" class="navbar">
    <div class="container flex items-center justify-between h-full">
      <!-- 品牌标识 -->
      <div class="navbar-brand flex items-center gap-2">
        <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/校园港湾.ico" alt="校园港湾" class="brand-logo" />
        <router-link to="/">校园港湾</router-link>
      </div>
      
      <!-- 导航菜单 - 桌面端 -->
      <div class="navbar-nav">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/part-time" class="nav-item">兼职</router-link>
        <router-link to="/second-hand" class="nav-item">二手交易</router-link>
        <router-link to="/user/user/center" class="nav-item">个人中心</router-link>
        <router-link v-if="isAdmin" to="/admin" class="nav-item">管理员</router-link>
      </div>
      
      <!-- 用户操作 -->
      <div class="user-actions" v-if="!isLoggedIn">
        <router-link to="/user/user/login" class="user-btn">登录</router-link>
        <router-link to="/user/user/register" class="user-btn user-btn-primary">注册</router-link>
      </div>
      <div class="user-actions" v-else>
        <el-button link class="user-btn" @click="handleLogout">注销</el-button>
      </div>
      
      <!-- 汉堡菜单按钮 - 移动端 -->
      <div class="mobile-menu-btn" @click="toggleMobileMenu">
        <i class="el-icon-menu"></i>
      </div>
    </div>
    
    <!-- 移动端菜单 -->
    <div class="mobile-menu" v-if="isMobileMenuOpen">
      <div class="mobile-menu-content">
        <div class="mobile-nav">
          <router-link to="/" class="mobile-nav-item" @click="closeMobileMenu">首页</router-link>
          <router-link to="/part-time" class="mobile-nav-item" @click="closeMobileMenu">兼职</router-link>
          <router-link to="/second-hand" class="mobile-nav-item" @click="closeMobileMenu">二手交易</router-link>
          <router-link to="/user/user/center" class="mobile-nav-item" @click="closeMobileMenu">个人中心</router-link>
          <router-link v-if="isAdmin" to="/admin" class="mobile-nav-item" @click="closeMobileMenu">管理员</router-link>
          <template v-if="!isLoggedIn">
            <router-link to="/user/user/login" class="mobile-nav-item" @click="closeMobileMenu">登录</router-link>
            <router-link to="/user/user/register" class="mobile-nav-item mobile-nav-item-primary" @click="closeMobileMenu">注册</router-link>
          </template>
          <template v-else>
            <el-button link class="mobile-nav-item" @click="handleLogout">注销</el-button>
          </template>
        </div>
      </div>
    </div>
  </el-header>
</template>

<script>
import { useUserStore } from '@/stores/user'

export default {
  name: 'AppNavbar',
  data() {
    return {
      isMobileMenuOpen: false
    }
  },
  computed: {
    userStore() {
      return useUserStore()
    },
    isLoggedIn() {
      return this.userStore.isLoggedIn
    },
    isAdmin() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      return userInfo.role === 'admin'
    }
  },
  mounted() {
    // 监听storage变化，处理其他标签页的登录状态变化
    window.addEventListener('storage', this.handleStorageChange)
    // 监听token过期事件
    window.addEventListener('token-expired', this.handleTokenExpired)
  },
  beforeUnmount() {
    // 移除监听器
    window.removeEventListener('storage', this.handleStorageChange)
    window.removeEventListener('token-expired', this.handleTokenExpired)
  },
  methods: {
    handleStorageChange(event) {
      if (event.key === 'token' || event.key === 'userInfo') {
        this.userStore.checkLogin()
      }
    },
    handleTokenExpired() {
      this.userStore.checkLogin()
    },
    toggleMobileMenu() {
      this.isMobileMenuOpen = !this.isMobileMenuOpen
    },
    closeMobileMenu() {
      this.isMobileMenuOpen = false
    },
    handleLogout() {
      this.userStore.logoutAction()
      // 显示成功消息
      this.$message.success('注销成功')
      // 关闭移动端菜单
      this.closeMobileMenu()
      // 刷新页面或跳转到首页
      this.$router.push('/')
    }
  }
}
</script>

<style scoped lang="scss">
.navbar {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.navbar-brand {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-color);
  display: flex;
  align-items: center;
  gap: 12px;
  
  a {
    color: var(--primary-color);
    text-decoration: none;
  }
}

.brand-logo {
  width: 36px;
  height: 36px;
  object-fit: contain;
}

.navbar-nav {
  display: flex;
  gap: 30px;
}

.nav-item {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s;
  padding: 8px 0;
  position: relative;
  
  &:hover {
    color: var(--primary-color);
  }
  
  &.router-link-active {
    color: var(--primary-color);
    font-weight: 500;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 2px;
      background-color: var(--primary-color);
    }
  }
}

.user-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.user-btn {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  text-decoration: none;
  transition: all 0.3s;
  
  &:hover {
    color: var(--primary-color);
  }
}

.user-btn-primary {
  background-color: var(--primary-color);
  color: #fff;
  
  &:hover {
    background-color: #66B1FF;
    color: #fff;
  }
}

/* 汉堡菜单按钮样式 */
.mobile-menu-btn {
  display: none;
  font-size: 24px;
  color: var(--text-color-primary);
  cursor: pointer;
  transition: color 0.3s;
  
  &:hover {
    color: var(--primary-color);
  }
}

/* 移动端菜单样式 */
.mobile-menu {
  position: absolute;
  top: 60px;
  left: 0;
  right: 0;
  background-color: var(--background-color);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.mobile-menu-content {
  padding: 20px;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mobile-nav-item {
  display: block;
  padding: 12px 16px;
  border-radius: 4px;
  color: var(--text-color-primary);
  text-decoration: none;
  transition: all 0.3s;
  
  &:hover {
    background-color: rgba(64, 158, 255, 0.1);
    color: var(--primary-color);
  }
}

.mobile-nav-item-primary {
  background-color: var(--primary-color);
  color: #fff !important;
  
  &:hover {
    background-color: #66B1FF !important;
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .navbar-nav {
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 10px;
  }
  
  .navbar-nav {
    display: none;
  }
  
  .user-actions {
    display: none;
  }
  
  .mobile-menu-btn {
    display: block;
  }
}

@media (max-width: 576px) {
  .navbar-brand {
    font-size: 18px;
  }
}
</style>