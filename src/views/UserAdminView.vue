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
              <el-input v-model="userSearch" placeholder="搜索用户" clearable style="width: 200px;" />
            </div>
            <el-table :data="filteredUsers" style="width: 100%" border>
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
          </div>
        </el-tab-pane>

        <!-- 订单管理 -->
        <el-tab-pane label="订单管理" name="orders">
          <div class="admin-section">
            <div class="section-header">
              <h3>订单管理</h3>
              <el-input v-model="orderSearch" placeholder="搜索订单" clearable style="width: 200px;" />
            </div>
            <el-table :data="filteredOrders" style="width: 100%" border>
              <el-table-column prop="id" label="订单ID" width="80" />
              <el-table-column prop="userId" label="用户ID" width="80" />
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
          </div>
        </el-tab-pane>

        <!-- 商品管理 -->
        <el-tab-pane label="商品管理" name="goods">
          <div class="admin-section">
            <div class="section-header">
              <h3>商品管理</h3>
              <el-input v-model="goodsSearch" placeholder="搜索商品" clearable style="width: 200px;" />
            </div>
            <el-table :data="filteredGoods" style="width: 100%" border>
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
          </div>
        </el-tab-pane>

        <!-- 争议订单管理 -->
        <el-tab-pane label="争议管理" name="disputes">
          <div class="admin-section">
            <div class="section-header">
              <h3>争议订单管理</h3>
              <el-input v-model="disputeSearch" placeholder="搜索争议" clearable style="width: 200px;" />
            </div>
            <el-table :data="filteredDisputes" style="width: 100%" border>
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
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElConfirm } from 'element-plus'

// 活跃选项卡
const activeTab = ref('users')

// 搜索关键词
const userSearch = ref('')
const orderSearch = ref('')
const goodsSearch = ref('')
const disputeSearch = ref('')

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
  if (!userSearch.value) return users.value
  return users.value.filter(user => 
    user.username.includes(userSearch.value) ||
    user.phone.includes(userSearch.value) ||
    user.email.includes(userSearch.value)
  )
})

// 过滤后的订单数据
const filteredOrders = computed(() => {
  if (!orderSearch.value) return orders.value
  return orders.value.filter(order => 
    order.itemTitle.includes(orderSearch.value)
  )
})

// 过滤后的商品数据
const filteredGoods = computed(() => {
  if (!goodsSearch.value) return goods.value
  return goods.value.filter(good => 
    good.title.includes(goodsSearch.value)
  )
})

// 过滤后的争议数据
const filteredDisputes = computed(() => {
  if (!disputeSearch.value) return disputes.value
  return disputes.value.filter(dispute => 
    dispute.disputeType.includes(disputeSearch.value)
  )
})

// 查看用户详情
const viewUserDetail = (id) => {
  ElMessage.info(`查看用户 ${id} 的详情`)
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

// 查看订单详情
const viewOrderDetail = (id) => {
  ElMessage.info(`查看订单 ${id} 的详情`)
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

// 查看商品详情
const viewGoodsDetail = (id) => {
  ElMessage.info(`查看商品 ${id} 的详情`)
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

// 查看争议详情
const viewDisputeDetail = (id) => {
  ElMessage.info(`查看争议 ${id} 的详情`)
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
    }
  }).catch(() => {
    // 取消操作
  })
}

onMounted(() => {
  // 模拟从后端获取数据
  console.log('管理员页面加载完成')
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