<template>
  <div class="second-hand-edit">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>{{ isEditMode ? '编辑二手交易' : '发布二手交易' }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">{{ isEditMode ? '编辑二手交易' : '发布二手交易' }}</h2>
      </div>

      <!-- 编辑表单 -->
      <div class="edit-form">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入商品标题" />
          </el-form-item>

          <el-form-item label="价格" prop="price">
            <el-input-number v-model="formData.price" :min="0" :step="0.01" placeholder="请输入价格" />
          </el-form-item>

          <el-form-item label="原价" prop="originalPrice">
            <el-input-number v-model="formData.originalPrice" :min="0" :step="0.01" placeholder="请输入原价（可选）" />
          </el-form-item>

          <el-form-item label="分类" prop="categoryId">
            <el-select v-model="formData.categoryId" placeholder="请选择分类">
              <el-option label="教材教辅" :value="1" />
              <el-option label="电子产品" :value="2" />
              <el-option label="生活用品" :value="3" />
              <el-option label="运动器材" :value="4" />
              <el-option label="其他" :value="5" />
            </el-select>
          </el-form-item>

          <el-form-item label="成色" prop="condition">
            <el-select v-model="formData.condition" placeholder="请选择成色">
              <el-option label="全新" :value="1" />
              <el-option label="九成新" :value="2" />
              <el-option label="八成新" :value="3" />
              <el-option label="七成新及以下" :value="4" />
            </el-select>
          </el-form-item>

          <el-form-item label="学校" prop="school">
            <el-input v-model="formData.school" placeholder="请输入学校名称" />
          </el-form-item>

          <el-form-item label="具体位置" prop="location">
            <el-input v-model="formData.location" placeholder="请输入具体位置，如：东区宿舍6栋" />
          </el-form-item>

          <el-form-item label="描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入商品描述"
            />
          </el-form-item>

          <el-form-item label="商品图片">
            <el-upload
              class="upload-demo"
              action="#"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              :auto-upload="false"
            >
              <el-button type="primary">点击上传</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div>
              </template>
            </el-upload>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createSecondHandItem, getSecondHandItemDetail, changeSecondHandItem } from '@/api/secondHand'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const fileList = ref([])

// 判断是编辑模式还是发布模式
const isEditMode = computed(() => {
  return route.params.id !== 'new'
})

// 表单数据
const formData = reactive({
  title: '',
  price: 0,
  originalPrice: null,
  categoryId: null,
  condition: null,
  school: 'XX学校',
  location: '',
  description: ''
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入商品标题', trigger: 'blur' },
    { min: 1, max: 50, message: '标题长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', message: '请输入有效数字', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  condition: [
    { required: true, message: '请选择成色', trigger: 'change' }
  ],
  school: [
    { required: true, message: '请输入学校名称', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入具体位置', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' }
  ]
}

// 处理图片预览
const handlePreview = (file) => {
  console.log(file)
}

// 处理图片移除
const handleRemove = (file, fileList) => {
  console.log(file, fileList)
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    
    const submitData = {
      title: formData.title,
      description: formData.description,
      categoryId: formData.categoryId,
      price: formData.price,
      condition: formData.condition,
      school: formData.school,
      location: formData.location
    }
    
    if (formData.originalPrice) {
      submitData.originalPrice = formData.originalPrice
    }
    
    if (isEditMode.value) {
      // 编辑模式：使用changeSecondHandItem API
      submitData.id = route.params.id
      await changeSecondHandItem(submitData)
      ElMessage.success('编辑成功')
    } else {
      // 发布模式：使用createSecondHandItem API
      await createSecondHandItem(submitData)
      ElMessage.success('发布成功')
    }
    
    router.push('/user/user/publish')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(error.message || '提交失败，请重试')
  }
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  // 重置学校为默认值
  formData.school = 'XX学校'
}

// 取消编辑
const cancelEdit = () => {
  router.push('/user/user/publish')
}

// 加载数据
const loadData = async () => {
  // 只有在编辑模式下才加载数据
  if (isEditMode.value) {
    try {
      console.log('加载二手商品详情，ID:', route.params.id)
      const res = await getSecondHandItemDetail(route.params.id)
      console.log('商品详情数据:', res)
      
      if (res.data) {
        const data = res.data
        formData.title = data.title || ''
        formData.price = data.price || 0
        formData.originalPrice = data.originalPrice || null
        formData.categoryId = data.categoryId || null
        formData.condition = data.condition || null
        formData.school = data.school || 'XX学校'
        formData.location = data.location || ''
        formData.description = data.description || ''
        
        // 如果有图片，设置文件列表
        if (data.images && data.images.length > 0) {
          fileList.value = data.images.map((img, index) => ({
            name: `image${index}.jpg`,
            url: img
          }))
        }
      }
    } catch (error) {
      console.error('加载商品详情失败:', error)
      ElMessage.error('加载商品详情失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.second-hand-edit {
  min-height: 100vh;
  padding: 80px 0 20px;
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

.edit-form {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

@media (max-width: 768px) {
  .second-hand-edit {
    padding: 70px 0 20px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .edit-form {
    padding: 20px;
  }
}
</style>