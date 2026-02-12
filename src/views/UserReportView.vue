<template>
  <div class="user-report">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/purchases">我的购买</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>举报</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">举报订单</h2>
      </div>

      <!-- 举报表单 -->
      <div class="report-form card">
        <h3 class="form-title">举报信息</h3>
        <el-form :model="reportForm" :rules="reportRules" ref="reportFormRef" label-width="120px">
          <el-form-item label="订单信息">
            <div class="order-info">
              <div class="order-item">
                <span class="order-label">订单号：</span>
                <span class="order-value">{{ orderInfo.orderId || '无' }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">商品名称：</span>
                <span class="order-value">{{ orderInfo.itemTitle || '无' }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">订单金额：</span>
                <span class="order-value">¥{{ orderInfo.price || 0 }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">购买时间：</span>
                <span class="order-value">{{ orderInfo.purchaseTime || '无' }}</span>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="举报类型" prop="reportType">
            <el-select v-model="reportForm.reportType" placeholder="请选择举报类型">
              <el-option label="虚假信息" value="false_info"></el-option>
              <el-option label="欺诈行为" value="fraud"></el-option>
              <el-option label="违规商品" value="illegal_item"></el-option>
              <el-option label="其他" value="other"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="举报原因" prop="reportReason">
            <el-input
              type="textarea"
              :rows="4"
              placeholder="请详细描述举报原因"
              v-model="reportForm.reportReason"
            ></el-input>
          </el-form-item>
          <el-form-item label="上传证据">
            <el-upload
              class="upload-demo"
              action="#"
              :auto-upload="false"
              :on-change="handleFileChange"
              :file-list="fileList"
              list-type="picture"
            >
              <el-button type="primary">
                <i class="el-icon-upload"></i> 选择文件
              </el-button>
              <template #tip>
                <div class="el-upload__tip">
                  请上传图片证据，最多3张
                </div>
              </template>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitReport">提交举报</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 举报须知 -->
      <div class="report-notice card">
        <h3 class="notice-title">举报须知</h3>
        <ul class="notice-list">
          <li>1. 请如实填写举报信息，不得虚假举报</li>
          <li>2. 请提供真实有效的证据，以便我们更好地处理</li>
          <li>3. 我们会在3个工作日内处理您的举报</li>
          <li>4. 多次虚假举报将影响您的信誉分</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElForm, ElFormItem, ElSelect, ElOption, ElInput, ElUpload, ElButton } from 'element-plus'

const route = useRoute()
const router = useRouter()

const orderId = computed(() => route.params.id)

// 订单信息
const orderInfo = ref({
  orderId: 'ORD' + orderId.value + Date.now(),
  itemTitle: '商品名称',
  price: 100,
  purchaseTime: '2026-02-10 10:00:00'
})

// 举报表单
const reportForm = ref({
  reportType: '',
  reportReason: ''
})

// 表单验证规则
const reportRules = ref({
  reportType: [
    { required: true, message: '请选择举报类型', trigger: 'change' }
  ],
  reportReason: [
    { required: true, message: '请填写举报原因', trigger: 'blur' },
    { min: 10, message: '举报原因至少10个字符', trigger: 'blur' }
  ]
})

// 表单引用
const reportFormRef = ref(null)

// 文件列表
const fileList = ref([])

// 处理文件变化
const handleFileChange = (file, fileList) => {
  if (fileList.length > 3) {
    ElMessage.warning('最多上传3张图片')
    fileList.pop()
  }
  return fileList
}

// 提交举报
const submitReport = async () => {
  if (!reportFormRef.value) return
  
  await reportFormRef.value.validate(async (valid) => {
    if (valid) {
      // 模拟提交举报
      try {
        // 这里应该调用后端API提交举报
        ElMessage.success('举报提交成功，我们会尽快处理')
        // 跳转到我的购买页面
        router.push('/user/user/purchases')
      } catch (error) {
        ElMessage.error('举报提交失败，请重试')
      }
    } else {
      ElMessage.warning('请完善举报信息')
    }
  })
}

// 重置表单
const resetForm = () => {
  if (reportFormRef.value) {
    reportFormRef.value.resetFields()
    fileList.value = []
  }
}

onMounted(() => {
  // 模拟获取订单信息
  // 这里应该从后端API获取订单信息
  console.log('举报订单ID:', orderId.value)
})
</script>

<style scoped lang="scss">
.user-report {
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

/* 举报表单样式 */
.report-form {
  padding: 20px;
  margin-bottom: 30px;
}

.form-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

/* 订单信息样式 */
.order-info {
  background-color: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
}

.order-item {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.order-label {
  font-size: 14px;
  color: #666;
  width: 80px;
}

.order-value {
  font-size: 14px;
  color: #333;
  flex: 1;
}

/* 举报须知样式 */
.report-notice {
  padding: 20px;
  margin-bottom: 30px;
}

.notice-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

.notice-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-list li {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .report-form,
  .report-notice {
    padding: 15px;
  }
  
  .order-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .order-label {
    width: 100%;
  }
}
</style>