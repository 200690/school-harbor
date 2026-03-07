<template>
  <div class="user-center">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">个人中心</h2>
        <p class="page-subtitle">欢迎回来，{{ userInfo?.username || '用户' }}</p>
      </div>
      
      <!-- 个人信息卡片 -->
      <div class="user-info-card card">
        <div class="user-info-header">
          <div class="user-avatar">
            <img v-if="userInfo?.avatar" :src="userInfo.avatar" :alt="userInfo?.username || '用户'" />
            <div v-else class="default-avatar">
              <i class="el-icon-user"></i>
            </div>
          </div>
          <div class="user-basic-info">
            <h3 class="user-name">{{ userInfo?.username || '用户' }}</h3>
            <div class="user-meta">
              <span class="user-meta-item">{{ userInfo?.phone || '未设置' }}</span>
              <span class="user-meta-divider">|</span>
              <span class="user-meta-item">{{ userInfo?.email || '未设置' }}</span>
            </div>
          </div>
          <div class="user-credit-info">
            <div class="credit-score-container">
              <span class="credit-label">信誉分</span>
              <div class="credit-score-value">
                <span class="credit-score">{{ userInfo?.creditScore || 60 }}</span>
                <span class="credit-level">{{ getCreditLevel(userInfo?.creditScore || 60) }}</span>
              </div>
              <div class="credit-progress">
                <div class="credit-progress-bar" :style="{ width: (userInfo?.creditScore || 60) + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="user-info-footer">
          <div class="user-stats">
            <div class="user-stat-item">
              <span class="stat-value">{{ userInfo?.publicCount || 0 }}</span>
              <span class="stat-label">发布</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userInfo?.applicationCount || 0 }}</span>
              <span class="stat-label">申请</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userInfo?.buyCounnt || 0 }}</span>
              <span class="stat-label">购买</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userInfo?.favCount || 0 }}</span>
              <span class="stat-label">收藏</span>
            </div>
          </div>
          <div class="user-actions">
            <router-link to="/user/user/info" class="btn btn-primary">编辑资料</router-link>
          </div>
        </div>
      </div>
      
      <!-- 余额卡片 -->
      <div class="balance-card card">
        <div class="balance-header">
          <h3 class="balance-title">账户余额</h3>
          <button class="recharge-btn btn btn-primary" @click="showRechargeDialog">充值</button>
        </div>
        <div class="balance-content">
          <div class="balance-item">
            <span class="balance-label">可用余额</span>
            <span class="balance-value">¥{{ userInfo?.balance || 0 }}</span>
          </div>
          <div class="balance-item">
            <span class="balance-label">冻结资金</span>
            <span class="balance-value frozen">{{ userInfo?.frozenBalance || 0 }}</span>
          </div>
        </div>
      </div>
      
      <!-- 功能导航 -->
      <div class="function-nav-section">
        <h3 class="section-title">快捷功能</h3>
        <div class="function-nav">
          <router-link to="/user/user/publish" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的发布.png" alt="我的发布" />
            </div>
            <span class="function-name">我的发布</span>
          </router-link>
          <router-link to="/user/user/applications" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的申请.png" alt="我的申请" />
            </div>
            <span class="function-name">我的申请</span>
          </router-link>
          <router-link to="/user/user/purchases" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的购买.png" alt="我的购买" />
            </div>
            <span class="function-name">我的购买</span>
          </router-link>
          <router-link to="/user/user/messages" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/消息通知.png" alt="消息通知" />
            </div>
            <span class="function-name">消息通知</span>
          </router-link>
          <router-link to="/user/user/favorites" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的收藏.png" alt="我的收藏" />
            </div>
            <span class="function-name">我的收藏</span>
          </router-link>
          <router-link to="/user/user/follows" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的关注.png" alt="我的关注" />
            </div>
            <span class="function-name">我的关注</span>
          </router-link>
          <router-link to="/user/user/blacklist" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/黑名单.png" alt="黑名单" />
            </div>
            <span class="function-name">黑名单</span>
          </router-link>
          <router-link to="/user/user/evaluations" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/harbor/background/我的评价.png" alt="我的评价" />
            </div>
            <span class="function-name">我的评价</span>
          </router-link>
        </div>
      </div>
      
      <!-- 最近活动 -->
      <div class="recent-activities card">
        <h3 class="section-title">最近活动</h3>
        <div class="activity-list">
          <div v-if="recentActivities.length === 0" class="activity-empty">
            <el-empty description="暂无最近活动" :image-size="60" />
          </div>
          <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
            <div class="activity-icon">
              <i :class="activity.icon"></i>
            </div>
            <div class="activity-content">
              <p class="activity-title">{{ activity.title }}</p>
              <p class="activity-time">{{ activity.time }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 充值弹窗 -->
  <el-dialog
    v-model="rechargeDialogVisible"
    title="账户充值"
    width="400px"
    center
  >
    <div class="recharge-dialog-content">
      <div class="recharge-amount-input">
        <el-input
          v-model="rechargeAmount"
          placeholder="请输入充值金额"
          type="number"
          step="1"
          min="1"
        />
      </div>
      <div class="recharge-options">
        <el-radio-group v-model="rechargeAmount">
          <el-radio
            v-for="option in rechargeOptions"
            :key="option"
            :label="option"
            @click="selectRechargeAmount(option)"
          >
            ¥{{ option }}
          </el-radio>
        </el-radio-group>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRecharge">确认充值</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { ElDialog, ElInput, ElButton, ElMessage, ElRadioGroup, ElRadio, ElEmpty } from 'element-plus'
import request from '../utils/request'
import { getUserCenter } from '../api/user'

const userStore = useUserStore()
const userInfo = ref(userStore.userInfo)
// 最近活动从 userInfo.tags 动态生成
const recentActivities = ref([])

// 根据 tags 生成最近活动列表
const generateRecentActivities = (tags) => {
  if (!tags || !Array.isArray(tags) || tags.length === 0) {
    return []
  }
  return tags.map((tag, index) => ({
    id: index,
    title: tag,
    time: new Date().toLocaleString(),
    icon: getActivityIcon(tag)
  }))
}

// 根据活动内容获取对应图标
const getActivityIcon = (title) => {
  if (title.includes('兼职')) {
    return 'el-icon-suitcase'
  } else if (title.includes('二手') || title.includes('商品')) {
    return 'el-icon-goods'
  } else if (title.includes('回复') || title.includes('评论')) {
    return 'el-icon-chat-dot-round'
  } else if (title.includes('申请')) {
    return 'el-icon-document-checked'
  } else if (title.includes('发布')) {
    return 'el-icon-circle-plus'
  } else if (title.includes('购买') || title.includes('订单')) {
    return 'el-icon-shopping-cart-full'
  } else if (title.includes('收藏')) {
    return 'el-icon-star-on'
  } else if (title.includes('消息') || title.includes('通知')) {
    return 'el-icon-bell'
  }
  return 'el-icon-info'
}

// 充值弹窗
const rechargeDialogVisible = ref(false)
const rechargeAmount = ref('')
const rechargeOptions = ref(['50', '100', '200', '500', '1000'])

// 显示充值弹窗
const showRechargeDialog = () => {
  rechargeDialogVisible.value = true
}

// 充值处理
const handleRecharge = async () => {
  if (!rechargeAmount.value || isNaN(rechargeAmount.value) || parseFloat(rechargeAmount.value) <= 0) {
    ElMessage.error('请输入有效的充值金额')
    return
  }
  
  try {
    const amount = parseFloat(rechargeAmount.value)
    // 获取userId
    const userId = userInfo.value?.userId || userInfo.value?.id || JSON.parse(localStorage.getItem('userInfo') || '{}').userId
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法充值')
      return
    }
    
    // 发送充值请求
    await request({
      url: '/user/balance/recharge',
      method: 'post',
      data: { 
        amount, 
        userId 
      }
    })
    
    // 请求成功后更新余额
    if (userInfo.value) {
      userInfo.value.balance = (userInfo.value.balance || 0) + amount
    }
    ElMessage.success(`充值成功，金额：¥${amount}`)
    rechargeDialogVisible.value = false
    rechargeAmount.value = ''
  } catch (error) {
    console.error('充值失败:', error)
    ElMessage.error('充值失败，请稍后重试')
  }
}

