<template>
  <div class="user-profile">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <template v-if="from === 'second-hand-detail'">
          <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link :to="`/second-hand/detail/${route.query.itemId}`">商品详情</router-link></el-breadcrumb-item>
        </template>
        <template v-else>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/follows">我的关注</router-link></el-breadcrumb-item>
        </template>
        <el-breadcrumb-item>{{ userProfile.username }}的主页</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">{{ userProfile.username }}的主页</h2>
      </div>

      <!-- 用户信息卡片 -->
      <div class="user-info-card card">
        <div class="user-avatar">
          <img :src="userProfile.avatar" :alt="userProfile.username" />
        </div>
        <div class="user-info">
          <h3 class="user-name">{{ userProfile.username }}</h3>
          <p class="user-bio" v-if="userProfile.bio">{{ userProfile.bio }}</p>
          <p class="user-details">注册时间：{{ userProfile.createTime }}</p>
          <p class="user-details user-credit">
            <span class="credit-label">信誉分：</span>
            <span class="credit-score">{{ userProfile.creditScore || 60 }}</span>
            <span class="credit-level">{{ getCreditLevel(userProfile.creditScore || 60) }}</span>
          </p>
          <p class="user-details" v-if="userProfile.school"><i class="el-icon-location"></i> {{ userProfile.school }}</p>
        </div>
        <div class="user-actions">
          <button v-if="!isFollowing" class="btn btn-primary" @click="followUser">
            关注
          </button>
          <button v-else class="btn btn-secondary" @click="unfollowUser">
            已关注
          </button>
          <button class="btn btn-danger" @click="blockUser">
            拉黑
          </button>
        </div>
      </div>

      <!-- 发布的商品和兼职 -->
      <div class="user-posts">
        <!-- 发布的二手商品 -->
        <div class="posts-section">
          <h3 class="section-title">发布的二手商品</h3>
          <div v-if="userSecondHandItems.length === 0" class="empty-state">
            <el-empty description="暂无发布的二手商品" />
          </div>
          <div v-else class="item-list">
            <div class="list-item" v-for="item in userSecondHandItems" :key="item.id">
              <div class="item-image">
                <img :src="item.image" :alt="item.title" />
              </div>
              <div class="item-info">
                <h4 class="item-title">{{ item.title }}</h4>
                <p class="item-description">{{ item.description || '暂无描述' }}</p>
                <div class="item-meta">
                  <span class="price">¥{{ item.price }}</span>
                  <span class="location">{{ item.location || '未知' }}</span>
                  <span class="publish-time">{{ item.publishTime }}</span>
                </div>
              </div>
              <div class="item-actions">
                <router-link :to="`/second-hand/detail/${item.id}`" class="btn btn-primary">
                  查看详情
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- 发布的兼职 -->
        <div class="posts-section">
          <h3 class="section-title">发布的兼职</h3>
          <div v-if="userPartTimeJobs.length === 0" class="empty-state">
            <el-empty description="暂无发布的兼职" />
          </div>
          <div v-else class="job-list">
            <div class="list-item" v-for="job in userPartTimeJobs" :key="job.id">
              <div class="job-info">
                <h4 class="job-title">{{ job.title }}</h4>
                <div class="job-meta">
                  <span class="meta-item">{{ job.location || '未知' }}</span>
                  <span class="meta-item">{{ job.publishTime || '' }}</span>
                </div>
                <p class="job-description">{{ job.description || '暂无描述' }}</p>
                <div class="job-tags">
                  <span class="tag tag-primary">{{ job.type || '兼职' }}</span>
                  <span class="tag tag-success">薪资: {{ job.salary || '面议' }}</span>
                </div>
              </div>
              <div class="job-actions">
                <router-link :to="`/part-time/detail/${job.id}`" class="btn btn-primary">
                  查看详情
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getUserInfo } from '@/api/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const userId = computed(() => route.params.id)
const from = computed(() => route.query.from || '')
const userProfile = ref({
  id: '',
  userId: '',
  username: '',
  avatar: '',
  phone: '',
  email: '',
  balance: 0,
  createTime: '',
  updateTime: '',
  school: '',
  bio: '',
  creditScore: 60
})

const userSecondHandItems = ref([])
const userPartTimeJobs = ref([])

const isFollowing = ref(false)

// 获取信誉等级
const getCreditLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '一般'
  if (score >= 60) return '及格'
  return '较差'
}

