<template>
  <div class="user-center">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">个人中心</h2>
        <p class="page-subtitle">欢迎回来，{{ userInfo.username }}</p>
      </div>
      
      <!-- 个人信息卡片 -->
      <div class="user-info-card card">
        <div class="user-info-header">
          <div class="user-avatar">
            <img v-if="userInfo.avatar" :src="userInfo.avatar" :alt="userInfo.username" />
            <div v-else class="default-avatar">
              <i class="el-icon-user"></i>
            </div>
          </div>
          <div class="user-basic-info">
            <h3 class="user-name">{{ userInfo.username }}</h3>
            <div class="user-meta">
              <span class="user-meta-item">{{ userInfo.phone }}</span>
              <span class="user-meta-divider">|</span>
              <span class="user-meta-item">{{ userInfo.email }}</span>
            </div>
          </div>
          <div class="user-credit-info">
            <div class="credit-score-container">
              <span class="credit-label">信誉分</span>
              <div class="credit-score-value">
                <span class="credit-score">{{ userInfo.creditScore || 60 }}</span>
                <span class="credit-level">{{ getCreditLevel(userInfo.creditScore || 60) }}</span>
              </div>
              <div class="credit-progress">
                <div class="credit-progress-bar" :style="{ width: (userInfo.creditScore || 60) + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="user-info-footer">
          <div class="user-stats">
            <div class="user-stat-item">
              <span class="stat-value">{{ userPosts.length }}</span>
              <span class="stat-label">发布</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userApplications.length }}</span>
              <span class="stat-label">申请</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userPurchases.length }}</span>
              <span class="stat-label">购买</span>
            </div>
            <div class="user-stat-item">
              <span class="stat-value">{{ userFavorites.length }}</span>
              <span class="stat-label">收藏</span>
            </div>
          </div>
          <div class="user-actions">
            <router-link to="/user/user/info" class="btn btn-primary">编辑资料</router-link>
            <router-link to="/user/user/settings" class="btn btn-secondary">账号设置</router-link>
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
            <span class="balance-value">¥{{ userInfo.balance || 0 }}</span>
          </div>
          <div class="balance-item">
            <span class="balance-label">冻结资金</span>
            <span class="balance-value frozen">{{ userInfo.frozenBalance || 0 }}</span>
          </div>
        </div>
      </div>
      
      <!-- 功能导航 -->
      <div class="function-nav-section">
        <h3 class="section-title">快捷功能</h3>
        <div class="function-nav">
          <router-link to="/user/user/publish" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的发布.png" alt="我的发布" />
            </div>
            <span class="function-name">我的发布</span>
            <span class="function-count">{{ userPosts.length }}</span>
          </router-link>
          <router-link to="/user/user/applications" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的申请.png" alt="我的申请" />
            </div>
            <span class="function-name">我的申请</span>
            <span class="function-count">{{ userApplications.length }}</span>
          </router-link>
          <router-link to="/user/user/purchases" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的购买.png" alt="我的购买" />
            </div>
            <span class="function-name">我的购买</span>
            <span class="function-count">{{ userPurchases.length }}</span>
          </router-link>
          <router-link to="/user/user/messages" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/消息通知.png" alt="消息通知" />
            </div>
            <span class="function-name">消息通知</span>
            <span class="function-count">{{ userMessages.length }}</span>
          </router-link>
          <router-link to="/user/user/favorites" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的收藏.png" alt="我的收藏" />
            </div>
            <span class="function-name">我的收藏</span>
            <span class="function-count">{{ userFavorites.length }}</span>
          </router-link>
          <router-link to="/user/user/follows" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的关注.png" alt="我的关注" />
            </div>
            <span class="function-name">我的关注</span>
            <span class="function-count">{{ userFollows.length }}</span>
          </router-link>
          <router-link to="/user/user/blacklist" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/黑名单.png" alt="黑名单" />
            </div>
            <span class="function-name">黑名单</span>
            <span class="function-count">{{ userBlacklist.length }}</span>
          </router-link>
          <router-link to="/user/user/evaluations" class="function-item">
            <div class="function-icon">
              <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/user-center/我的评价.png" alt="我的评价" />
            </div>
            <span class="function-name">我的评价</span>
          </router-link>
        </div>
      </div>
      
      <!-- 最近活动 -->
      <div class="recent-activities card">
        <h3 class="section-title">最近活动</h3>
        <div class="activity-list">
          <div v-for="(activity, index) in recentActivities" :key="index" class="activity-item">
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
import { ElDialog, ElInput, ElButton, ElMessage, ElRadioGroup, ElRadio } from 'element-plus'
import request from '../utils/request'

const userStore = useUserStore()
const userInfo = ref(userStore.userInfo)
const userPosts = ref(userStore.userPosts)
const userApplications = ref(userStore.userApplications)
const userPurchases = ref(userStore.userPurchases)
const userMessages = ref(userStore.userMessages)
const userFavorites = ref(userStore.userFavorites)
const userFollows = ref(userStore.userFollows)
const userBlacklist = ref(userStore.userBlacklist)
const recentActivities = ref(userStore.recentActivities)

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
    const userId = userInfo.value.userId || userInfo.value.id || JSON.parse(localStorage.getItem('userInfo') || '{}').userId
    
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
    userInfo.value.balance = (userInfo.value.balance || 0) + amount
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
  // 模拟从后端获取数据
  await userStore.getUserInfoAction()
  await userStore.getUserPosts()
  await userStore.getUserApplications()
  await userStore.getUserMessages()
  await userStore.getUserFollows()
  await userStore.getUserBlacklist()
  await userStore.getRecentActivities()
  
  // 更新本地数据
  userInfo.value = userStore.userInfo
  userPosts.value = userStore.userPosts
  userApplications.value = userStore.userApplications
  userMessages.value = userStore.userMessages
  userFollows.value = userStore.userFollows
  userBlacklist.value = userStore.userBlacklist
  recentActivities.value = userStore.recentActivities
})
</script>

