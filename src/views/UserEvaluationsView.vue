<template>
  <div class="user-evaluations">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的评价</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的评价</h2>
      </div>

      <!-- 评价分类 -->
      <div class="evaluation-tabs">
        <!-- 一级菜单：全部、兼职、二手交易 -->
        <el-tabs v-model="activeCategory" class="category-tabs">
          <el-tab-pane label="全部" name="all"></el-tab-pane>
          <el-tab-pane label="兼职" name="partTime"></el-tab-pane>
          <el-tab-pane label="二手交易" name="secondHand"></el-tab-pane>
        </el-tabs>
        
        <!-- 二级菜单：发出的评价、收到的评价 -->
        <el-tabs v-model="activeType" class="type-tabs">
          <el-tab-pane label="发出的评价" name="given"></el-tab-pane>
          <el-tab-pane label="收到的评价" name="received"></el-tab-pane>
        </el-tabs>
        
        <!-- 评价列表 -->
        <div class="evaluation-list">
          <div v-if="filteredEvaluations.length === 0" class="empty-state">
            <el-empty description="暂无评价记录" />
          </div>
          <div v-else class="evaluation-items">
            <div v-for="evaluation in filteredEvaluations" :key="evaluation.id" class="evaluation-item">
              <div class="eval-header">
                <div class="eval-info">
                  <div class="user-info">
                    <img :src="evaluation.userAvatar.trim()" :alt="evaluation.userName" class="user-avatar" />
                    <div class="user-details">
                      <span class="user-name">{{ evaluation.userName }}</span>
                      <span v-if="evaluation.replyUserName" class="reply-info">
                        回复 {{ evaluation.replyUserName }}
                      </span>
                    </div>
                  </div>
                  <span class="eval-time">{{ formatTime(evaluation.createdAt) }}</span>
                </div>
                <el-tag>{{ evaluation.targetTypeDesc }}</el-tag>
              </div>
              <div class="eval-content">
                <p class="eval-text">{{ evaluation.content }}</p>
              </div>
              <div class="eval-actions">
                <div class="interaction-buttons">
                  <el-button 
                    size="small" 
                    type="text" 
                    @click="toggleLike(evaluation)"
                    :class="{ 'liked': evaluation.isLiked }"
                  >
                    <i :class="evaluation.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" />
                    <span>{{ evaluation.likeCount }} 点赞</span>
                  </el-button>

                </div>
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
                :total="totalRawEvaluations"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

import { useRoute } from 'vue-router'

const route = useRoute()

// 一级菜单：全部、兼职、二手交易
const activeCategory = ref('all')
// 二级菜单：发出的评价、收到的评价
const activeType = ref('given')

// 从URL参数获取目标类型和ID
const targetTypeFromUrl = route.query.targetType
const targetIdFromUrl = route.query.targetId

// 如果有URL参数，自动设置对应的分类
if (targetTypeFromUrl === '0') {
  activeCategory.value = 'partTime' // 兼职
} else if (targetTypeFromUrl === '1') {
  activeCategory.value = 'secondHand' // 商品
}

const currentPage = ref(1)
const pageSize = ref(10)

// 评价数据
const allEvaluations = ref([])
const totalRawEvaluations = ref(0)

// 计算属性：过滤后的评价
const filteredEvaluations = computed(() => {
  if (activeCategory.value === 'all') {
    return allEvaluations.value
  } else if (activeCategory.value === 'partTime') {
    // targetType=0表示兼职，显示在全部和兼职下
    return allEvaluations.value.filter(item => item.targetType === 0 || item.targetTypeDesc === '兼职')
  } else if (activeCategory.value === 'secondHand') {
    // targetType=1表示二手交易，显示在全部和二手交易下
    return allEvaluations.value.filter(item => item.targetType === 1 || item.targetTypeDesc === '商品')
  }
  return allEvaluations.value
})

