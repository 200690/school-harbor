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
          <el-button :type="activeStatus === 'shipped' ? 'primary' : 'default'" @click="activeStatus = 'shipped'">待发货</el-button>
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
              <span class="order-id">订单号：{{ purchase.orderNo }}</span>
            </div>
            <div class="item-content">
              <div class="item-image" @click="navigateToEvaluation(purchase)">
                <img :src="purchase.itemCoverImage" :alt="purchase.itemTitle" style="cursor: pointer;" />
              </div>
              <div class="item-info">
                <h3 class="item-title" @click="navigateToEvaluation(purchase)" style="cursor: pointer;">{{ purchase.itemTitle }}</h3>
                <div class="item-details">
                  <span v-if="purchase.tradeLocation" class="item-location"><i class="el-icon-location"></i> {{ purchase.tradeLocation }}</span>
                </div>
                <div class="item-price">
                  <span class="price">¥{{ purchase.price }}</span>
                  <span class="quantity">×{{ purchase.quantity }}</span>
                  <span class="total-amount">总计：¥{{ purchase.totalAmount }}</span>
                </div>
              </div>
            </div>
            <div class="item-actions">
              <el-button size="small" type="primary" @click="viewOrderDetail(purchase.id)">
                查看订单详情
              </el-button>
              <el-button v-if="purchase.status === 1" size="small" type="success" @click="payOrder(purchase.id)">
                立即付款
              </el-button>
              <el-button v-if="purchase.status === 3" size="small" type="warning" @click="confirmReceipt(purchase.id)">
                确认收货
              </el-button>
              <el-button v-if="purchase.status === 4" size="small" type="info" @click="evaluateItem(purchase.id)">
                评价商品
              </el-button>
              <el-button v-if="purchase.status === 1" size="small" type="danger" @click="cancelOrder(purchase.id)">
                取消订单
              </el-button>
              <el-button size="small" type="danger" @click="reportOrder(purchase.id)">
                举报
              </el-button>
              <el-button v-if="purchase.status === 1 || purchase.status === 2" size="small" type="warning" @click="contactSeller(purchase.id)">
                联系卖家
              </el-button>
              <el-button v-if="purchase.status !== 1 && purchase.status !== 2" size="small" type="warning" @click="afterSales(purchase.id)">
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

      <!-- 订单详情对话框 -->
      <el-dialog
        v-model="orderDetailDialog"
        title="订单详情"
        width="80%"
        :before-close="() => orderDetailDialog = false"
      >
        <div v-if="orderDetailLoading" class="dialog-loading">
          <el-empty description="加载中..." />
        </div>
        <div v-else-if="orderDetailData" class="order-detail-content">
          <div class="order-detail-header">
            <el-image
              :src="orderDetailData.coverImage"
              :alt="orderDetailData.title"
              fit="cover"
              class="detail-image"
            />
            <div class="detail-info">
              <h3 class="detail-title">{{ orderDetailData.title }}</h3>
              <p class="detail-price">¥{{ orderDetailData.price }}</p>
              <p class="detail-description">{{ orderDetailData.description }}</p>
            </div>
          </div>
          <div class="order-detail-body">
            <el-descriptions title="商品详情" :column="3" border>
              <el-descriptions-item label="商品ID">{{ orderDetailData.id }}</el-descriptions-item>
              <el-descriptions-item label="分类">{{ orderDetailData.category }}</el-descriptions-item>
              <el-descriptions-item label="成色">{{ orderDetailData.condition }}</el-descriptions-item>
              <el-descriptions-item label="发布时间">{{ orderDetailData.publishTime }}</el-descriptions-item>
              <el-descriptions-item label="浏览次数">{{ orderDetailData.viewCount }}</el-descriptions-item>
              <el-descriptions-item label="状态">{{ orderDetailData.status }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
        <div v-else class="dialog-error">
          <el-empty description="获取商品详情失败" />
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElDialog, ElImage, ElDescriptions, ElDescriptionsItem } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getMyOrders, getSecondHandDetail, confirmReceipt as confirmReceiptApi, cancelOrder as cancelOrderApi } from '@/api/secondHand'

const router = useRouter()

const userStore = useUserStore()
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// 获取用户的购买记录
const purchases = ref([])

// 订单详情对话框
const orderDetailDialog = ref(false)
const orderDetailLoading = ref(false)
const orderDetailData = ref(null)

// 计算属性：根据状态筛选购买记录
const filteredPurchases = computed(() => {
  if (activeStatus.value === 'all') {
    return purchases.value
  }
  // 映射状态值
  const statusMap = {
    'pending': 1, // 待付款
    'shipped': 2, // 待发货
    'paid': 3, // 待收货
    'delivered': 4, // 待评价
    'completed': 5, // 已完成
    'canceled': 6 // 已取消
  }
  return purchases.value.filter(purchase => purchase.status === statusMap[activeStatus.value])
})

// 根据状态获取标签类型
const getStatusType = (status) => {
  switch (status) {
    case 1:
      return 'warning'
    case 2:
      return 'info'
    case 3:
      return 'primary'
    case 4:
      return 'info'
    case 5:
      return 'success'
    case 6:
      return 'danger'
    default:
      return 'default'
  }
}