// 选择充值金额
const selectRechargeAmount = (amount) => {
  rechargeAmount.value = amount
}

// 获取信誉等级
const getCreditLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '一般'
  if (score >= 60) return '及格'
  return '较差'
}

onMounted(async () => {
  // 只发送一个请求获取用户中心数据
  try {
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = storedUserInfo.userId || storedUserInfo.id || 6
    
    const response = await getUserCenter(userId)
    console.log('获取用户中心数据:', response.data)
    
    // 更新用户信息
    if (response.data) {
      userInfo.value = response.data
      // 同步更新 store 中的用户信息
      userStore.setUserInfo(response.data)
      // 根据 tags 字段生成最近活动
      recentActivities.value = generateRecentActivities(response.data.tags)
    }
  } catch (error) {
    console.error('获取用户中心数据失败:', error)
    // 如果请求失败，使用本地存储的用户信息
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (storedUserInfo && storedUserInfo.username) {
      userInfo.value = storedUserInfo
    }
    // 使用 store 中的默认最近活动数据
    recentActivities.value = userStore.recentActivities
  }
})
</script>

<style scoped lang="scss">
.user-center {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 80px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
}

.main-content {
  flex: 1;
  margin-top: 24px;
  padding: 24px 0 40px;
}

/* 页面标题 */
.page-header {
  margin-bottom: 32px;
  text-align: center;
  
  .page-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 12px;
    color: #1a1a2e;
    letter-spacing: -0.5px;
  }
  
  .page-subtitle {
    font-size: 16px;
    color: #666;
    margin: 0;
    font-weight: 400;
  }
}

