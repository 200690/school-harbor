<template>
  <div class="part-time-edit">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/part-time">兼职</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>{{ isEditMode ? '编辑兼职' : '发布兼职' }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">{{ isEditMode ? '编辑兼职' : '发布兼职' }}</h2>
      </div>

      <!-- 编辑表单 -->
      <div class="edit-form">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
          <el-form-item label="兼职标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入兼职标题" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="招聘方" prop="employer">
            <el-input v-model="formData.employer" placeholder="请输入招聘方名称" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="工作地点" prop="location">
            <el-input v-model="formData.location" placeholder="请输入工作地点" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="工作时间" prop="workTime">
            <el-input v-model="formData.workTime" placeholder="请输入工作时间，如：周末 9:00-17:00" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="薪资单位" prop="salaryUnit">
            <el-input v-model="formData.salaryUnit" placeholder="请输入薪资单位，如：元/小时" maxlength="20" show-word-limit />
          </el-form-item>

          <el-form-item label="薪资说明" prop="salaryDesc">
            <el-input v-model="formData.salaryDesc" placeholder="请输入薪资说明，如：15-20" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="兼职类型" prop="type">
            <el-select v-model="formData.type" placeholder="请选择兼职类型">
              <el-option label="校内兼职" :value="1" />
              <el-option label="校外兼职" :value="2" />
              <el-option label="实习" :value="3" />
            </el-select>
          </el-form-item>

          <el-form-item label="兼职描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入兼职描述"
            />
          </el-form-item>

          <el-form-item label="招聘要求" prop="requirements">
            <el-input
              v-model="formData.requirements"
              type="textarea"
              :rows="4"
              placeholder="请输入招聘要求"
            />
          </el-form-item>

          <el-form-item label="联系人" prop="contactPerson">
            <el-input v-model="formData.contactPerson" placeholder="请输入联系人姓名" maxlength="50" show-word-limit />
          </el-form-item>

          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
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
import { publishPartTimeJob, getPartTimeJobDetail, changePartTimeJob } from '@/api/partTime'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)

// 判断是编辑模式还是发布模式
const isEditMode = computed(() => {
  return route.params.id !== 'new'
})

// 表单数据
const formData = reactive({
  title: '',
  employer: '',
  location: '',
  workTime: '',
  salaryUnit: '',
  salaryDesc: '',
  type: null,
  description: '',
  requirements: '',
  contactPerson: '',
  contactPhone: ''
})

const rules = {
  title: [
    { required: true, message: '兼职标题不能为空', trigger: 'blur' },
    { max: 100, message: '兼职标题不能超过100个字符', trigger: 'blur' }
  ],
  employer: [
    { required: true, message: '招聘方不能为空', trigger: 'blur' },
    { max: 100, message: '招聘方不能超过100个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '工作地点不能为空', trigger: 'blur' },
    { max: 100, message: '工作地点不能超过100个字符', trigger: 'blur' }
  ],
  workTime: [
    { required: true, message: '工作时间不能为空', trigger: 'blur' },
    { max: 100, message: '工作时间不能超过100个字符', trigger: 'blur' }
  ],
  salaryUnit: [
    { max: 20, message: '薪资单位不能超过20个字符', trigger: 'blur' }
  ],
  salaryDesc: [
    { max: 100, message: '薪资说明不能超过100个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '兼职类型不能为空', trigger: 'change' },
    { type: 'number', min: 1, max: 3, message: '兼职类型值错误', trigger: 'change' }
  ],
  description: [
    { required: true, message: '兼职描述不能为空', trigger: 'blur' }
  ],
  requirements: [
    { required: true, message: '招聘要求不能为空', trigger: 'blur' }
  ],
  contactPerson: [
    { required: true, message: '联系人不能为空', trigger: 'blur' },
    { max: 50, message: '联系人不能超过50个字符', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '联系电话不能为空', trigger: 'blur' }
  ]
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    const requestData = {
      title: formData.title,
      employer: formData.employer,
      location: formData.location,
      workTime: formData.workTime,
      salaryUnit: formData.salaryUnit,
      salaryDesc: formData.salaryDesc,
      type: formData.type,
      description: formData.description,
      requirements: formData.requirements,
      contactPerson: formData.contactPerson,
      contactPhone: formData.contactPhone
    }
    
    if (isEditMode.value) {
      // 编辑模式：使用changePartTimeJob API
      requestData.id = route.params.id
      await changePartTimeJob(requestData)
      ElMessage.success('编辑成功')
    } else {
      // 发布模式：使用publishPartTimeJob API
      await publishPartTimeJob(requestData)
      ElMessage.success('发布成功')
    }
    
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
const loadData = async () => {
  if (isEditMode.value) {
    try {
      console.log('加载兼职详情，ID:', route.params.id)
      const res = await getPartTimeJobDetail(route.params.id)
      console.log('兼职详情数据:', res)
      
      if (res.data) {
        const data = res.data
        formData.title = data.title || ''
        formData.employer = data.employer || ''
        formData.location = data.location || ''
        formData.workTime = data.workTime || ''
        formData.salaryUnit = data.salaryUnit || ''
        formData.salaryDesc = data.salaryDesc || ''
        formData.type = data.type || null
        formData.description = data.description || ''
        formData.requirements = data.requirements || ''
        formData.contactPerson = data.contactPerson || ''
        formData.contactPhone = data.contactPhone || ''
      }
    } catch (error) {
      console.error('加载兼职详情失败:', error)
      ElMessage.error('加载兼职详情失败')
    }
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