<template>
  <div class="user-after-sales">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/purchases">我的购买</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>售后/聊天</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">售后/聊天</h2>
      </div>

      <!-- 订单信息 -->
      <div class="order-info-card card">
        <h3 class="card-title">订单信息</h3>
        <div class="order-details">
          <div class="order-item">
            <span class="order-label">订单号：</span>
            <span class="order-value">{{ orderInfo.orderId || '无' }}</span>
          </div>
          <div class="order-item">
            <span class="order-label">商品名称：</span>
            <span class="order-value">{{ orderInfo.itemTitle || '无' }}</span>
          </div>
          <div class="order-item">
            <span class="order-label">订单金额：</span>
            <span class="order-value">¥{{ orderInfo.price || 0 }}</span>
          </div>
          <div class="order-item">
            <span class="order-label">购买时间：</span>
            <span class="order-value">{{ orderInfo.purchaseTime || '无' }}</span>
          </div>
          <div class="order-item">
            <span class="order-label">订单状态：</span>
            <span class="order-value">{{ orderInfo.status || '无' }}</span>
          </div>
        </div>
      </div>

      <!-- 聊天区域 -->
      <div class="chat-area card">
        <div class="chat-header">
          <div class="seller-info">
            <div class="seller-avatar">
              <img :src="sellerInfo.avatar" :alt="sellerInfo.username" />
            </div>
            <div class="seller-details">
              <h3 class="seller-name">{{ sellerInfo.username || '卖家' }}</h3>
              <p class="seller-credit">
                <i class="el-icon-coin"></i> 信誉分：{{ sellerInfo.creditScore || 60 }}
              </p>
            </div>
          </div>
          <div class="chat-actions">
            <router-link :to="`/user/profile/${sellerInfo.id || 1}`" class="btn btn-primary">
              查看卖家主页
            </router-link>
          </div>
        </div>
        
        <div class="chat-messages">
          <div v-for="message in chatMessages" :key="message.id" class="message-item" :class="message.isMine ? 'message-mine' : 'message-other'">
            <div class="message-avatar">
              <img :src="message.avatar" :alt="message.sender" />
            </div>
            <div class="message-content">
              <div class="message-sender">{{ message.sender }}</div>
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ message.time }}</div>
            </div>
          </div>
        </div>
        
        <div class="chat-input">
          <el-input
            v-model="messageInput"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            resize="none"
          ></el-input>
          <div class="input-actions">
            <el-upload
              class="upload-btn"
              action="#"
              :auto-upload="false"
              :on-change="handleImageUpload"
            >
              <el-button type="primary">
                <i class="el-icon-picture"></i> 图片
              </el-button>
            </el-upload>
            <el-button type="success" @click="sendMessage" :disabled="!messageInput.trim()">
              发送
            </el-button>
          </div>
        </div>
      </div>

      <!-- 售后申请 -->
      <div class="after-sales-form card">
        <h3 class="form-title">售后申请</h3>
        <el-form :model="afterSalesForm" :rules="afterSalesRules" ref="afterSalesFormRef" label-width="120px">
          <el-form-item label="售后类型" prop="afterSalesType">
            <el-select v-model="afterSalesForm.afterSalesType" placeholder="请选择售后类型">
              <el-option label="退款" value="refund"></el-option>
              <el-option label="换货" value="exchange"></el-option>
              <el-option label="维修" value="repair"></el-option>
              <el-option label="其他" value="other"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="售后原因" prop="afterSalesReason">
            <el-input
              type="textarea"
              :rows="4"
              placeholder="请详细描述售后原因"
              v-model="afterSalesForm.afterSalesReason"
            ></el-input>
          </el-form-item>
          <el-form-item label="上传证据">
            <el-upload
              class="upload-demo"
              action="#"
              :auto-upload="false"
              :on-change="handleAfterSalesFileChange"
              :file-list="afterSalesFileList"
              list-type="picture"
            >
              <el-button type="primary">
                <i class="el-icon-upload"></i> 选择文件
              </el-button>
              <template #tip>
                <div class="el-upload__tip">
                  请上传图片证据，最多3张
                </div>
              </template>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitAfterSales">提交售后申请</el-button>
            <el-button @click="resetAfterSalesForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const orderId = computed(() => route.params.id)

// 订单信息
const orderInfo = ref({
  orderId: 'ORD' + orderId.value + Date.now(),
  itemTitle: '商品名称',
  price: 100,
  purchaseTime: '2026-02-10 10:00:00',
  status: '已完成'
})

// 卖家信息
const sellerInfo = ref({
  id: 2,
  username: '卖家',
  avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20seller&image_size=square',
  creditScore: 60
})

