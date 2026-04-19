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
          placeholder="搜索商品标题"
          class="search-input"
          clearable
          @keyup.enter="fetchItems"
        />
        <el-select v-model="itemCondition" placeholder="选择成色" class="condition-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="全新" :value="1" />
          <el-option label="99新" :value="2" />
          <el-option label="95新" :value="3" />
          <el-option label="9成新" :value="4" />
          <el-option label="8成新" :value="5" />
          <el-option label="7成新及以下" :value="6" />
        </el-select>
        <el-select v-model="itemStatus" placeholder="选择商品状态" class="status-select" clearable>
          <el-option label="全部" :value="null" />
          <el-option label="下架" :value="0" />
          <el-option label="上架" :value="1" />
          <el-option label="交易中" :value="2" />
          <el-option label="交易完成" :value="3" />
          <el-option label="交易取消" :value="4" />
        </el-select>
        <el-button type="primary" @click="fetchItems">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>

      <!-- 商品列表 -->
      <div class="item-list">
        <el-table :data="items" style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="商品ID" width="80" />
          <el-table-column label="封面" width="80">
            <template #default="scope">
              <el-image
                v-if="scope.row.coverImage"
                :src="scope.row.coverImage"
                fit="cover"
                style="width: 50px; height: 50px; border-radius: 4px;"
                :preview-src-list="[scope.row.coverImage]"
              />
              <span v-else class="no-image">无图片</span>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
          <el-table-column prop="sellerName" label="卖家" width="100" show-overflow-tooltip />
          <el-table-column prop="school" label="学校" width="120" show-overflow-tooltip />
          <el-table-column label="价格" width="100">
            <template #default="scope">
              <span class="price">¥{{ scope.row.price?.toFixed(2) || '0.00' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="成色" width="100">
            <template #default="scope">
              <el-tag :type="getConditionType(scope.row.condition)" size="small">
                {{ getConditionText(scope.row.condition) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" width="160" />
          <el-table-column label="统计" width="120">
            <template #default="scope">
              <div class="stats">
                <span title="浏览量"><el-icon><View /></el-icon> {{ scope.row.viewCount || 0 }}</span>
                <span title="收藏数"><el-icon><Star /></el-icon> {{ scope.row.favoriteCount || 0 }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button type="primary" size="small" @click="viewItem(scope.row)">查看</el-button>
                <el-button
                  :type="getActionButtonType(scope.row.status)"
                  size="small"
                  :disabled="scope.row.status === 2 || scope.row.status === 3 || scope.row.status === 4"
                  @click="handleStatusAction(scope.row)"
                >
                  {{ getActionButtonText(scope.row.status) }}
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="deleteItem(scope.row)"
                >
                  删除
                </el-button>
              </div>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, View, Star } from '@element-plus/icons-vue'
import { getAllSecondHandItems, updateSecondHandStatus, deleteSecondHandItem } from '@/api/secondHand'

const router = useRouter()

// 搜索和筛选参数
const searchKeyword = ref('')
const itemCondition = ref(null)
const itemStatus = ref(null)

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载状态
const loading = ref(false)

// 商品列表
const items = ref([])

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'primary'
    case 4: return 'danger'
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0: return '下架'
    case 1: return '上架'
    case 2: return '交易中'
    case 3: return '交易完成'
    case 4: return '交易取消'
    default: return '未知状态'
  }
}

// 获取成色类型
const getConditionType = (condition) => {
  switch (condition) {
    case 1: return 'success'
    case 2: return 'primary'
    case 3: return 'primary'
    case 4: return 'warning'
    case 5: return 'warning'
    case 6: return 'danger'
    default: return 'info'
  }
}

// 获取成色文本
const getConditionText = (condition) => {
  switch (condition) {
    case 1: return '全新'
    case 2: return '99新'
    case 3: return '95新'
    case 4: return '9成新'
    case 5: return '8成新'
    case 6: return '7成新及以下'
    default: return '未知'
  }
}

// 获取操作按钮类型
const getActionButtonType = (status) => {
  switch (status) {
    case 0: return 'success'
    case 1: return 'warning'
    case 2: return 'info'
    case 3: return 'info'
    case 4: return 'info'
    default: return 'info'
  }
}

// 获取操作按钮文本
const getActionButtonText = (status) => {
  switch (status) {
    case 0: return '上架'
    case 1: return '下架'
    case 2: return '交易中'
    case 3: return '完成'
    case 4: return '取消'
    default: return '未知'
  }
}

// 处理状态操作
const handleStatusAction = (item) => {
  if (item.status === 0) {
    toggleStatus(item, 1)
  } else if (item.status === 1) {
    toggleStatus(item, 0)
  }
}

// 获取商品列表
const fetchItems = async () => {
  loading.value = true
  try {
    const data = {
      title: searchKeyword.value || null,
      condition: itemCondition.value,
      status: itemStatus.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    
    const response = await getAllSecondHandItems(data)

    if (response.code === 1) {
      items.value = response.data.list || []
      total.value = parseInt(response.data.total) || 0
    } else {
      ElMessage.error(response.msg || '获取商品列表失败')
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 查看商品详情
const viewItem = (item) => {
  // 跳转到商品详情页面
  router.push(`/second-hand/detail/${item.id}`)
}

// 切换商品状态
const toggleStatus = async (item, newStatus) => {
  try {
    const response = await updateSecondHandStatus(item.id, newStatus)

    if (response.code === 1) {
      ElMessage.success(newStatus === 1 ? '商品已上架' : '商品已下架')
      fetchItems()
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除商品
const deleteItem = async (item) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品 "${item.title}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteSecondHandItem(item.id)

    if (response.code === 1) {
      ElMessage.success('商品删除成功')
      fetchItems()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
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

  .condition-select,
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

  .no-image {
    color: #999;
    font-size: 12px;
  }

  .price {
    color: #f56c6c;
    font-weight: 600;
  }

  .action-buttons {
    display: flex;
    flex-wrap: nowrap;
    gap: 8px;
    white-space: nowrap;
  }
}

.stats {
  display: flex;
  gap: 12px;
  color: #666;
  font-size: 13px;
  
  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
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