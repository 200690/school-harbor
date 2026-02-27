<template>
  <div class="user-blacklist">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>黑名单</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">黑名单</h2>
      </div>

      <!-- 黑名单标签页 -->
      <el-tabs v-model="activeTab" class="blacklist-tabs" @tab-click="handleTabClick">
        <!-- 用户黑名单 -->
        <el-tab-pane label="用户黑名单" name="user">
          <div class="blacklist-list">
            <div v-if="userBlacklist.length === 0" class="empty-state">
              <el-empty description="暂无拉黑的用户" />
            </div>
            <div v-else class="blacklist-items">
              <div v-for="block in userBlacklist" :key="block.id" class="blacklist-item">
                <div class="user-avatar">
                  <img :src="block.avatar" :alt="block.username" />
                </div>
                <div class="user-info">
                  <h3 class="user-name">{{ block.username }}</h3>
                  <p class="block-time">拉黑时间：{{ block.blockTime }}</p>
                </div>
                <div class="user-actions">
                  <button class="btn btn-success" @click="unblockUser(block.blockId)">
                    取消拉黑
                  </button>
                </div>
              </div>
              <!-- 分页 -->
              <div class="pagination">
                <el-pagination
                  @size-change="handleUserSizeChange"
                  @current-change="handleUserCurrentChange"
                  :current-page="userCurrentPage"
                  :page-sizes="[5, 10, 20]"
                  :page-size="userPageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalUserBlacklist"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <!-- 商品黑名单 -->
        <el-tab-pane label="商品黑名单" name="item">
          <div class="blacklist-list">
            <div v-if="itemBlacklist.length === 0" class="empty-state">
              <el-empty description="暂无拉黑的商品" />
            </div>
            <div v-else class="blacklist-items">
              <div v-for="block in itemBlacklist" :key="block.id" class="blacklist-item">
                <div class="item-image">
                  <img :src="block.coverImage" :alt="block.title" />
                </div>
                <div class="item-info">
                  <h3 class="item-title">{{ block.title }}</h3>
                  <p class="item-price">价格：¥{{ block.price }}</p>
                  <p class="item-description">{{ block.description }}</p>
                  <p class="block-time">拉黑时间：{{ block.blockTime }}</p>
                </div>
                <div class="item-actions">
                  <button class="btn btn-success" @click="unblockItem(block.productId)">
                    取消拉黑
                  </button>
                </div>
              </div>
              <!-- 分页 -->
              <div class="pagination">
                <el-pagination
                  @size-change="handleItemSizeChange"
                  @current-change="handleItemCurrentChange"
                  :current-page="itemCurrentPage"
                  :page-sizes="[5, 10, 20]"
                  :page-size="itemPageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalItemBlacklist"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserBlacklist, getItemBlacklist, unblockUser as unblockUserApi, unblockItem as unblockItemApi } from '@/api/user'

const activeTab = ref('user')

// 用户黑名单分页
const userCurrentPage = ref(1)
const userPageSize = ref(10)
const userBlacklist = ref([])
const totalUserBlacklist = ref(0)

// 商品黑名单分页
const itemCurrentPage = ref(1)
const itemPageSize = ref(10)
const itemBlacklist = ref([])
const totalItemBlacklist = ref(0)

// 取消拉黑用户
const unblockUser = async (blockId) => {
  try {
    await unblockUserApi(blockId)
    await fetchUserBlacklist()
    ElMessage.success('取消拉黑成功')
  } catch (error) {
    console.error('取消拉黑失败:', error)
    ElMessage.error('取消拉黑失败')
  }
}

// 取消拉黑商品
const unblockItem = async (itemId) => {
  try {
    await unblockItemApi(itemId)
    await fetchItemBlacklist()
    ElMessage.success('取消拉黑成功')
  } catch (error) {
    console.error('取消拉黑失败:', error)
    ElMessage.error('取消拉黑失败')
  }
}

// 分页处理 - 用户黑名单
const handleUserSizeChange = (size) => {
  userPageSize.value = size
  userCurrentPage.value = 1
  fetchUserBlacklist()
}

const handleUserCurrentChange = (current) => {
  userCurrentPage.value = current
  fetchUserBlacklist()
}

// 分页处理 - 商品黑名单
const handleItemSizeChange = (size) => {
  itemPageSize.value = size
  itemCurrentPage.value = 1
  fetchItemBlacklist()
}

const handleItemCurrentChange = (current) => {
  itemCurrentPage.value = current
  fetchItemBlacklist()
}

// 标签页切换
const handleTabClick = (tab) => {
  if (tab.props.name === 'user') {
    fetchUserBlacklist()
  } else if (tab.props.name === 'item') {
    fetchItemBlacklist()
  }
}

// 获取用户黑名单
const fetchUserBlacklist = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取黑名单列表')
      return
    }
    
    const response = await getUserBlacklist({
      id: userId,
      pageNum: userCurrentPage.value,
      pageSize: userPageSize.value
    })
    
    userBlacklist.value = response.data?.list || []
    totalUserBlacklist.value = parseInt(response.data?.total) || 0
  } catch (error) {
    console.error('获取用户黑名单失败:', error)
    ElMessage.error('获取用户黑名单失败，请稍后重试')
  }
}

// 获取商品黑名单
const fetchItemBlacklist = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取黑名单列表')
      return
    }
    
    const response = await getItemBlacklist({
      id: userId,
      pageNum: itemCurrentPage.value,
      pageSize: itemPageSize.value
    })
    
    itemBlacklist.value = response.data?.list || []
    totalItemBlacklist.value = parseInt(response.data?.total) || 0
  } catch (error) {
    console.error('获取商品黑名单失败:', error)
    ElMessage.error('获取商品黑名单失败，请稍后重试')
  }
}

onMounted(async () => {
  await fetchUserBlacklist()
})
</script>

<style scoped lang="scss">
.user-blacklist {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
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

.blacklist-list {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.blacklist-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.blacklist-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  transition: all 0.3s;
  background-color: #fef2f2;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.block-time {
  font-size: 12px;
  color: #999;
}

.user-actions {
  display: flex;
  gap: 10px;
}

.item-actions {
  display: flex;
  gap: 10px;
}

.item-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.item-price {
  font-size: 14px;
  color: #f56c6c;
  font-weight: 500;
  margin-bottom: 4px;
}

.item-description {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.blacklist-tabs {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .blacklist-list {
    padding: 15px;
  }
  
  .blacklist-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .user-actions,
  .item-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .pagination {
    margin-top: 20px;
  }
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 按钮样式 */
.btn {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  outline: none;
}

.btn-success {
  background-color: #67C23A;
  color: #fff;
  
  &:hover {
    background-color: #85ce61;
  }
}
</style>