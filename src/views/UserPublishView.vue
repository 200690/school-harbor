<template>
  <div class="user-publish">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>我的发布</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题和操作 -->
      <div class="page-header">
        <h2 class="page-title">我的发布</h2>
        <div class="page-actions">
          <el-button type="primary" @click="navigateToPublish('second-hand')">
            <i class="el-icon-s-goods"></i> 发布二手
          </el-button>
          <el-button type="primary" @click="navigateToPublish('part-time')">
            <i class="el-icon-s-order"></i> 发布兼职
          </el-button>
        </div>
      </div>

      <!-- 分类标签 -->
      <div class="category-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部" name="all">
            <!-- 全部发布 -->
            <div class="publish-list">
              <div v-if="allPosts.length === 0" class="empty-state">
                <el-empty description="暂无发布记录" />
              </div>
              <div v-else class="publish-items">
                <div v-for="post in allPosts" :key="post.id" class="publish-item">
                  <div class="item-header">
                    <el-tag :type="post.type === 'second-hand' ? 'info' : 'success'">
                      {{ post.type === 'second-hand' ? '二手交易' : '兼职' }}
                    </el-tag>
                    <span class="publish-time">{{ post.publishTime }}</span>
                  </div>
                  <h3 class="item-title">{{ post.title }}</h3>
                  <div class="item-content">
                    <div v-if="post.type === 'second-hand'" class="second-hand-info">
                      <span class="price">¥{{ post.price }}</span>
                      <span class="location"><i class="el-icon-location"></i> {{ post.location }}</span>
                      <span class="condition">{{ post.condition }}</span>
                    </div>
                    <div v-else class="part-time-info">
                      <span class="salary">{{ post.salary }}</span>
                      <span class="location"><i class="el-icon-location"></i> {{ post.location }}</span>
                      <span class="work-time">{{ post.workTime }}</span>
                    </div>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost()">
                        编辑
                      </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status === 'active' ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post.id)">
                      删除
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="二手交易" name="second-hand">
            <!-- 二手交易发布 -->
            <div class="publish-list">
              <div v-if="secondHandPosts.length === 0" class="empty-state">
                <el-empty description="暂无二手交易发布记录" />
              </div>
              <div v-else class="publish-items">
                <div v-for="post in secondHandPosts" :key="post.id" class="publish-item">
                  <div class="item-header">
                    <el-tag type="info">二手交易</el-tag>
                    <span class="publish-time">{{ post.publishTime }}</span>
                  </div>
                  <h3 class="item-title">{{ post.title }}</h3>
                  <div class="item-content">
                    <span class="price">¥{{ post.price }}</span>
                    <span class="location"><i class="el-icon-location"></i> {{ post.location }}</span>
                    <span class="condition">{{ post.condition }}</span>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost()">
                      编辑
                    </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status === 'active' ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post.id)">
                      删除
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="兼职" name="part-time">
            <!-- 兼职发布 -->
            <div class="publish-list">
              <div v-if="partTimePosts.length === 0" class="empty-state">
                <el-empty description="暂无兼职发布记录" />
              </div>
              <div v-else class="publish-items">
                <div v-for="post in partTimePosts" :key="post.id" class="publish-item">
                  <div class="item-header">
                    <el-tag type="success">兼职</el-tag>
                    <span class="publish-time">{{ post.publishTime }}</span>
                  </div>
                  <h3 class="item-title">{{ post.title }}</h3>
                  <div class="item-content">
                    <span class="salary">{{ post.salary }}</span>
                    <span class="location"><i class="el-icon-location"></i> {{ post.location }}</span>
                    <span class="work-time">{{ post.workTime }}</span>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost()">
                      编辑
                    </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status === 'active' ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post.id)">
                      删除
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('all')

// 获取用户发布的所有内容
const allPosts = ref([])

// 计算属性：二手交易发布
const secondHandPosts = computed(() => {
  return allPosts.value.filter(post => post.type === 'second-hand')
})

// 计算属性：兼职发布
const partTimePosts = computed(() => {
  return allPosts.value.filter(post => post.type === 'part-time')
})

// 导航到发布页面
const navigateToPublish = (type) => {
  // 这里应该导航到发布页面，暂时先跳转到列表页
  router.push(`/${type}`)
  ElMessage.info('跳转到发布页面')
}

// 查看详情
const viewDetail = (post) => {
  if (post.type === 'second-hand') {
    router.push(`/second-hand/detail/${post.id}`)
  } else {
    router.push(`/part-time/detail/${post.id}`)
  }
}

// 编辑发布
const editPost = () => {
  ElMessage.info('跳转到编辑页面')
  // 这里应该导航到编辑页面
}

// 切换状态（上架/下架）
const toggleStatus = (post) => {
  post.status = post.status === 'active' ? 'inactive' : 'active'
  ElMessage.success(`${post.status === 'active' ? '上架' : '下架'}成功`)
}

// 删除发布
const deletePost = (postId) => {
  ElConfirm('确定要删除这条发布吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    allPosts.value = allPosts.value.filter(post => post.id !== postId)
    ElMessage.success('删除成功')
  }).catch(() => {
    // 取消删除
  })
}

onMounted(async () => {
  // 从 store 获取用户发布的内容
  await userStore.getUserPosts()
  allPosts.value = userStore.userPosts
})
</script>

<style scoped lang="scss">
.user-publish {
  min-height: 100vh;
  padding: 20px 0;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.page-actions {
  display: flex;
  gap: 10px;
}

.category-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.publish-list {
  margin-top: 20px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.publish-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.publish-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.publish-time {
  font-size: 12px;
  color: #999;
}

.item-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.item-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.price, .salary {
  font-size: 16px;
  font-weight: bold;
  color: var(--danger-color, #F56C6C);
}

.location, .condition, .work-time {
  font-size: 14px;
  color: #666;
}

.item-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .page-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .category-tabs {
    padding: 15px;
  }
  
  .publish-item {
    padding: 15px;
  }
  
  .item-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .item-actions {
    flex-wrap: wrap;
  }
}
</style>