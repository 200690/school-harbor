<template>
  <div class="chat-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/messages">消息通知</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>聊天</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 聊天容器 -->
      <div class="chat-container">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <div class="chat-user-info">
            <img :src="cleanAvatarUrl(otherUserAvatar)" :alt="otherUserNickname" class="user-avatar" />
            <div class="user-details">
              <h3 class="user-name">{{ otherUserNickname }}</h3>
              <div v-if="itemTitle" class="item-info">
                <span class="item-title">{{ itemTitle }}</span>
                <span v-if="itemPrice" class="item-price">¥{{ itemPrice }}</span>
              </div>
            </div>
          </div>
          <el-button @click="goBack" size="small">
            <i class="el-icon-back"></i> 返回
          </el-button>
        </div>

        <!-- 聊天消息区域 -->
        <div class="chat-messages" ref="messagesContainer">
          <el-loading v-if="loading" element-loading-text="加载中..." />
          <div v-else-if="messages.length === 0" class="empty-state">
            <el-empty description="暂无消息，开始聊天吧" />
          </div>
          <div v-else class="message-list">
            <div v-for="message in messages" :key="message.id" class="message-item" :class="{ 'is-mine': message.isMine }">
              <div class="message-avatar">
                <img :src="cleanAvatarUrl(message.isMine ? myAvatar : otherUserAvatar)" :alt="message.isMine ? '我' : otherUserNickname" />
              </div>
              <div class="message-content">
                <div class="message-sender">{{ message.isMine ? '我' : otherUserNickname }}</div>
                <div class="message-text">{{ message.content }}</div>
                <div class="message-time">{{ formatTime(message.sendTime) }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 消息输入区域 -->
        <div class="chat-input">
          <el-input
            v-model="newMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            @keyup.ctrl.enter="sendMessage"
          />
          <div class="input-actions">
            <span class="tip">按 Ctrl+Enter 发送</span>
            <el-button type="primary" @click="sendMessage" :loading="sending">
              发送
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getChatHistory, sendMessage as sendMessageApi } from '@/api/secondHand'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const messagesContainer = ref(null)
const loading = ref(false)
const sending = ref(false)
const newMessage = ref('')
const messages = ref([])
let pollTimer = null
const pollInterval = 5000

const otherUserId = computed(() => route.query.otherUserId || '')
const otherUserNickname = computed(() => route.query.otherUserNickname || '对方')
const otherUserAvatar = computed(() => route.query.otherUserAvatar || '/default-avatar.png')
const itemId = computed(() => route.query.itemId || '')
const itemTitle = computed(() => route.query.itemTitle || '')
const itemPrice = computed(() => route.query.itemPrice || '')

const myAvatar = computed(() => userStore.userInfo?.avatar || '/default-avatar.png')
const myUserId = computed(() => userStore.userInfo?.userId || userStore.userInfo?.id || localStorage.getItem('userId'))

const cleanAvatarUrl = (url) => {
  if (!url) return '/default-avatar.png'
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  return url.startsWith('/') ? url : `/${url}`
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
  
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadMessages = async () => {
  if (!otherUserId.value || !myUserId.value) {
    ElMessage.error('缺少必要参数')
    return
  }

  loading.value = true
  try {
    const response = await getChatHistory(otherUserId.value, myUserId.value, 1, 50)
    if (response.code === 1 && response.data) {
      const messageList = response.data.list || response.data || []
      messages.value = messageList.map(msg => ({
        id: msg.id,
        content: msg.content,
        sendTime: msg.sendTime || msg.createTime,
        isMine: String(msg.senderId) === String(myUserId.value)
      }))
      await scrollToBottom()
    }
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  } finally {
    loading.value = false
  }
}

const sendMessage = async () => {
  const content = newMessage.value.trim()
  if (!content) {
    ElMessage.warning('请输入消息内容')
    return
  }

  if (!otherUserId.value) {
    ElMessage.error('缺少接收人信息')
    return
  }

  sending.value = true
  try {
    const response = await sendMessageApi({
      receiverId: otherUserId.value,
      content: content,
      itemId: itemId.value || null
    })

    if (response.code === 1) {
      newMessage.value = ''
      await loadMessages()
    } else {
      ElMessage.error(response.msg || '发送失败')
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败')
  } finally {
    sending.value = false
  }
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const goBack = () => {
  router.push('/user/user/messages')
}

const startPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer)
  }
  pollTimer = setInterval(() => {
    if (!loading.value && !sending.value) {
      loadMessages()
    }
  }, pollInterval)
}

const stopPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

onMounted(() => {
  if (!otherUserId.value) {
    ElMessage.error('缺少接收人信息')
    router.push('/user/user/messages')
    return
  }
  loadMessages()
  startPolling()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped lang="scss">
.chat-page {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.chat-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  height: calc(100vh - 140px);
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #666;
}

.item-title {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  color: #f56c6c;
  font-weight: bold;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f9fafc;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message-item {
  display: flex;
  gap: 12px;
  max-width: 70%;

  &.is-mine {
    align-self: flex-end;
    flex-direction: row-reverse;
  }
}

.message-avatar {
  flex-shrink: 0;

  img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
  }
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-sender {
  font-size: 12px;
  color: #999;
}

.message-text {
  padding: 10px 14px;
  border-radius: 8px;
  background-color: #fff;
  color: #333;
  line-height: 1.5;
  word-wrap: break-word;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);

  .message-item.is-mine & {
    background-color: #409eff;
    color: #fff;
  }
}

.message-time {
  font-size: 11px;
  color: #999;
  text-align: right;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.tip {
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .chat-page {
    padding-top: 60px;
  }

  .chat-container {
    height: calc(100vh - 100px);
  }

  .chat-header {
    padding: 15px;
  }

  .user-avatar {
    width: 40px;
    height: 40px;
  }

  .user-name {
    font-size: 14px;
  }

  .item-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 3px;
  }

  .item-title {
    max-width: 150px;
  }

  .chat-messages {
    padding: 10px;
  }

  .message-item {
    max-width: 85%;
  }

  .message-avatar img {
    width: 35px;
    height: 35px;
  }

  .chat-input {
    padding: 15px;
  }
}
</style>