// 聊天消息
const chatMessages = ref([
  {
    id: 1,
    sender: '系统',
    content: '聊天已开始，请注意文明用语',
    time: '2026-02-12 10:00:00',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=system%20avatar%20icon&image_size=square',
    isMine: false
  },
  {
    id: 2,
    sender: '卖家',
    content: '您好，有什么可以帮助您的？',
    time: '2026-02-12 10:01:00',
    avatar: sellerInfo.value.avatar,
    isMine: false
  }
])

// 消息输入
const messageInput = ref('')

// 发送消息
const sendMessage = () => {
  if (!messageInput.value.trim()) return
  
  const newMessage = {
    id: Date.now(),
    sender: userStore.userInfo.username || '我',
    content: messageInput.value.trim(),
    time: new Date().toLocaleString('zh-CN'),
    avatar: userStore.userInfo.avatar || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20student&image_size=square',
    isMine: true
  }
  
  chatMessages.value.push(newMessage)
  messageInput.value = ''
  
  // 模拟卖家回复
  setTimeout(() => {
    const replyMessage = {
      id: Date.now() + 1,
      sender: sellerInfo.value.username,
      content: '好的，我了解了，我们会尽快处理',
      time: new Date().toLocaleString('zh-CN'),
      avatar: sellerInfo.value.avatar,
      isMine: false
    }
    chatMessages.value.push(replyMessage)
  }, 1000)
}

// 处理图片上传
const handleImageUpload = () => {
  // 这里应该处理图片上传逻辑
  ElMessage.info('图片上传功能开发中')
}

// 售后申请表单
const afterSalesForm = ref({
  afterSalesType: '',
  afterSalesReason: ''
})

// 售后申请验证规则
const afterSalesRules = ref({
  afterSalesType: [
    { required: true, message: '请选择售后类型', trigger: 'change' }
  ],
  afterSalesReason: [
    { required: true, message: '请填写售后原因', trigger: 'blur' },
    { min: 10, message: '售后原因至少10个字符', trigger: 'blur' }
  ]
})

// 售后表单引用
const afterSalesFormRef = ref(null)

// 售后文件列表
const afterSalesFileList = ref([])

// 处理售后文件变化
const handleAfterSalesFileChange = (_, fileList) => {
  if (fileList.length > 3) {
    ElMessage.warning('最多上传3张图片')
    fileList.pop()
  }
  return fileList
}

// 提交售后申请
const submitAfterSales = async () => {
  if (!afterSalesFormRef.value) return
  
  await afterSalesFormRef.value.validate(async (valid) => {
    if (valid) {
      // 模拟提交售后申请
      try {
        // 这里应该调用后端API提交售后申请
        ElMessage.success('售后申请提交成功，我们会尽快处理')
        // 跳转到我的购买页面
        router.push('/user/user/purchases')
      } catch (error) {
        ElMessage.error('售后申请提交失败，请重试')
      }
    } else {
      ElMessage.warning('请完善售后申请信息')
    }
  })
}

// 重置售后表单
const resetAfterSalesForm = () => {
  if (afterSalesFormRef.value) {
    afterSalesFormRef.value.resetFields()
    afterSalesFileList.value = []
  }
}

onMounted(() => {
  // 模拟获取订单信息和卖家信息
  // 这里应该从后端API获取订单信息和卖家信息
  console.log('售后订单ID:', orderId.value)
})
</script>

<style scoped lang="scss">
.user-after-sales {
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

/* 订单信息卡片样式 */
.order-info-card {
  padding: 20px;
  margin-bottom: 30px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.order-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  display: flex;
  align-items: center;
}

.order-label {
  font-size: 14px;
  color: #666;
  width: 100px;
}

.order-value {
  font-size: 14px;
  color: #333;
  flex: 1;
}

/* 聊天区域样式 */
.chat-area {
  padding: 20px;
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.seller-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
}

.seller-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.seller-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.seller-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.seller-credit {
  font-size: 14px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 4px;
}

.chat-actions {
  display: flex;
  gap: 10px;
}

.chat-messages {
  flex: 1;
  min-height: 400px;
  max-height: 500px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.message-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.message-mine {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.message-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-sender {
  font-size: 12px;
  color: #999;
}

.message-text {
  font-size: 14px;
  color: #333;
  padding: 10px 14px;
  border-radius: 18px;
  background-color: #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-mine .message-text {
  background-color: #409EFF;
  color: #fff;
}

.message-time {
  font-size: 10px;
  color: #999;
  align-self: flex-end;
}

.chat-input {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 售后申请表单样式 */
.after-sales-form {
  padding: 20px;
  margin-bottom: 30px;
}

.form-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .order-info-card,
  .chat-area,
  .after-sales-form {
    padding: 15px;
  }
  
  .chat-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .chat-actions {
    width: 100%;
    justify-content: flex-start;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .input-actions {
    flex-direction: column;
  }
}
</style>