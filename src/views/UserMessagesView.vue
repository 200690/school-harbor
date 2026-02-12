<template>
  <div class="user-messages">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>消息通知</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题和操作 -->
      <div class="page-header">
        <h2 class="page-title">消息通知</h2>
        <div class="page-actions">
          <el-button type="primary" @click="markAllAsRead">
            <i class="el-icon-check"></i> 全部已读
          </el-button>
          <el-button type="danger" @click="deleteAllRead">
            <i class="el-icon-delete"></i> 删除已读
          </el-button>
        </div>
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
                <div v-for="msg in allMessages" :key="msg.id" :class="['message-item', { 'unread': !msg.read }]" @click="markAsRead(msg)">
                  <div class="msg-icon">
                    <i :class="getMsgIcon(msg.type)"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">{{ msg.title }}</h4>
                      <span class="msg-time">{{ msg.time }}</span>
                    </div>
                    <p class="msg-body">{{ msg.content }}</p>
                  </div>
                  <div class="msg-actions">
                    <el-button size="small" type="text" @click.stop="deleteMessage(msg.id)">
                      <i class="el-icon-delete"></i>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="未读" name="unread">
            <!-- 未读消息 -->
            <div class="message-list">
              <div v-if="unreadMessages.length === 0" class="empty-state">
                <el-empty description="暂无未读消息" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in unreadMessages" :key="msg.id" class="message-item unread" @click="markAsRead(msg)">
                  <div class="msg-icon">
                    <i :class="getMsgIcon(msg.type)"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">{{ msg.title }}</h4>
                      <span class="msg-time">{{ msg.time }}</span>
                    </div>
                    <p class="msg-body">{{ msg.content }}</p>
                  </div>
                  <div class="msg-actions">
                    <el-button size="small" type="text" @click.stop="deleteMessage(msg.id)">
                      <i class="el-icon-delete"></i>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="系统" name="system">
            <!-- 系统消息 -->
            <div class="message-list">
              <div v-if="systemMessages.length === 0" class="empty-state">
                <el-empty description="暂无系统消息" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in systemMessages" :key="msg.id" :class="['message-item', { 'unread': !msg.read }]" @click="markAsRead(msg)">
                  <div class="msg-icon">
                    <i :class="getMsgIcon(msg.type)"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">{{ msg.title }}</h4>
                      <span class="msg-time">{{ msg.time }}</span>
                    </div>
                    <p class="msg-body">{{ msg.content }}</p>
                  </div>
                  <div class="msg-actions">
                    <el-button size="small" type="text" @click.stop="deleteMessage(msg.id)">
                      <i class="el-icon-delete"></i>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="交易" name="trade">
            <!-- 交易消息 -->
            <div class="message-list">
              <div v-if="tradeMessages.length === 0" class="empty-state">
                <el-empty description="暂无交易消息" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in tradeMessages" :key="msg.id" :class="['message-item', { 'unread': !msg.read }]" @click="markAsRead(msg)">
                  <div class="msg-icon">
                    <i :class="getMsgIcon(msg.type)"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">{{ msg.title }}</h4>
                      <span class="msg-time">{{ msg.time }}</span>
                    </div>
                    <p class="msg-body">{{ msg.content }}</p>
                  </div>
                  <div class="msg-actions">
                    <el-button size="small" type="text" @click.stop="deleteMessage(msg.id)">
                      <i class="el-icon-delete"></i>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="申请" name="application">
            <!-- 申请消息 -->
            <div class="message-list">
              <div v-if="applicationMessages.length === 0" class="empty-state">
                <el-empty description="暂无申请消息" />
              </div>
              <div v-else class="message-items">
                <div v-for="msg in applicationMessages" :key="msg.id" :class="['message-item', { 'unread': !msg.read }]" @click="markAsRead(msg)">
                  <div class="msg-icon">
                    <i :class="getMsgIcon(msg.type)"></i>
                  </div>
                  <div class="msg-content">
                    <div class="msg-header">
                      <h4 class="msg-title">{{ msg.title }}</h4>
                      <span class="msg-time">{{ msg.time }}</span>
                    </div>
                    <p class="msg-body">{{ msg.content }}</p>
                  </div>
                  <div class="msg-actions">
                    <el-button size="small" type="text" @click.stop="deleteMessage(msg.id)">
                      <i class="el-icon-delete"></i>
                    </el-button>
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElConfirm } from 'element-plus'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const activeTab = ref('all')

// 获取所有消息
const allMessages = ref([])

// 计算属性：未读消息
const unreadMessages = computed(() => {
  return allMessages.value.filter(msg => !msg.read)
})

// 计算属性：系统消息
const systemMessages = computed(() => {
  return allMessages.value.filter(msg => msg.type === 'system')
})

// 计算属性：交易消息
const tradeMessages = computed(() => {
  return allMessages.value.filter(msg => msg.type === 'trade')
})

// 计算属性：申请消息
const applicationMessages = computed(() => {
  return allMessages.value.filter(msg => msg.type === 'application')
})

// 根据消息类型获取图标
const getMsgIcon = (type) => {
  switch (type) {
    case 'system':
      return 'el-icon-message'
    case 'trade':
      return 'el-icon-s-finance'
    case 'application':
      return 'el-icon-s-flag'
    default:
      return 'el-icon-bell'
  }
}

// 标记为已读
const markAsRead = (msg) => {
  if (!msg.read) {
    msg.read = true
    ElMessage.success('已标记为已读')
  }
}

// 标记全部为已读
const markAllAsRead = () => {
  allMessages.value.forEach(msg => {
    msg.read = true
  })
  ElMessage.success('全部消息已标记为已读')
}

// 删除消息
const deleteMessage = (msgId) => {
  ElConfirm('确定要删除这条消息吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    allMessages.value = allMessages.value.filter(msg => msg.id !== msgId)
    ElMessage.success('消息已删除')
  }).catch(() => {
    // 取消操作
  })
}

// 删除所有已读消息
const deleteAllRead = () => {
  ElConfirm('确定要删除所有已读消息吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    allMessages.value = allMessages.value.filter(msg => !msg.read)
    ElMessage.success('已读消息已删除')
  }).catch(() => {
    // 取消操作
  })
}

onMounted(async () => {
  // 从 store 获取用户消息
  await userStore.getUserMessages()
  allMessages.value = userStore.userMessages
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