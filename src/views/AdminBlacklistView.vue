<template>
  <div class="admin-blacklist-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>黑名单管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">黑名单管理</h2>
        <p class="page-subtitle">管理平台黑名单，包括用户和物品</p>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab">
        <el-tab-pane label="用户黑名单" name="users">
          <!-- 用户黑名单列表 -->
          <div class="blacklist-section">
            <div class="section-header">
              <h3 class="section-title">用户黑名单</h3>
              <el-button type="primary" @click="addUserToBlacklist">添加用户到黑名单</el-button>
            </div>
            
            <div class="search-box">
              <el-input
                v-model="userSearchKeyword"
                placeholder="搜索用户ID、用户名或手机号"
                class="search-input"
                @keyup.enter="fetchUserBlacklist"
              >
                <template #append>
                  <el-button @click="fetchUserBlacklist"><i class="el-icon-search"></i></el-button>
                </template>
              </el-input>
            </div>

            <div class="blacklist-list">
              <el-table :data="userBlacklist" style="width: 100%">
                <el-table-column prop="userId" label="用户ID" width="100" />
                <el-table-column prop="username" label="用户名" width="150" />
                <el-table-column prop="phone" label="手机号" width="180" />
                <el-table-column prop="reason" label="拉黑原因" min-width="200" />
                <el-table-column prop="createdAt" label="拉黑时间" width="200" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template #default="scope">
                    <el-button type="danger" size="small" @click="removeUserFromBlacklist(scope.row)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                v-model:current-page="userCurrentPage"
                v-model:page-size="userPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="userTotal"
                @size-change="handleUserSizeChange"
                @current-change="handleUserCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="物品黑名单" name="items">
          <!-- 物品黑名单列表 -->
          <div class="blacklist-section">
            <div class="section-header">
              <h3 class="section-title">物品黑名单</h3>
              <el-button type="primary" @click="addItemToBlacklist">添加物品到黑名单</el-button>
            </div>
            
            <div class="search-box">
              <el-input
                v-model="itemSearchKeyword"
                placeholder="搜索物品ID或标题"
                class="search-input"
                @keyup.enter="fetchItemBlacklist"
              >
                <template #append>
                  <el-button @click="fetchItemBlacklist"><i class="el-icon-search"></i></el-button>
                </template>
              </el-input>
            </div>

            <div class="blacklist-list">
              <el-table :data="itemBlacklist" style="width: 100%">
                <el-table-column prop="itemId" label="物品ID" width="100" />
                <el-table-column prop="title" label="物品标题" min-width="200" />
                <el-table-column prop="sellerName" label="卖家" width="150" />
                <el-table-column prop="reason" label="拉黑原因" min-width="200" />
                <el-table-column prop="createdAt" label="拉黑时间" width="200" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template #default="scope">
                    <el-button type="danger" size="small" @click="removeItemFromBlacklist(scope.row)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                v-model:current-page="itemCurrentPage"
                v-model:page-size="itemPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="itemTotal"
                @size-change="handleItemSizeChange"
                @current-change="handleItemCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// 标签页
const activeTab = ref('users')

// 用户黑名单
const userSearchKeyword = ref('')
const userCurrentPage = ref(1)
const userPageSize = ref(10)
const userTotal = ref(0)
const userBlacklist = ref([])

// 物品黑名单
const itemSearchKeyword = ref('')
const itemCurrentPage = ref(1)
const itemPageSize = ref(10)
const itemTotal = ref(0)
const itemBlacklist = ref([])

// 获取用户黑名单
const fetchUserBlacklist = async () => {
  try {
    const response = await request.get('/admin/blacklist/users', {
      params: {
        keyword: userSearchKeyword.value,
        page: userCurrentPage.value,
        size: userPageSize.value
      }
    })

    if (response.code === 1) {
      userBlacklist.value = response.data.list || []
      userTotal.value = response.data.total || 0
    } else {
      ElMessage.error(response.msg || '获取用户黑名单失败')
    }
  } catch (error) {
    console.error('获取用户黑名单失败:', error)
    ElMessage.error('获取用户黑名单失败')
  }
}

// 获取物品黑名单
const fetchItemBlacklist = async () => {
  try {
    const response = await request.get('/admin/blacklist/items', {
      params: {
        keyword: itemSearchKeyword.value,
        page: itemCurrentPage.value,
        size: itemPageSize.value
      }
    })

    if (response.code === 1) {
      itemBlacklist.value = response.data.list || []
      itemTotal.value = response.data.total || 0
    } else {
      ElMessage.error(response.msg || '获取物品黑名单失败')
    }
  } catch (error) {
    console.error('获取物品黑名单失败:', error)
    ElMessage.error('获取物品黑名单失败')
  }
}

// 从黑名单移除用户
const removeUserFromBlacklist = async (user) => {
  try {
    const response = await request.post(`/admin/blacklist/user/remove/${user.userId}`)

    if (response.code === 1) {
      ElMessage.success('用户已从黑名单移除')
      // 重新获取用户黑名单
      fetchUserBlacklist()
    } else {
      ElMessage.error(response.msg || '移除失败')
    }
  } catch (error) {
    console.error('移除失败:', error)
    ElMessage.error('移除失败')
  }
}

// 从黑名单移除物品
const removeItemFromBlacklist = async (item) => {
  try {
    const response = await request.post(`/admin/blacklist/item/remove/${item.itemId}`)

    if (response.code === 1) {
      ElMessage.success('物品已从黑名单移除')
      // 重新获取物品黑名单
      fetchItemBlacklist()
    } else {
      ElMessage.error(response.msg || '移除失败')
    }
  } catch (error) {
    console.error('移除失败:', error)
    ElMessage.error('移除失败')
  }
}

// 添加用户到黑名单
const addUserToBlacklist = () => {
  // 这里可以打开添加用户到黑名单的对话框
  ElMessage.info('添加用户到黑名单功能开发中')
}

// 添加物品到黑名单
const addItemToBlacklist = () => {
  // 这里可以打开添加物品到黑名单的对话框
  ElMessage.info('添加物品到黑名单功能开发中')
}

// 用户分页处理
const handleUserSizeChange = (size) => {
  userPageSize.value = size
  userCurrentPage.value = 1
  fetchUserBlacklist()
}

const handleUserCurrentChange = (current) => {
  userCurrentPage.value = current
  fetchUserBlacklist()
}

// 物品分页处理
const handleItemSizeChange = (size) => {
  itemPageSize.value = size
  itemCurrentPage.value = 1
  fetchItemBlacklist()
}

const handleItemCurrentChange = (current) => {
  itemCurrentPage.value = current
  fetchItemBlacklist()
}

// 监听标签页变化
watch(activeTab, (newTab) => {
  if (newTab === 'users') {
    fetchUserBlacklist()
  } else {
    fetchItemBlacklist()
  }
})

onMounted(() => {
  fetchUserBlacklist()
})
</script>

<style scoped lang="scss">
.admin-blacklist-page {
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

.blacklist-section {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
  }
}

.search-box {
  margin-bottom: 20px;

  .search-input {
    width: 100%;
  }
}

.blacklist-list {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .admin-blacklist-page {
    padding: 60px 0 20px;
  }

  .blacklist-section {
    padding: 10px;
  }

  .section-header {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;

    .section-title {
      font-size: 16px;
    }
  }

  .page-title {
    font-size: 20px;
  }
}
</style>