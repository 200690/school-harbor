<template>
  <div class="part-time-edit">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/part-time">兼职</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>编辑兼职</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">编辑兼职</h2>
      </div>

      <!-- 编辑表单 -->
      <div class="edit-form">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入兼职标题" />
          </el-form-item>

          <el-form-item label="薪资" prop="salary">
            <el-input v-model="formData.salary" placeholder="请输入薪资，如：15元/小时" />
          </el-form-item>

          <el-form-item label="类型" prop="type">
            <el-select v-model="formData.type" placeholder="请选择兼职类型">
              <el-option label="校内兼职" value="campus" />
              <el-option label="校外兼职" value="off-campus" />
              <el-option label="实习" value="internship" />
            </el-select>
          </el-form-item>

          <el-form-item label="工作地点" prop="location">
            <el-input v-model="formData.location" placeholder="请输入工作地点" />
          </el-form-item>

          <el-form-item label="工作时间" prop="workTime">
            <el-input v-model="formData.workTime" placeholder="请输入工作时间，如：周末 9:00-17:00" />
          </el-form-item>

          <el-form-item label="描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入兼职描述"
            />
          </el-form-item>

          <el-form-item label="联系人" prop="contact">
            <el-input v-model="formData.contact" placeholder="请输入联系人姓名" />
          </el-form-item>

          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入联系电话" />
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

// 表单数据
const formData = reactive({
  title: '',
  salary: '',
  type: '',
  location: '',
  workTime: '',
  description: '',
  contact: '',
  phone: ''
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入兼职标题', trigger: 'blur' },
    { min: 1, max: 50, message: '标题长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  salary: [
    { required: true, message: '请输入薪资', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择兼职类型', trigger: 'change' }
  ],
  location: [
    { required: true, message: '请输入工作地点', trigger: 'blur' }
  ],
  workTime: [
    { required: true, message: '请输入工作时间', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入兼职描述', trigger: 'blur' },
    { min: 10, message: '描述长度至少 10 个字符', trigger: 'blur' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
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
    formData.title = '校园超市收银员兼职'
    formData.salary = '15元/小时'
    formData.type = 'campus'
    formData.location = '校园超市'
    formData.workTime = '周末 9:00-17:00'
    formData.description = '负责校园超市收银工作，要求工作认真负责，有良好的服务态度。'
    formData.contact = '张经理'
    formData.phone = '13800138000'
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.part-time-edit {
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
  .part-time-edit {
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