<style scoped lang="scss">
.user-center {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 80px;
}

.main-content {
  flex: 1;
  margin-top: 20px;
  padding: 20px 0;
}

/* 页面标题 */
.page-header {
  margin-bottom: 30px;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.page-subtitle {
  font-size: 16px;
  color: #666;
  margin: 0;
}

/* 个人信息卡片样式 */
.user-info-card {
  margin-bottom: 30px;
  padding: 30px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.user-info-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  gap: 40px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.3);
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
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
  color: rgba(255, 255, 255, 0.6);
}

.user-basic-info {
  flex: 1;
}

.user-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #fff;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 14px;
  opacity: 0.9;
}

.user-meta-item {
  display: flex;
  align-items: center;
}

.user-meta-divider {
  color: rgba(255, 255, 255, 0.6);
}

.user-credit-info {
  min-width: 250px;
}

.credit-score-container {
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 20px;
  backdrop-filter: blur(10px);
}

.credit-label {
  font-size: 14px;
  margin-bottom: 8px;
  display: block;
  opacity: 0.9;
}

.credit-score-value {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.credit-score {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
}

.credit-level {
  font-size: 14px;
  padding: 4px 12px;
  border-radius: 16px;
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-weight: 500;
}

.credit-progress {
  width: 100%;
  height: 6px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  overflow: hidden;
}

.credit-progress-bar {
  height: 100%;
  background-color: #fff;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.user-info-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.user-stats {
  display: flex;
  gap: 40px;
}

.user-stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.user-actions {
  display: flex;
  gap: 12px;
}

.btn {
  display: inline-block;
  padding: 10px 24px;
  border-radius: 24px;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  outline: none;
}

.btn-primary {
  background-color: #fff;
  color: #667eea;
  font-weight: 600;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.9);
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }
}

.btn-secondary {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  backdrop-filter: blur(10px);
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }
}

/* 余额卡片样式 */
.balance-card {
  margin-bottom: 30px;
  padding: 30px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.balance-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.balance-title {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  margin: 0;
}

.recharge-btn {
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }
}

.balance-content {
  display: flex;
  gap: 40px;
}

.balance-item {
  flex: 1;
}

.balance-label {
  display: block;
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.balance-value {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  
  &.frozen {
    color: rgba(255, 255, 255, 0.7);
    font-size: 20px;
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
  margin-bottom: 30px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 20px;
  background-color: #667eea;
  border-radius: 2px;
}

.function-nav {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.function-item {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 24px;
  text-align: center;
  transition: all 0.3s ease;
  text-decoration: none;
  color: #333;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    transform: scaleX(0);
    transform-origin: left;
    transition: transform 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
    
    &::before {
      transform: scaleX(1);
    }
  }
}

.function-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  overflow: hidden;
  background-color: rgba(64, 158, 255, 0.05);
  transition: all 0.3s ease;
}

.function-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all 0.3s ease;
}

.function-item {
  flex: 1;
  min-width: 150px;
  text-align: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  text-decoration: none;
  color: #333;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    transform: scaleX(0);
    transform-origin: left;
    transition: transform 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
    
    &::before {
      transform: scaleX(1);
    }
  }
}

.function-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}

.function-count {
  position: absolute;
  top: 12px;
  right: 12px;
  background-color: #F56C6C;
  color: white;
  font-size: 12px;
  font-weight: bold;
  min-width: 20px;
  height: 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

/* 最近活动样式 */
.recent-activities {
  margin-bottom: 30px;
  padding: 30px;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.activity-item:hover {
  background-color: #f0f7ff;
  transform: translateX(8px);
  border-left-color: #667eea;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.activity-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: #fff;
  flex-shrink: 0;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.activity-time {
  font-size: 14px;
  color: #999;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .user-info-header {
    flex-direction: column;
    text-align: center;
    gap: 30px;
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
    justify-content: space-around;
  }
  
  .function-nav {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  }
}

@media (max-width: 768px) {
  .user-info-card {
    padding: 20px;
  }
  
  .user-avatar {
    width: 100px;
    height: 100px;
  }
  
  .user-name {
    font-size: 20px;
  }
  
  .user-meta {
    flex-direction: column;
    align-items: center;
    gap: 8px;
  }
  
  .user-meta-divider {
    display: none;
  }
  
  .user-actions {
    flex-direction: column;
    width: 100%;
  }
  
  .btn {
    width: 100%;
    text-align: center;
  }
  
  .function-nav {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .function-item {
    padding: 20px 12px;
  }
  
  .function-icon {
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
  
  .function-name {
    font-size: 14px;
  }
  
  .recent-activities {
    padding: 20px;
  }
  
  .activity-item {
    padding: 16px;
  }
  
  .activity-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }
  
  .activity-title {
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .function-nav {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .user-stats {
    flex-wrap: wrap;
    gap: 16px;
  }
  
  .user-stat-item {
    flex: 1 1 calc(50% - 8px);
  }
}
</style>