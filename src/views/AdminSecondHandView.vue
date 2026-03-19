<template>
  <div class="admin-second-hand-page">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/admin">管理员</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>二手交易管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">二手交易管理</h2>
        <p class="page-subtitle">管理二手交易信息，包括发布和审核</p>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品标题或描述"
          class="search-input"
          @keyup.enter="fetchItems"
        >
          <template #append>
            <el-button @click="fetchItems"><i class="el-icon-search"></i></el-button>
          </template>
        </el-input>
        <el-select v-model="itemStatus" placeholder="选择商品状态" class="status-select">
          <el-option label="全部" value="" />
          <el-option label="待审核" value="pending" />
          <el-option label="已发布" value="published" />
          <el-option label="已下架" value="unpublished" />
          <el-option label="已售出" value="sold" />
        </el-select>
      </div>

      <!-- 商品列表 -->
      <div class="item-list">
        <el-table :data="items" style="width: 100%">
          <el-table-column prop="id" label="商品ID" width="100" />
          <el-table-column prop="title" label="标题" min-width="200" />
          <el-table-column prop="sellerName" label="卖家" width="150" />
          <el-table-column prop="price" label="价格" width="120" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="发布时间" width="200" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewItem(scope.row)">查看</el-button>
              <el-button 
                v-if="scope.row.status !== 'sold'" 
                :type="scope.row.status === 'published' ? 'warning' : 'success'" 
                size="small" 
                @click="toggleStatus(scope.row)"
              >
                {{ scope.row.status === 'published' ? '下架' : '发布' }}
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()

// 搜索和筛选参数
const searchKeyword = ref('')
const itemStatus = ref('')

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 商品列表
const items = ref([])

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 'pending': return 'info'
    case 'published': return 'success'
    case 'unpublished': return 'danger'
    case 'sold': return 'warning'
    default: return 'warning'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'pending': return '待审核'
    case 'published': return '已发布'
    case 'unpublished': return '已下架'
    case 'sold': return '已售出'
    default: return '未知状态'
  }
}

// 获取商品列表
const fetchItems = async () => {
  try {
    const response = await request.get('/admin/second-hand', {
      params: {
        keyword: searchKeyword.value,
        status: itemStatus.value,
        page: currentPage.value,
        size: pageSize.value
      }
    })

    if (response.code === 1) {
      items.value = response.data.list || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.msg || '获取商品列表失败')
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  }
}

// 查看商品详情
const viewItem = (item) => {
  // 跳转到商品详情页面
  router.push(`/second-hand/detail/${item.id}`)
}

// 切换商品状态
const toggleStatus = async (item) => {
  try {
    const action = item.status === 'published' ? 'unpublish' : 'publish'
    const response = await request.post(`/admin/second-hand/${action}/${item.id}`)

    if (response.code === 1) {
      ElMessage.success(item.status === 'published' ? '商品已下架' : '商品已发布')
      // 重新获取商品列表
      fetchItems()
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
  fetchItems()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchItems()
}

onMounted(() => {
  fetchItems()
})
</script>

<style scoped lang="scss">
.admin-second-hand-page {
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

  .status-select {
    width: 150px;
  }
}

.item-list {
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
  .admin-second-hand-page {
    padding: 60px 0 20px;
  }

  .search-filter {
    flex-direction: column;
    align-items: stretch;

    .search-input,
    .status-select {
      width: 100%;
    }
  }

  .item-list {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }
}
</style>