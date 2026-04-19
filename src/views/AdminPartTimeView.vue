<template>
  <div class="admin-part-time-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>兼职管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">兼职管理</h2>
        <p class="page-subtitle">管理兼职信息，包括发布和审核</p>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchTitle"
          placeholder="请输入兼职标题"
          class="search-input"
          clearable
          @keyup.enter="fetchJobs"
        />
        <el-select v-model="jobStatus" placeholder="选择兼职状态" class="status-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="已下架" :value="0" />
          <el-option label="招聘中" :value="1" />
          <el-option label="已招满" :value="2" />
          <el-option label="已截止" :value="3" />
        </el-select>
        <el-button type="primary" @click="fetchJobs">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>

      <!-- 兼职列表 -->
      <div class="job-list">
        <el-table :data="jobs" style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="兼职ID" width="80" />
          <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
          <el-table-column prop="employer" label="雇主" width="120" show-overflow-tooltip />
          <el-table-column prop="location" label="工作地点" width="100" />
          <el-table-column prop="workTime" label="工作时间" width="100" />
          <el-table-column label="薪资" width="120">
            <template #default="scope">
              <span>{{ scope.row.salaryDesc }} {{ scope.row.salaryUnit }}</span>
            </template>
          </el-table-column>
          <el-table-column label="类型" width="100">
            <template #default="scope">
              <el-tag :type="getTypeType(scope.row.type)" size="small">
                {{ getTypeText(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" width="160" />
          <el-table-column label="统计" width="120">
            <template #default="scope">
              <div class="stats">
                <span title="浏览量"><el-icon><View /></el-icon> {{ scope.row.viewCount || 0 }}</span>
                <span title="申请人数"><el-icon><User /></el-icon> {{ scope.row.applicantCount || 0 }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewJob(scope.row)">查看</el-button>
              <el-button 
                v-if="scope.row.status === 0"
                type="success" 
                size="small" 
                @click="toggleStatus(scope.row, 1)"
              >
                上架
              </el-button>
              <el-button 
                v-else-if="scope.row.status === 1"
                type="warning" 
                size="small" 
                @click="toggleStatus(scope.row, 0)"
              >
                下架
              </el-button>
              <el-button 
                v-else-if="scope.row.status === 2"
                type="info" 
                size="small" 
                disabled
              >
                已招满
              </el-button>
              <el-button 
                v-else-if="scope.row.status === 3"
                type="info" 
                size="small" 
                disabled
              >
                已截止
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="deleteJob(scope.row)"
              >
                删除
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, View, User } from '@element-plus/icons-vue'
import { getAllPartTimeJobs, updatePartTimeStatus, deletePartTimeItem } from '@/api/partTime'

const router = useRouter()

// 搜索和筛选参数
const searchTitle = ref('')
const jobStatus = ref(null)

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载状态
const loading = ref(false)

// 兼职列表
const jobs = ref([])

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 0: return 'danger'
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'info'
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0: return '已下架'
    case 1: return '招聘中'
    case 2: return '已招满'
    case 3: return '已截止'
    default: return '未知状态'
  }
}

// 获取类型类型
const getTypeType = (type) => {
  switch (type) {
    case 1: return 'success'
    case 2: return 'primary'
    case 3: return 'warning'
    default: return 'info'
  }
}

// 获取类型文本
const getTypeText = (type) => {
  switch (type) {
    case 1: return '校内'
    case 2: return '校外'
    case 3: return '实习'
    default: return '未知'
  }
}

// 获取兼职列表
const fetchJobs = async () => {
  loading.value = true
  try {
    const data = {
      title: searchTitle.value || null,
      status: jobStatus.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    
    const response = await getAllPartTimeJobs(data)

    if (response.code === 1) {
      jobs.value = response.data.list || []
      total.value = parseInt(response.data.total) || 0
    } else {
      ElMessage.error(response.msg || '获取兼职列表失败')
    }
  } catch (error) {
    console.error('获取兼职列表失败:', error)
    ElMessage.error('获取兼职列表失败')
  } finally {
    loading.value = false
  }
}

// 查看兼职详情
const viewJob = (job) => {
  router.push(`/item/${job.id}`)
}

// 切换兼职状态
const toggleStatus = async (job, newStatus) => {
  try {
    const response = await updatePartTimeStatus(job.id, newStatus)

    if (response.code === 1) {
      ElMessage.success(newStatus === 1 ? '兼职已上架' : '兼职已下架')
      fetchJobs()
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除兼职
const deleteJob = async (job) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除兼职 "${job.title}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deletePartTimeItem(job.id)

    if (response.code === 1) {
      ElMessage.success('兼职删除成功')
      fetchJobs()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchJobs()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchJobs()
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
.admin-part-time-page {
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

  .status-select {
    width: 150px;
  }
}

.job-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.stats {
  display: flex;
  gap: 12px;
  color: #666;
  font-size: 13px;
  
  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.pagination {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .admin-part-time-page {
    padding: 60px 0 20px;
  }

  .search-filter {
    flex-direction: column;
    align-items: stretch;

    .search-input,
    .status-select {
      width: 100%;
    }
  }

  .job-list {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }
}
</style>
