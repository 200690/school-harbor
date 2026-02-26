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
      </div>

      <!-- 收藏分类 -->
      <div class="favorite-tabs">
        <el-tabs v-model="activeTab">
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
            <!-- 分页 -->
            <div v-if="secondHandFavorites.length > 0" class="pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="secondHandFavorites.length"
              />
            </div>
          </el-tab-pane>
          <el-tab-pane label="兼职" name="part-time">
            <!-- 兼职收藏 -->
            <div class="favorite-list">
              <div v-if="partTimeFavorites.length === 0" class="empty-state">
                <el-empty description="暂无兼职收藏" />
              </div>
              <div v-else class="favorite-grid">
                <div v-for="fav in partTimeFavorites" :key="fav.id" class="favorite-item part-time-item">
                  <div class="favorite-badge part-time-badge" @click.stop="removeFavorite(fav.id)">
                    <i class="el-icon-star-on"></i>
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
            <!-- 分页 -->
            <div v-if="partTimeFavorites.length > 0" class="pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="partTimeFavorites.length"
              />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMySecondHandFavorites, removeSecondHandFavorite } from '@/api/secondHand'
import { getMyPartTimeFavorites, removePartTimeFavorite } from '@/api/partTime'

const router = useRouter()
const activeTab = ref('second-hand')
const currentPage = ref(1)
const pageSize = ref(10)

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
    router.push(`/second-hand/detail/${fav.itemId}?from=favorites`)
  } else {
    router.push(`/item/${fav.itemId}?from=favorites`)
  }
}

// 移除收藏
const removeFavorite = (favId) => {
  ElMessageBox.confirm('确定要移除这个收藏吗？', '移除收藏', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 查找要移除的收藏项
      const favItem = allFavorites.value.find(fav => fav.id === favId)
      if (!favItem) {
        ElMessage.error('收藏项不存在')
        return
      }
      
      // 根据收藏类型调用对应的API
      if (favItem.type === 'second-hand') {
        await removeSecondHandFavorite(favItem.itemId)
      } else if (favItem.type === 'part-time') {
        await removePartTimeFavorite(favItem.itemId)
      }
      
      // 重新获取收藏数据
      await fetchFavorites()
      ElMessage.success('已移除收藏')
    } catch (error) {
      console.error('移除收藏失败:', error)
      ElMessage.error('移除收藏失败，请稍后重试')
    }
  }).catch(() => {
    // 取消操作
  })
}



// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchFavorites()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchFavorites()
}

// 获取收藏数据
const fetchFavorites = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取收藏记录')
      return
    }
    
    // 并行请求二手商品和兼职收藏数据
    let secondHandItems = []
    let partTimeItems = []
    
    try {
      const secondHandResponse = await getMySecondHandFavorites({
        id: userId,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      })
      // 确保secondHandItems是一个数组
      secondHandItems = secondHandResponse.data?.list || []
      console.log('二手商品收藏响应:', secondHandResponse)
      console.log('二手商品收藏数据:', secondHandItems)
    } catch (error) {
      console.error('获取二手商品收藏失败:', error)
      ElMessage.warning('获取二手商品收藏失败，仅显示兼职收藏数据')
      secondHandItems = []
    }
    
    try {
      const partTimeResponse = await getMyPartTimeFavorites({
        id: userId,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      })
      // 确保partTimeItems是一个数组
      partTimeItems = partTimeResponse.data?.list || []
      console.log('兼职收藏响应:', partTimeResponse)
      console.log('兼职收藏数据:', partTimeItems)
    } catch (error) {
      console.error('获取兼职收藏失败:', error)
      ElMessage.warning('获取兼职收藏失败，仅显示二手商品收藏数据')
      partTimeItems = []
    }
    
    console.log('最终数据 - 二手商品收藏:', secondHandItems)
    console.log('最终数据 - 兼职收藏:', partTimeItems)
    
    // 转换数据格式并合并
    allFavorites.value = [
      ...secondHandItems.map((item, index) => ({
        id: item.id || `sh-${index}`,
        type: 'second-hand',
        itemId: item.itemId || item.id,
        title: item.itemTitle || item.title,
        price: item.itemPrice || item.price,
        location: item.itemLocation || item.location,
        image: item.itemCoverImage ? item.itemCoverImage.trim().replace(/`/g, '') : item.image,
        favoriteTime: item.favoriteTime || item.createTime,
        condition: item.condition
      })),
      ...partTimeItems.map((item, index) => ({
        id: item.id || `pt-${index}`,
        type: 'part-time',
        itemId: item.partTimeId || item.itemId || item.id,
        title: item.title,
        salary: item.salaryDesc || item.salary,
        location: item.location,
        image: item.image,
        favoriteTime: item.favoriteTime || item.createTime,
        workTime: item.workTime
      }))
    ]
    
    console.log('转换后的数据:', allFavorites.value)
    console.log('转换后的数据长度:', allFavorites.value.length)
  } catch (error) {
    console.error('获取收藏记录失败:', error)
    ElMessage.error('获取收藏记录失败，请稍后重试')
    allFavorites.value = []
  }
}

onMounted(() => {
  fetchFavorites()
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

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 兼职项目样式调整 */
.part-time-item {
  position: relative;
  padding-top: 40px; /* 为收藏按钮留出空间 */
}

.part-time-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
}

.part-time-item .item-info {
  flex: 1;
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
  
  .pagination {
    margin-top: 20px;
  }
  
  .part-time-item {
    padding-top: 30px;
  }
  
  .part-time-badge {
    top: 5px;
    right: 5px;
  }
}
</style>