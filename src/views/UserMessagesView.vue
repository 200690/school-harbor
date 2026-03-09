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
          <el-tab-pane label="通知" name="notification">
            <div class="message-list">
              <div v-if="notificationMessages.length === 0" class="empty-state">
                <el-empty description="暂无通知" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in notificationMessages" :key="msg.id" class="message-item">
                  <div class="msg-icon">
                    <i class="el-icon-message"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">系统通知</h4>
                      <span class="msg-time">{{ formatTime(msg.processedTime) }}</span>
                    </div>
                    <p class="msg-body">{{ msg.message }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="消息" name="message">
            <div class="message-sub-tabs">
              <el-tabs v-model="messageSubTab">
                <el-tab-pane label="收到的消息" name="received">
                  <div class="message-list">
                    <el-loading v-if="loading" element-loading-text="加载中..." fullscreen />
                    <div v-else-if="receivedMessages.length === 0" class="empty-state">
                      <el-empty description="暂无收到的消息" />
                    </div>
                    <div v-else class="message-items">
                      <div v-for="msg in receivedMessages" :key="msg.id" class="message-item">
                        <div class="msg-icon">
                          <img :src="cleanAvatarUrl(msg.avatar)" :alt="msg.senderName" class="msg-avatar" />
                        </div>
                        <div class="msg-content">
                          <div class="msg-header">
                            <h4 class="msg-title">{{ msg.senderName }}</h4>
                            <span class="msg-time">{{ formatTime(msg.lastMessageTime) }}</span>
                          </div>
                          <p class="msg-body">{{ msg.lastMessage }}</p>
                          <div class="msg-footer" v-if="msg.itemTitle">
                            <span class="msg-item-title">{{ msg.itemTitle }}</span>
                            <span class="msg-item-status" :class="{ 'unread': msg.status === 0 }">{{ msg.status === 0 ? '未读' : '已读' }}</span>
                          </div>
                          <div class="msg-footer" v-if="msg.unreadCount > 0">
                            <span class="msg-unread-count">未读消息: {{ msg.unreadCount }}</span>
                          </div>
                          <div class="msg-actions">
                            <el-button type="primary" size="small" @click="enterChat(msg, 'received')">
                              <i class="el-icon-chat-dot-round"></i> 进入聊天
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-if="receivedMessages.length > 0" class="pagination">
                      <el-pagination
                        v-model:current-page="currentPage"
                        v-model:page-size="pageSize"
                        :page-sizes="[10, 20, 50]"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                      />
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="发出的消息" name="sent">
                  <div class="message-list">
                    <el-loading v-if="loading" element-loading-text="加载中..." fullscreen />
                    <div v-else-if="sentMessages.length === 0" class="empty-state">
                      <el-empty description="暂无发出的消息" />
                    </div>
                    <div v-else class="message-items">
                      <div v-for="msg in sentMessages" :key="msg.id" class="message-item">
                        <div class="msg-icon">
                          <img :src="cleanAvatarUrl(msg.otherUserAvatar || '/default-avatar.png')" :alt="msg.otherUserNickname || '对方'" class="msg-avatar" />
                        </div>
                        <div class="msg-content">
                          <div class="msg-header">
                            <h4 class="msg-title">{{ msg.otherUserNickname || '对方' }}</h4>
                            <span class="msg-time">{{ formatTime(msg.lastMessageTime) }}</span>
                          </div>
                          <p class="msg-body">{{ msg.lastMessage }}</p>
                          <div class="msg-footer" v-if="msg.itemTitle">
                            <span class="msg-item-title">{{ msg.itemTitle }}</span>
                          </div>
                          <div class="msg-actions">
                            <el-button type="primary" size="small" @click="enterChat(msg, 'sent')">
                              <i class="el-icon-chat-dot-round"></i> 进入聊天
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-if="sentMessages.length > 0" class="pagination">
                      <el-pagination
                        v-model:current-page="currentPage"
                        v-model:page-size="pageSize"
                        :page-sizes="[10, 20, 50]"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                      />
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { getMyMessage } from '../api/user'
import * as secondHandApi from '@/api/secondHand'

const userStore = useUserStore()
const router = useRouter()
const activeTab = ref('notification')
const messageSubTab = ref('received')
const notificationMessages = ref([])
const chatMessages = ref([])
const receivedMessages = ref([])
const sentMessages = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const loading = ref(false)

// 清理头像URL（去除多余的反引号和引号）
const cleanAvatarUrl = (url) => {
  if (!url) return '/default-avatar.png'
  return url.toString().replace(/`/g, '').replace(/"/g, '').trim() || '/default-avatar.png'
}

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

// 获取聊天消息列表
const getChatMessages = async (page, size) => {
  loading.value = true
  try {
    const response = await secondHandApi.getMessageList({ page, size })
    if (response.code === 1 && response.data) {
      const allMessages = response.data.list || []
      total.value = parseInt(response.data.total) || 0
      
      // 从本地存储获取用户ID
      const userInfoStr = localStorage.getItem('userInfo')
      let currentUserId = ''
      
      if (userInfoStr) {
        try {
          const userInfo = JSON.parse(userInfoStr)
          currentUserId = userInfo.userId || ''
        } catch (parseError) {
          console.error('解析userInfo失败:', parseError)
        }
      }
      
      // 如果localStorage中没有userId，尝试从单独的userId键获取
      if (!currentUserId) {
        currentUserId = localStorage.getItem('userId') || ''
      }
      
      // 根据数据结构分类消息
      receivedMessages.value = allMessages.filter(msg => String(msg.receiverId) === String(currentUserId))
      sentMessages.value = allMessages.filter(msg => String(msg.senderId) === String(currentUserId))
      
      console.log('消息分类:', {
        currentUserId: currentUserId,
        totalMessages: allMessages.length,
        receivedMessages: receivedMessages.value.length,
        sentMessages: sentMessages.value.length
      })
    } else {
      chatMessages.value = []
      receivedMessages.value = []
      sentMessages.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取聊天消息失败:', error)
    chatMessages.value = []
    receivedMessages.value = []
    sentMessages.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 进入聊天
const enterChat = async (msg, type) => {
  try {
    // 根据消息类型决定使用的ID
    let otherUserId = ''
    let otherUserNickname = ''
    let otherUserAvatar = ''
    
    if (type === 'received') {
      // 收到的消息：使用senderId作为otherUserId
      otherUserId = msg.senderId
      otherUserNickname = msg.senderName
      otherUserAvatar = cleanAvatarUrl(msg.avatar || '/default-avatar.png')
    } else if (type === 'sent') {
      // 发出的消息：使用otherUserId
      otherUserId = msg.otherUserId
      otherUserNickname = msg.otherUserNickname || '对方'
      otherUserAvatar = cleanAvatarUrl(msg.otherUserAvatar || '/default-avatar.png')
    }
    
    // 跳转到聊天界面
    router.push({
      path: '/chat',
      query: {
        otherUserId: otherUserId,
        otherUserNickname: otherUserNickname,
        otherUserAvatar: otherUserAvatar,
        itemId: msg.itemId,
        itemTitle: msg.itemTitle || '',
        itemPrice: msg.itemPrice || ''
      }
    })
  } catch (error) {
    console.error('进入聊天失败:', error)
  }
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  getChatMessages(1, size)
}

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  getChatMessages(page, pageSize.value)
}

// 监听标签切换
watch(activeTab, (newTab) => {
  if (newTab === 'message') {
    // 默认显示收到的消息
    messageSubTab.value = 'received'
    getChatMessages(currentPage.value, pageSize.value)
  }
})

// 监听消息子标签切换
watch(messageSubTab, () => {
  // 切换子标签时重新加载消息
  currentPage.value = 1
  getChatMessages(1, pageSize.value)
})

onMounted(async () => {
  const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const userId = storedUserInfo.userId || storedUserInfo.id || userStore.userInfo?.userId || userStore.userInfo?.id
  
  if (!userId) {
    return
  }
  
  try {
    const res = await getMyMessage(userId)
    const responseData = res.data?.data ? res.data : res
    
    if (responseData && responseData.code == 1) {
      notificationMessages.value = responseData.data || []
    } else {
      notificationMessages.value = []
    }
  } catch (error) {
    notificationMessages.value = []
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

.message-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.message-sub-tabs {
  margin-top: 20px;
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
  border: 1px solid #ebeef5;
  
  &:hover {
    background-color: #f5f7fa;
    border-color: #409eff;
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
  overflow: hidden;
  
  .msg-avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
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
  margin-bottom: 8px;
}

.msg-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.msg-item-title {
  font-size: 12px;
  color: #666;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.msg-item-status {
  font-size: 12px;
  color: #67c23a;
  
  &.unread {
    color: #f56c6c;
    font-weight: bold;
  }
}

.msg-unread-count {
  font-size: 12px;
  color: #f56c6c;
  font-weight: bold;
}

.msg-actions {
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
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
  
  .msg-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}
</style>
