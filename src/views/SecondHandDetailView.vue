<template>
  <div class="second-hand-detail" :class="{ 'banned-mode': isPublisherBanned }">
    <!-- 服务提供者被封禁提示 - 全屏显示 -->
    <div v-if="isPublisherBanned" class="banned-notice">
      <div class="banned-content">
        <div class="error-code">403</div>
        <div class="error-icon">
          <i class="el-icon-warning-outline"></i>
        </div>
        <h2 class="error-title">服务提供者已封禁</h2>
        <p class="error-description">
          抱歉，该服务提供者已被封禁。<br>
          无法查看相关信息，请返回首页继续浏览。
        </p>
        <div class="error-actions">
          <button @click="goHome" class="btn btn-primary">
            <i class="el-icon-house"></i> 返回首页
          </button>
          <button @click="goBack" class="btn btn-secondary">
            <i class="el-icon-back"></i> 返回上一页
          </button>
        </div>
        <div class="quick-links">
          <p>您可能在找：</p>
          <div class="links">
            <router-link to="/part-time" class="link-item">
              <i class="el-icon-s-finance"></i> 校园兼职
            </router-link>
            <router-link to="/second-hand" class="link-item">
              <i class="el-icon-s-goods"></i> 二手交易
            </router-link>
            <router-link to="/user/user/center" class="link-item">
              <i class="el-icon-user"></i> 个人中心
            </router-link>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 正常内容 -->
    <div v-else class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <!-- 根据导航来源显示不同的面包屑 -->
        <template v-if="isFromMyPublish">
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/publish">我的发布</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>商品详情</el-breadcrumb-item>
        </template>
        <template v-else-if="isFromFavorites">
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/favorites">我的收藏</router-link></el-breadcrumb-item>
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
                <img :src="getCurrentImage()" :alt="itemDetail.title" />
              </div>
              <div class="image-list" v-if="parseImages(itemDetail.images).length > 0">
                <div 
                  class="image-item" 
                  v-for="(img, index) in parseImages(itemDetail.images)" 
                  :key="index"
                  :class="{ 'active': index === currentImageIndex }"
                  @click="selectImage(index)"
                >
                  <img :src="img" :alt="itemDetail.title" />
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
              <router-link :to="`/user/profile/${itemDetail.sellerId}?from=second-hand-detail&itemId=${itemId}`" class="seller-profile-link">
                <div class="seller-avatar">
                  <img :src="itemDetail.sellerAvatar || '/default-avatar.png'" :alt="itemDetail.sellerName || '卖家'" />
                </div>
                <div class="seller-details">
                  <h4>{{ itemDetail.sellerName || '卖家' }}</h4>
                </div>
              </router-link>
              <el-button v-if="isSeller" type="primary" class="contact-btn" @click="editItem">
                <i class="el-icon-edit"></i> 编辑
              </el-button>
              <el-button v-if="!isSeller" type="primary" class="contact-btn" @click="contactSeller">
                <i class="el-icon-chat-line-round"></i> 联系卖家
              </el-button>
              <el-button v-if="!isSeller" type="info" class="comment-btn" @click="viewComments">
                <i class="el-icon-chat-dot-round"></i> 查看评论
              </el-button>
              <el-button v-if="!isSeller" type="danger" class="block-btn" @click="blockItem">
                <i class="el-icon-circle-close"></i> 拉黑商品
              </el-button>
            </div>
            <div class="item-actions">
              <el-button v-if="!isSeller" type="primary" size="large" class="buy-btn" @click="buyItem">
                <i class="el-icon-s-finance"></i> 立即购买
              </el-button>
              <el-button v-if="!isSeller" :type="isFavorite ? 'warning' : 'default'" size="large" class="favorite-btn" @click="toggleFavorite">
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
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useSecondHandStore } from '../stores/secondHand'
import { useUserStore } from '../stores/user'
import { getSecondHandDetail, checkSecondHandFavorite } from '@/api/secondHand'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const secondHandStore = useSecondHandStore()
const userStore = useUserStore()

const itemId = ref(route.params.id || 1)
const itemDetail = ref(null)
const recommendedItems = ref([])
const isFavorited = ref(false)
const isFromMyPublish = ref(false)
const isFromFavorites = ref(false)
const isPublisherBanned = ref(false)
const bannedMessage = ref('')
const currentImageIndex = ref(0)

// 计算当前用户是否是商品卖家
const isSeller = computed(() => {
  if (!itemDetail.value || !itemDetail.value.sellerId) {
    return false
  }
  
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const currentUserId = userInfo.userId || userInfo.id
  const itemSellerId = itemDetail.value.sellerId
  
  // 确保都是字符串类型进行比较
  return String(currentUserId) === String(itemSellerId)
})

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

