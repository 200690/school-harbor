<template>
  <div class="user-admin">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>管理员中心</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">管理员中心</h2>
      </div>

      <!-- 功能选项卡 -->
      <el-tabs v-model="activeTab" class="admin-tabs">
        <!-- 用户管理 -->
        <el-tab-pane label="用户管理" name="users">
          <div class="admin-section">
            <div class="section-header">
              <h3>用户信息管理</h3>
              <div class="search-filter">
                <el-input v-model="userSearch" placeholder="搜索用户" clearable style="width: 200px; margin-right: 10px;" />
                <el-select v-model="userStatusFilter" placeholder="用户状态" clearable style="width: 120px; margin-right: 10px;">
                  <el-option label="正常" value="normal" />
                  <el-option label="已拉黑" value="blocked" />
                </el-select>
                <el-select v-model="userCreditFilter" placeholder="信誉分" clearable style="width: 120px; margin-right: 10px;">
                  <el-option label="差 (0-60)" value="poor" />
                  <el-option label="良 (60-80)" value="good" />
                  <el-option label="优秀 (80-95)" value="excellent" />
                  <el-option label="极好 (95-100)" value="perfect" />
                </el-select>
                <el-button type="primary" @click="resetUserFilter">重置筛选</el-button>
              </div>
            </div>
            <el-table :data="pagedUsers" style="width: 100%" border>
              <el-table-column prop="id" label="用户ID" width="80" />
              <el-table-column label="头像" width="80">
                <template #default="scope">
                  <img :src="scope.row.avatar" :alt="scope.row.username" class="user-avatar-small" />
                </template>
              </el-table-column>
              <el-table-column prop="username" label="用户名" />
              <el-table-column prop="phone" label="手机号" />
              <el-table-column prop="email" label="邮箱" />
              <el-table-column prop="creditScore" label="信誉分" />
              <el-table-column prop="registerTime" label="注册时间" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="viewUserDetail(scope.row.id)">
                    查看详情
                  </el-button>
                  <el-button size="small" type="danger" @click="blockUser(scope.row.id)" :disabled="scope.row.isBlocked">
                    {{ scope.row.isBlocked ? '已拉黑' : '拉黑用户' }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="userCurrentPage"
                v-model:page-size="userPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredUsers.length"
                @size-change="handleUserSizeChange"
                @current-change="handleUserCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 订单管理 -->
        <el-tab-pane label="订单管理" name="orders">
          <div class="admin-section">
            <div class="section-header">
              <h3>订单管理</h3>
              <div class="search-filter">
                <el-input v-model="orderSearch" placeholder="搜索订单" clearable style="width: 200px; margin-right: 10px;" />
                <el-select v-model="orderStatusFilter" placeholder="订单状态" clearable style="width: 120px; margin-right: 10px;">
                  <el-option label="已完成" value="已完成" />
                  <el-option label="待收货" value="待收货" />
                </el-select>
                <el-select v-model="orderUserFilter" placeholder="按用户筛选" clearable style="width: 120px; margin-right: 10px;">
                  <el-option v-for="user in users" :key="user.id" :label="user.username" :value="user.id" />
                </el-select>
                <el-button type="primary" @click="resetOrderFilter">重置筛选</el-button>
              </div>
            </div>
            <el-table :data="pagedOrders" style="width: 100%" border>
              <el-table-column prop="id" label="订单ID" width="80" />
              <el-table-column prop="userId" label="用户ID" width="80" />
              <el-table-column label="用户名" width="120">
                <template #default="scope">
                  {{ getUsernameById(scope.row.userId) }}
                </template>
              </el-table-column>
              <el-table-column prop="itemTitle" label="商品名称" />
              <el-table-column prop="price" label="价格" width="100">
                <template #default="scope">
                  ¥{{ scope.row.price }}
                </template>
              </el-table-column>
              <el-table-column prop="purchaseTime" label="购买时间" />
              <el-table-column prop="status" label="状态" />
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="viewOrderDetail(scope.row.id)">
                    查看详情
                  </el-button>
                  <el-button size="small" type="danger" @click="deleteOrder(scope.row.id)">
                    删除订单
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="orderCurrentPage"
                v-model:page-size="orderPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredOrders.length"
                @size-change="handleOrderSizeChange"
                @current-change="handleOrderCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 商品管理 -->
        <el-tab-pane label="商品管理" name="goods">
          <div class="admin-section">
            <div class="section-header">
              <h3>商品管理</h3>
              <div class="search-filter">
                <el-input v-model="goodsSearch" placeholder="搜索商品" clearable style="width: 200px; margin-right: 10px;" />
                <el-select v-model="goodsSellerFilter" placeholder="按卖家筛选" clearable style="width: 120px; margin-right: 10px;">
                  <el-option v-for="user in users" :key="user.id" :label="user.username" :value="user.id" />
                </el-select>
                <el-button type="primary" @click="resetGoodsFilter">重置筛选</el-button>
              </div>
            </div>
            <el-table :data="pagedGoods" style="width: 100%" border>
              <el-table-column prop="id" label="商品ID" width="80" />
              <el-table-column label="图片" width="100">
                <template #default="scope">
                  <img :src="scope.row.image" :alt="scope.row.title" class="goods-image-small" />
                </template>
              </el-table-column>
              <el-table-column prop="title" label="商品名称" />
              <el-table-column prop="price" label="价格" width="100">
                <template #default="scope">
                  ¥{{ scope.row.price }}
                </template>
              </el-table-column>
              <el-table-column prop="sellerId" label="卖家ID" width="80" />
              <el-table-column prop="publishTime" label="发布时间" />
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="viewGoodsDetail(scope.row.id)">
                    查看详情
                  </el-button>
                  <el-button size="small" type="danger" @click="deleteGoods(scope.row.id)">
                    删除商品
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="goodsCurrentPage"
                v-model:page-size="goodsPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredGoods.length"
                @size-change="handleGoodsSizeChange"
                @current-change="handleGoodsCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 争议订单管理 -->
        <el-tab-pane label="争议管理" name="disputes">
          <div class="admin-section">
            <div class="section-header">
              <h3>争议订单管理</h3>
              <div class="search-filter">
                <el-input v-model="disputeSearch" placeholder="搜索争议" clearable style="width: 200px; margin-right: 10px;" />
                <el-select v-model="disputeStatusFilter" placeholder="争议状态" clearable style="width: 120px; margin-right: 10px;">
                  <el-option label="待处理" value="待处理" />
                  <el-option label="已处理" value="已处理" />
                </el-select>
                <el-button type="primary" @click="resetDisputeFilter">重置筛选</el-button>
              </div>
            </div>
            <el-table :data="pagedDisputes" style="width: 100%" border>
              <el-table-column prop="id" label="争议ID" width="80" />
              <el-table-column prop="orderId" label="订单ID" width="80" />
              <el-table-column prop="userId" label="用户ID" width="80" />
              <el-table-column prop="sellerId" label="卖家ID" width="80" />
              <el-table-column prop="disputeType" label="争议类型" />
              <el-table-column prop="createTime" label="创建时间" />
              <el-table-column prop="status" label="状态" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="viewDisputeDetail(scope.row.id)">
                    查看详情
                  </el-button>
                  <el-button size="small" type="success" @click="handleDispute(scope.row.id)" :disabled="scope.row.status === 'resolved'">
                    {{ scope.row.status === 'resolved' ? '已处理' : '处理争议' }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="disputeCurrentPage"
                v-model:page-size="disputePageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredDisputes.length"
                @size-change="handleDisputeSizeChange"
                @current-change="handleDisputeCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'

// 活跃选项卡
const activeTab = ref('users')
const router = useRouter()
const route = useRoute()

// 从URL参数中获取初始选项卡
onMounted(() => {
  // 模拟从后端获取数据
  console.log('管理员页面加载完成')
  
  // 检查URL参数中的tab或table参数
  if (route.query.tab) {
    activeTab.value = route.query.tab
  } else if (route.query.table) {
    activeTab.value = route.query.table
  }
})

// 搜索关键词
const userSearch = ref('')
const orderSearch = ref('')
const goodsSearch = ref('')
const disputeSearch = ref('')

// 用户管理分页和筛选
const userCurrentPage = ref(1)
const userPageSize = ref(10)
const userStatusFilter = ref('')
const userCreditFilter = ref('')

// 订单管理分页和筛选
const orderCurrentPage = ref(1)
const orderPageSize = ref(10)
const orderStatusFilter = ref('')
const orderUserFilter = ref('')

// 商品管理分页和筛选
const goodsCurrentPage = ref(1)
const goodsPageSize = ref(10)
const goodsSellerFilter = ref('')

// 争议管理分页和筛选
const disputeCurrentPage = ref(1)
const disputePageSize = ref(10)
const disputeStatusFilter = ref('')

// 模拟用户数据
const users = ref([
  {
    id: 1,
    username: '小明',
    phone: '138****8888',
    email: 'xiaoming@example.com',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20student&image_size=square',
    creditScore: 60,
    registerTime: '2026-01-01',
    isBlocked: false
  },
  {
    id: 2,
    username: '张三',
    phone: '139****9999',
    email: 'zhangsan@example.com',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20young%20man&image_size=square',
    creditScore: 85,
    registerTime: '2026-01-15',
    isBlocked: false
  },
  {
    id: 3,
    username: '李四',
    phone: '137****7777',
    email: 'lisi@example.com',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20serious%20man&image_size=square',
    creditScore: 45,
    registerTime: '2026-02-01',
    isBlocked: true
  }
])

// 模拟订单数据
const orders = ref([
  {
    id: 1,
    userId: 1,
    itemTitle: '篮球',
    price: 80,
    purchaseTime: '2026-02-05',
    status: '已完成'
  },
  {
    id: 2,
    userId: 2,
    itemTitle: '笔记本电脑',
    price: 3500,
    purchaseTime: '2026-02-10',
    status: '待收货'
  },
  {
    id: 3,
    userId: 1,
    itemTitle: '大学英语四级词汇书',
    price: 25,
    purchaseTime: '2026-02-15',
    status: '已完成'
  }
])

// 模拟商品数据
const goods = ref([
  {
    id: 1,
    title: '大学英语四级词汇书',
    price: 25,
    sellerId: 3,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20college%20student&image_size=square',
    publishTime: '2026-02-09'
  },
  {
    id: 2,
    title: '笔记本电脑',
    price: 3500,
    sellerId: 2,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=laptop%20computer%20portable&image_size=square',
    publishTime: '2026-02-10'
  },
  {
    id: 3,
    title: '篮球',
    price: 80,
    sellerId: 1,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=basketball%20sports%20equipment&image_size=square',
    publishTime: '2026-02-01'
  }
])

// 模拟争议订单数据
const disputes = ref([
  {
    id: 1,
    orderId: 2,
    userId: 2,
    sellerId: 1,
    disputeType: '商品质量问题',
    createTime: '2026-02-12',
    status: '待处理'
  },
  {
    id: 2,
    orderId: 1,
    userId: 1,
    sellerId: 3,
    disputeType: '虚假宣传',
    createTime: '2026-02-08',
    status: '已处理'
  }
])

// 过滤后的用户数据
const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const matchesSearch = !userSearch.value || 
      user.username.includes(userSearch.value) ||
      user.phone.includes(userSearch.value) ||
      user.email.includes(userSearch.value)
    
    const matchesStatus = !userStatusFilter.value || 
      (userStatusFilter.value === 'normal' && !user.isBlocked) ||
      (userStatusFilter.value === 'blocked' && user.isBlocked)
    
    const matchesCredit = !userCreditFilter.value || (
      (userCreditFilter.value === 'poor' && user.creditScore >= 0 && user.creditScore < 60) ||
      (userCreditFilter.value === 'good' && user.creditScore >= 60 && user.creditScore < 80) ||
      (userCreditFilter.value === 'excellent' && user.creditScore >= 80 && user.creditScore < 95) ||
      (userCreditFilter.value === 'perfect' && user.creditScore >= 95 && user.creditScore <= 100)
    )
    
    return matchesSearch && matchesStatus && matchesCredit
  })
})

