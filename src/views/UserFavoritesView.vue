<template>
  <div class="user-favorites">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的收藏</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题和操作 -->
      <div class="page-header">
        <h2 class="page-title">我的收藏</h2>
        <div class="page-actions">
          <el-button type="danger" @click="clearAllFavorites">
            <i class="el-icon-delete"></i> 清空收藏
          </el-button>
        </div>
      </div>

      <!-- 收藏分类 -->
      <div class="favorite-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部" name="all">
            <!-- 全部收藏 -->
            <div class="favorite-list">
              <div v-if="allFavorites.length === 0" class="empty-state">
                <el-empty description="暂无收藏记录" />
              </div>
              <div v-else class="favorite-grid">
                <div v-for="fav in allFavorites" :key="fav.id" class="favorite-item">
                  <div class="item-image">
                    <img :src="fav.image" :alt="fav.title" />
                    <div class="favorite-badge" @click.stop="removeFavorite(fav.id)">
                      <i class="el-icon-star-on"></i>
                    </div>
                  </div>
                  <div class="item-info">
                    <h3 class="item-title">{{ fav.title }}</h3>
                    <div class="item-details">
                      <span v-if="fav.type === 'second-hand'" class="item-price">¥{{ fav.price }}</span>
                      <span v-else class="item-salary">{{ fav.salary }}</span>
                      <span class="item-location"><i class="el-icon-location"></i> {{ fav.location }}</span>
                    </div>
                    <div class="item-meta">
                      <span class="fav-time">{{ fav.favoriteTime }}</span>
                      <el-tag :type="fav.type === 'second-hand' ? 'info' : 'success'" size="small">
                        {{ fav.type === 'second-hand' ? '二手交易' : '兼职' }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(fav)">
                      查看详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="二手交易" name="second-hand">
            <!-- 二手交易收藏 -->
            <div class="favorite-list">
              <div v-if="secondHandFavorites.length === 0" class="empty-state">
                <el-empty description="暂无二手交易收藏" />
              </div>
              <div v-else class="favorite-grid">
                <div v-for="fav in secondHandFavorites" :key="fav.id" class="favorite-item">
                  <div class="item-image">
                    <img :src="fav.image" :alt="fav.title" />
                    <div class="favorite-badge" @click.stop="removeFavorite(fav.id)">
                      <i class="el-icon-star-on"></i>
                    </div>
                  </div>
                  <div class="item-info">
                    <h3 class="item-title">{{ fav.title }}</h3>
                    <div class="item-details">
                      <span class="item-price">¥{{ fav.price }}</span>
                      <span class="item-location"><i class="el-icon-location"></i> {{ fav.location }}</span>
                    </div>
                    <div class="item-meta">
                      <span class="fav-time">{{ fav.favoriteTime }}</span>
                      <el-tag type="info" size="small">二手交易</el-tag>
                    </div>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(fav)">
                      查看详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="兼职" name="part-time">
            <!-- 兼职收藏 -->
            <div class="favorite-list">
              <div v-if="partTimeFavorites.length === 0" class="empty-state">
                <el-empty description="暂无兼职收藏" />
              </div>
              <div v-else class="favorite-grid">
                <div v-for="fav in partTimeFavorites" :key="fav.id" class="favorite-item">
                  <div class="item-image">
                    <img :src="fav.image" :alt="fav.title" />
                    <div class="favorite-badge" @click.stop="removeFavorite(fav.id)">
                      <i class="el-icon-star-on"></i>
                    </div>
                  </div>
                  <div class="item-info">
                    <h3 class="item-title">{{ fav.title }}</h3>
                    <div class="item-details">
                      <span class="item-salary">{{ fav.salary }}</span>
                      <span class="item-location"><i class="el-icon-location"></i> {{ fav.location }}</span>
                    </div>
                    <div class="item-meta">
                      <span class="fav-time">{{ fav.favoriteTime }}</span>
                      <el-tag type="success" size="small">兼职</el-tag>
                    </div>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(fav)">
                      查看详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
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
const activeTab = ref('all')

// 获取所有收藏
const allFavorites = ref([])

// 计算属性：二手交易收藏
const secondHandFavorites = computed(() => {
  return allFavorites.value.filter(fav => fav.type === 'second-hand')
})

// 计算属性：兼职收藏
const partTimeFavorites = computed(() => {
  return allFavorites.value.filter(fav => fav.type === 'part-time')
})

// 查看详情
const viewDetail = (fav) => {
  if (fav.type === 'second-hand') {
    router.push(`/second-hand/detail/${fav.itemId}`)
  } else {
    router.push(`/part-time/detail/${fav.itemId}`)
  }
}

// 移除收藏
const removeFavorite = (favId) => {
  ElConfirm('确定要移除这个收藏吗？', '移除收藏', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    allFavorites.value = allFavorites.value.filter(fav => fav.id !== favId)
    ElMessage.success('已移除收藏')
  }).catch(() => {
    // 取消操作
  })
}

// 清空收藏
const clearAllFavorites = () => {
  ElConfirm('确定要清空所有收藏吗？', '清空收藏', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    allFavorites.value = []
    ElMessage.success('收藏已清空')
  }).catch(() => {
    // 取消操作
  })
}

onMounted(async () => {
  // 从 store 获取用户收藏
  await userStore.getUserFavorites()
  allFavorites.value = userStore.userFavorites
})
</script>

<style scoped lang="scss">
.user-favorites {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.favorite-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.favorite-list {
  margin-top: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.favorite-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.favorite-item {
  background-color: #f9fafc;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.item-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  background-color: #e6f7ff;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.favorite-item:hover .item-image img {
  transform: scale(1.05);
}

.favorite-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  color: #f56c6c;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    background-color: #f56c6c;
    color: white;
  }
}

.item-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-details {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.item-price, .item-salary {
  font-size: 16px;
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
}

.item-location {
  font-size: 12px;
  color: #666;
  flex-shrink: 0;
}

.item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.fav-time {
  font-size: 12px;
  color: #999;
}

.item-actions {
  padding: 15px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .favorite-tabs {
    padding: 15px;
  }
  
  .favorite-grid {
    grid-template-columns: 1fr;
  }
  
  .item-image {
    height: 250px;
  }
  
  .item-details {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .item-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}
</style>