// 根据状态获取文本
const getStatusText = (status) => {
  switch (status) {
    case 1:
      return '待付款'
    case 2:
      return '待发货'
    case 3:
      return '待收货'
    case 4:
      return '待评价'
    case 5:
      return '已完成'
    case 6:
      return '已取消'
    default:
      return '未知状态'
  }
}

// 查看订单详情
const viewOrderDetail = (id) => {
  // 查找购买记录
  const purchase = purchases.value.find(p => p.id === id)
  if (!purchase) {
    ElMessage.error('订单记录不存在')
    return
  }
  
  // 打开订单详情对话框
  orderDetailDialog.value = true
  orderDetailLoading.value = true
  orderDetailData.value = null
  
  // 调用API获取商品详情
  getSecondHandDetail(purchase.itemId)
    .then(response => {
      if (response.data) {
        orderDetailData.value = response.data
      }
    })
    .catch(error => {
      console.error('获取商品详情失败:', error)
      ElMessage.error('获取商品详情失败，请稍后重试')
    })
    .finally(() => {
      orderDetailLoading.value = false
    })
}

// 立即付款
const payOrder = (id) => {
  ElMessage.info(`立即付款功能: ${id}`)
  // 这里应该实现付款功能
}

// 确认收货
const confirmReceipt = (id) => {
  ElMessageBox.confirm('确定已经收到商品了吗？', '确认收货', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(() => {
    // 发送确认收货请求
    confirmReceiptApi(id)
      .then(response => {
        if (response.code === 1) {
          ElMessage.success('确认收货成功')
          // 重新查询购买记录，使用当前分页参数
          fetchPurchases()
        } else if (response.msg) {
          ElMessage.error(response.msg)
        } else {
          ElMessage.error('确认收货失败，请稍后重试')
        }
      })
      .catch(error => {
        console.error('确认收货失败:', error)
        // 错误消息已经在响应拦截器中显示，不需要重复显示
      })
  }).catch(() => {
    // 取消操作
  })
}

// 评价商品
const evaluateItem = (id) => {
  // 查找购买记录
  const purchase = purchases.value.find(p => p.id === id)
  if (purchase) {
    // 跳转到商品评价详情页面
    router.push({
      path: `/second-hand/comments/${purchase.itemId}`
    })
  }
}

// 导航到评价页面
const navigateToEvaluation = (purchase) => {
  if (purchase) {
    // 跳转到商品评价详情页面
    router.push({
      path: `/second-hand/comments/${purchase.itemId}`
    })
  }
}

// 取消订单
const cancelOrder = (id) => {
  ElMessageBox.confirm('确定要取消这个订单吗？', '取消订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 发送取消订单请求
    cancelOrderApi(id)
      .then(response => {
        console.log('取消订单响应:', response)
        if (response.code === 1) {
          ElMessage.success('订单已取消')
          // 重新查询购买记录
          fetchPurchases()
        } else if (response.msg) {
          ElMessage.error(response.msg)
        } else {
          ElMessage.error('取消订单失败，请稍后重试')
        }
      })
      .catch(error => {
        console.error('取消订单失败:', error)
        // 错误消息已经在响应拦截器中显示，不需要重复显示
      })
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

// 联系卖家
const contactSeller = (id) => {
  ElMessage.info(`联系卖家功能: ${id}`)
  // 这里应该实现联系卖家功能
}

// 分页处理
const handleSizeChange = async (size) => {
  pageSize.value = size
  currentPage.value = 1
  // 重新获取数据
  await fetchPurchases()
}

const handleCurrentChange = async (current) => {
  currentPage.value = current
  // 重新获取数据
  await fetchPurchases()
}

// 获取购买记录的函数
const fetchPurchases = async () => {
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('请先登录')
      router.push('/user/user/login')
      return
    }
    
    // 调用API获取用户购买记录，传入分页参数
    const response = await getMyOrders({
      id: userId,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    console.log('获取购买记录响应:', response)
    // 灵活处理不同的响应结构
    if (response.data && response.data.list) {
      purchases.value = response.data.list
    } else if (response.list) {
      purchases.value = response.list
    }
  } catch (error) {
    console.error('获取购买记录失败:', error)
    ElMessage.error('获取购买记录失败，请稍后重试')
  }
}

onMounted(async () => {
  // 清除现有数据
  purchases.value = []
  
  // 调用获取购买记录的函数
  await fetchPurchases()
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
  margin-bottom: 10px;
}

.item-location {
  font-size: 14px;
  color: #666;
}

.item-price {
  display: flex;
  align-items: center;
  gap: 15px;
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

.total-amount {
  font-size: 16px;
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
  margin-left: auto;
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
  
  .item-price {
    flex-wrap: wrap;
  }
  
  .total-amount {
    margin-left: 0;
    width: 100%;
    margin-top: 10px;
  }
  
  .item-actions {
    flex-wrap: wrap;
  }
}

/* 订单详情对话框样式 */
.order-detail-content {
  max-height: 70vh;
  overflow-y: auto;
}

.order-detail-header {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-image {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.detail-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.detail-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.detail-price {
  font-size: 24px;
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
  margin-bottom: 15px;
}

.detail-description {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.order-detail-body {
  margin-top: 20px;
}

.dialog-loading,
.dialog-error {
  padding: 60px 0;
  text-align: center;
}

@media (max-width: 768px) {
  .order-detail-header {
    flex-direction: column;
  }
  
  .detail-image {
    width: 100%;
    height: 250px;
  }
}
</style>