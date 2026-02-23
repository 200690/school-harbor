<template>
  <div class="user-applications">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的申请</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">我的申请</h2>
      </div>

      <!-- 状态筛选 -->
      <div class="status-filter">
        <el-button-group>
          <el-button :type="activeStatus === 'all' ? 'primary' : 'default'" @click="activeStatus = 'all'">全部</el-button>
          <el-button :type="activeStatus === 'pending' ? 'primary' : 'default'" @click="activeStatus = 'pending'">待审核</el-button>
          <el-button :type="activeStatus === 'approved' ? 'primary' : 'default'" @click="activeStatus = 'approved'">已通过</el-button>
          <el-button :type="activeStatus === 'rejected' ? 'primary' : 'default'" @click="activeStatus = 'rejected'">已拒绝</el-button>
          <el-button :type="activeStatus === 'canceled' ? 'primary' : 'default'" @click="activeStatus = 'canceled'">已取消</el-button>
        </el-button-group>
      </div>

      <!-- 申请列表 -->
      <div class="applications-list">
        <div v-if="filteredApplications.length === 0" class="empty-state">
          <el-empty description="暂无申请记录" />
        </div>
        <div v-else class="application-items">
          <div v-for="app in filteredApplications" :key="app.id" class="application-item">
            <div class="item-header">
              <el-tag :type="getStatusType(app.status)">{{ getStatusText(app.status) }}</el-tag>
              <span class="apply-time">{{ app.applyTime }}</span>
            </div>
            <h3 class="job-title">{{ app.partTimeTitle }}</h3>
            <div class="job-info">
              <span class="employer">{{ app.employer }}</span>
              <span class="salary">{{ app.salaryDesc }}</span>
              <span class="location"><i class="el-icon-location"></i> {{ app.partTimeLocation }}</span>
              <span class="work-time" v-if="app.interviewTime">{{ app.interviewTime }}</span>
            </div>
            <div class="app-actions">
              <el-button size="small" type="primary" @click="viewJobDetail(app.partTimeId)">
                查看兼职详情
              </el-button>
              <el-button v-if="app.status === 0" size="small" type="warning" @click="cancelApplication(app.id)">
                取消申请
              </el-button>
              <el-button v-if="app.status === 1" size="small" type="success" @click="contactEmployer()">
                联系雇主
              </el-button>
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
          :total="filteredApplications.length"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getMyApplicationsById } from '@/api/partTime'

const router = useRouter()
const userStore = useUserStore()
const activeStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// 获取用户的申请记录
const applications = ref([])

// 计算属性：根据状态筛选申请
const filteredApplications = computed(() => {
  if (activeStatus.value === 'all') {
    return applications.value
  }
  
  // 状态映射：字符串状态到数字状态
  const statusMap = {
    'pending': 0,      // 待审核
    'approved': 1,     // 已通过
    'rejected': 2,     // 已拒绝
    'canceled': 3      // 已取消
  }
  
  const targetStatus = statusMap[activeStatus.value]
  return applications.value.filter(app => app.status === targetStatus)
})

// 根据状态获取标签类型
const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'info'
    case 1:
      return 'success'
    case 2:
      return 'danger'
    case 3:
      return 'warning'
    case 4:
      return 'default'
    default:
      return 'default'
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
    case 3:
      return '已取消'
    case 4:
      return '已完成'
    default:
      return '未知状态'
  }
}

// 查看兼职详情
const viewJobDetail = (jobId) => {
  router.push(`/item/${jobId}`)
}

// 取消申请
const cancelApplication = (appId) => {
  ElMessageBox.confirm('确定要取消这条申请吗？', '取消申请', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const app = applications.value.find(a => a.id === appId)
    if (app) {
      app.status = 3
      ElMessage.success('申请已取消')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 联系雇主
const contactEmployer = () => {
  ElMessage.info('联系雇主功能')
  // 这里应该实现联系雇主的功能
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (current) => {
  currentPage.value = current
}

// 获取我的申请
const fetchMyApplications = async () => {
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('请先登录')
      router.push('/user/user/login')
      return
    }
    
    console.log('获取我的申请，用户ID:', userId)
    const response = await getMyApplicationsById(userId)
    console.log('申请数据:', response)
    
    if (response.data) {
      applications.value = response.data
    } else {
      applications.value = []
    }
  } catch (error) {
    console.error('获取申请记录失败:', error)
    ElMessage.error('获取申请记录失败')
    applications.value = []
  }
}

onMounted(() => {
  fetchMyApplications()
})
</script>

<style scoped lang="scss">
.user-applications {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
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

.status-filter {
  margin-bottom: 20px;
}

.applications-list {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.application-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.application-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.apply-time {
  font-size: 12px;
  color: #999;
}

.job-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.job-info {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.employer, .salary, .location, .work-time {
  font-size: 14px;
  color: #666;
}

.salary {
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
}

.app-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .status-filter {
    overflow-x: auto;
    white-space: nowrap;
    padding-bottom: 10px;
  }
  
  .applications-list {
    padding: 15px;
  }
  
  .application-item {
    padding: 15px;
  }
  
  .job-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .app-actions {
    flex-wrap: wrap;
  }
}
</style>