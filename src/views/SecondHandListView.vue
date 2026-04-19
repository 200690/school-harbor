<template>
  <div class="second-hand-list">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <h2 class="page-title">二手交易</h2>
      
      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <div class="filter-row">
          <div class="search-box">
            <input 
              type="text" 
              placeholder="搜索二手物品" 
              class="search-input"
              v-model="searchKeyword"
            />
            <button class="search-btn" @click="handleSearch">
              <i class="el-icon-search"></i> 搜索
            </button>
          </div>
          
          <div class="filter-options">
            <el-select v-model="filterCategory" placeholder="物品分类" class="filter-select">
              <el-option label="全部" value=""></el-option>
              <el-option label="教材教辅" value="1"></el-option>
              <el-option label="电子产品" value="2"></el-option>
              <el-option label="生活用品" value="3"></el-option>
              <el-option label="运动器材" value="4"></el-option>
              <el-option label="其他" value="5"></el-option>
            </el-select>
            
            <el-select v-model="filterPrice" placeholder="价格范围" class="filter-select">
              <el-option label="全部" value=""></el-option>
              <el-option label="50元以下" value="<50"></el-option>
              <el-option label="50-200元" value="50-200"></el-option>
              <el-option label="200-500元" value="200-500"></el-option>
              <el-option label="500元以上" value=">500"></el-option>
            </el-select>
            
            <el-select v-model="filterCondition" placeholder="物品成色" class="filter-select">
              <el-option label="全部" value=""></el-option>
              <el-option label="全新" value="1"></el-option>
              <el-option label="九成新" value="2"></el-option>
              <el-option label="八成新" value="3"></el-option>
              <el-option label="七成新及以下" value="4"></el-option>
            </el-select>
            
            <el-select v-model="sortField" placeholder="排序字段" class="filter-select">
              <el-option label="发布时间" value="publishTime"></el-option>
              <el-option label="价格" value="price"></el-option>
              <el-option label="浏览量" value="viewCount"></el-option>
              <el-option label="收藏量" value="favoriteCount"></el-option>
            </el-select>
            
            <button class="sort-toggle-btn" @click="toggleSort">
              <i :class="sortOrder === 'desc' ? 'el-icon-sort-down' : 'el-icon-sort-up'"></i>
              {{ sortOrder === 'desc' ? '降序' : '升序' }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- 二手物品列表 -->
      <div class="item-list">
        <div class="list-item" v-for="item in items" :key="item.id">
          <div class="item-image">
            <img :src="cleanImageUrl(item.coverImage)" :alt="item.title" />
          </div>
          <div class="item-info">
            <h3 class="item-title">{{ item.title }}</h3>
            <p class="item-description">{{ item.description || '暂无描述' }}</p>
            <div class="item-meta">
              <span class="price">¥{{ item.price }}</span>
              <span class="location"><i class="el-icon-s-position"></i> {{ item.school }} - {{ item.location }}</span>
              <span class="publish-time">{{ item.publishTime }}</span>
            </div>
            <div class="item-tags">
              <span class="tag tag-primary">{{ getConditionText(item.condition) }}</span>
            </div>
          </div>
          <div class="item-actions">
            <router-link :to="`/second-hand/detail/${item.id}?from=list`" class="btn btn-primary">查看详情</router-link>
            <button v-if="isItemSeller(item)" class="btn btn-primary" @click="editItem(item.id)">编辑</button>
            <button v-else class="btn btn-success" @click="contactSeller(item)">联系卖家</button>
          </div>
        </div>
      </div>
      
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <div class="pagination-container">
          <div class="pagination">
            <el-pagination
              v-model:current-page="pageNum"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="Number(totalItems) || 0"
              :page-count="Number(totalPages) || 1"
              layout="total, sizes, prev, pager, next, jumper"
              :hide-on-single-page="false"
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
      
      <!-- 发布二手物品按钮 -->
      <div class="publish-btn-container">
        <router-link to="/second-hand/edit/new" class="btn btn-primary publish-btn">
          <i class="el-icon-plus"></i> 发布二手物品
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { getSecondHandList } from '@/api/secondHand'

export default {
  name: 'SecondHandListView',
  components: {
  },
  data() {
    return {
      searchKeyword: '',
      filterCategory: '',
      filterPrice: '',
      filterCondition: '',
      sortField: 'publishTime',
      sortOrder: 'desc',
      pageNum: 1,
      pageSize: 10,
      totalItems: 0,
      totalPages: 0,
      items: [],
      loading: false,
      isRequesting: false // 添加请求锁，防止重复请求
    }
  },
  mounted() {
    // 手动刷新页面时，清除缓存并重新获取数据
    this.clearCache()
    
    // 从路由查询参数中获取 category 参数
    if (this.$route.query.category) {
      const categoryParam = this.$route.query.category
      // 映射 category 参数到对应的 filterCategory 值
      const categoryMap = {
        'textbook': '1', // 教材教辅
        'electronics': '2', // 电子产品
        'life': '3' // 生活用品
      }
      if (categoryMap[categoryParam]) {
        this.filterCategory = categoryMap[categoryParam]
      }
    }
    
    this.fetchSecondHandList()
  },
  methods: {
    // 清除与二手交易列表相关的所有缓存
    clearCache() {
      // 遍历localStorage中的所有键，删除所有以secondHandList_开头的键
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)
        if (key && key.startsWith('secondHandList_')) {
          localStorage.removeItem(key)
          // 同时删除对应的时间戳键
          const timeKey = `${key}_time`
          localStorage.removeItem(timeKey)
          i-- // 因为删除了一个键，所以索引需要减1
        }
      }
    },
    async fetchSecondHandList() {
      // 如果已经有请求在执行，直接返回
      if (this.isRequesting) {
        return
      }
      
      this.loading = true
      this.isRequesting = true // 设置请求锁
      try {
        const params = {
          page: this.pageNum,
          size: this.pageSize,
          keyword: this.searchKeyword,
          categoryId: this.filterCategory ? parseInt(this.filterCategory) : null,
          condition: this.filterCondition ? parseInt(this.filterCondition) : null,
          status: 1,
          sortField: this.sortField,
          sortOrder: this.sortOrder
        }
        
        if (this.filterPrice) {
          const priceRange = this.filterPrice.split('-')
          if (priceRange.length === 2) {
            params.minPrice = parseInt(priceRange[0])
            params.maxPrice = parseInt(priceRange[1])
          } else if (this.filterPrice.startsWith('<')) {
            params.maxPrice = parseInt(this.filterPrice.substring(1))
          } else if (this.filterPrice.startsWith('>')) {
            params.minPrice = parseInt(this.filterPrice.substring(1))
          }
        }
        
        const response = await getSecondHandList(params)
        
        const { list, total, pages } = response.data
        this.items = list || []
        this.totalItems = total || 0
        this.totalPages = pages || 0
      } catch (error) {
        console.error('获取二手物品列表失败:', error)
        this.$message.error('获取二手物品列表失败')
      } finally {
        this.loading = false
        this.isRequesting = false // 释放请求锁
      }
    },
    handleSearch() {
      this.pageNum = 1
      this.fetchSecondHandList()
    },
    toggleSort() {
      this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc'
      this.fetchSecondHandList()
    },
    contactSeller(item) {
      if (!item || !item.id || !item.sellerId) {
        this.$message.error('无法获取商品或卖家信息')
        return
      }
      
      // 直接跳转到聊天界面
      this.$router.push({
        path: '/chat',
        query: {
          otherUserId: item.sellerId,
          otherUserNickname: item.sellerName || '卖家',
          otherUserAvatar: item.sellerAvatar || '/default-avatar.png',
          itemId: item.id,
          itemTitle: item.title,
          itemPrice: item.price
        }
      })
    },
    // 判断当前用户是否是商品的卖家
    isItemSeller(item) {
      if (!item || !item.sellerId) return false
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const currentUserId = userInfo.userId || userInfo.id
      return String(currentUserId) === String(item.sellerId)
    },
    // 编辑商品
    editItem(itemId) {
      this.$router.push(`/second-hand/edit/${itemId}?from=list`)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.fetchSecondHandList()
    },
    handleCurrentChange(current) {
      this.pageNum = current
      this.fetchSecondHandList()
    },
    // 清理图片URL（去除多余的反引号和引号）
    cleanImageUrl(url) {
      if (!url) return '/default-image.png'
      return url.toString().replace(/`/g, '').replace(/"/g, '').trim() || '/default-image.png'
    },
    // 获取成色文本
    getConditionText(condition) {
      const conditionMap = {
        1: '全新',
        2: '九成新',
        3: '八成新',
        4: '七成新及以下',
        5: '其他'
      }
      return conditionMap[condition] || '未知'
    }
  }
}
</script>

<style scoped lang="scss">
.second-hand-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 80px;
}

.main-content {
  flex: 1;
  padding: 20px 0;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

/* 搜索和筛选样式 */
.search-filter {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.filter-row {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 300px;
}

.search-input {
  width: 100%;
  padding: 12px 120px 12px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;

  &:focus {
    outline: none;
    border-color: #409EFF;
  }
}

.search-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  padding: 8px 16px;
  background-color: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;

  &:hover {
    background-color: #66B1FF;
  }
}

.filter-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-select {
  width: 160px;
}

.sort-toggle-btn {
  padding: 8px 20px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;

  &:hover {
    color: #409EFF;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }

  &:active {
    transform: scale(0.98);
  }
}

/* 二手物品列表样式 */
.item-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.list-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
}

.list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.item-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 12px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.list-item:hover .item-image img {
  transform: scale(1.05);
}

.item-info {
  flex: 1;
  margin-bottom: 16px;
}

.item-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.item-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: var(--danger-color);
}

.location {
  font-size: 14px;
  color: #666;
}

.publish-time {
  font-size: 12px;
  color: #999;
}

.item-tags {
  margin-top: 8px;
}

.item-actions {
  display: flex;
  gap: 10px;
}

/* 分页样式 */
.pagination-wrapper {
  width: 100%;
  display: block;
  margin: 10px 0 20px;
  padding: 10px 0;
}

.pagination-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.pagination {
  display: flex;
  justify-content: center;
}

/* 发布按钮样式 */
.publish-btn-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.publish-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 24px;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    min-width: 100%;
  }
  
  .item-list {
    grid-template-columns: 1fr;
  }
  
  .filter-select {
    width: 100%;
  }
  
  .filter-options {
    flex-direction: column;
  }
  
  .item-actions {
    flex-direction: column;
  }
}
</style>