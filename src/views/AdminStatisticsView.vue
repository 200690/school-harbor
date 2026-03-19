<template>
  <div class="admin-statistics-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>数据统计</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">数据统计</h2>
        <p class="page-subtitle">查看平台数据统计和分析</p>
      </div>

      <!-- 数据概览 -->
      <div class="statistics-overview">
        <div class="stat-card">
          <div class="stat-icon user-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ userCount }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon part-time-icon">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ partTimeCount }}</div>
            <div class="stat-label">兼职总数</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon second-hand-icon">
            <i class="el-icon-goods"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ secondHandCount }}</div>
            <div class="stat-label">二手商品总数</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon order-icon">
            <i class="el-icon-s-shop"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ orderCount }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon report-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ reportCount }}</div>
            <div class="stat-label">举报总数</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon comment-icon">
            <i class="el-icon-chat-line-round"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ commentCount }}</div>
            <div class="stat-label">评论总数</div>
          </div>
        </div>
      </div>

      <!-- 数据趋势 -->
      <div class="statistics-trend">
        <div class="trend-section">
          <h3 class="section-title">用户增长趋势</h3>
          <div class="trend-chart">
            <!-- 这里可以使用 ECharts 等图表库 -->
            <div class="chart-placeholder">
              <i class="el-icon-data-analysis"></i>
              <p>用户增长趋势图表</p>
            </div>
          </div>
        </div>

        <div class="trend-section">
          <h3 class="section-title">发布内容趋势</h3>
          <div class="trend-chart">
            <!-- 这里可以使用 ECharts 等图表库 -->
            <div class="chart-placeholder">
              <i class="el-icon-data-analysis"></i>
              <p>发布内容趋势图表</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 热门内容 -->
      <div class="statistics-popular">
        <div class="popular-section">
          <h3 class="section-title">热门兼职</h3>
          <div class="popular-list">
            <el-table :data="popularPartTime" style="width: 100%">
              <el-table-column prop="title" label="标题" min-width="200" />
              <el-table-column prop="viewCount" label="浏览量" width="100" />
              <el-table-column prop="applyCount" label="申请人数" width="100" />
              <el-table-column prop="createdAt" label="发布时间" width="200" />
            </el-table>
          </div>
        </div>

        <div class="popular-section">
          <h3 class="section-title">热门二手商品</h3>
          <div class="popular-list">
            <el-table :data="popularSecondHand" style="width: 100%">
              <el-table-column prop="title" label="标题" min-width="200" />
              <el-table-column prop="viewCount" label="浏览量" width="100" />
              <el-table-column prop="price" label="价格" width="100" />
              <el-table-column prop="createdAt" label="发布时间" width="200" />
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// 数据统计
const userCount = ref(0)
const partTimeCount = ref(0)
const secondHandCount = ref(0)
const orderCount = ref(0)
const reportCount = ref(0)
const commentCount = ref(0)

// 热门内容
const popularPartTime = ref([])
const popularSecondHand = ref([])

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const response = await request.get('/admin/statistics')

    if (response.code === 1) {
      const data = response.data || {}
      userCount.value = data.userCount || 0
      partTimeCount.value = data.partTimeCount || 0
      secondHandCount.value = data.secondHandCount || 0
      orderCount.value = data.orderCount || 0
      reportCount.value = data.reportCount || 0
      commentCount.value = data.commentCount || 0
    } else {
      ElMessage.error(response.msg || '获取统计数据失败')
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 获取热门内容
const fetchPopularContent = async () => {
  try {
    const response = await request.get('/admin/statistics/popular')

    if (response.code === 1) {
      const data = response.data || {}
      popularPartTime.value = data.partTime || []
      popularSecondHand.value = data.secondHand || []
    } else {
      ElMessage.error(response.msg || '获取热门内容失败')
    }
  } catch (error) {
    console.error('获取热门内容失败:', error)
    ElMessage.error('获取热门内容失败')
  }
}

onMounted(() => {
  fetchStatistics()
  fetchPopularContent()
})
</script>

<style scoped lang="scss">
.admin-statistics-page {
  min-height: 100vh;
  padding: 80px 0 20px;
  background-color: #f5f7fa;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;

  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin-bottom: 10px;
  }

  .page-subtitle {
    font-size: 14px;
    color: #666;
  }
}

.statistics-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  }

  .stat-icon {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #fff;

    &.user-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.part-time-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.second-hand-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.order-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.report-icon {
      background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    }

    &.comment-icon {
      background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
    }
  }

  .stat-content {
    flex: 1;

    .stat-value {
      font-size: 24px;
      font-weight: 600;
      color: #333;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 14px;
      color: #666;
    }
  }
}

.statistics-trend {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.trend-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    margin-bottom: 20px;
  }

  .trend-chart {
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f9f9f9;
    border-radius: 4px;

    .chart-placeholder {
      text-align: center;
      color: #999;

      i {
        font-size: 48px;
        margin-bottom: 16px;
        display: block;
      }

      p {
        font-size: 16px;
      }
    }
  }
}

.statistics-popular {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.popular-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    margin-bottom: 20px;
  }

  .popular-list {
    margin-top: 10px;
  }
}

@media (max-width: 768px) {
  .admin-statistics-page {
    padding: 60px 0 20px;
  }

  .statistics-overview {
    grid-template-columns: repeat(2, 1fr);
  }

  .statistics-trend,
  .statistics-popular {
    grid-template-columns: 1fr;
  }

  .trend-section {
    padding: 10px;

    .trend-chart {
      height: 200px;
    }
  }

  .popular-section {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }

  .stat-card {
    padding: 15px;

    .stat-value {
      font-size: 20px;
    }
  }
}
</style>