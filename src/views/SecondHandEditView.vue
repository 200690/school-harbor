<template>
  <div class="second-hand-edit">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>编辑二手交易</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">编辑二手交易</h2>
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

          <el-form-item label="分类" prop="category">
            <el-select v-model="formData.category" placeholder="请选择分类">
              <el-option label="教材教辅" value="textbook" />
              <el-option label="电子产品" value="electronics" />
              <el-option label="生活用品" value="life" />
              <el-option label="运动器材" value="sports" />
              <el-option label="其他" value="other" />
            </el-select>
          </el-form-item>

          <el-form-item label="成色" prop="condition">
            <el-select v-model="formData.condition" placeholder="请选择成色">
              <el-option label="全新" value="new" />
              <el-option label="九成新" value="90%" />
              <el-option label="八成新" value="80%" />
              <el-option label="七成新及以下" value="<70%" />
            </el-select>
          </el-form-item>

          <el-form-item label="交易地点" prop="location">
            <el-input v-model="formData.location" placeholder="请输入交易地点" />
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const fileList = ref([])

// 表单数据
const formData = reactive({
  title: '',
  price: 0,
  category: '',
  condition: '',
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
  category: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  condition: [
    { required: true, message: '请选择成色', trigger: 'change' }
  ],
  location: [
    { required: true, message: '请输入交易地点', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' },
    { min: 10, message: '描述长度至少 10 个字符', trigger: 'blur' }
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
    // 这里应该调用 API 提交数据
    ElMessage.success('编辑成功')
    router.push('/user/user/publish')
  } catch (error) {
    console.error('表单验证失败:', error)
    ElMessage.error('请检查表单数据')
  }
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
}

// 取消编辑
const cancelEdit = () => {
  router.push('/user/user/publish')
}

// 加载数据
const loadData = () => {
  const id = route.params.id
  // 这里应该调用 API 获取数据
  // 暂时使用模拟数据
  if (id) {
    // 模拟加载数据
    formData.title = '大学英语四级词汇书'
    formData.price = 25
    formData.category = 'textbook'
    formData.condition = '90%'
    formData.location = '学校图书馆'
    formData.description = '全新未使用的大学英语四级词汇书，包含光盘和练习册。'
    // 模拟文件列表
    fileList.value = [
      {
        name: 'book1.jpg',
        url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20college%20student&image_size=square'
      }
    ]
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