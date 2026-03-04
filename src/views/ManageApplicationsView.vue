<template>
  <div class="manage-applications">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/part-time">兼职列表</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>管理申请</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">管理申请</h2>
        <el-button @click="goBack" icon="el-icon-back" size="small">返回</el-button>
      </div>

      <!-- 兼职信息 -->
      <div v-if="jobInfo" class="job-info-card">
        <h3 class="job-title">{{ jobInfo.title }}</h3>
        <div class="job-meta">
          <span class="meta-item"><i class="el-icon-s-flag"></i> {{ jobInfo.employer }}</span>
          <span class="meta-item"><i class="el-icon-s-position"></i> {{ jobInfo.location }}</span>
          <span class="meta-item"><i class="el-icon-time"></i> {{ jobInfo.workTime }}</span>
          <span class="meta-item salary">{{ jobInfo.salaryDesc }} {{ jobInfo.salaryUnit }}</span>
        </div>
      </div>

      <!-- 状态筛选 -->
      <div class="status-filter">
        <el-radio-group v-model="activeStatus" size="small">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="pending">待审核</el-radio-button>
          <el-radio-button label="approved">已通过</el-radio-button>
          <el-radio-button label="rejected">已拒绝</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 申请列表 -->
      <div class="applications-list">
        <div v-if="filteredApplications.length === 0" class="empty-state">
          <el-empty description="暂无申请记录" />
        </div>
        <div v-else class="application-items">
          <div v-for="app in filteredApplications" :key="app.id" class="application-item">
            <div class="item-main">
              <div class="item-header">
                <div class="header-left">
                  <el-tag :type="getStatusType(app.status)" size="small">{{ getStatusText(app.status) }}</el-tag>
                  <span class="apply-time">{{ formatTime(app.applyTime) }}</span>
                </div>
                <div class="app-actions">
                  <el-button v-if="app.status === 0" size="small" type="success" @click="approveApplication(app.id)">
                    通过
                  </el-button>
                  <el-button v-if="app.status === 0" size="small" type="danger" @click="rejectApplication(app.id)">
                    拒绝
                  </el-button>
                  <el-button v-if="app.status === 1 && app.companyPhone" size="small" type="primary" @click="contactApplicant(app)">
                    联系
                  </el-button>
                </div>
              </div>
              <div class="applicant-info">
                <div class="info-row">
                  <span class="info-label">申请人：</span>
                  <span class="info-value name">{{ app.companyName || '未知' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">电话：</span>
                  <span class="info-value">{{ app.companyPhone || '暂无' }}</span>
                </div>
                <div v-if="app.resume" class="info-row">
                  <span class="info-label">简历：</span>
                  <span class="info-value resume">{{ app.resume }}</span>
                </div>
                <div v-if="app.remark" class="info-row">
                  <span class="info-label">备注：</span>
                  <span class="info-value remark">{{ app.remark }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredApplications.length > 0" class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          small
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 兼职信息
const jobInfo = ref(null)

// 申请列表
const applications = ref([])

// 计算属性：根据状态筛选申请
const filteredApplications = computed(() => {
  if (activeStatus.value === 'all') {
    return applications.value
  }
  
  const statusMap = {
    'pending': 0,
    'approved': 1,
    'rejected': 2
  }
  
  const targetStatus = statusMap[activeStatus.value]
  return applications.value.filter(app => app.status === targetStatus)
})

// 根据状态获取标签类型
const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'warning'
    case 1:
      return 'success'
    case 2:
      return 'danger'
    default:
      return 'info'
  }
}

// 根据状态获取文本
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '待审核'
    case 1:
      return '已通过'
    case 2:
      return '已拒绝'
    default:
      return '未知'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 16)
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 通过申请
const approveApplication = (appId) => {
  ElMessageBox.confirm('确定要通过这条申请吗？', '通过申请', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(() => {
    fetch(`http://localhost:8080/api/part-time/apply/approve/${appId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    })
    .then(response => response.json())
    .then(data => {
      if (data.code === 1 || data.success) {
        const app = applications.value.find(a => a.id === appId)
        if (app) {
          app.status = 1
        }
        ElMessage.success('申请已通过')
      } else {
        ElMessage.error(data.msg || '操作失败')
      }
    })
    .catch(error => {
      console.error('通过申请失败:', error)
      ElMessage.error('操作失败，请稍后重试')
    })
  }).catch(() => {})
}

// 拒绝申请
const rejectApplication = (appId) => {
  ElMessageBox.confirm('确定要拒绝这条申请吗？', '拒绝申请', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    fetch(`http://localhost:8080/api/part-time/apply/reject/${appId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    })
    .then(response => response.json())
    .then(data => {
      if (data.code === 1 || data.success) {
        const app = applications.value.find(a => a.id === appId)
        if (app) {
          app.status = 2
        }
        ElMessage.success('申请已拒绝')
      } else {
        ElMessage.error(data.msg || '操作失败')
      }
    })
    .catch(error => {
      console.error('拒绝申请失败:', error)
      ElMessage.error('操作失败，请稍后重试')
    })
  }).catch(() => {})
}

// 联系申请人
const contactApplicant = (app) => {
  if (app.companyPhone) {
    ElMessage.info(`联系电话: ${app.companyPhone}`)
  } else {
    ElMessage.info('暂无联系方式')
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchApplications()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchApplications()
}

// 获取兼职详情
const fetchJobDetail = async () => {
  const jobId = route.query.jobId
  if (!jobId) {
    return
  }
  
  try {
    const response = await fetch(`http://localhost:8080/api/part-time/job/${jobId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    })
    const data = await response.json()
    if (data.code === 1 || data.success) {
      jobInfo.value = data.data
    }
  } catch (error) {
    console.error('获取兼职详情失败:', error)
  }
}

// 获取申请列表
const fetchApplications = async () => {
  const jobId = route.query.jobId
  if (!jobId) {
    return
  }
  
  try {
    const response = await fetch('http://localhost:8080/api/part-time/apply/applyMy', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      },
      body: JSON.stringify({
        id: jobId,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      })
    })
    const data = await response.json()
    if (data.code === 1 || data.success) {
      if (data.data && data.data.list) {
        applications.value = data.data.list
        total.value = parseInt(data.data.total) || data.data.list.length
      } else if (Array.isArray(data.data)) {
        applications.value = data.data
        total.value = data.data.length
      }
    }
  } catch (error) {
    console.error('获取申请列表失败:', error)
    ElMessage.error('获取申请列表失败')
  }
}

onMounted(() => {
  fetchJobDetail()
  fetchApplications()
})
</script>

<style scoped lang="scss">
.manage-applications {
  min-height: 100vh;
  padding-top: 70px;
  padding-bottom: 20px;
  background-color: #f5f7fa;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 15px;
}

.breadcrumb {
  margin-bottom: 15px;
  font-size: 13px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.job-info-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 15px;
  color: #fff;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.job-title {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 10px 0;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.meta-item {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  gap: 4px;
}

.salary {
  font-weight: 600;
  color: #ffe066;
}

.status-filter {
  margin-bottom: 15px;
  background: #fff;
  padding: 12px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.applications-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  padding: 15px;
  min-height: 200px;
}

.empty-state {
  padding: 40px 0;
}

.application-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.application-item {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 12px 15px;
  transition: all 0.2s ease;
  background: #fafbfc;
}

.application-item:hover {
  border-color: #c6e2ff;
  background: #f0f7ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.item-main {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.apply-time {
  font-size: 12px;
  color: #909399;
}

.applicant-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-row {
  display: flex;
  align-items: flex-start;
  font-size: 13px;
  line-height: 1.5;
}

.info-label {
  color: #606266;
  min-width: 60px;
  flex-shrink: 0;
}

.info-value {
  color: #303133;
  flex: 1;
  word-break: break-all;
}

.info-value.name {
  font-weight: 600;
  color: #409eff;
}

.info-value.resume,
.info-value.remark {
  color: #606266;
  font-style: italic;
}

.app-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

@media (max-width: 768px) {
  .manage-applications {
    padding-top: 60px;
  }
  
  .page-title {
    font-size: 18px;
  }
  
  .job-info-card {
    padding: 12px 15px;
  }
  
  .job-meta {
    flex-direction: column;
    gap: 6px;
  }
  
  .application-item {
    padding: 10px 12px;
  }
  
  .item-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .app-actions {
    width: 100%;
    justify-content: flex-end;
    margin-top: 8px;
  }
  
  .info-row {
    flex-direction: column;
    gap: 2px;
  }
  
  .info-label {
    min-width: auto;
  }
}
</style>
