<template>
  <div class="admin-user-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin">管理员中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin?tab=users">用户管理</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>用户详情</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">用户详情</h2>
      </div>

      <!-- 用户信息卡片 -->
      <div class="user-info-card card">
        <div class="user-header">
          <div class="user-avatar">
            <img :src="user.avatar" :alt="user.username" />
          </div>
          <div class="user-basic-info">
            <h3 class="user-name">{{ user.username }}</h3>
            <div class="user-meta">
              <span class="meta-item">{{ user.phone }}</span>
              <span class="meta-item">{{ user.email }}</span>
            </div>
          </div>
          <div class="user-credit-info">
            <div class="credit-score-container">
              <span class="credit-label">信誉分</span>
              <div class="credit-score-value">
                <span class="credit-score">{{ user.creditScore }}</span>
                <span class="credit-level">{{ getCreditLevel(user.creditScore) }}</span>
              </div>
            </div>
          </div>
        </div>

        <h3 class="section-title">用户详细信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">用户ID：</span>
            <span class="info-value">{{ user.id }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">注册时间：</span>
            <span class="info-value">{{ user.registerTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">账号状态：</span>
            <span class="info-value status-badge" :class="user.isBlocked ? 'status-blocked' : 'status-active'">
              {{ user.isBlocked ? '已拉黑' : '正常' }}
            </span>
          </div>
        </div>
      </div>

      <!-- 备注信息卡片 -->
      <div class="remark-card card">
        <h3 class="section-title">备注信息</h3>
        <el-input
          v-model="userRemark"
          type="textarea"
          :rows="4"
          placeholder="请输入备注信息"
          style="width: 100%; margin-bottom: 15px;"
        />
        <el-button type="primary" @click="saveRemark">保存备注</el-button>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="goBack">返回</el-button>
        <el-button type="danger" @click="toggleBlockStatus" :disabled="user.isBlocked">
          拉黑用户
        </el-button>
        <el-button type="success" @click="toggleBlockStatus" :disabled="!user.isBlocked">
          解除拉黑
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userId = route.params.id

// 模拟用户数据
const user = ref({
  id: userId,
  username: '小明',
  phone: '138****8888',
  email: 'xiaoming@example.com',
  avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20student&image_size=square',
  creditScore: 60,
  registerTime: '2026-01-01',
  isBlocked: false,
  remark: ''
})

// 备注信息
const userRemark = ref('')

// 获取信誉等级
const getCreditLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '一般'
  if (score >= 60) return '及格'
  return '较差'
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 切换拉黑状态
const toggleBlockStatus = () => {
  const action = user.value.isBlocked ? '解除拉黑' : '拉黑'
  ElConfirm(`确定要${action}这个用户吗？`, action + '用户', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: user.value.isBlocked ? 'success' : 'danger'
  }).then(() => {
    user.value.isBlocked = !user.value.isBlocked
    ElMessage.success(`${action}用户成功`)
  }).catch(() => {
    // 取消操作
  })
}

// 保存备注
const saveRemark = () => {
  // 这里应该调用后端API保存备注
  user.value.remark = userRemark.value
  ElMessage.success('保存备注成功')
}

onMounted(() => {
  // 这里应该根据用户ID从后端获取用户详情
  console.log(`加载用户 ${userId} 的详情`)
  // 模拟从后端获取数据
  setTimeout(() => {
    // 模拟数据加载完成
  }, 500)
})
</script>

<style scoped lang="scss">
.admin-user-detail {
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

.user-info-card {
  margin-bottom: 30px;
}

.user-header {
  display: flex;
  align-items: center;
  gap: 40px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #f0f0f0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-basic-info {
  flex: 1;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.user-meta {
  display: flex;
  gap: 20px;
}

.meta-item {
  color: #666;
  font-size: 14px;
}

.user-credit-info {
  min-width: 200px;
}

.credit-score-container {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
}

.credit-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  display: block;
}

.credit-score-value {
  display: flex;
  align-items: center;
  gap: 10px;
}

.credit-score {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.credit-level {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  background-color: #f0f0f0;
  color: #666;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
}

.info-value {
  color: #333;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
}

.status-active {
  background-color: rgba(103, 194, 58, 0.1);
  color: var(--success-color);
}

.status-blocked {
  background-color: rgba(245, 108, 108, 0.1);
  color: var(--danger-color);
}

.remark-card {
  margin-bottom: 30px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .user-header {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>