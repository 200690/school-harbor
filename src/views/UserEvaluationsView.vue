<template>
  <div class="user-evaluations">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的评价</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的评价</h2>
      </div>

      <!-- 评价分类 -->
      <div class="evaluation-tabs">
        <el-tabs v-model="activeTab">
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

// 模拟评价数据
const allEvaluations = ref([
  {
    id: 1,
    type: 'received',
    rating: 5,
    content: '商品质量很好，卖家服务态度也很棒，非常满意的一次交易！',
    relatedItem: '大学英语四级词汇书',
    evalTime: '2026-02-15 14:30',
    images: []
  },
  {
    id: 2,
    type: 'given',
    rating: 4,
    content: '兼职工作环境不错，工资发放及时，推荐给大家！',
    relatedItem: '校园超市收银员',
    evalTime: '2026-02-10 10:20',
    images: []
  },
  {
    id: 3,
    type: 'received',
    rating: 5,
    content: '卖家很热心，商品和描述的一样，物流也很快，赞！',
    relatedItem: '小米蓝牙耳机',
    evalTime: '2026-02-05 09:15',
    images: [
      'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bluetooth%20earphones%20evaluation%20photo&image_size=square'
    ]
  }
])

// 计算属性：收到的评价
const receivedEvaluations = computed(() => {
  return allEvaluations.value.filter(evaluation => evaluation.type === 'received')
})

// 计算属性：发出的评价
const givenEvaluations = computed(() => {
  return allEvaluations.value.filter(evaluation => evaluation.type === 'given')
})

const activeTab = ref('all')

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

onMounted(() => {
  // 这里可以从后端获取评价数据
  console.log('获取评价数据')
})
</script>

<style scoped lang="scss">
.user-evaluations {
  min-height: 100vh;
  padding: 20px 0;
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
}
</style>