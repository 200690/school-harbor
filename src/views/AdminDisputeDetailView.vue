<template>
  <div class="admin-dispute-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin">管理员中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin?tab=disputes">争议管理</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>争议详情</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">争议详情</h2>
      </div>

      <!-- 争议信息卡片 -->
      <div class="dispute-info-card card">
        <h3 class="section-title">争议基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">争议ID：</span>
            <span class="info-value">{{ dispute.id }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">订单ID：</span>
            <span class="info-value">{{ dispute.orderId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">用户ID：</span>
            <span class="info-value">{{ dispute.userId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">卖家ID：</span>
            <span class="info-value">{{ dispute.sellerId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">争议类型：</span>
            <span class="info-value">{{ dispute.disputeType }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建时间：</span>
            <span class="info-value">{{ dispute.createTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">争议状态：</span>
            <span class="info-value status-badge" :class="getStatusClass(dispute.status)">
              {{ dispute.status }}
            </span>
          </div>
        </div>

        <h3 class="section-title">争议详情</h3>
        <div class="dispute-description">
          <p>{{ dispute.description || '暂无详细描述' }}</p>
        </div>

        <h3 class="section-title">处理记录</h3>
        <div class="process-records">
          <div v-for="(record, index) in dispute.processRecords" :key="index" class="record-item">
            <div class="record-time">{{ record.time }}</div>
            <div class="record-content">{{ record.content }}</div>
          </div>
          <div v-if="dispute.processRecords.length === 0" class="empty-records">
            暂无处理记录
          </div>
        </div>
      </div>

      <!-- 备注信息卡片 -->
      <div class="remark-card card">
        <h3 class="section-title">备注信息</h3>
        <el-input
          v-model="disputeRemark"
          type="textarea"
          :rows="4"
          placeholder="请输入备注信息"
          style="width: 100%; margin-bottom: 15px;"
        />
        <el-button type="primary" @click="saveRemark">保存备注</el-button>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="goBack">返回</el-button>
        <el-button type="success" @click="handleDispute" :disabled="dispute.status === '已处理'">
          处理争议
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'

const route = useRoute()
const router = useRouter()
const disputeId = route.params.id

// 模拟争议数据
const dispute = ref({
  id: disputeId,
  orderId: 2,
  userId: 2,
  sellerId: 1,
  disputeType: '商品质量问题',
  createTime: '2026-02-12',
  status: '待处理',
  description: '收到的商品与描述不符，存在质量问题，要求退款或换货。',
  processRecords: [
    {
      time: '2026-02-12 10:00',
      content: '用户提交争议申请'
    }
  ],
  remark: ''
})

// 备注信息
const disputeRemark = ref('')

// 获取争议状态对应的样式类
const getStatusClass = (status) => {
  switch (status) {
    case '待处理':
      return 'status-pending'
    case '已处理':
      return 'status-processed'
    case '已解决':
      return 'status-resolved'
    default:
      return ''
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 处理争议
const handleDispute = () => {
  ElConfirm('确定要处理这个争议吗？', '处理争议', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success',
    // 自定义确认对话框内容
    showInput: true,
    inputPlaceholder: '请输入处理结果',
    inputType: 'textarea'
  }).then(({ value }) => {
    if (value) {
      // 这里应该调用后端API处理争议
      dispute.value.status = '已处理'
      dispute.value.processRecords.push({
        time: new Date().toLocaleString(),
        content: `管理员处理：${value}`
      })
      ElMessage.success('处理争议成功')
    } else {
      ElMessage.warning('请输入处理结果')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 保存备注
const saveRemark = () => {
  // 这里应该调用后端API保存备注
  dispute.value.remark = disputeRemark.value
  ElMessage.success('保存备注成功')
}

onMounted(() => {
  // 这里应该根据争议ID从后端获取争议详情
  console.log(`加载争议 ${disputeId} 的详情`)
  // 模拟从后端获取数据
  setTimeout(() => {
    // 模拟数据加载完成
  }, 500)
})
</script>

<style scoped lang="scss">
.admin-dispute-detail {
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

.dispute-info-card {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.section-title:first-child {
  border-top: none;
  padding-top: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
}

.info-value {
  color: #333;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
}

.status-pending {
  background-color: rgba(230, 162, 60, 0.1);
  color: var(--warning-color);
}

.status-processed {
  background-color: rgba(64, 158, 255, 0.1);
  color: var(--primary-color);
}

.status-resolved {
  background-color: rgba(103, 194, 58, 0.1);
  color: var(--success-color);
}

.dispute-description {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.dispute-description p {
  margin: 0;
  color: #333;
  line-height: 1.6;
}

.process-records {
  margin-top: 10px;
}

.record-item {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border-left: 4px solid var(--primary-color);
}

.record-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.record-content {
  color: #333;
  line-height: 1.4;
}

.empty-records {
  color: #999;
  text-align: center;
  padding: 40px 0;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.remark-card {
  margin-bottom: 30px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>