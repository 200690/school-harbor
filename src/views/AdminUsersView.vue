<template>
  <div class="admin-users-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">用户管理</h2>
        <p class="page-subtitle">管理平台用户，包括封禁和解封用户</p>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchUserName"
          placeholder="请输入用户名"
          class="search-input"
          clearable
          @keyup.enter="fetchUsers"
        />
        <el-input
          v-model="searchPhone"
          placeholder="请输入手机号"
          class="search-input"
          clearable
          @keyup.enter="fetchUsers"
        />
        <el-select v-model="userRole" placeholder="选择用户角色" class="role-select" clearable>
          <el-option label="全部" value="" />
          <el-option label="普通用户" value="user" />
          <el-option label="管理员" value="admin" />
        </el-select>
        <el-select v-model="userStatus" placeholder="选择用户状态" class="status-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="正常" :value="1" />
          <el-option label="冻结" :value="0" />
          <el-option label="拉黑" :value="2" />
        </el-select>
        <el-button type="primary" @click="fetchUsers">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>

      <!-- 用户列表 -->
      <div class="user-list">
        <el-table :data="users" style="width: 100%">
          <el-table-column prop="id" label="用户ID" width="100" />
          <el-table-column prop="username" label="用户名" width="180" />
          <el-table-column prop="phone" label="手机号" width="180" />
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="gender" label="性别" width="80">
            <template #default="scope">
              {{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '未知' }}
            </template>
          </el-table-column>
          <el-table-column prop="creditScore" label="信誉分" width="100" />
          <el-table-column prop="role" label="角色" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.role === 'admin' ? 'primary' : 'success'">
                {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="注册时间" width="200" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewUser(scope.row)">查看</el-button>
              <el-button 
                :type="scope.row.status === 1 ? 'danger' : 'success'" 
                size="small" 
                @click="toggleBan(scope.row)"
              >
                {{ scope.row.status === 1 ? '封禁' : '解封' }}
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
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()

// 取消令牌
let cancelToken = null

// 搜索和筛选参数
const searchUserName = ref('')
const searchPhone = ref('')
const userRole = ref('')
const userStatus = ref(null)

// 状态类型映射
const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'danger'
    case 1:
      return 'success'
    case 2:
      return 'warning'
    default:
      return 'info'
  }
}

// 状态文本映射
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '冻结'
    case 1:
      return '正常'
    case 2:
      return '拉黑'
    default:
      return '未知'
  }
}

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 用户列表
const users = ref([])

// 获取用户列表
const fetchUsers = async () => {
  try {
    // 取消之前的请求
    if (cancelToken) {
      cancelToken.cancel()
    }
    // 创建新的取消令牌
    cancelToken = request.CancelToken.source()
    
    const response = await request.post('/user/admin/list', {
      userName: searchUserName.value || null,
      phone: searchPhone.value || null,
      role: userRole.value || null,
      status: userStatus.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }, {
      cancelToken: cancelToken.token
    })

    if (response.code === 1) {
      users.value = response.data?.list || []
      total.value = parseInt(response.data?.total || 0)
    } else {
      ElMessage.error(response.msg || '获取用户列表失败')
    }
  } catch (error) {
    // 忽略取消请求的错误
    if (!request.isCancel(error)) {
      console.error('获取用户列表失败:', error)
      ElMessage.error('获取用户列表失败')
    }
  }
}

// 查看用户详情
const viewUser = (user) => {
  // 跳转到商家主页
  router.push(`/user/profile/${user.id}`)
}

// 切换用户封禁状态
const toggleBan = async (user) => {
  try {
    const action = user.status === 1 ? 'ban' : 'unban'
    const response = await request.post(`/user/admin/${action}/${user.id}`)

    if (response.code === 1) {
      ElMessage.success(user.status === 1 ? '用户已封禁' : '用户已解封')
      // 重新获取用户列表
      fetchUsers()
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchUsers()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchUsers()
}

onMounted(() => {
  fetchUsers()
})

onUnmounted(() => {
  // 组件卸载时取消请求
  if (cancelToken) {
    cancelToken.cancel()
  }
})
</script>

<style scoped lang="scss">
.admin-users-page {
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

  .role-select,
  .status-select {
    width: 150px;
  }
}

.user-list {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .admin-users-page {
    padding: 60px 0 20px;
  }

  .search-filter {
    flex-direction: column;
    align-items: stretch;

    .search-input,
    .role-select,
    .status-select {
      width: 100%;
    }
  }

  .user-list {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }
}
</style>