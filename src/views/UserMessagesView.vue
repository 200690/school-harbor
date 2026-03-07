<template>
  <div class="user-messages">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>消息通知</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">消息通知</h2>
      </div>

      <!-- 消息分类 -->
      <div class="message-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部" name="all">
            <!-- 全部消息 -->
            <div class="message-list">
              <div v-if="allMessages.length === 0" class="empty-state">
                <el-empty description="暂无消息通知" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in allMessages" :key="msg.id" class="message-item">
                  <div class="msg-icon">
                    <i class="el-icon-message"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">系统消息</h4>
                      <span class="msg-time">{{ formatTime(msg.processedTime) }}</span>
                    </div>
                    <p class="msg-body">{{ msg.message }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getMyMessage } from '../api/user'

const userStore = useUserStore()
const activeTab = ref('all')

// 获取所有消息
const allMessages = ref([])

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(async () => {
  // 从 localStorage 获取用户信息，优先于 store 中的默认值
  const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const userId = storedUserInfo.userId || storedUserInfo.id || userStore.userInfo?.userId || userStore.userInfo?.id
  
  console.log('store中的userInfo:', userStore.userInfo)
  console.log('localStorage中的userInfo:', storedUserInfo)
  console.log('最终使用的用户ID:', userId)
  
  if (!userId) {
    console.error('用户未登录')
    return
  }
  
  // 发送请求获取消息列表
  try {
    console.log('开始获取消息列表, 用户ID:', userId)
    const res = await getMyMessage(userId)
    console.log('消息列表API响应:', res)
    
    // 处理响应数据，res 可能是 {data: {code, msg, data}} 或者直接是 {code, msg, data}
    const responseData = res.data?.data ? res.data : res
    
    if (responseData && responseData.code == 1) {
      allMessages.value = responseData.data || []
      console.log('消息列表获取成功:', allMessages.value)
    } else {
      allMessages.value = []
      console.log('消息列表为空或API返回错误, code:', responseData?.code, 'data:', responseData)
    }
  } catch (error) {
    console.error('获取消息列表失败:', error)
    allMessages.value = []
  }
})
</script>

<style scoped lang="scss">
.user-messages {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.page-actions {
  display: flex;
  gap: 10px;
}

.message-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.message-list {
  margin-top: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.message-items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
  
  &:hover {
    background-color: #f5f7fa;
  }
  
  &.unread {
    background-color: #ecf5ff;
    border-left: 4px solid #409EFF;
  }
}

.msg-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f0f9eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #67c23a;
  flex-shrink: 0;
}

.msg-content {
  flex: 1;
}

.msg-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.msg-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.msg-time {
  font-size: 12px;
  color: #999;
}

.msg-body {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.5;
}

.msg-actions {
  display: flex;
  align-items: center;
  pointer-events: none;
  
  .el-button {
    pointer-events: auto;
  }
}

// 消息详情弹窗样式
.message-detail-content {
  min-height: 200px;
}

.detail-wrapper {
  .detail-header {
    display: flex;
    align-items: flex-start;
    gap: 15px;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;
  }

  .detail-icon {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: #f0f9eb;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: #67c23a;
    flex-shrink: 0;
  }

  .detail-info {
    flex: 1;
  }

  .detail-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin: 0 0 8px 0;
  }

  .detail-time {
    font-size: 13px;
    color: #999;
  }

  .detail-body {
    .detail-content {
      font-size: 15px;
      color: #606266;
      line-height: 1.8;
      margin: 0;
      white-space: pre-wrap;
    }
  }

  .detail-extra {
    margin-top: 20px;

    .extra-data {
      pre {
        background-color: #f5f7fa;
        padding: 10px;
        border-radius: 4px;
        font-size: 12px;
        color: #666;
        overflow-x: auto;
      }
    }
  }
}

.detail-empty {
  padding: 40px 0;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .page-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .message-tabs {
    padding: 15px;
  }
  
  .message-item {
    padding: 12px;
  }
  
  .msg-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .msg-time {
    align-self: flex-end;
  }
}
</style>