/* 个人信息卡片样式 */
.user-info-card {
  margin-bottom: 24px;
  padding: 32px;
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -20%;
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
    border-radius: 50%;
  }
}

.user-info-header {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
  gap: 32px;
  position: relative;
  z-index: 1;
}

.user-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.4);
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  
  &:hover {
    transform: scale(1.05);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  }
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  color: rgba(255, 255, 255, 0.8);
}

.user-basic-info {
  flex: 1;
}

.user-name {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 10px;
  color: #fff;
  letter-spacing: 0.5px;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  opacity: 0.95;
}

.user-meta-item {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  padding: 6px 14px;
  border-radius: 20px;
  backdrop-filter: blur(10px);
}

.user-meta-divider {
  display: none;
}

.user-credit-info {
  min-width: 220px;
}

.credit-score-container {
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  padding: 20px 24px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.credit-label {
  font-size: 13px;
  margin-bottom: 10px;
  display: block;
  opacity: 0.9;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.credit-score-value {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
}

.credit-score {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
}

.credit-level {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.25);
  color: #fff;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.credit-progress {
  width: 100%;
  height: 8px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  overflow: hidden;
}

.credit-progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #fff 0%, rgba(255,255,255,0.8) 100%);
  border-radius: 4px;
  transition: width 0.5s ease;
  box-shadow: 0 2px 8px rgba(255, 255, 255, 0.3);
}

.user-info-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
}

.user-stats {
  display: flex;
  gap: 48px;
}

.user-stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  transition: transform 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #fff;
}

.stat-label {
  font-size: 13px;
  opacity: 0.85;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.user-actions {
  display: flex;
  gap: 12px;
}

.btn {
  display: inline-block;
  padding: 12px 28px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  outline: none;
  letter-spacing: 0.5px;
}

.btn-primary {
  background: linear-gradient(135deg, #fff 0%, #f0f0f0 100%);
  color: #667eea;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  
  &:hover {
    background: linear-gradient(135deg, #fff 0%, #e8e8e8 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  }
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }
}

/* 余额卡片样式 */
.balance-card {
  margin-bottom: 24px;
  padding: 28px 32px;
  border-radius: 16px;
  background: #fff;
  color: #333;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.balance-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.balance-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  
  &::before {
    content: '';
    width: 4px;
    height: 18px;
    background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
    border-radius: 2px;
  }
}

.recharge-btn {
  padding: 10px 24px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  }
}

.balance-content {
  display: flex;
  gap: 48px;
}

.balance-item {
  flex: 1;
  padding: 16px 20px;
  background: linear-gradient(135deg, #f8f9fc 0%, #f0f3f8 100%);
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.1);
}

.balance-label {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.balance-value {
  font-size: 28px;
  font-weight: 700;
  color: #667eea;
  
  &.frozen {
    color: #999;
    font-size: 22px;
  }
}

/* 充值弹窗样式 */
.recharge-dialog-content {
  padding: 20px 0;
}

.recharge-amount-input {
  margin-bottom: 24px;
}

.recharge-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

/* 功能导航样式 */
.function-nav-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #1a1a2e;
  display: flex;
  align-items: center;
  gap: 10px;
  
  &::before {
    content: '';
    width: 4px;
    height: 22px;
    background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
    border-radius: 2px;
  }
}

.function-nav {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
}

.function-item {
  background: #fff;
  border-radius: 16px;
  padding: 24px 16px;
  text-align: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  color: #333;
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.04);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    transform: scaleX(0);
    transform-origin: center;
    transition: transform 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(102, 126, 234, 0.15);
    border-color: rgba(102, 126, 234, 0.1);
    
    &::before {
      transform: scaleX(1);
    }
    
    .function-icon {
      transform: scale(1.1);
      box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
    }
  }
}

