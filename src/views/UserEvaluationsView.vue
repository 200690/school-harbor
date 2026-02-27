<template>
  <div class="user-evaluations">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的评价</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的评价</h2>
      </div>

      <!-- 评价分类 -->
      <div class="evaluation-tabs">
        <!-- 一级菜单：全部、兼职、二手交易 -->
        <el-tabs v-model="activeCategory" @tab-click="handleCategoryClick" class="category-tabs">
          <el-tab-pane label="全部" name="all"></el-tab-pane>
          <el-tab-pane label="兼职" name="partTime"></el-tab-pane>
          <el-tab-pane label="二手交易" name="secondHand"></el-tab-pane>
        </el-tabs>
        
        <!-- 二级菜单：发出的评价、收到的评价 -->
        <el-tabs v-model="activeType" @tab-click="handleTypeClick" class="type-tabs">
          <el-tab-pane label="发出的评价" name="given"></el-tab-pane>
          <el-tab-pane label="收到的评价" name="received"></el-tab-pane>
        </el-tabs>
        
        <!-- 评价列表 -->
        <div class="evaluation-list">
          <div v-if="filteredEvaluations.length === 0" class="empty-state">
            <el-empty description="暂无评价记录" />
          </div>
          <div v-else class="evaluation-items">
            <div v-for="evaluation in filteredEvaluations" :key="evaluation.id" class="evaluation-item">
              <div class="eval-header">
                <div class="eval-info">
                  <el-rate v-model="evaluation.rating" :max="5" disabled />
                  <span class="eval-time">{{ evaluation.evalTime }}</span>
                </div>
                <el-tag :type="evaluation.type === 'received' ? 'info' : 'success'">
                  {{ evaluation.type === 'received' ? '收到的评价' : '发出的评价' }}
                </el-tag>
              </div>
              <div class="eval-content">
                <div class="related-item">
                  <h4 class="item-title">{{ evaluation.relatedItem }}</h4>
                </div>
                <p class="eval-text">{{ evaluation.content }}</p>
                <div v-if="evaluation.images && evaluation.images.length > 0" class="eval-images">
                  <img v-for="(img, index) in evaluation.images" :key="index" :src="img" :alt="'评价图片' + (index + 1)" />
                </div>
              </div>
              <div class="eval-actions">
                <el-button v-if="evaluation.type === 'received'" size="small" type="primary" @click="replyEvaluation()">
                  回复
                </el-button>
                <el-button size="small" type="text" @click="viewEvaluationDetail()">
                  查看详情
                </el-button>
              </div>
            </div>
            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalFilteredEvaluations"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyEvaluations } from '@/api/user'

// 一级菜单：全部、兼职、二手交易
const activeCategory = ref('all')
// 二级菜单：发出的评价、收到的评价
const activeType = ref('given')

const currentPage = ref(1)
const pageSize = ref(10)

// 评价数据
const allEvaluations = ref([])
const totalFilteredEvaluations = ref(0)

// 计算属性：过滤后的评价
const filteredEvaluations = computed(() => {
  return allEvaluations.value
})

// 回复评价
const replyEvaluation = () => {
  ElMessage.info('回复评价功能')
  // 这里应该实现回复评价的功能
}

// 查看评价详情
const viewEvaluationDetail = () => {
  ElMessage.info('查看评价详情')
  // 这里应该导航到评价详情页面
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchEvaluations()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchEvaluations()
}

// 一级菜单切换
const handleCategoryClick = () => {
  currentPage.value = 1
  fetchEvaluations()
}

// 二级菜单切换
const handleTypeClick = () => {
  currentPage.value = 1
  fetchEvaluations()
}

// 获取评价列表
const fetchEvaluations = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取评价列表')
      return
    }
    
    const response = await getMyEvaluations({
      id: userId,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      type: activeType.value,
      category: activeCategory.value === 'all' ? '' : activeCategory.value
    })
    
    allEvaluations.value = response.data?.list || []
    totalFilteredEvaluations.value = parseInt(response.data?.total) || 0
  } catch (error) {
    console.error('获取评价列表失败:', error)
    ElMessage.error('获取评价列表失败，请稍后重试')
  }
}

onMounted(async () => {
  await fetchEvaluations()
})
</script>

<style scoped lang="scss">
.user-evaluations {
  min-height: 100vh;
  padding: 80px 0 20px;
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

.evaluation-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.category-tabs {
  margin-bottom: 10px;
  border-bottom: 2px solid #409EFF;
  font-weight: bold;
}

.type-tabs {
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  margin-left: 20px;
}

/* 调整标签页样式 */
:deep(.el-tabs__nav-wrap::after) {
  display: none;
}

:deep(.category-tabs .el-tabs__active-bar) {
  background-color: #409EFF;
}

:deep(.category-tabs .el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
  padding: 0 20px;
}

:deep(.type-tabs .el-tabs__item) {
  font-size: 14px;
  padding: 0 15px;
  color: #606266;
}

:deep(.type-tabs .el-tabs__item.is-active) {
  color: #409EFF;
}

.evaluation-list {
  margin-top: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.evaluation-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.evaluation-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.eval-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.eval-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.eval-time {
  font-size: 12px;
  color: #999;
}

.eval-content {
  margin-bottom: 20px;
}

.related-item {
  margin-bottom: 12px;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 12px 0;
}

.eval-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0 0 15px 0;
}

.eval-images {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.eval-images img {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  object-fit: cover;
}

.eval-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .evaluation-tabs {
    padding: 15px;
  }
  
  .evaluation-item {
    padding: 15px;
  }
  
  .eval-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .eval-actions {
    flex-wrap: wrap;
  }
  
  .eval-images img {
    width: 60px;
    height: 60px;
  }
  
  .pagination {
    margin-top: 20px;
  }
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>