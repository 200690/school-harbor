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
          v-model="reasonDesc"
          placeholder="请输入举报内容"
          class="search-input"
          clearable
          @keyup.enter="fetchReports"
        />
        <el-select v-model="reportType" placeholder="举报类型" class="type-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="评论" :value="1" />
          <el-option label="商品" :value="2" />
          <el-option label="兼职" :value="3" />
          <el-option label="用户" :value="4" />
        </el-select>
        <el-select v-model="reasonType" placeholder="理由类型" class="reason-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="虚假信息" :value="1" />
          <el-option label="诈骗行为" :value="2" />
          <el-option label="色情内容" :value="3" />
          <el-option label="暴力内容" :value="4" />
          <el-option label="违法违规" :value="5" />
          <el-option label="其他" :value="6" />
        </el-select>
        <el-select v-model="status" placeholder="处理状态" class="status-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="待处理" :value="0" />
          <el-option label="已处理（违规）" :value="1" />
          <el-option label="已处理（正常）" :value="2" />
        </el-select>
        <el-button type="primary" @click="fetchReports">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>

      <!-- 举报列表 -->
      <div class="report-list">
        <el-table :data="reports" style="width: 100%">
          <el-table-column prop="id" label="举报ID" width="100" />
          <el-table-column prop="reporterId" label="举报人ID" width="100" />
          <el-table-column prop="targetId" label="目标ID" width="100" />
          <el-table-column prop="reportType" label="举报类型" width="100">
            <template #default="scope">
              <el-tag type="info">
                {{ getReportTypeName(scope.row.reportType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reasonType" label="理由类型" width="100">
            <template #default="scope">
              <el-tag type="warning">
                {{ getReasonTypeName(scope.row.reasonType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reasonDesc" label="举报内容" min-width="200" />
          <el-table-column label="证据图片" width="120">
            <template #default="scope">
              <div v-if="scope.row.evidenceImages && scope.row.evidenceImages.length > 0" class="evidence-images">
                <el-image
                  v-for="(img, index) in scope.row.evidenceImages.slice(0, 3)"
                  :key="index"
                  :src="img"
                  :preview-src-list="scope.row.evidenceImages"
                  fit="cover"
                  style="width: 30px; height: 30px; margin-right: 4px; border-radius: 4px; cursor: pointer;"
                />
                <span v-if="scope.row.evidenceImages.length > 3" class="more-images">
                  +{{ scope.row.evidenceImages.length - 3 }}
                </span>
              </div>
              <span v-else class="no-image">无图片</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="举报时间" width="180">
            <template #default="scope">
              {{ formatTime(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewReport(scope.row)">查看</el-button>
              <el-button 
                v-if="scope.row.status === 0" 
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

    <!-- 举报详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="举报详情"
      width="600px"
      destroy-on-close
    >
      <el-descriptions :column="2" border v-if="currentReport">
        <el-descriptions-item label="举报ID">{{ currentReport.id }}</el-descriptions-item>
        <el-descriptions-item label="举报人ID">{{ currentReport.reporterId }}</el-descriptions-item>
        <el-descriptions-item label="目标ID">
          <template v-if="currentReport.reportType === 1">
            {{ currentReport.targetId }}
          </template>
          <template v-else-if="currentReport.status === 0">
            <router-link :to="getTargetLink(currentReport)" class="target-link">
              {{ currentReport.targetId }} (点击查看)
            </router-link>
          </template>
          <template v-else>
            <span class="target-link-disabled">
              {{ currentReport.targetId }}
            </span>
          </template>
        </el-descriptions-item>
        <el-descriptions-item label="举报类型">
          <el-tag type="info">{{ getReportTypeName(currentReport.reportType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="理由类型">
          <el-tag type="warning">{{ getReasonTypeName(currentReport.reasonType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentReport.status)">{{ getStatusName(currentReport.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="举报时间" :span="2">{{ formatTime(currentReport.createdAt) }}</el-descriptions-item>
        <el-descriptions-item v-if="currentReport.reportType === 1" label="评论内容" :span="2">
          <el-skeleton v-if="loadingComment" :rows="1" animated />
          <span v-else>{{ commentContent || '无' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="举报内容" :span="2">{{ currentReport.reasonDesc || '无' }}</el-descriptions-item>
        <el-descriptions-item label="证据图片" :span="2">
          <div v-if="currentReport.evidenceImages && currentReport.evidenceImages.length > 0" class="dialog-images">
            <el-image
              v-for="(img, index) in currentReport.evidenceImages"
              :key="index"
              :src="img"
              :preview-src-list="currentReport.evidenceImages"
              :initial-index="index"
              fit="cover"
              style="width: 80px; height: 80px; margin-right: 8px; margin-bottom: 8px; border-radius: 4px; cursor: pointer;"
            />
          </div>
          <span v-else>无证据图片</span>
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <template v-if="currentReport && currentReport.status === 0">
            <el-button type="danger" @click="handleProcess(4)">标记正常</el-button>
            <el-button type="primary" @click="confirmViolation">确认违规</el-button>
          </template>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 搜索和筛选参数
const reasonDesc = ref('')
const reportType = ref(null)
const reasonType = ref(null)
const status = ref(null)

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 举报列表
const reports = ref([])

// 弹窗相关
const dialogVisible = ref(false)
const currentReport = ref(null)
const commentContent = ref('')
const loadingComment = ref(false)

// 理由类型映射
const reasonTypeMap = {
  1: '虚假信息',
  2: '诈骗行为',
  3: '色情内容',
  4: '暴力内容',
  5: '违法违规',
  6: '其他'
}

// 举报类型映射
const reportTypeMap = {
  1: '评论',
  2: '商品',
  3: '兼职',
  4: '用户'
}

// 状态类型映射
const statusTypeMap = {
  0: 'warning',
  1: 'danger',
  2: 'success'
}

// 状态名称映射
const statusNameMap = {
  0: '待处理',
  1: '已处理（违规）',
  2: '已处理（正常）'
}

// 获取理由类型名称
const getReasonTypeName = (type) => {
  return reasonTypeMap[type] || '未知'
}

// 获取举报类型名称
const getReportTypeName = (type) => {
  return reportTypeMap[type] || '未知'
}

// 获取状态类型
const getStatusType = (s) => {
  return statusTypeMap[s] || 'info'
}

// 获取状态名称
const getStatusName = (s) => {
  return statusNameMap[s] || '未知'
}

// 获取目标链接
const getTargetLink = (report) => {
  if (!report) return '#'
  switch (report.reportType) {
    case 2:
      return `/second-hand/detail/${report.targetId}`
    case 3:
      return `/item/${report.targetId}`
    case 4:
      return `/user/profile/${report.targetId}`
    default:
      return '#'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ')
}

// 获取举报列表
const fetchReports = async () => {
  try {
    const response = await request.post('/report/list', {
      id: null,
      reasonDesc: reasonDesc.value || null,
      reportType: reportType.value,
      reasonType: reasonType.value,
      status: status.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })

    if (response.code === 1) {
      reports.value = response.data?.list || []
      total.value = parseInt(response.data?.total || 0)
    } else {
      ElMessage.error(response.msg || '获取举报列表失败')
    }
  } catch (error) {
    console.error('获取举报列表失败:', error)
    ElMessage.error('获取举报列表失败')
  }
}

// 查看举报详情
const viewReport = async (report) => {
  currentReport.value = report
  
  if (report.reportType === 1) {
    loadingComment.value = true
    commentContent.value = ''
    try {
      const response = await request.post(`/comment/getComment/${report.targetId}`)
      if (response.code === 1) {
        commentContent.value = response.data || '无内容'
      } else {
        commentContent.value = '获取评论内容失败'
      }
    } catch (error) {
      console.error('获取评论内容失败:', error)
      commentContent.value = '获取评论内容失败'
    } finally {
      loadingComment.value = false
    }
  }
  
  dialogVisible.value = true
}

// 打开处理弹窗
const processReport = (report) => {
  currentReport.value = report
  dialogVisible.value = true
}

// 处理举报
const handleProcess = async (action) => {
  if (!currentReport.value) return
  
  try {
    const response = await request.post('/report/handle', {
      reportId: currentReport.value.id,
      reportType: currentReport.value.reportType,
      action: action
    })

    if (response.code === 1) {
      const actionText = action === 4 ? '已标记为正常' : '已确认违规'
      ElMessage.success(actionText)
      dialogVisible.value = false
      fetchReports()
    } else {
      ElMessage.error(response.msg || '处理失败')
    }
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  }
}

// 确认违规（根据举报类型决定action值）
const confirmViolation = () => {
  if (!currentReport.value) return
  // reportType: 1-评论, 2-商品, 3-兼职, 4-用户
  // action: 1-非用户违规, 3-用户违规
  const action = currentReport.value.reportType === 4 ? 3 : 1
  handleProcess(action)
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
  .reason-select,
  .status-select {
    width: 150px;
  }
}

.dialog-images {
  display: flex;
  flex-wrap: wrap;
}

.target-link {
  color: #409eff;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.target-link-disabled {
  color: #909399;
  cursor: not-allowed;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.report-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;

  .evidence-images {
    display: flex;
    align-items: center;
  }

  .more-images {
    color: #909399;
    font-size: 12px;
  }

  .no-image {
    color: #909399;
    font-size: 12px;
  }
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