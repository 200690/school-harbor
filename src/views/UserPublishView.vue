<template>
  <div class="user-publish">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
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
                  <div class="item-stats">
                    <span class="stat-item"><i class="el-icon-view"></i> 浏览 {{ post.viewCount }}</span>
                    <span class="stat-item"><i class="el-icon-star-off"></i> {{ post.type === 'second-hand' ? '收藏' : '申请' }} {{ post.applicantCount }}</span>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost(post)">
                        编辑
                      </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status == 1 ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post)">
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
                  <div class="item-stats">
                    <span class="stat-item"><i class="el-icon-view"></i> 浏览 {{ post.viewCount }}</span>
                    <span class="stat-item"><i class="el-icon-star-off"></i> 收藏 {{ post.applicantCount }}</span>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost(post)">
                      编辑
                    </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status == 1 ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post)">
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
                  <div class="item-stats">
                    <span class="stat-item"><i class="el-icon-view"></i> 浏览 {{ post.viewCount }}</span>
                    <span class="stat-item"><i class="el-icon-star-off"></i> 申请 {{ post.applicantCount }}</span>
                  </div>
                  <div class="item-actions">
                    <el-button size="small" type="primary" @click="viewDetail(post)">
                      查看详情
                    </el-button>
                    <el-button size="small" type="success" @click="editPost(post)">
                      编辑
                    </el-button>
                    <el-button size="small" type="warning" @click="toggleStatus(post)">
                      {{ post.status == 1 ? '下架' : '上架' }}
                    </el-button>
                    <el-button size="small" type="danger" @click="deletePost(post)">
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
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMySecondHandItems, updateSecondHandStatus, deleteSecondHandItem } from '@/api/secondHand'
import { getMyPartTimeJobs, updatePartTimeStatus, deletePartTimeJob } from '@/api/partTime'

const router = useRouter()
const route = useRoute()
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
  if (type === 'second-hand') {
    router.push('/second-hand/edit/new')
  } else if (type === 'part-time') {
    router.push('/item/edit/new')
  }
}

// 查看详情
const viewDetail = (post) => {
  if (post.type === 'second-hand') {
    // 使用URL查询参数传递导航来源信息
    router.push(`/second-hand/detail/${post.id}?from=myPublish`)
  } else {
    // 使用URL查询参数传递导航来源信息
    router.push(`/item/${post.id}?from=myPublish`)
  }
}

// 编辑发布
const editPost = (post) => {
  ElMessage.info('跳转到编辑页面')
  if (post.type === 'second-hand') {
    router.push(`/second-hand/edit/${post.id}`)
  } else {
    router.push(`/item/edit/${post.id}`)
  }
}

