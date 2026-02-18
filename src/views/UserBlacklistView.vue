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
      <el-tabs v-model="activeTab" class="blacklist-tabs">
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
                  <button class="btn btn-success" @click="unblockUser(block.userId)">
                    取消拉黑
                  </button>
                </div>
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
                  <img :src="block.itemImage" :alt="block.itemTitle" />
                </div>
                <div class="item-info">
                  <h3 class="item-title">{{ block.itemTitle }}</h3>
                  <p class="block-time">拉黑时间：{{ block.blockTime }}</p>
                </div>
                <div class="item-actions">
                  <button class="btn btn-success" @click="unblockItem(block.itemId)">
                    取消拉黑
                  </button>
                </div>
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
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const userBlacklist = ref([])
const itemBlacklist = ref([])
const activeTab = ref('user')

// 取消拉黑用户
const unblockUser = async (userId) => {
  try {
    await userStore.unblockUser(userId)
    userBlacklist.value = userStore.userBlacklist
    ElMessage.success('取消拉黑成功')
  } catch (error) {
    ElMessage.error('取消拉黑失败')
  }
}

// 取消拉黑商品
const unblockItem = async (itemId) => {
  try {
    await userStore.unblockItem(itemId)
    itemBlacklist.value = userStore.itemBlacklist
    ElMessage.success('取消拉黑成功')
  } catch (error) {
    ElMessage.error('取消拉黑失败')
  }
}

onMounted(async () => {
  // 获取黑名单列表
  await userStore.getUserBlacklist()
  await userStore.getItemBlacklist()
  userBlacklist.value = userStore.userBlacklist
  itemBlacklist.value = userStore.itemBlacklist
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