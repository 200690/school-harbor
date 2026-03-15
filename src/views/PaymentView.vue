<template>
  <div class="payment-view">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>支付</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">支付</h2>
      </div>

      <!-- 支付内容 -->
      <div class="payment-content">
        <!-- 商品信息 -->
        <div class="payment-info">
          <h3 class="info-title">商品信息</h3>
          <div class="info-item">
            <span class="info-label">商品名称：</span>
            <span class="info-value">{{ itemTitle }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">商品价格：</span>
            <span class="info-value price">¥{{ itemPrice }}</span>
          </div>
        </div>

        <!-- 余额信息 -->
        <div class="balance-info">
          <h3 class="info-title">余额信息</h3>
          <div class="info-item">
            <span class="info-label">账户余额：</span>
            <span class="info-value balance">¥{{ userInfo?.balance || 0 }}</span>
          </div>
        </div>

        <!-- 支付方式 -->
        <div class="payment-method">
          <h3 class="method-title">支付方式</h3>
          <el-radio-group v-model="paymentMethod">
            <el-radio label="balance">余额支付</el-radio>
          </el-radio-group>
        </div>

        <!-- 支付按钮 -->
        <div class="payment-actions">
          <el-button type="primary" size="large" @click="confirmPayment">确认支付</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const route = useRoute()
const router = useRouter()

// 商品信息
const itemId = ref(route.query.itemId || '')
const itemTitle = ref(route.query.itemTitle || '')
const itemPrice = ref(route.query.itemPrice || '')
const sellerId = ref(route.query.sellerId || '')
const paymentType = ref(route.query.paymentType || 'balance')

// 支付方式
const paymentMethod = ref(paymentType.value)

// 确认支付
const confirmPayment = async () => {
  try {
    // 检查是否有卖家ID
    if (!sellerId.value) {
      ElMessage.error('缺少卖家信息')
      return
    }
    
    // 准备支付数据
    const paymentData = {
      itemId: Number(itemId.value),
      sellerId: Number(sellerId.value),
      price: Number(itemPrice.value),
      totalAmount: Number(itemPrice.value)
    }
    
    // 发送支付请求
    const response = await request({
      url: 'http://localhost:8080/api/second-hand/order/create',
      method: 'post',
      data: paymentData
    })
    
    if (response.code === 1) {
      ElMessage.success('支付成功')
      setTimeout(() => {
        router.push('/user/user/purchases')
      }, 1500)
    } else {
      ElMessage.error(response.msg || '支付失败')
    }
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败，请稍后重试')
  }
}

onMounted(() => {
  // 检查是否有商品信息
  if (!itemId.value || !itemTitle.value || !itemPrice.value || !sellerId.value) {
    ElMessage.error('缺少商品信息')
    router.push('/second-hand')
  }
})
</script>

<style scoped lang="scss">
.payment-view {
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

.payment-content {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.payment-info {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.balance-info {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.info-value.balance {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

.info-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  margin-bottom: 15px;
}

.info-label {
  width: 100px;
  font-weight: 500;
  color: #666;
}

.info-value {
  flex: 1;
  color: #333;
}

.info-value.price {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
}

.payment-method {
  margin-bottom: 30px;
}

.method-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.payment-actions {
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .payment-content {
    padding: 20px;
  }

  .info-item {
    flex-direction: column;
    gap: 5px;
  }

  .info-label {
    width: auto;
  }
}
</style>