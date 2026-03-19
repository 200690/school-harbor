<template>
  <div class="admin-reports-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>举报管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">举报管理</h2>
        <p class="page-subtitle">处理用户举报，维护平台秩序</p>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索举报内容或目标信息"
          class="search-input"
          @keyup.enter="fetchReports"
        >
          <template #append>
            <el-button @click="fetchReports"><i class="el-icon-search"></i></el-button>
          </template>
        </el-input>
        <el-select v-model="reportType" placeholder="选择举报类型" class="type-select">
          <el-option label="全部" value="" />
          <el-option label="虚假信息" value="false_info" />
          <el-option label="诈骗行为" value="fraud" />
          <el-option label="色情内容" value="porn" />
          <el-option label="暴力内容" value="violence" />
          <el-option label="违法违规" value="illegal" />
          <el-option label="其他" value="other" />
        </el-select>
        <el-select v-model="reportStatus" placeholder="选择处理状态" class="status-select">
          <el-option label="全部" value="" />
          <el-option label="待处理" value="pending" />
          <el-option label="已处理" value="processed" />
          <el-option label="已忽略" value="ignored" />
        </el-select>
      </div>

      <!-- 举报列表 -->
      <div class="report-list">
        <el-table :data="reports" style="width: 100%">
          <el-table-column prop="id" label="举报ID" width="100" />
          <el-table-column prop="reporterName" label="举报人" width="150" />
          <el-table-column prop="targetType" label="举报对象" width="120">
            <template #default="scope">
              <el-tag type="info">
                {{ getTargetTypeText(scope.row.targetType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reportType" label="举报类型" width="120">
            <template #default="scope">
              <el-tag type="warning">
                {{ getReportTypeText(scope.row.reportType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="举报内容" min-width="300" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="举报时间" width="200" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewReport(scope.row)">查看</el-button>
              <el-button 
                v-if="scope.row.status === 'pending'" 
                type="success" 
                size="small" 
                @click="processReport(scope.row)"
              >
                处理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// 搜索和筛选参数
const searchKeyword = ref('')
const reportType = ref('')
const reportStatus = ref('')

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 举报列表
const reports = ref([])

// 举报类型映射
const reportTypeMap = {
  'false_info': '虚假信息',
  'fraud': '诈骗行为',
  'porn': '色情内容',
  'violence': '暴力内容',
  'illegal': '违法违规',
  'other': '其他'
}

// 目标类型映射
const targetTypeMap = {
  'part-time': '兼职',
  'second-hand': '二手商品',
  'comment': '评论'
}

// 状态类型映射
const statusTypeMap = {
  'pending': 'info',
  'processed': 'success',
  'ignored': 'danger'
}

// 状态文本映射
const statusTextMap = {
  'pending': '待处理',
  'processed': '已处理',
  'ignored': '已忽略'
}

// 获取举报类型文本
const getReportTypeText = (type) => {
  return reportTypeMap[type] || '未知类型'
}

// 获取目标类型文本
const getTargetTypeText = (type) => {
  return targetTypeMap[type] || '未知对象'
}

// 获取状态类型
const getStatusType = (status) => {
  return statusTypeMap[status] || 'warning'
}

// 获取状态文本
const getStatusText = (status) => {
  return statusTextMap[status] || '未知状态'
}

// 获取举报列表
const fetchReports = async () => {
  try {
    const response = await request.get('/admin/reports', {
      params: {
        keyword: searchKeyword.value,
        type: reportType.value,
        status: reportStatus.value,
        page: currentPage.value,
        size: pageSize.value
      }
    })

    if (response.code === 1) {
      reports.value = response.data.list || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.msg || '获取举报列表失败')
    }
  } catch (error) {
    console.error('获取举报列表失败:', error)
    ElMessage.error('获取举报列表失败')
  }
}

// 查看举报详情
const viewReport = () => {
  // 这里可以跳转到举报详情页面
  ElMessage.info('举报详情功能开发中')
}

// 处理举报
const processReport = async (report) => {
  try {
    const response = await request.post(`/admin/report/process/${report.id}`)

    if (response.code === 1) {
      ElMessage.success('举报已处理')
      // 重新获取举报列表
      fetchReports()
    } else {
      ElMessage.error(response.msg || '处理失败')
    }
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchReports()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchReports()
}

onMounted(() => {
  fetchReports()
})
</script>

<style scoped lang="scss">
.admin-reports-page {
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

.search-filter {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  align-items: center;

  .search-input {
    flex: 1;
  }

  .type-select,
  .status-select {
    width: 150px;
  }
}

.report-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .admin-reports-page {
    padding: 60px 0 20px;
  }

  .search-filter {
    flex-direction: column;
    align-items: stretch;

    .search-input,
    .type-select,
    .status-select {
      width: 100%;
    }
  }

  .report-list {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }
}
</style>