<template>
  <div class="admin-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>管理员</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">管理员控制台</h2>
        <p class="page-subtitle">欢迎回来，{{ username }} 管理员</p>
      </div>

      <!-- 管理员功能卡片 -->
      <div class="admin-cards">
        <div class="admin-card" @click="goToUsers">
          <div class="card-icon user-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="card-content">
            <h3 class="card-title">用户管理</h3>
            <p class="card-desc">管理平台用户，包括封禁和解封用户</p>
          </div>
        </div>

        <div class="admin-card" @click="goToPartTime">
          <div class="card-icon part-time-icon">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="card-content">
            <h3 class="card-title">兼职管理</h3>
            <p class="card-desc">管理兼职信息，包括发布和审核</p>
          </div>
        </div>

        <div class="admin-card" @click="goToSecondHand">
          <div class="card-icon second-hand-icon">
            <i class="el-icon-goods"></i>
          </div>
          <div class="card-content">
            <h3 class="card-title">二手交易管理</h3>
            <p class="card-desc">管理二手交易信息，包括发布和审核</p>
          </div>
        </div>

        <div class="admin-card" @click="goToReports">
          <div class="card-icon report-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="card-content">
            <h3 class="card-title">举报管理</h3>
            <p class="card-desc">处理用户举报，维护平台秩序</p>
          </div>
        </div>


      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const username = ref('')

// 获取用户名
const getUserInfo = () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  username.value = userInfo.username || '管理员'
  
  // 检查是否是管理员
  if (userInfo.role !== 'admin') {
    ElMessage.error('您没有管理员权限')
    setTimeout(() => {
      router.push('/')
    }, 1500)
  }
}

// 跳转到用户管理
const goToUsers = () => {
  router.push('/admin/users')
}

// 跳转到兼职管理
const goToPartTime = () => {
  router.push('/admin/part-time')
}

// 跳转到二手交易管理
const goToSecondHand = () => {
  router.push('/admin/second-hand')
}

// 跳转到举报管理
const goToReports = () => {
  router.push('/admin/reports')
}



onMounted(() => {
  getUserInfo()
})
</script>

<style scoped lang="scss">
.admin-page {
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

.admin-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.admin-card {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 20px;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  }

  .card-icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: #fff;

    &.user-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.part-time-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.second-hand-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.report-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
  }

  .card-content {
    flex: 1;

    .card-title {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      margin-bottom: 8px;
    }

    .card-desc {
      font-size: 14px;
      color: #666;
      line-height: 1.5;
    }
  }
}

@media (max-width: 768px) {
  .admin-page {
    padding: 60px 0 20px;
  }

  .admin-cards {
    grid-template-columns: 1fr;
  }

  .page-title {
    font-size: 20px;
  }

  .admin-card {
    padding: 20px;
  }
}
</style>