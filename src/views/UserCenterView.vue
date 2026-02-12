<template>
  <div class="user-center">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <h2 class="page-title">个人中心</h2>
      
      <!-- 个人信息卡片 -->
      <div class="user-info-card card">
        <div class="user-avatar">
          <img :src="userInfo.avatar" :alt="userInfo.username" />
        </div>
        <div class="user-info">
          <h3 class="user-name">{{ userInfo.username }}</h3>
          <p class="user-details">手机号：{{ userInfo.phone }}</p>
          <p class="user-details">邮箱：{{ userInfo.email }}</p>
          <p class="user-details">注册时间：{{ userInfo.registerTime }}</p>
        </div>
        <div class="user-actions">
          <router-link to="/user/info" class="btn btn-primary">编辑资料</router-link>
        </div>
      </div>
      
      <!-- 功能导航 -->
      <div class="function-nav">
        <div class="function-item">
          <router-link to="/user/publish" class="function-link">
            <div class="function-icon">
              <i class="el-icon-s-order"></i>
            </div>
            <span>我的发布</span>
            <span class="function-count">{{ userPosts.length }}</span>
          </router-link>
        </div>
        <div class="function-item">
          <router-link to="/user/applications" class="function-link">
            <div class="function-icon">
              <i class="el-icon-s-flag"></i>
            </div>
            <span>我的申请</span>
            <span class="function-count">{{ userApplications.length }}</span>
          </router-link>
        </div>
        <div class="function-item">
          <router-link to="/user/purchases" class="function-link">
            <div class="function-icon">
              <i class="el-icon-s-goods"></i>
            </div>
            <span>我的购买</span>
            <span class="function-count">{{ userPurchases.length }}</span>
          </router-link>
        </div>
        <div class="function-item">
          <router-link to="/user/messages" class="function-link">
            <div class="function-icon">
              <i class="el-icon-message"></i>
            </div>
            <span>消息通知</span>
            <span class="function-count">{{ userMessages.length }}</span>
          </router-link>
        </div>
        <div class="function-item">
          <router-link to="/user/favorites" class="function-link">
            <div class="function-icon">
              <i class="el-icon-star-on"></i>
            </div>
            <span>我的收藏</span>
            <span class="function-count">{{ userFavorites.length }}</span>
          </router-link>
        </div>
        <div class="function-item">
          <router-link to="/user/settings" class="function-link">
            <div class="function-icon">
              <i class="el-icon-setting"></i>
            </div>
            <span>账号设置</span>
          </router-link>
        </div>
      </div>
      
      <!-- 最近活动 -->
      <div class="recent-activities card">
        <h3 class="section-title">最近活动</h3>
        <div class="activity-list">
          <div v-for="(activity, index) in recentActivities" :key="index" class="activity-item">
            <div class="activity-icon">
              <i :class="activity.icon"></i>
            </div>
            <div class="activity-content">
              <p class="activity-title">{{ activity.title }}</p>
              <p class="activity-time">{{ activity.time }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const userInfo = ref(userStore.userInfo)
const userPosts = ref(userStore.userPosts)
const userApplications = ref(userStore.userApplications)
const userPurchases = ref(userStore.userPurchases)
const userMessages = ref(userStore.userMessages)
const userFavorites = ref(userStore.userFavorites)
const recentActivities = ref(userStore.recentActivities)

onMounted(async () => {
  // 模拟从后端获取数据
  await userStore.getUserInfoAction()
  await userStore.getUserPosts()
  await userStore.getUserApplications()
  await userStore.getUserMessages()
  await userStore.getRecentActivities()
  
  // 更新本地数据
  userInfo.value = userStore.userInfo
  userPosts.value = userStore.userPosts
  userApplications.value = userStore.userApplications
  userMessages.value = userStore.userMessages
  recentActivities.value = userStore.recentActivities
})
</script>

<style scoped lang="scss">
.user-center {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  margin-top: 60px;
  padding: 20px 0;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

/* 个人信息卡片样式 */
.user-info-card {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 30px;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.user-details {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.user-actions {
  margin-left: 30px;
}

/* 功能导航样式 */
.function-nav {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.function-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
  transition: all 0.3s;
}

.function-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.function-link {
  text-decoration: none;
  color: #333;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.function-count {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: var(--primary-color);
  color: white;
  font-size: 12px;
  font-weight: bold;
  min-width: 20px;
  height: 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
}

.function-icon {
  width: 50px;
  height: 50px;
  background-color: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  font-size: 24px;
  color: var(--primary-color);
}

/* 最近活动样式 */
.recent-activities {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  transition: all 0.3s;
}

.activity-item:hover {
  background-color: #f0f7ff;
}

.activity-icon {
  width: 40px;
  height: 40px;
  background-color: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
  color: var(--primary-color);
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-info-card {
    flex-direction: column;
    align-items: flex-start;
    text-align: center;
  }
  
  .user-avatar {
    margin: 0 auto 20px;
  }
  
  .user-actions {
    margin: 20px 0 0;
    width: 100%;
  }
  
  .function-nav {
    grid-template-columns: repeat(3, 1fr);
  }
}
</style>