.function-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  overflow: hidden;
  background: linear-gradient(135deg, #f0f4ff 0%, #e8eeff 100%);
  transition: all 0.3s ease;
  
  img {
    width: 60%;
    height: 60%;
    object-fit: contain;
  }
}

.function-name {
  font-size: 14px;
  font-weight: 500;
  color: #1a1a2e;
}

/* 最近活动样式 */
.recent-activities {
  margin-bottom: 24px;
  padding: 28px 32px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-empty {
  padding: 40px 0;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  background: linear-gradient(135deg, #fafbfc 0%, #f5f7fa 100%);
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
  
  &:hover {
    background: #fff;
    transform: translateX(6px);
    border-color: rgba(102, 126, 234, 0.2);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.1);
  }
}

.activity-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
  color: #fff;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.25);
}

.activity-content {
  flex: 1;
  min-width: 0;
}

.activity-title {
  font-size: 15px;
  color: #1a1a2e;
  margin-bottom: 4px;
  font-weight: 500;
  line-height: 1.4;
}

.activity-time {
  font-size: 13px;
  color: #999;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .user-info-header {
    flex-direction: column;
    text-align: center;
    gap: 24px;
  }
  
  .user-credit-info {
    min-width: 100%;
  }
  
  .user-info-footer {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .user-stats {
    width: 100%;
    justify-content: center;
    gap: 32px;
  }
  
  .balance-content {
    flex-direction: column;
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .user-center {
    padding-top: 70px;
  }
  
  .main-content {
    margin-top: 16px;
    padding: 16px 0 24px;
  }
  
  .page-header {
    margin-bottom: 24px;
    
    .page-title {
      font-size: 26px;
    }
    
    .page-subtitle {
      font-size: 14px;
    }
  }
  
  .user-info-card {
    padding: 24px;
    border-radius: 16px;
  }
  
  .user-info-header {
    margin-bottom: 24px;
  }
  
  .user-avatar {
    width: 90px;
    height: 90px;
  }
  
  .user-name {
    font-size: 22px;
  }
  
  .user-meta {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
    gap: 8px;
  }
  
  .user-meta-item {
    font-size: 12px;
    padding: 4px 10px;
  }
  
  .user-credit-info {
    min-width: auto;
    width: 100%;
  }
  
  .credit-score-container {
    padding: 16px 20px;
  }
  
  .credit-score {
    font-size: 28px;
  }
  
  .user-info-footer {
    padding-top: 20px;
  }
  
  .user-stats {
    gap: 24px;
  }
  
  .stat-value {
    font-size: 20px;
  }
  
  .stat-label {
    font-size: 12px;
  }
  
  .user-actions {
    width: 100%;
  }
  
  .btn {
    width: 100%;
    text-align: center;
    padding: 10px 20px;
  }
  
  .balance-card {
    padding: 20px 24px;
  }
  
  .balance-value {
    font-size: 24px;
  }
  
  .function-nav {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }
  
  .function-item {
    padding: 16px 10px;
    border-radius: 12px;
  }
  
  .function-icon {
    width: 44px;
    height: 44px;
    border-radius: 10px;
  }
  
  .function-name {
    font-size: 12px;
  }
  
  .recent-activities {
    padding: 20px 24px;
  }
  
  .activity-item {
    padding: 12px 16px;
  }
  
  .activity-icon {
    width: 38px;
    height: 38px;
    border-radius: 10px;
    font-size: 18px;
    margin-right: 12px;
  }
  
  .activity-title {
    font-size: 14px;
  }
  
  .activity-time {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .function-nav {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  
  .user-stats {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .user-stat-item {
    padding: 8px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
  }
}
</style>