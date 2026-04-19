<template>
  <div class="report-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>举报</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">举报</h2>
        <p class="page-subtitle">{{ getReportTitle() }}</p>
      </div>

      <!-- 举报表单 -->
      <div class="report-form">
        <el-form :model="reportForm" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="举报原因" prop="reasonType">
            <el-select v-model="reportForm.reasonType" placeholder="请选择举报原因">
              <el-option label="虚假信息" :value="1" />
              <el-option label="诈骗行为" :value="2" />
              <el-option label="色情内容" :value="3" />
              <el-option label="暴力内容" :value="4" />
              <el-option label="违法违规" :value="5" />
              <el-option label="其他" :value="6" />
            </el-select>
          </el-form-item>

          <el-form-item label="原因描述" prop="reasonDesc">
            <el-input
              v-model="reportForm.reasonDesc"
              type="textarea"
              :rows="5"
              placeholder="请详细描述您的举报原因"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="上传证据">
            <div class="image-uploader">
              <div v-for="(image, index) in reportForm.evidenceImages" :key="index" class="uploaded-image">
                <img :src="image" alt="证据图片" />
                <div class="image-actions">
                  <el-button type="danger" size="small" @click="removeImage(index)">删除</el-button>
                </div>
              </div>
              <div class="upload-btn-container">
                <input
                  type="file"
                  ref="fileInput"
                  style="display: none"
                  accept="image/*"
                  multiple
                  @change="handleImageUpload"
                />
                <el-button type="primary" @click="triggerFileInput">点击上传</el-button>
                <div class="upload-tip">
                  只能上传jpg/png文件，且不超过5MB
                </div>
              </div>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitReport">提交举报</el-button>
            <el-button @click="cancelReport">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const fileInput = ref(null)

// 举报表单数据
const reportForm = ref({
  reasonType: null,
  reasonDesc: '',
  evidenceImages: []
})

// 路由参数
const targetId = ref(route.query.targetId)
const targetType = ref(route.query.targetType)
const targetTitle = ref(route.query.targetTitle)
const targetContent = ref(route.query.targetContent)

// 根据 targetType 计算 reportType
// reportType: 1-评论，2-商品，3-兼职，4-用户
const reportType = computed(() => {
  switch (targetType.value) {
    case 'comment':
      return 1
    case 'second-hand':
      return 2
    case 'part-time':
      return 3
    case 'user':
      return 4
    default:
      return null
  }
})

// 验证规则
const rules = {
  reasonType: [
    { required: true, message: '请选择举报原因', trigger: 'change' }
  ],
  reasonDesc: [
    { max: 500, message: '原因描述不能超过500个字符', trigger: 'blur' }
  ]
}

// 获取举报标题
const getReportTitle = () => {
  switch (targetType.value) {
    case 'part-time':
      return `举报兼职：${targetTitle.value || '未知兼职'}`
    case 'second-hand':
      return `举报商品：${targetTitle.value || '未知商品'}`
    case 'comment':
      return `举报评论：${targetContent.value ? targetContent.value.substring(0, 20) + '...' : '未知评论'}`
    case 'user':
      return `举报用户：${targetTitle.value || '未知用户'}`
    default:
      return '举报'
  }
}

// 触发文件输入
const triggerFileInput = () => {
  fileInput.value?.click()
}

// 处理图片上传
const handleImageUpload = async (event) => {
  const files = event.target.files
  if (!files || files.length === 0) return

  for (const file of files) {
    // 检查文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('只能上传图片文件')
      continue
    }

    // 检查文件大小
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过5MB')
      continue
    }

    // 上传文件
    try {
      const formData = new FormData()
      formData.append('file', file)

      const token = localStorage.getItem('token')
      const response = await fetch('http://localhost:8080/api/oss/upload', {
        method: 'POST',
        body: formData,
        headers: {
          'Authorization': token ? token : ''
        }
      })

      if (!response.ok) {
        throw new Error('上传失败')
      }

      const result = await response.json()
      if (result.code === 1) {
        reportForm.value.evidenceImages.push(result.data)
        ElMessage.success('图片上传成功')
      } else {
        ElMessage.error(result.msg || '上传失败')
      }
    } catch (error) {
      console.error('上传失败:', error)
      ElMessage.error('上传失败，请重试')
    }
  }

  // 清空文件输入
  event.target.value = ''
}

// 移除图片
const removeImage = (index) => {
  reportForm.value.evidenceImages.splice(index, 1)
}

// 提交举报
const submitReport = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    const reportData = {
      reportType: reportType.value,
      targetId: Number(targetId.value),
      reasonType: reportForm.value.reasonType,
      reasonDesc: reportForm.value.reasonDesc || null,
      evidenceImages: reportForm.value.evidenceImages.length > 0 ? reportForm.value.evidenceImages : null
    }

    const response = await request.post('/report/submit', reportData)

    if (response.code === 1) {
      ElMessage.success('举报成功，我们会尽快处理')
      setTimeout(() => {
        router.back()
      }, 1500)
    } else {
      ElMessage.error(response.msg || '举报失败')
    }
  } catch (error) {
    console.error('提交举报失败:', error)
    ElMessage.error('提交举报失败')
  }
}

// 取消举报
const cancelReport = () => {
  router.back()
}

onMounted(() => {
  // 检查是否有必要的参数
  if (!targetId.value || !targetType.value) {
    ElMessage.error('缺少必要参数')
    setTimeout(() => {
      router.back()
    }, 1500)
  }
})
</script>

<style scoped lang="scss">
.report-page {
  min-height: 100vh;
  padding: 80px 0 20px;
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

.report-form {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.image-uploader {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 10px;

  .uploaded-image {
    position: relative;
    width: 120px;
    height: 120px;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .image-actions {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      background: rgba(0, 0, 0, 0.6);
      padding: 4px;
      text-align: center;

      .el-button {
        padding: 2px 8px;
        font-size: 12px;
      }
    }
  }

  .upload-btn-container {
    width: 120px;
    height: 120px;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      border-color: #409eff;
      background: #ecf5ff;
    }

    .upload-tip {
      font-size: 12px;
      color: #909399;
      margin-top: 4px;
      text-align: center;
    }
  }
}

@media (max-width: 768px) {
  .report-page {
    padding: 60px 0 20px;
  }

  .report-form {
    padding: 20px;
  }

  .page-title {
    font-size: 20px;
  }
}
</style>