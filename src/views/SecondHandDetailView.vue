<template>
  <div class="second-hand-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <!-- 根据导航来源显示不同的面包屑 -->
        <template v-if="isFromMyPublish">
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/publish">我的发布</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>商品详情</el-breadcrumb-item>
        </template>
        <template v-else>
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>商品详情</el-breadcrumb-item>
        </template>
      </el-breadcrumb>

      <!-- 商品详情 -->
      <div class="detail-card" v-if="itemDetail">
        <!-- 商品图片和信息 -->
        <div class="item-header">
          <div class="item-images">
            <div class="main-image">
              <img :src="cleanImageUrl(itemDetail.coverImage)" :alt="itemDetail.title" />
            </div>
            <div class="image-list">
              <div class="image-item" v-for="(img, index) in itemDetail.images" :key="index">
                <img :src="cleanImageUrl(img)" :alt="itemDetail.title" />
              </div>
            </div>
          </div>
          
          <div class="item-info">
            <h1 class="item-title">{{ itemDetail.title }}</h1>
            <div class="item-price">
              <span class="current-price">¥{{ itemDetail.price }}</span>
              <span class="original-price" v-if="itemDetail.originalPrice">原价 ¥{{ itemDetail.originalPrice }}</span>
            </div>
            <div class="item-meta">
              <span class="meta-item"><i class="el-icon-view"></i> {{ itemDetail.viewCount || 0 }} 浏览</span>
              <span class="meta-item"><i class="el-icon-star-off"></i> {{ itemDetail.favoriteCount || 0 }} 收藏</span>
              <span class="meta-item"><i class="el-icon-time"></i> {{ itemDetail.publishTime }}</span>
            </div>
            <div class="item-location">
              <i class="el-icon-location"></i>
              <span>{{ itemDetail.school }} - {{ itemDetail.location }}</span>
            </div>
            <div class="seller-info">
              <div class="seller-avatar">
                <img :src="itemDetail.sellerAvatar || '/default-avatar.png'" :alt="itemDetail.sellerName || '卖家'" />
              </div>
              <div class="seller-details">
                <h4>{{ itemDetail.sellerName || '卖家' }}</h4>
              </div>
              <el-button type="primary" class="contact-btn">
                <i class="el-icon-chat-line-round"></i> 联系卖家
              </el-button>
            </div>
            <div class="item-actions">
              <el-button type="primary" size="large" class="buy-btn" @click="buyItem">
                <i class="el-icon-s-finance"></i> 立即购买
              </el-button>
              <el-button :type="isFavorite ? 'warning' : 'default'" size="large" class="favorite-btn" @click="toggleFavorite">
                <i :class="isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i> {{ isFavorite ? '已收藏' : '收藏' }}
              </el-button>
            </div>
          </div>
        </div>

        <!-- 商品描述 -->
        <div class="item-content">
          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-document"></i> 商品描述</h3>
            <div class="content-text">
              <p v-if="itemDetail.description">{{ itemDetail.description }}</p>
              <p v-else>暂无商品描述</p>
            </div>
          </div>

          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-info"></i> 商品详情</h3>
            <div class="detail-info">
              <div class="info-row">
                <span class="info-label">商品成色：</span>
                <span class="info-value">{{ itemDetail.conditionDesc || getConditionText(itemDetail.condition) }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">浏览次数：</span>
                <span class="info-value">{{ itemDetail.viewCount || 0 }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">收藏次数：</span>
                <span class="info-value">{{ itemDetail.favoriteCount || 0 }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">发布时间：</span>
                <span class="info-value">{{ itemDetail.publishTime }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">更新时间：</span>
                <span class="info-value">{{ itemDetail.updateTime }}</span>
              </div>
            </div>
          </div>

          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-location"></i> 交易地点</h3>
            <div class="location-info">
              <p>{{ itemDetail.school }} - {{ itemDetail.location }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-else class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <!-- 推荐商品 -->
      <div class="recommended-items" v-if="recommendedItems.length > 0">
        <h3 class="section-title">相关推荐</h3>
        <div class="item-list">
          <div class="list-item" v-for="item in recommendedItems" :key="item.id">
            <div class="item-image">
              <img :src="cleanImageUrl(item.coverImage)" :alt="item.title" />
            </div>
            <div class="item-info">
              <h4 class="item-title">{{ item.title }}</h4>
              <p class="item-description">{{ item.conditionDesc || getConditionText(item.condition) }}</p>
              <div class="item-meta">
                <span class="price">¥{{ item.price }}</span>
                <span class="location"><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
              </div>
            </div>
            <router-link :to="`/second-hand/detail/${item.id}`" class="btn btn-primary">查看详情</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useSecondHandStore } from '../stores/secondHand'
import { useUserStore } from '../stores/user'
import { getSecondHandDetail, checkSecondHandFavorite } from '@/api/secondHand'

const route = useRoute()
const router = useRouter()
const secondHandStore = useSecondHandStore()
const userStore = useUserStore()

const itemId = ref(route.params.id || 1)
const itemDetail = ref(null)
const recommendedItems = ref([])
const isFavorited = ref(false)
const isFromMyPublish = ref(false)

// 计算是否已收藏
const isFavorite = computed(() => {
  return isFavorited.value
})

// 切换收藏状态
const toggleFavorite = async () => {
  if (isFavorited.value) {
    await secondHandStore.removeFavorite(Number(itemId.value))
    ElMessage.success('已取消收藏')
    isFavorited.value = false
  } else {
    await secondHandStore.addFavorite(itemDetail.value)
    ElMessage.success('收藏成功')
    isFavorited.value = true
  }
}

// 购买商品
const buyItem = async () => {
  try {
    await secondHandStore.buyItemAction(Number(itemId.value))
    await userStore.getUserPurchases()
    ElMessage.success('购买成功')
    setTimeout(() => {
      router.push('/user/purchases')
    }, 1500)
  } catch (error) {
    ElMessage.error('购买失败，请稍后重试')
  }
}

// 获取商品详情
const fetchItemDetail = async () => {
  try {
    console.log('正在获取商品详情，ID:', itemId.value)
    const response = await getSecondHandDetail(itemId.value)
    console.log('后端返回的商品详情:', response.data)
    itemDetail.value = response.data
    // 设置推荐商品为相关商品
    recommendedItems.value = response.data.relatedItems || []
    
    // 检查是否从"我的发布"或"首页"页面导航过来
    // 如果是从这些页面来的，不需要检查收藏状态
    const fromMyPublish = route.query.from === 'myPublish'
    const fromHome = route.query.from === 'home'
    isFromMyPublish.value = fromMyPublish
    
    if (!fromMyPublish && !fromHome) {
      // 检查收藏状态
      await checkFavoriteStatus()
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    ElMessage.error('获取商品详情失败')
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  try {
    const response = await checkSecondHandFavorite(itemId.value)
    isFavorited.value = response.data || false
  } catch (error) {
    console.error('检查收藏状态失败:', error)
    isFavorited.value = false
  }
}

// 清理图片URL（去除多余的反引号和引号）
const cleanImageUrl = (url) => {
  if (!url) return '/default-image.png'
  return url.replace(/`/g, '').replace(/"/g, '').trim()
}

// 获取成色文本
const getConditionText = (condition) => {
  const conditionMap = {
    1: '全新',
    2: '九成新',
    3: '八成新',
    4: '七成新及以下',
    5: '其他'
  }
  return conditionMap[condition] || '未知'
}

onMounted(async () => {
  await fetchItemDetail()
  // 获取收藏列表
  await secondHandStore.getFavorites()
})
</script>

<style scoped lang="scss">
.second-hand-detail {
  min-height: 100vh;
  padding: 80px 0 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.detail-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 30px;
}

.item-header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  margin-bottom: 40px;
  padding-bottom: 40px;
  border-bottom: 1px solid #f0f0f0;
}

.item-images {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.main-image {
  width: 100%;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f9fafc;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.image-list {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.image-item {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f9fafc;
  flex-shrink: 0;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    transform: scale(1.05);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.item-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  line-height: 1.3;
}

.item-price {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 10px;
}

.current-price {
  font-size: 32px;
  font-weight: bold;
  color: #F56C6C;
}

.original-price {
  font-size: 16px;
  color: #999;
  text-decoration: line-through;
}

.item-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.meta-item i {
  margin-right: 6px;
  color: #409EFF;
}

.item-location {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
  padding: 15px;
  background-color: #f9fafc;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
}

.item-location i {
  margin-right: 8px;
  color: #409EFF;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background-color: #f9fafc;
  border-radius: 8px;
}

.seller-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  background-color: #e6f7ff;
}

.seller-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.seller-details {
  flex: 1;
}

.seller-details h4 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.seller-details p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.contact-btn {
  white-space: nowrap;
}

.item-actions {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.buy-btn {
  flex: 1;
}

.favorite-btn {
  flex: 1;
}

.item-content {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  display: flex;
  align-items: center;
}

.section-title i {
  margin-right: 8px;
  color: #409EFF;
}

.content-text {
  font-size: 15px;
  color: #666;
  line-height: 1.8;
}

.detail-info {
  background-color: #f9fafc;
  border-radius: 8px;
  padding: 20px;
}

.info-row {
  display: flex;
  margin-bottom: 16px;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.info-label {
  width: 120px;
  font-weight: 500;
  color: #333;
}

.info-value {
  flex: 1;
  color: #666;
}

.location-info {
  background-color: #f9fafc;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #409EFF;
}

.location-info p {
  margin-bottom: 8px;
  color: #666;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.recommended-items {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.item-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.list-item {
  background-color: #f9fafc;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  gap: 16px;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.list-item .item-image {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #e6f7ff;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-item .item-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.list-item .item-title {
  font-size: 16px;
  margin-bottom: 12px;
}

.list-item .item-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.btn {
  display: inline-block;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  text-decoration: none;
  z-index: 1;
  position: relative;
}

.btn-primary {
  background-color: #409EFF;
  color: #fff;
  
  &:hover {
    background-color: #66B1FF;
  }
}

.loading-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 30px;
}

@media (max-width: 768px) {
  .detail-card,
  .recommended-items {
    padding: 20px;
  }
  
  .item-header {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .main-image {
    height: 300px;
  }
  
  .item-title {
    font-size: 20px;
  }
  
  .item-price {
    font-size: 24px;
  }
  
  .seller-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .item-actions {
    flex-direction: column;
    gap: 10px;
  }
  
  .item-list {
    grid-template-columns: 1fr;
  }
}
</style>
