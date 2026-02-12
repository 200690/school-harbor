<template>
  <div class="user-purchases">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的购买</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的购买</h2>
      </div>

      <!-- 状态筛选 -->
      <div class="status-filter">
        <el-button-group>
          <el-button :type="activeStatus === 'all' ? 'primary' : 'default'" @click="activeStatus = 'all'">全部</el-button>
          <el-button :type="activeStatus === 'pending' ? 'primary' : 'default'" @click="activeStatus = 'pending'">待付款</el-button>
          <el-button :type="activeStatus === 'paid' ? 'primary' : 'default'" @click="activeStatus = 'paid'">待收货</el-button>
          <el-button :type="activeStatus === 'delivered' ? 'primary' : 'default'" @click="activeStatus = 'delivered'">待评价</el-button>
          <el-button :type="activeStatus === 'completed' ? 'primary' : 'default'" @click="activeStatus = 'completed'">已完成</el-button>
          <el-button :type="activeStatus === 'canceled' ? 'primary' : 'default'" @click="activeStatus = 'canceled'">已取消</el-button>
        </el-button-group>
      </div>

      <!-- 购买列表 -->
      <div class="purchases-list">
        <div v-if="filteredPurchases.length === 0" class="empty-state">
          <el-empty description="暂无购买记录" />
        </div>
        <div v-else class="purchase-items">
          <div v-for="purchase in filteredPurchases" :key="purchase.id" class="purchase-item">
            <div class="item-header">
              <el-tag :type="getStatusType(purchase.status)">{{ getStatusText(purchase.status) }}</el-tag>
              <span class="order-time">{{ purchase.orderTime }}</span>
              <span class="order-id">订单号：{{ purchase.orderId }}</span>
            </div>
            <div class="item-content">
              <div class="item-image">
                <img :src="purchase.itemImage" :alt="purchase.itemTitle" />
              </div>
              <div class="item-info">
                <h3 class="item-title">{{ purchase.itemTitle }}</h3>
                <div class="item-details">
                  <span class="item-condition">{{ purchase.itemCondition }}</span>
                  <span class="item-location"><i class="el-icon-location"></i> {{ purchase.tradeLocation }}</span>
                </div>
                <div class="item-price">
                  <span class="price">¥{{ purchase.price }}</span>
                  <span class="quantity">×{{ purchase.quantity }}</span>
                </div>
              </div>
            </div>
            <div class="item-actions">
              <el-button size="small" type="primary" @click="viewOrderDetail()">
                查看订单详情
              </el-button>
              <el-button v-if="purchase.status === 'pending'" size="small" type="success" @click="payOrder()">
                立即付款
              </el-button>
              <el-button v-if="purchase.status === 'paid'" size="small" type="warning" @click="confirmReceipt(purchase.id)">
                确认收货
              </el-button>
              <el-button v-if="purchase.status === 'delivered'" size="small" type="info" @click="evaluateItem()">
                评价商品
              </el-button>
              <el-button v-if="purchase.status === 'pending'" size="small" type="danger" @click="cancelOrder(purchase.id)">
                取消订单
              </el-button>
              <el-button size="small" type="danger" @click="reportOrder(purchase.id)">
                举报
              </el-button>
              <el-button size="small" type="warning" @click="afterSales(purchase.id)">
                售后
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredPurchases.length > 0" class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredPurchases.length"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'
import { useUserStore } from '../stores/user'

const router = useRouter()

const userStore = useUserStore()
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// 获取用户的购买记录
const purchases = ref([])

// 计算属性：根据状态筛选购买记录
const filteredPurchases = computed(() => {
  if (activeStatus.value === 'all') {
    return purchases.value
  }
  return purchases.value.filter(purchase => purchase.status === activeStatus.value)
})

// 根据状态获取标签类型
const getStatusType = (status) => {
  switch (status) {
    case 'pending':
      return 'warning'
    case 'paid':
      return 'primary'
    case 'delivered':
      return 'info'
    case 'completed':
      return 'success'
    case 'canceled':
      return 'danger'
    default:
      return 'default'
  }
}

// 根据状态获取文本
const getStatusText = (status) => {
  switch (status) {
    case 'pending':
      return '待付款'
    case 'paid':
      return '待收货'
    case 'delivered':
      return '待评价'
    case 'completed':
      return '已完成'
    case 'canceled':
      return '已取消'
    default:
      return '未知状态'
  }
}

// 查看订单详情
const viewOrderDetail = () => {
  ElMessage.info('查看订单详情')
  // 这里应该导航到订单详情页面
}

// 立即付款
const payOrder = () => {
  ElMessage.info('立即付款功能')
  // 这里应该实现付款功能
}

// 确认收货
const confirmReceipt = (id) => {
  ElConfirm('确定已经收到商品了吗？', '确认收货', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(() => {
    const purchase = purchases.value.find(p => p.id === id)
    if (purchase) {
      purchase.status = 'delivered'
      ElMessage.success('确认收货成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 评价商品
const evaluateItem = () => {
  ElMessage.info('评价商品功能')
  // 这里应该导航到评价页面
}

// 取消订单
const cancelOrder = (id) => {
  ElConfirm('确定要取消这个订单吗？', '取消订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const purchase = purchases.value.find(p => p.id === id)
    if (purchase) {
      purchase.status = 'canceled'
      ElMessage.success('订单已取消')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 举报订单
const reportOrder = (id) => {
  ElMessage.info('跳转到举报页面')
  // 这里应该导航到举报页面
  router.push(`/user/user/report/${id}`)
}

// 售后
const afterSales = (id) => {
  ElMessage.info('跳转到售后页面')
  // 这里应该导航到售后页面
  router.push(`/user/user/after-sales/${id}`)
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (current) => {
  currentPage.value = current
}

onMounted(async () => {
  // 从 store 获取用户购买记录
  await userStore.getUserPurchases()
  purchases.value = userStore.userPurchases
})
</script>

<style scoped lang="scss">
.user-purchases {
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

.status-filter {
  margin-bottom: 20px;
}

.purchases-list {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.purchase-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.purchase-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.item-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.order-time, .order-id {
  font-size: 12px;
  color: #999;
}

.item-content {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.item-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f9fafc;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.item-details {
  display: flex;
  gap: 20px;
  margin-bottom: 10px;
}

.item-condition, .item-location {
  font-size: 14px;
  color: #666;
}

.item-price {
  display: flex;
  align-items: center;
  gap: 10px;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
}

.quantity {
  font-size: 14px;
  color: #666;
}

.item-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .status-filter {
    overflow-x: auto;
    white-space: nowrap;
    padding-bottom: 10px;
  }
  
  .purchases-list {
    padding: 15px;
  }
  
  .purchase-item {
    padding: 15px;
  }
  
  .item-header {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .item-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .item-image {
    width: 150px;
    height: 150px;
  }
  
  .item-actions {
    flex-wrap: wrap;
  }
}
</style>