// 切换状态（上架/下架）
const toggleStatus = async (post) => {
  try {
    const newStatus = post.status == 1 ? 0 : 1
    const actionText = newStatus == 1 ? '上架' : '下架'
    
    // 发送HTTP请求到后端
    let response
    if (post.type === 'second-hand') {
      response = await updateSecondHandStatus(post.id, newStatus)
    } else {
      response = await updatePartTimeStatus(post.id, newStatus)
    }
    
    // 检查响应是否成功（code为1）
    if (response.code === 1) {
      // 立即更新本地状态
      post.status = newStatus
      ElMessage.success(`${actionText}成功`)
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('切换状态失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 删除发布
const deletePost = async (post) => {
  ElMessageBox.confirm('确定要删除这条发布吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      let response
      
      if (post.type === 'second-hand') {
        // 删除二手商品
        response = await deleteSecondHandItem(post.id)
      } else {
        // 删除兼职
        response = await deletePartTimeJob(post.id)
      }
      
      // 检查响应码是否为1
      if (response.code === 1) {
        ElMessage.success('删除成功')
        // 立即刷新页面，重新加载数据
        location.reload()
      } else {
        ElMessage.error(response.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请重试')
    }
  }).catch(() => {
    // 取消删除
  })
}

onMounted(async () => {
  try {
    console.log('开始获取发布记录...')
    // 获取用户ID
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = userInfo.userId || userInfo.id
    
    console.log('用户ID:', userId)
    console.log('用户信息:', userInfo)
    
    if (!userId) {
      ElMessage.error('用户信息不完整，无法获取发布记录')
      console.error('用户信息不完整:', userInfo)
      return
    }
    
    // 并行请求二手商品和兼职数据
    console.log('开始请求API...')
    
    // 分别处理两个API请求，确保一个失败不影响另一个
    let secondHandItems = []
    let partTimeItems = []
    
    try {
      const secondHandResponse = await getMySecondHandItems(userId)
      secondHandItems = secondHandResponse.data || []
      console.log('二手商品响应:', secondHandResponse)
      console.log('二手商品数据:', secondHandItems)
    } catch (error) {
      console.error('获取二手商品失败:', error)
      ElMessage.warning('获取二手商品失败，仅显示兼职数据')
    }
    
    try {
      const partTimeResponse = await getMyPartTimeJobs(userId)
      partTimeItems = partTimeResponse.data || []
      console.log('兼职响应:', partTimeResponse)
      console.log('兼职数据:', partTimeItems)
    } catch (error) {
      console.error('获取兼职失败:', error)
      ElMessage.warning('获取兼职失败，仅显示二手商品数据')
    }
    
    console.log('最终数据 - 二手商品:', secondHandItems)
    console.log('最终数据 - 兼职:', partTimeItems)
    
    // 转换数据格式
    const transformedPartTimeItems = partTimeItems.map((item, index) => {
      const transformedItem = {
        id: item.id || `pt-${index}`,
        type: 'part-time',
        title: item.title,
        employer: item.employer,
        salary: item.salaryDesc,
        location: item.location,
        workTime: item.workTime,
        salaryUnit: item.salaryUnit,
        status: item.status !== undefined ? parseInt(item.status) : 1,
        publishTime: item.createTime || item.publishTime,
        viewCount: item.viewCount || 0,
        applicantCount: item.applicantCount || 0
      }
      console.log(`兼职项 ${index} - 原始status: ${item.status} (${typeof item.status}), 转换后status: ${transformedItem.status} (${typeof transformedItem.status})`)
      return transformedItem
    })
    
    console.log('转换后的兼职数据:', transformedPartTimeItems)
    
    allPosts.value = [
      ...secondHandItems.map((item, index) => ({
        id: item.id || `sh-${index}`,
        type: 'second-hand',
        title: item.title,
        price: item.price,
        location: item.location,
        condition: item.condition,
        status: item.status !== undefined ? parseInt(item.status) : 1,
        publishTime: item.createTime || item.publishTime,
        viewCount: item.viewCount || 0,
        applicantCount: item.applicantCount || 0
      })),
      ...transformedPartTimeItems
    ]
    
    console.log('转换后的数据:', allPosts.value)
    console.log('转换后的数据长度:', allPosts.value.length)
    
    // 打印每个post的status值
    allPosts.value.forEach((post, index) => {
      console.log(`Post ${index} - ID: ${post.id}, Type: ${post.type}, Status: ${post.status}, Button: ${post.status === 1 ? '下架' : '上架'}`)
    })
    
    // 检查 URL 参数，设置默认标签页
    const type = route.query.type
    if (type === 'second-hand') {
      activeTab.value = 'second-hand'
    } else if (type === 'part-time') {
      activeTab.value = 'part-time'
    }
    
    console.log('发布记录获取完成')
  } catch (error) {
    console.error('获取发布记录失败:', error)
    ElMessage.error('获取发布记录失败，请检查控制台日志')
  }
})
</script>

<style scoped lang="scss">
.user-publish {
  min-height: 100vh;
  padding-top: 80px;
  padding-bottom: 20px;
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

.item-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #666;
  
  i {
    font-size: 16px;
    color: #909399;
  }
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