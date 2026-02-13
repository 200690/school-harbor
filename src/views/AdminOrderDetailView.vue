<template>
  <div class="admin-order-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin">管理员中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin?tab=orders">订单管理</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>订单详情</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">订单详情</h2>
      </div>

      <!-- 订单信息卡片 -->
      <div class="order-info-card card">
        <h3 class="section-title">订单基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">订单ID：</span>
            <span class="info-value">{{ order.id }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">买家信息：</span>
            <span class="info-value">{{ getUsernameById(order.userId) }} (ID: {{ order.userId }})</span>
          </div>
          <div class="info-item">
            <span class="info-label">卖家信息：</span>
            <span class="info-value">{{ getUsernameById(order.sellerId) }} (ID: {{ order.sellerId }})</span>
          </div>
          <div class="info-item">
            <span class="info-label">商品名称：</span>
            <span class="info-value">{{ order.itemTitle }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">价格：</span>
            <span class="info-value price">¥{{ order.price }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">购买时间：</span>
            <span class="info-value">{{ order.purchaseTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">订单状态：</span>
            <span class="info-value status-badge" :class="getStatusClass(order.status)">{{ order.status }}</span>
          </div>
        </div>
      </div>

      <!-- 备注信息卡片 -->
      <div class="remark-card card">
        <h3 class="section-title">备注信息</h3>
        <el-input
          v-model="orderRemark"
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
        <el-button type="danger" @click="deleteOrder">删除订单</el-button>
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
const orderId = route.params.id

// 模拟用户数据
const users = ref([
  { id: 1, username: '小明' },
  { id: 2, username: '张三' },
  { id: 3, username: '李四' }
])

// 模拟订单数据
const order = ref({
  id: orderId,
  userId: 1,
  sellerId: 3,
  itemTitle: '篮球',
  price: 80,
  purchaseTime: '2026-02-05',
  status: '已完成'
})

// 订单备注
const orderRemark = ref('')

// 根据用户ID获取用户名
const getUsernameById = (userId) => {
  const user = users.value.find(u => u.id === userId)
  return user ? user.username : '未知用户'
}

// 保存备注
const saveRemark = () => {
  // 这里应该调用后端API保存备注
  ElMessage.success('备注保存成功')
}

// 获取订单状态对应的样式类
const getStatusClass = (status) => {
  switch (status) {
    case '已完成':
      return 'status-completed'
    case '待收货':
      return 'status-pending'
    case '已取消':
      return 'status-cancelled'
    default:
      return ''
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 删除订单
const deleteOrder = () => {
  ElConfirm('确定要删除这个订单吗？', '删除订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    // 这里应该调用后端API删除订单
    ElMessage.success('删除订单成功')
    router.push('/user/admin?tab=orders')
  }).catch(() => {
    // 取消操作
  })
}

onMounted(() => {
  // 这里应该根据订单ID从后端获取订单详情
  console.log(`加载订单 ${orderId} 的详情`)
  // 模拟从后端获取数据
  setTimeout(() => {
    // 模拟数据加载完成
  }, 500)
})
</script>

<style scoped lang="scss">
.admin-order-detail {
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

.order-info-card,
.remark-card {
  margin-bottom: 30px;
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

.price {
  font-weight: bold;
  color: var(--danger-color);
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
}

.status-completed {
  background-color: rgba(103, 194, 58, 0.1);
  color: var(--success-color);
}

.status-pending {
  background-color: rgba(230, 162, 60, 0.1);
  color: var(--warning-color);
}

.status-cancelled {
  background-color: rgba(144, 147, 153, 0.1);
  color: var(--info-color);
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>