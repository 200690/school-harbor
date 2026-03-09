<template>
  <div class="chat-view">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>聊天</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">聊天</h2>
      </div>

      <!-- 聊天内容 -->
      <div class="chat-container">
        <div class="chat-header">
          <h3 class="chat-title">与 {{ otherUserNickname }} 的聊天</h3>
          <div class="item-info">
            <span class="item-title">{{ itemTitle }}</span>
            <span class="item-price">¥{{ itemPrice }}</span>
          </div>
        </div>

        <div class="chat-messages" ref="messageListRef">
          <div class="message-list">
            <!-- 消息列表 -->
            <div v-for="(msg, index) in messages" :key="msg.id || index" class="message-item" :class="{ 'my-message': msg.isMine }">
              <div class="message-avatar">
                <img :src="msg.avatar" :alt="msg.sender" />
              </div>
              <div class="message-content">
                <div class="message-sender">{{ msg.sender }}</div>
                <div class="message-text">{{ msg.text }}</div>
                <div class="message-time">{{ msg.time }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            class="message-input"
  
          />
          <div class="input-actions">
            <el-button type="primary" @click="sendMessage" :disabled="!inputMessage.trim()">发送</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as secondHandApi from '@/api/secondHand'

const route = useRoute()



// 会话信息
const otherUserId = ref(route.query.otherUserId || '')
const otherUserNickname = ref(route.query.otherUserNickname || '用户')
const otherUserAvatar = ref(route.query.otherUserAvatar || '/default-avatar.png')

// 聊天信息
const itemId = ref(route.query.itemId || '')
const itemTitle = ref(route.query.itemTitle || '商品')
const itemPrice = ref(route.query.itemPrice || '0.00')

// 消息列表
const messages = ref([])

// 输入消息
const inputMessage = ref('')

// 消息列表容器的ref
const messageListRef = ref(null)

// 清理头像URL（去除多余的反引号和引号）
const cleanAvatarUrl = (url) => {
  if (!url) return '/default-avatar.png'
  return url.toString().replace(/`/g, '').replace(/"/g, '').trim() || '/default-avatar.png'
}

// 滚动到消息列表底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

// 加载聊天历史
const loadChatHistory = async () => {
  if (!otherUserId.value) {
    ElMessage.error('缺少用户ID')
    return
  }

  try {
    const response = await secondHandApi.getChatHistory(otherUserId.value)
    if (response.code === 1 && response.data && response.data.list && response.data.list.length > 0) {
      // 清空现有消息
      messages.value = []
      
      // 从本地存储获取用户信息
      const userInfoStr = localStorage.getItem('userInfo')
      let currentUserId = ''
      let currentUserAvatar = '/default-avatar.png'
      
      if (userInfoStr) {
        try {
          const userInfo = JSON.parse(userInfoStr)
          currentUserId = userInfo.userId || ''
          currentUserAvatar = userInfo.avatar || '/default-avatar.png'
          console.log('从userInfo获取的用户ID:', currentUserId)
          console.log('从userInfo获取的用户头像:', currentUserAvatar)
        } catch (parseError) {
          console.error('解析userInfo失败:', parseError)
        }
      }
      
      // 如果localStorage中没有userId，尝试从单独的userId键获取
      if (!currentUserId) {
        currentUserId = localStorage.getItem('userId') || ''
        console.log('从userId键获取的用户ID:', currentUserId)
      }
      
      // 添加历史消息并按时间排序
      const messageItems = []
      response.data.list.forEach((msg, index) => {
        // 跳过无效消息
        if (!msg.content || !msg.sendTime) return
        
        // 判断消息是否是当前用户发送的
        let isMine = false
        if (String(msg.senderId) === String(currentUserId)) {
          isMine = true
        }
        
        console.log('消息判断:', {
          senderId: msg.senderId,
          receiverId: msg.receiverId,
          currentUserId: currentUserId,
          isMine: isMine,
          content: msg.content,
          sendTime: msg.sendTime
        })
        
        const messageItem = {
          id: msg.id || `history-${index}-${Date.now()}`,
          isMine: isMine,
          sender: isMine ? '我' : (msg.senderName || otherUserNickname.value),
          text: msg.content,
          time: new Date(msg.sendTime).toLocaleTimeString(),
          avatar: isMine ? cleanAvatarUrl(currentUserAvatar) : cleanAvatarUrl(msg.senderAvatar || otherUserAvatar.value),
          sendTime: new Date(msg.sendTime)
        }
        messageItems.push(messageItem)
      })
      
      // 按发送时间排序，早发的在前
      messageItems.sort((a, b) => a.sendTime - b.sendTime)
      
      // 添加排序后的消息
      messages.value = messageItems
      
      // 滚动到消息列表底部
      scrollToBottom()
    } else {
      // 如果没有消息，清空消息列表
      messages.value = []
    }
  } catch (error) {
    console.error('加载聊天历史失败:', error)
    ElMessage.error('加载聊天历史失败')
    messages.value = []
  }
}



// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return


  // 从本地存储获取用户信息
  const userInfoStr = localStorage.getItem('userInfo')
  let currentUserAvatar = '/default-avatar.png'
  
  if (userInfoStr) {
    try {
      const userInfo = JSON.parse(userInfoStr)
      currentUserAvatar = userInfo.avatar || '/default-avatar.png'
    } catch (parseError) {
      console.error('解析userInfo失败:', parseError)
    }
  }

  const now = new Date()
  const newMessage = {
    id: `sent-${now.getTime()}-${Math.random().toString(36).substr(2, 9)}`,
    isMine: true,
    sender: '我',
    text: inputMessage.value,
    time: now.toLocaleTimeString(),
    avatar: cleanAvatarUrl(currentUserAvatar),
    sendTime: now
  }

  messages.value.push(newMessage)
  console.log('新消息添加到列表末尾:', newMessage)
  console.log('发送消息:', newMessage)
  console.log('消息列表长度:', messages.value.length)
  console.log('最后一条消息:', messages.value[messages.value.length - 1])
  
  // 滚动到消息列表底部
  scrollToBottom()

  // 通过API发送消息
  try {
    const messageData = {
      receiverId: otherUserId.value,
      itemId: itemId.value,
      content: inputMessage.value
    }
    console.log('发送消息数据:', messageData)
    await secondHandApi.sendMessage(messageData)
    inputMessage.value = ''
    ElMessage.success('消息发送成功')
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败，请重试')
    // 移除刚才添加的消息
    messages.value.pop()
    console.log('发送失败，已移除消息')
  }
}

// 定时器引用
let pollingTimer = null

// 组件挂载时
onMounted(() => {
  // 初始加载聊天历史
  loadChatHistory()
  
  // 设置每秒轮询一次
  pollingTimer = setInterval(() => {
    loadChatHistory()
  }, 1000)
})

// 组件卸载时
onUnmounted(() => {
  // 清除定时器
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
})
</script>

<style scoped lang="scss">
.chat-view {
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

.chat-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.chat-header {
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 20px;
}

.chat-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.item-info {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.item-title {
  font-weight: 500;
}

.item-price {
  color: var(--danger-color);
  font-weight: bold;
}



.chat-messages {
  height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;

  &.my-message {
    flex-direction: row-reverse;

    .message-content {
      background-color: #e6f7ff;
      border-radius: 12px 12px 0 12px;
    }
  }

  &.system-message {
    justify-content: center;
    .message-avatar {
      display: none;
    }
    .message-content {
      background-color: #f0f0f0;
      color: #666;
      font-size: 12px;
      padding: 5px 15px;
      border-radius: 10px;
    }
  }
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.message-content {
  flex: 1;
  max-width: 70%;
  background-color: #fff;
  padding: 10px 15px;
  border-radius: 12px 12px 12px 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.message-sender {
  font-size: 12px;
  color: #999;
  margin-bottom: 5px;
}

.message-text {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  margin-bottom: 5px;
}

.message-time {
  font-size: 11px;
  color: #999;
  text-align: right;
}

.chat-input {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.message-input {
  margin-bottom: 10px;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .chat-container {
    padding: 15px;
  }

  .chat-messages {
    height: 300px;
  }

  .message-content {
    max-width: 80%;
  }
}
</style>