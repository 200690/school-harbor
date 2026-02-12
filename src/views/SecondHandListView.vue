<template>
  <div class="second-hand-list">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <h2 class="page-title">二手交易</h2>
      
      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <div class="search-box">
          <input 
            type="text" 
            placeholder="搜索二手物品" 
            class="search-input"
            v-model="searchKeyword"
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">
            <i class="el-icon-search"></i>
          </button>
        </div>
        
        <div class="filter-options">
          <el-select v-model="filterCategory" placeholder="物品分类" class="filter-select">
            <el-option label="全部" value=""></el-option>
            <el-option label="教材教辅" value="textbook"></el-option>
            <el-option label="电子产品" value="electronics"></el-option>
            <el-option label="生活用品" value="life"></el-option>
            <el-option label="运动器材" value="sports"></el-option>
            <el-option label="其他" value="other"></el-option>
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
            <el-option label="全新" value="new"></el-option>
            <el-option label="九成新" value="90%"></el-option>
            <el-option label="八成新" value="80%"></el-option>
            <el-option label="七成新及以下" value="<70%"></el-option>
          </el-select>
        </div>
      </div>
      
      <!-- 二手物品列表 -->
      <div class="item-list">
        <div class="list-item" v-for="item in items" :key="item.id">
          <div class="item-image">
            <img :src="item.image" :alt="item.title" />
          </div>
          <div class="item-info">
            <h3 class="item-title">{{ item.title }}</h3>
            <p class="item-description">{{ item.description }}</p>
            <div class="item-meta">
              <span class="price">¥{{ item.price }}</span>
              <span class="location"><i class="el-icon-s-position"></i> {{ item.location }}</span>
              <span class="publish-time">{{ item.publishTime }}</span>
            </div>
            <div class="item-seller">
              <router-link :to="`/user/profile/${item.sellerId || 1}`" class="seller-link">
                <i class="el-icon-user"></i> {{ item.sellerName || '卖家' }}
              </router-link>
            </div>
            <div class="item-tags">
              <span class="tag tag-primary">{{ item.category }}</span>
              <span class="tag tag-success">{{ item.condition }}</span>
            </div>
          </div>
          <div class="item-actions">
            <router-link :to="`/second-hand/detail/${item.id}`" class="btn btn-primary">查看详情</router-link>
            <button class="btn btn-success" @click="contactSeller(item.id)">联系卖家</button>
          </div>
        </div>
      </div>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalItems"
        >
        </el-pagination>
      </div>
      
      <!-- 发布二手物品按钮 -->
      <div class="publish-btn-container">
        <router-link to="/second-hand/publish" class="btn btn-primary publish-btn">
          <i class="el-icon-plus"></i> 发布二手物品
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { getSecondHandList, buySecondHandItem } from '@/api/secondHand'

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
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      items: [],
      loading: false
    }
  },
  mounted() {
    // 组件创建时获取二手物品列表
    this.fetchSecondHandList();
  },
  methods: {
    async fetchSecondHandList() {
      this.loading = true;
      try {
        // 构建查询参数
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          keyword: this.searchKeyword,
          category: this.filterCategory,
          condition: this.filterCondition
        };
        
        // 处理价格范围筛选
        if (this.filterPrice) {
          const priceRange = this.filterPrice.split('-');
          if (priceRange.length === 2) {
            params.priceMin = parseInt(priceRange[0]);
            params.priceMax = parseInt(priceRange[1]);
          } else if (this.filterPrice.startsWith('<')) {
            params.priceMax = parseInt(this.filterPrice.substring(1));
          } else if (this.filterPrice.startsWith('>')) {
            params.priceMin = parseInt(this.filterPrice.substring(1));
          }
        }
        
        const response = await getSecondHandList(params);
        const { records, total } = response.data;
        this.items = records;
        this.totalItems = total;
      } catch (error) {
        console.error('获取二手物品列表失败:', error);
        this.$message.error('获取二手物品列表失败');
      } finally {
        this.loading = false;
      }
    },
    handleSearch() {
      // 重置页码并搜索
      this.currentPage = 1;
      this.fetchSecondHandList();
    },
    async contactSeller(itemId) {
      try {
        const response = await buySecondHandItem(itemId);
        if (response.data.success) {
          this.$message.success('购买成功，请联系卖家完成交易');
          // 刷新列表
          this.fetchSecondHandList();
        } else {
          this.$message.error(response.data.message || '购买失败');
        }
      } catch (error) {
        console.error('购买失败:', error);
        this.$message.error('购买失败');
      }
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.fetchSecondHandList();
    },
    handleCurrentChange(current) {
      this.currentPage = current;
      this.fetchSecondHandList();
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

.search-box {
  margin-bottom: 20px;
}

.filter-options {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-select {
  width: 200px;
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

.item-seller {
  margin: 8px 0;
}

.seller-link {
  font-size: 14px;
  color: #409EFF;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  
  &:hover {
    text-decoration: underline;
  }
}

.item-tags {
  margin-top: 8px;
}

.item-actions {
  display: flex;
  gap: 10px;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
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
  .item-list {
    grid-template-columns: 1fr;
  }
  
  .filter-select {
    width: 100%;
    margin-bottom: 10px;
  }
  
  .filter-options {
    flex-direction: column;
  }
  
  .item-actions {
    flex-direction: column;
  }
}
</style>