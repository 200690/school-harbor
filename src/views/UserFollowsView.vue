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
          <div v-for="follow in userFollows" :key="follow.followId" class="follow-item">
            <div class="user-avatar">
              <img :src="follow.avatar.replace(/`/g, '')" :alt="follow.username" />
            </div>
            <div class="user-info">
              <h3 class="user-name">{{ follow.username }}</h3>
              <p class="user-signature">{{ follow.signature || '暂无签名' }}</p>
              <p class="user-gender">
                {{ follow.gender === 1 ? '男' : '女' }}
              </p>
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
          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalFollows"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyFollows, unfollowUser as unfollowUserApi } from '@/api/user'

const currentPage = ref(1)
const pageSize = ref(10)
const userFollows = ref([])
const totalFollows = ref(0)

// 取消关注
const unfollowUser = async (userId) => {
  try {
    await unfollowUserApi(userId)
    await fetchFollows()
    ElMessage.success('取消关注成功')
  } catch (error) {
    console.error('取消关注失败:', error)
    ElMessage.error('取消关注失败')
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchFollows()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchFollows()
}

// 获取关注列表
const fetchFollows = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取关注列表')
      return
    }
    
    const response = await getMyFollows({
      id: userId,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    userFollows.value = response.data?.list || []
    totalFollows.value = parseInt(response.data?.total) || 0
  } catch (error) {
    console.error('获取关注列表失败:', error)
    ElMessage.error('获取关注列表失败，请稍后重试')
  }
}

onMounted(async () => {
  await fetchFollows()
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

.user-signature {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
  line-height: 1.4;
}

.user-gender {
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
  
  .pagination {
    margin-top: 20px;
  }
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>