// 格式化时间
const formatTime = (timeString) => {
  const date = new Date(timeString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}


// 切换点赞状态
const toggleLike = async (evaluation) => {
  try {
    const status = evaluation.isLiked ? 0 : 1
    
    console.log('[评价页面] 开始处理点赞操作:', {
      evaluationId: evaluation.id,
      currentLiked: evaluation.isLiked,
      targetStatus: status
    })
    
    // 构建完整的请求URL
    const fullUrl = 'http://localhost:8080/api/comment/commentLike/like'
    console.log('[评价页面] 准备发送点赞请求:', {
      fullUrl: fullUrl,
      data: {
        commentId: Number(evaluation.id),
        status: status
      },
      token: localStorage.getItem('token') ? '存在' : '不存在'
    })
    
    // 直接使用完整的URL，确保请求发送到正确的端点
    const response = await axios.post(fullUrl, {
      commentId: Number(evaluation.id),
      status: status
    }, {
      timeout: 15000,
      withCredentials: true,
      headers: {
        'Authorization': localStorage.getItem('token'),
        'Content-Type': 'application/json'
      }
    })
    
    console.log('[评价页面] 点赞请求成功，响应数据:', response)
    
    // 检查返回值
    if (response.data && response.data.code === 1) {
      console.log('[评价页面] 点赞操作成功，准备刷新评价列表')
      // 刷新评价列表
      await fetchEvaluations()
      console.log('[评价页面] 评价列表刷新完成')
    } else {
      console.log('[评价页面] 点赞操作返回异常:', response.data)
      ElMessage.error('操作失败，请稍后重试')
    }
    
  } catch (error) {
    console.error('[评价页面] 点赞请求失败:', error)
    console.error('[评价页面] 错误详情:', {
      message: error.message,
      response: error.response,
      request: error.request,
      config: error.config
    })
    
    if (error.response && error.response.status === 401) {
      console.log('[评价页面] 401错误，登录已过期，准备跳转到登录页面')
      ElMessage.error('登录已过期，请重新登录')
      await userStore.logoutAction()
      window.location.href = '/user/user/login'
    } else {
      ElMessage.error('操作失败，请稍后重试')
    }
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchEvaluations()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchEvaluations()
}

// 监听一级菜单切换
watch(activeCategory, () => {
  // 当切换分类时，不需要重新获取数据，只需要过滤显示
  // 因为我们已经在filteredEvaluations计算属性中处理了过滤逻辑
})

// 监听二级菜单切换
watch(activeType, () => {
  currentPage.value = 1
  fetchEvaluations()
})

// 获取评价列表
const fetchEvaluations = async () => {
  try {
    console.log('[评价页面] 开始获取评价列表')
    const token = localStorage.getItem('token')
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    console.log('[评价页面] 登录状态检查:', {
      token: token ? '存在' : '不存在',
      userId: userId,
      userInfo: userInfo
    })
    
    if (!userId) {
      console.log('[评价页面] 用户信息不完整，无法获取评价列表')
      ElMessage.error('用户信息不完整，无法获取评价列表')
      return
    }
    
    // 始终请求全部数据，前端进行过滤
    // targetType=2表示全部，targetType=0表示兼职，targetType=1表示二手交易
    // 评价根据targetType值显示在多个分类下：
    // - targetType=0：显示在全部和兼职下
    // - targetType=1：显示在全部和二手交易下
    const targetType = 2 // 始终获取全部数据
    
    // 根据 activeType 设置 type
    const type = activeType.value === 'given' ? 1 : 2
    
    // 使用URL参数中的targetId（如果存在）
    const targetId = targetIdFromUrl || null
    
    console.log('[评价页面] 准备发送API请求:', {
      url: '/comment/showMyComments',
      data: {
        type: type,
        targetType: targetType,
        targetId: targetId,
        userId: userId,
        sortType: 1,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    const response = await request.post('/comment/showMyComments', {
      type: type,
      targetType: targetType,
      targetId: targetId,
      userId: userId,
      sortType: 1,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    console.log('[评价页面] API请求成功，响应数据:', response)
    
    allEvaluations.value = response?.data?.list || []
    totalRawEvaluations.value = parseInt(response?.data?.total) || 0
    
    console.log('[评价页面] 评价列表更新完成:', {
      total: totalRawEvaluations.value,
      count: allEvaluations.value.length
    })
  } catch (error) {
    console.error('[评价页面] 获取评价列表失败:', error)
    if (error.response) {
      console.error('[评价页面] 错误响应:', {
        status: error.response.status,
        data: error.response.data
      })
    } else if (error.request) {
      console.error('[评价页面] 请求发送失败，未收到响应:', error.request)
    } else {
      console.error('[评价页面] 请求配置错误:', error.message)
    }
    
    if (error.response && error.response.status === 401) {
      console.log('[评价页面] 401错误，登录已过期，准备跳转到登录页面')
      ElMessage.error('登录已过期，请重新登录')
      // 使用store的logoutAction处理退出登录
      await userStore.logoutAction()
      // 跳转到登录页面
      window.location.href = '/user/user/login'
    } else {
      console.log('[评价页面] 非401错误，显示错误提示')
      ElMessage.error('获取评价列表失败，请稍后重试')
    }
  }
}

onMounted(async () => {
  await fetchEvaluations()
})
</script>

<style scoped lang="scss">
.user-evaluations {
  min-height: 100vh;
  padding: 80px 0 20px;
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

.evaluation-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.category-tabs {
  margin-bottom: 10px;
  border-bottom: 2px solid #409EFF;
  font-weight: bold;
}

.type-tabs {
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  margin-left: 20px;
}

/* 调整标签页样式 */
:deep(.el-tabs__nav-wrap::after) {
  display: none;
}

:deep(.category-tabs .el-tabs__active-bar) {
  background-color: #409EFF;
}

:deep(.category-tabs .el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
  padding: 0 20px;
}

:deep(.type-tabs .el-tabs__item) {
  font-size: 14px;
  padding: 0 15px;
  color: #606266;
}

:deep(.type-tabs .el-tabs__item.is-active) {
  color: #409EFF;
}

.evaluation-list {
  margin-top: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.evaluation-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.evaluation-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.eval-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.eval-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.reply-info {
  font-size: 12px;
  color: #999;
}

.eval-time {
  font-size: 12px;
  color: #999;
}

.eval-content {
  margin-bottom: 15px;
  padding-left: 52px;
}

.eval-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0;
}

.eval-actions {
  padding-left: 52px;
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
}

.interaction-buttons {
  display: flex;
  gap: 20px;
  
  .el-button {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 4px 8px;
    color: #909399;
    transition: all 0.3s;
    
    &:hover {
      color: #409EFF;
      background-color: rgba(64, 158, 255, 0.1);
    }
    
    &.liked {
      color: #F56C6C;
      
      i {
        animation: likeAnimation 0.3s ease-in-out;
      }
      
      &:hover {
        color: #F78989;
        background-color: rgba(245, 108, 108, 0.1);
      }
    }
    
    i {
      font-size: 16px;
    }
    
    span {
      font-size: 13px;
    }
  }
}

@keyframes likeAnimation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .evaluation-tabs {
    padding: 15px;
  }
  
  .evaluation-item {
    padding: 15px;
  }
  
  .eval-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .eval-content {
    padding-left: 42px;
  }
  
  .eval-actions {
    padding-left: 42px;
  }
  
  .user-avatar {
    width: 32px;
    height: 32px;
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