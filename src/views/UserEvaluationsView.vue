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
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="全部" name="all">
            <!-- 全部评价 -->
            <div class="evaluation-list">
              <div v-if="allEvaluations.length === 0" class="empty-state">
                <el-empty description="暂无评价记录" />
              </div>
              <div v-else class="evaluation-items">
                <div v-for="evaluation in allEvaluations" :key="evaluation.id" class="evaluation-item">
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
                  :total="totalEvaluations"
                />
              </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="收到的评价" name="received">
            <!-- 收到的评价 -->
            <div class="evaluation-list">
              <div v-if="receivedEvaluations.length === 0" class="empty-state">
                <el-empty description="暂无收到的评价" />
              </div>
              <div v-else class="evaluation-items">
                <div v-for="evaluation in receivedEvaluations" :key="evaluation.id" class="evaluation-item">
                <div class="eval-header">
                  <div class="eval-info">
                    <el-rate v-model="evaluation.rating" :max="5" disabled />
                    <span class="eval-time">{{ evaluation.evalTime }}</span>
                  </div>
                  <el-tag type="info">收到的评价</el-tag>
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
                  <el-button size="small" type="primary" @click="replyEvaluation()">
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
                  :total="totalReceivedEvaluations"
                />
              </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="发出的评价" name="given">
            <!-- 发出的评价 -->
            <div class="evaluation-list">
              <div v-if="givenEvaluations.length === 0" class="empty-state">
                <el-empty description="暂无发出的评价" />
              </div>
              <div v-else class="evaluation-items">
                <div v-for="evaluation in givenEvaluations" :key="evaluation.id" class="evaluation-item">
                <div class="eval-header">
                  <div class="eval-info">
                    <el-rate v-model="evaluation.rating" :max="5" disabled />
                    <span class="eval-time">{{ evaluation.evalTime }}</span>
                  </div>
                  <el-tag type="success">发出的评价</el-tag>
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
                  :total="totalGivenEvaluations"
                />
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
import { ElMessage } from 'element-plus'
import { getMyEvaluations } from '@/api/user'

const activeTab = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// 评价数据
const allEvaluations = ref([])
const totalEvaluations = ref(0)
const totalReceivedEvaluations = ref(0)
const totalGivenEvaluations = ref(0)

// 计算属性：收到的评价
const receivedEvaluations = computed(() => {
  return allEvaluations.value.filter(evaluation => evaluation.type === 'received')
})

// 计算属性：发出的评价
const givenEvaluations = computed(() => {
  return allEvaluations.value.filter(evaluation => evaluation.type === 'given')
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

// 标签页切换
const handleTabClick = (tab) => {
  activeTab.value = tab.props.name
  currentPage.value = 1
  fetchEvaluations()
}

// 获取评价列表
const fetchEvaluations = async () => {
  try {
    console.log('开始获取评价列表...')
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    console.log('用户信息:', userInfo)
    const userId = userInfo.userId || userInfo.id
    console.log('用户ID:', userId)
    
    if (!userId) {
      console.error('用户信息不完整，无法获取评价列表')
      ElMessage.error('用户信息不完整，无法获取评价列表')
      return
    }
    
    console.log('请求参数:', {
      id: userId,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      type: activeTab.value === 'all' ? '' : activeTab.value
    })
    
    const response = await getMyEvaluations({
      id: userId,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      type: activeTab.value === 'all' ? '' : activeTab.value
    })
    
    console.log('API响应:', response)
    allEvaluations.value = response.data?.list || []
    totalEvaluations.value = parseInt(response.data?.total) || 0
    totalReceivedEvaluations.value = parseInt(response.data?.receivedTotal) || 0
    totalGivenEvaluations.value = parseInt(response.data?.givenTotal) || 0
    console.log('评价列表数据:', allEvaluations.value)
    console.log('评价列表总数:', totalEvaluations.value)
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