// 联系卖家
const contactSeller = () => {
  if (!itemDetail.value || !itemDetail.value.id || !itemDetail.value.sellerId) {
    ElMessage.error('无法获取商品或卖家信息')
    return
  }
  
  // 直接跳转到聊天界面
  router.push({
    path: '/chat',
    query: {
      otherUserId: itemDetail.value.sellerId,
      otherUserNickname: itemDetail.value.sellerName || '卖家',
      otherUserAvatar: itemDetail.value.sellerAvatar || '/default-avatar.png',
      itemId: itemDetail.value.id,
      itemTitle: itemDetail.value.title,
      itemPrice: itemDetail.value.price
    }
  })
}

// 编辑商品
const editItem = () => {
  if (!itemDetail.value || !itemDetail.value.id) {
    ElMessage.error('无法获取商品信息')
    return
  }
  
  // 跳转到编辑页面，带上来源参数
  router.push(`/second-hand/edit/${itemDetail.value.id}?from=detail`)
}

// 拉黑商品
const blockItem = async () => {
  if (!itemDetail.value || !itemDetail.value.id) {
    ElMessage.error('无法获取商品信息')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要拉黑商品 "${itemDetail.value.title}" 吗？拉黑后将无法看到该商品。`,
      '拉黑确认',
      {
        confirmButtonText: '确定拉黑',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用拉黑API - 使用路径参数
    const response = await request({
      url: `/user/blacklist/item/add/${itemDetail.value.id}`,
      method: 'post'
    })
    
    // 检查响应是否成功
    if (response.code === 1) {
      ElMessage.success('拉黑成功')
      setTimeout(() => {
        router.push('/second-hand')
      }, 1500)
    } else {
      ElMessage.error(response.msg || '拉黑失败，请稍后重试')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('拉黑失败:', error)
      ElMessage.error('拉黑失败，请稍后重试')
    }
  }
}

// 获取商品详情
const fetchItemDetail = async () => {
  try {
    console.log('正在获取商品详情，ID:', itemId.value)
    try {
      const response = await getSecondHandDetail(itemId.value)
    itemDetail.value = response.data
    // 重置图片索引
    currentImageIndex.value = 0
      // 设置推荐商品为相关商品
      recommendedItems.value = response.data.relatedItems || []
      isPublisherBanned.value = false
      
      // 检查是否从"我的发布"、"首页"、"收藏"或"列表"页面导航过来
      // 如果是从这些页面来的，不需要检查收藏状态
      const fromMyPublish = route.query.from === 'myPublish'
      const fromHome = route.query.from === 'home'
      const fromFavorites = route.query.from === 'favorites'
      const fromList = route.query.from === 'list'
      isFromMyPublish.value = fromMyPublish
      isFromFavorites.value = fromFavorites
      
      if (!fromMyPublish && !fromHome && !fromFavorites && !fromList) {
        // 检查收藏状态
        await checkFavoriteStatus()
      }
    } catch (error) {
      console.error('获取商品详情失败:', error)
      console.log('错误详情:', {
        message: error.message,
        response: error.response,
        status: error.response?.status
      })
      
      // 检查是否是服务提供者被封禁的错误（HTTP 403 或 消息包含"用户状态异常"）
      const isBanned = error.response?.status === 403 || 
                      (error.message && error.message.includes('用户状态异常'))
      
      if (isBanned) {
        console.log('服务提供者被封禁')
        isPublisherBanned.value = true
        console.log('isPublisherBanned 设置为:', isPublisherBanned.value)
        bannedMessage.value = '用户状态异常'
        // 清空itemDetail数据，确保不显示任何内容
        itemDetail.value = null
        console.log('itemDetail 已清空')
        // 触发事件通知App.vue隐藏导航栏和页脚
        window.dispatchEvent(new CustomEvent('publisher-banned'))
        // 使用 nextTick 确保 DOM 更新
        nextTick(() => {
          console.log('DOM 已更新，isPublisherBanned:', isPublisherBanned.value)
        })
      } else {
        ElMessage.error('获取商品详情失败')
      }
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    console.log('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status
    })
    
    // 检查是否是服务提供者被封禁的错误（HTTP 403 或 消息包含"用户状态异常"）
    const isBanned = error.response?.status === 403 || 
                    (error.message && error.message.includes('用户状态异常'))
    
    if (isBanned) {
      console.log('服务提供者被封禁')
      isPublisherBanned.value = true
      console.log('isPublisherBanned 设置为:', isPublisherBanned.value)
      bannedMessage.value = '用户状态异常'
      // 清空itemDetail数据，确保不显示任何内容
      itemDetail.value = null
      console.log('itemDetail 已清空')
      // 触发事件通知App.vue隐藏导航栏和页脚
      window.dispatchEvent(new CustomEvent('publisher-banned'))
      // 使用 nextTick 确保 DOM 更新
      nextTick(() => {
        console.log('DOM 已更新，isPublisherBanned:', isPublisherBanned.value)
      })
    } else {
      ElMessage.error('获取商品详情失败')
    }
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

// 清理图片URL（去除多余的反引号和引号，并解析嵌套的JSON字符串）
const cleanImageUrl = (url) => {
  if (!url) return '/default-image.png'
  
  let cleanedUrl = url
  
  // 如果是字符串形式的JSON数组，先解析
  if (typeof cleanedUrl === 'string' && cleanedUrl.startsWith('[')) {
    try {
      const parsed = JSON.parse(cleanedUrl)
      if (Array.isArray(parsed) && parsed.length > 0) {
        cleanedUrl = parsed[0]
      }
    } catch (e) {
      console.log('解析图片URL失败，使用原始值:', e)
    }
  }
  
  // 去除多余的反引号和引号
  cleanedUrl = cleanedUrl.toString().replace(/`/g, '').replace(/"/g, '').trim()
  
  return cleanedUrl || '/default-image.png'
}

// 解析图片列表
const parseImages = (images) => {
  if (!images || !Array.isArray(images)) return []
  
  return images.map(img => {
    if (typeof img === 'string' && img.startsWith('[')) {
      try {
        const parsed = JSON.parse(img)
        if (Array.isArray(parsed) && parsed.length > 0) {
          return cleanImageUrl(parsed[0])
        }
      } catch (e) {
        console.log('解析图片URL失败，使用原始值:', e)
      }
    }
    return cleanImageUrl(img)
  }).filter(url => url && url !== '/default-image.png')
}

// 获取当前显示的大图片
const getCurrentImage = () => {
  const images = parseImages(itemDetail.value?.images)
  if (images.length === 0) {
    return cleanImageUrl(itemDetail.value?.coverImage)
  }
  return images[currentImageIndex.value] || images[0]
}

// 切换大图片
const selectImage = (index) => {
  currentImageIndex.value = index
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

// 返回上一页
const goBack = () => {
  router.back()
}

// 返回首页
const goHome = () => {
  router.push('/')
}

// 处理用户被封禁事件
const handleUserBanned = () => {
  console.log('收到用户被封禁事件')
  isPublisherBanned.value = true
  bannedMessage.value = '用户状态异常'
  // 清空itemDetail数据，确保不显示任何内容
  itemDetail.value = null
  // 触发事件通知App.vue隐藏导航栏和页脚
  window.dispatchEvent(new CustomEvent('publisher-banned'))
}

onMounted(async () => {
  await fetchItemDetail()
  // 获取收藏列表
  await secondHandStore.getFavorites()
  // 监听用户被封禁事件
  window.addEventListener('user-banned', handleUserBanned)
  // 监听清除缓存事件
  window.addEventListener('clear-cache', handleClearCache)
})

// 组件卸载时移除事件监听
onBeforeUnmount(() => {
  window.removeEventListener('user-banned', handleUserBanned)
  window.removeEventListener('clear-cache', handleClearCache)
})

// 查看评论
const viewComments = () => {
  if (!itemDetail.value || !itemDetail.value.id) {
    ElMessage.error('无法获取商品信息')
    return
  }
  
  // 跳转到商品评论页面
  router.push(`/second-hand/comments/${itemDetail.value.id}`)
}

// 处理清除缓存事件
const handleClearCache = () => {
  console.log('收到清除缓存事件')
  isPublisherBanned.value = false
  itemDetail.value = null
  secondHandStore.clearCache()
}
</script>

<style scoped lang="scss">
.second-hand-detail {
  min-height: 100vh;
  padding: 80px 0 20px;
}

.second-hand-detail.banned-mode {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
}

.banned-notice {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.banned-content {
  text-align: center;
  color: #fff;
  max-width: 600px;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 20px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.error-icon {
  font-size: 60px;
  margin-bottom: 20px;
  opacity: 0.9;
}

.error-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.error-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 32px;
  opacity: 0.9;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border-radius: 20px;
  font-size: 16px;
  text-decoration: none;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
  font-weight: 500;
}

.btn-primary {
  background-color: #fff;
  color: #667eea;
  
  &:hover {
    background-color: #f0f0f0;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }
}

.btn-secondary {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.3);
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }
}

.quick-links {
  margin-top: 40px;
  padding-top: 40px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  
  p {
    font-size: 14px;
    margin-bottom: 16px;
    opacity: 0.9;
  }
}

.links {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.link-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
  }
}

@media (max-width: 768px) {
  .error-icon {
    font-size: 40px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-description {
    font-size: 14px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .links {
    flex-direction: column;
    align-items: center;
  }
  
  .link-item {
    width: 100%;
    justify-content: center;
  }
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
  border: 2px solid transparent;
  
  &:hover {
    transform: scale(1.05);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  &.active {
    border-color: #409EFF;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
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

.seller-profile-link {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  text-decoration: none;
  color: inherit;
  transition: all 0.3s;
  cursor: pointer;
  
  &:hover {
    color: #409EFF;
    transform: translateY(-2px);
  }
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

.block-btn {
  white-space: nowrap;
  margin-left: 10px;
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