// 关注用户
const followUser = async () => {
  try {
    await userStore.followUser(userProfile.value.id, userProfile.value.username, userProfile.value.avatar)
    isFollowing.value = true
    ElMessage.success('关注成功')
  } catch (error) {
    ElMessage.error('关注失败')
  }
}

// 取消关注
const unfollowUser = async () => {
  try {
    await userStore.unfollowUser(userProfile.value.id)
    isFollowing.value = false
    ElMessage.success('取消关注成功')
  } catch (error) {
    ElMessage.error('取消关注失败')
  }
}

// 拉黑用户
const blockUser = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要拉黑 ${userProfile.value.username} 吗？拉黑后将无法看到该用户的发布内容。`,
      '拉黑确认',
      {
        confirmButtonText: '确定拉黑',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await userStore.blockUser(userProfile.value.userId || userProfile.value.id, userProfile.value.username, userProfile.value.avatar)
    ElMessage.success('拉黑成功')
    router.push('/user/user/center')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('拉黑失败')
    }
  }
}

onMounted(async () => {
  try {
    // 从后端API获取用户信息
    const response = await getUserInfo(userId.value)
    if (response.data) {
      const data = response.data
      
      // 更新用户基本信息
      userProfile.value = {
        id: data.id || '',
        userId: data.userId || '',
        username: data.username || '',
        avatar: data.avatar ? data.avatar.replace(/`/g, '') : 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20shopkeeper%20portrait%20professional&image_size=square',
        phone: data.phone || '',
        email: data.email || '',
        balance: data.balance || 0,
        createTime: data.createTime || '',
        updateTime: data.updateTime || '',
        school: data.school || '',
        bio: data.bio || '',
        creditScore: data.creditScore || 60
      }
      
      // 更新二手商品列表
      if (data.items && Array.isArray(data.items)) {
        userSecondHandItems.value = data.items.map(item => ({
          id: item.id,
          title: item.title,
          price: item.price,
          image: item.coverImage || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=second%20hand%20item&image_size=square',
          publishTime: item.publishTime,
          status: item.status
        }))
      }
      
      // 更新兼职列表
      if (data.jobs && Array.isArray(data.jobs)) {
        userPartTimeJobs.value = data.jobs.map(job => ({
          id: job.id,
          title: job.title,
          description: job.employer || '',
          location: job.location || '',
          salary: job.salaryDesc || '',
          type: '兼职',
          publishTime: job.publishTime,
          status: job.status
        }))
      }
    }
    
    // 检查是否已关注
    isFollowing.value = userStore.userFollows.some(follow => follow.userId === parseInt(userId.value))
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败，请稍后重试')
  }
})
</script>

<style scoped lang="scss">
.user-profile {
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

/* 用户信息卡片样式 */
.user-info-card {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.user-bio {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
}

.user-details {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-details i {
  color: #409EFF;
  font-size: 14px;
}

.user-credit {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
}

.credit-label {
  font-size: 14px;
  color: #666;
}

.credit-score {
  font-size: 16px;
  font-weight: bold;
  color: #409EFF;
}

.credit-level {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  background-color: #ECF5FF;
  color: #409EFF;
}

.user-actions {
  margin-left: 30px;
  display: flex;
  gap: 10px;
}

/* 发布的商品和兼职样式 */
.user-posts {
  margin-bottom: 30px;
}

.posts-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

/* 二手商品列表样式 */
.item-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.list-item {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
}

.list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 12px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  margin-bottom: 16px;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.item-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
}

.price {
  font-size: 16px;
  font-weight: bold;
  color: var(--danger-color);
}

.location {
  font-size: 14px;
  color: #666;
}

.publish-time {
  font-size: 12px;
  color: #999;
}

.item-actions {
  display: flex;
  gap: 10px;
}

/* 兼职列表样式 */
.job-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.job-info {
  flex: 1;
  margin-right: 20px;
}

.job-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
}

.job-meta {
  margin-bottom: 12px;
}

.meta-item {
  display: inline-block;
  margin-right: 16px;
  font-size: 14px;
  color: #666;
}

.job-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.job-tags {
  margin-top: 12px;
}

.job-actions {
  display: flex;
  gap: 10px;
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
    justify-content: center;
  }
  
  .item-list {
    grid-template-columns: 1fr;
  }
  
  .list-item {
    flex-direction: column;
  }
  
  .job-info {
    margin-right: 0;
    margin-bottom: 16px;
  }
}
</style>