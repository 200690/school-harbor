<template>
  <div class="user-follows">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的关注</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的关注</h2>
      </div>

      <!-- 关注列表 -->
      <div class="follows-list">
        <div v-if="userFollows.length === 0" class="empty-state">
          <el-empty description="暂无关注的用户" />
        </div>
        <div v-else class="follow-items">
          <div v-for="follow in userFollows" :key="follow.id" class="follow-item">
            <div class="user-avatar">
              <img :src="follow.avatar" :alt="follow.username" />
            </div>
            <div class="user-info">
              <h3 class="user-name">{{ follow.username }}</h3>
              <p class="follow-time">关注时间：{{ follow.followTime }}</p>
            </div>
            <div class="user-actions">
              <router-link :to="`/user/profile/${follow.userId}`" class="btn btn-primary">
                查看主页
              </router-link>
              <button class="btn btn-danger" @click="unfollowUser(follow.userId)">
                取消关注
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const userFollows = ref([])

// 取消关注
const unfollowUser = async (userId) => {
  try {
    await userStore.unfollowUser(userId)
    userFollows.value = userStore.userFollows
    ElMessage.success('取消关注成功')
  } catch (error) {
    ElMessage.error('取消关注失败')
  }
}

onMounted(async () => {
  // 获取关注列表
  await userStore.getUserFollows()
  userFollows.value = userStore.userFollows
})
</script>

<style scoped lang="scss">
.user-follows {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.follows-list {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.follow-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.follow-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
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
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.follow-time {
  font-size: 12px;
  color: #999;
}

.user-actions {
  display: flex;
  gap: 10px;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .follows-list {
    padding: 15px;
  }
  
  .follow-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .user-actions {
    width: 100%;
    justify-content: space-between;
  }
}
</style>