// 分页后的用户数据
const pagedUsers = computed(() => {
  const startIndex = (userCurrentPage.value - 1) * userPageSize.value
  const endIndex = startIndex + userPageSize.value
  return filteredUsers.value.slice(startIndex, endIndex)
})

// 过滤后的订单数据
const filteredOrders = computed(() => {
  return orders.value.filter(order => {
    const matchesSearch = !orderSearch.value || 
      order.itemTitle.includes(orderSearch.value)
    
    const matchesStatus = !orderStatusFilter.value || 
      order.status === orderStatusFilter.value
    
    const matchesUser = !orderUserFilter.value || 
      order.userId === parseInt(orderUserFilter.value)
    
    return matchesSearch && matchesStatus && matchesUser
  })
})

// 分页后的订单数据
const pagedOrders = computed(() => {
  const startIndex = (orderCurrentPage.value - 1) * orderPageSize.value
  const endIndex = startIndex + orderPageSize.value
  return filteredOrders.value.slice(startIndex, endIndex)
})

// 过滤后的商品数据
const filteredGoods = computed(() => {
  return goods.value.filter(good => {
    const matchesSearch = !goodsSearch.value || 
      good.title.includes(goodsSearch.value)
    
    const matchesSeller = !goodsSellerFilter.value || 
      good.sellerId === parseInt(goodsSellerFilter.value)
    
    return matchesSearch && matchesSeller
  })
})

