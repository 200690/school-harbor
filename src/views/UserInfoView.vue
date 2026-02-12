<template>
  <div class="user-info">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>编辑资料</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 编辑资料表单 -->
      <div class="info-card">
        <h2 class="page-title">编辑个人资料</h2>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
          <!-- 头像上传 -->
          <el-form-item label="头像">
            <div class="avatar-uploader">
              <el-avatar :size="100" :src="form.avatar">
                <img :src="form.avatar" :alt="form.username" />
              </el-avatar>
              <el-button type="primary" size="small" class="upload-btn">
                <i class="el-icon-upload"></i> 更换头像
              </el-button>
            </div>
          </el-form-item>

          <!-- 用户名 -->
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>

          <!-- 手机号 -->
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" disabled />
            <el-button type="text" size="small" class="change-btn">更换手机号</el-button>
          </el-form-item>

          <!-- 邮箱 -->
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>

          <!-- 性别 -->
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="form.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
              <el-radio label="保密">保密</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 个性签名 -->
          <el-form-item label="个性签名" prop="bio">
            <el-input
              v-model="form.bio"
              type="textarea"
              rows="3"
              placeholder="请输入个性签名"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>

          <!-- 注册时间 -->
          <el-form-item label="注册时间">
            <el-input v-model="form.registerTime" disabled />
          </el-form-item>

          <!-- 操作按钮 -->
          <el-form-item>
            <el-button type="primary" @click="submitForm">保存修改</el-button>
            <el-button @click="resetForm">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getUserInfo, updateUserInfo } from '@/api/user'

const router = useRouter()
const formRef = ref(null)

const form = reactive({
  username: '',
  phone: '',
  email: '',
  avatar: '',
  gender: '保密',
  bio: '',
  registerTime: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 准备更新数据
        const updateData = {
          phone: form.phone,
          email: form.email,
          avatar: form.avatar
        }
        
        await updateUserInfo(updateData)
        ElMessage.success('保存成功')
        router.push('/user/center')
      } catch (error) {
        console.error('更新用户信息失败:', error)
        ElMessage.error('保存失败，请重试')
      }
    } else {
      ElMessage.error('请检查表单填写是否正确')
      return false
    }
  })
}

const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  // 重新获取用户信息
  fetchUserInfo()
}

const fetchUserInfo = async () => {
  try {
    const response = await getUserInfo()
    const userInfo = response.data
    // 更新表单数据
    form.username = userInfo.username
    form.phone = userInfo.phone
    form.email = userInfo.email
    form.avatar = userInfo.avatar
    form.registerTime = userInfo.createTime
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

onMounted(async () => {
  // 获取用户信息
  await fetchUserInfo()
})
</script>

<style scoped lang="scss">
.user-info {
  min-height: 100vh;
  padding: 20px 0;
}

.breadcrumb {
  margin-bottom: 20px;
}

.info-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 30px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  gap: 20px;
}

.upload-btn {
  margin-left: 20px;
}

.change-btn {
  margin-left: 10px;
}

.el-form-item {
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .info-card {
    padding: 20px;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .avatar-uploader {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .upload-btn {
    margin-left: 0;
  }
}
</style>