// 分页后的商品数据
const pagedGoods = computed(() => {
  const startIndex = (goodsCurrentPage.value - 1) * goodsPageSize.value
  const endIndex = startIndex + goodsPageSize.value
  return filteredGoods.value.slice(startIndex, endIndex)
})

// 过滤后的争议数据
const filteredDisputes = computed(() => {
  return disputes.value.filter(dispute => {
    const matchesSearch = !disputeSearch.value || 
      dispute.disputeType.includes(disputeSearch.value)
    
    const matchesStatus = !disputeStatusFilter.value || 
      dispute.status === disputeStatusFilter.value
    
    return matchesSearch && matchesStatus
  })
})

// 分页后的争议数据
const pagedDisputes = computed(() => {
  const startIndex = (disputeCurrentPage.value - 1) * disputePageSize.value
  const endIndex = startIndex + disputePageSize.value
  return filteredDisputes.value.slice(startIndex, endIndex)
})

// 查看用户详情
const viewUserDetail = (id) => {
  router.push(`/user/admin/user/${id}`)
}

// 拉黑用户
const blockUser = (id) => {
  ElConfirm('确定要拉黑这个用户吗？', '拉黑用户', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    const user = users.value.find(u => u.id === id)
    if (user) {
      user.isBlocked = true
      ElMessage.success('拉黑用户成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 用户管理分页方法
const handleUserSizeChange = (size) => {
  userPageSize.value = size
  userCurrentPage.value = 1
}

const handleUserCurrentChange = (current) => {
  userCurrentPage.value = current
}

// 重置用户筛选
const resetUserFilter = () => {
  userSearch.value = ''
  userStatusFilter.value = ''
  userCreditFilter.value = ''
  userCurrentPage.value = 1
}

// 查看订单详情
const viewOrderDetail = (id) => {
  router.push(`/user/admin/order/${id}`)
}

// 删除订单
const deleteOrder = (id) => {
  ElConfirm('确定要删除这个订单吗？', '删除订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    const index = orders.value.findIndex(o => o.id === id)
    if (index !== -1) {
      orders.value.splice(index, 1)
      ElMessage.success('删除订单成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 订单管理分页方法
const handleOrderSizeChange = (size) => {
  orderPageSize.value = size
  orderCurrentPage.value = 1
}

const handleOrderCurrentChange = (current) => {
  orderCurrentPage.value = current
}

// 重置订单筛选
const resetOrderFilter = () => {
  orderSearch.value = ''
  orderStatusFilter.value = ''
  orderUserFilter.value = ''
  orderCurrentPage.value = 1
}

// 查看商品详情
const viewGoodsDetail = (id) => {
  router.push(`/user/admin/goods/${id}`)
}

// 删除商品
const deleteGoods = (id) => {
  ElConfirm('确定要删除这个商品吗？', '删除商品', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    const index = goods.value.findIndex(g => g.id === id)
    if (index !== -1) {
      goods.value.splice(index, 1)
      ElMessage.success('删除商品成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 商品管理分页方法
const handleGoodsSizeChange = (size) => {
  goodsPageSize.value = size
  goodsCurrentPage.value = 1
}

const handleGoodsCurrentChange = (current) => {
  goodsCurrentPage.value = current
}

// 重置商品筛选
const resetGoodsFilter = () => {
  goodsSearch.value = ''
  goodsSellerFilter.value = ''
  goodsCurrentPage.value = 1
}

// 根据用户ID获取用户名
const getUsernameById = (userId) => {
  const user = users.value.find(u => u.id === userId)
  return user ? user.username : '未知用户'
}

// 查看争议详情
const viewDisputeDetail = (id) => {
  router.push(`/user/admin/dispute/${id}`)
}

// 处理争议
const handleDispute = (id) => {
  ElConfirm('确定要处理这个争议吗？', '处理争议', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(() => {
    const dispute = disputes.value.find(d => d.id === id)
    if (dispute) {
      dispute.status = 'resolved'
      ElMessage.success('处理争议成功')
      // 处理完成后跳转到用户管理页面
      router.push('/user/admin?tab=users')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 争议管理分页方法
const handleDisputeSizeChange = (size) => {
  disputePageSize.value = size
  disputeCurrentPage.value = 1
}

const handleDisputeCurrentChange = (current) => {
  disputeCurrentPage.value = current
}

// 重置争议筛选
const resetDisputeFilter = () => {
  disputeSearch.value = ''
  disputeStatusFilter.value = ''
  disputeCurrentPage.value = 1
}

onMounted(() => {
  // 模拟从后端获取数据
  console.log('管理员页面加载完成')
  
  // 检查URL参数中的tab或table参数
  if (route.query.tab) {
    activeTab.value = route.query.tab
  } else if (route.query.table) {
    activeTab.value = route.query.table
  }
})
</script>

<style scoped lang="scss">
.user-admin {
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

.admin-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.admin-section {
  margin-top: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.search-filter {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.user-avatar-small {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.goods-image-small {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .admin-tabs {
    padding: 15px;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .section-header .el-input {
    width: 100%;
  }
}
</style>