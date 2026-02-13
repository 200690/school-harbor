<template>
  <div class="admin-goods-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin">管理员中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/admin?tab=goods">商品管理</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>商品详情</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">商品详情</h2>
      </div>

      <!-- 商品信息卡片 -->
      <div class="goods-info-card card">
        <div class="goods-header">
          <div class="goods-image">
            <img :src="goods.image" :alt="goods.title" />
          </div>
          <div class="goods-basic-info">
            <h3 class="goods-title">{{ goods.title }}</h3>
            <div class="goods-price">¥{{ goods.price }}</div>
            <div class="goods-meta">
              <span class="meta-item">卖家ID：{{ goods.sellerId }}</span>
              <span class="meta-item">发布时间：{{ goods.publishTime }}</span>
            </div>
          </div>
        </div>

        <h3 class="section-title">商品详细信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">商品ID：</span>
            <span class="info-value">{{ goods.id }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">商品描述：</span>
            <span class="info-value">{{ goods.description || '暂无描述' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">商品分类：</span>
            <span class="info-value">{{ goods.category || '未分类' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">商品状态：</span>
            <span class="info-value status-badge" :class="goods.status ? 'status-active' : 'status-inactive'">
              {{ goods.status ? '在售' : '已下架' }}
            </span>
          </div>
        </div>
      </div>

      <!-- 备注信息卡片 -->
      <div class="remark-card card">
        <h3 class="section-title">备注信息</h3>
        <el-input
          v-model="goodsRemark"
          type="textarea"
          :rows="4"
          placeholder="请输入备注信息"
          style="width: 100%; margin-bottom: 15px;"
        />
        <el-button type="primary" @click="saveRemark">保存备注</el-button>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="goBack">返回</el-button>
        <el-button type="danger" @click="deleteGoods">删除商品</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElConfirm } from 'element-plus'

const route = useRoute()
const router = useRouter()
const goodsId = route.params.id

// 模拟商品数据
const goods = ref({
  id: goodsId,
  title: '大学英语四级词汇书',
  price: 25,
  sellerId: 3,
  image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20college%20student&image_size=square',
  publishTime: '2026-02-09',
  description: '全新大学英语四级词汇书，包含最新词汇和例句，适合备考四级的学生使用。',
  category: '教材教辅',
  status: true,
  remark: ''
})

// 备注信息
const goodsRemark = ref('')

// 返回上一页
const goBack = () => {
  router.back()
}

// 删除商品
const deleteGoods = () => {
  ElConfirm('确定要删除这个商品吗？', '删除商品', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    // 这里应该调用后端API删除商品
    ElMessage.success('删除商品成功')
    router.push('/user/admin?tab=goods')
  }).catch(() => {
    // 取消操作
  })
}

// 保存备注
const saveRemark = () => {
  // 这里应该调用后端API保存备注
  goods.value.remark = goodsRemark.value
  ElMessage.success('保存备注成功')
}

onMounted(() => {
  // 这里应该根据商品ID从后端获取商品详情
  console.log(`加载商品 ${goodsId} 的详情`)
  // 模拟从后端获取数据
  setTimeout(() => {
    // 模拟数据加载完成
  }, 500)
})
</script>

<style scoped lang="scss">
.admin-goods-detail {
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

.goods-info-card {
  margin-bottom: 30px;
}

.goods-header {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.goods-image {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.goods-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.goods-basic-info {
  flex: 1;
}

.goods-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.goods-price {
  font-size: 24px;
  font-weight: bold;
  color: var(--danger-color);
  margin-bottom: 15px;
}

.goods-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  color: #666;
  font-size: 14px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.info-label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
  flex-shrink: 0;
}

.info-value {
  color: #333;
  flex: 1;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
}

.status-active {
  background-color: rgba(103, 194, 58, 0.1);
  color: var(--success-color);
}

.status-inactive {
  background-color: rgba(144, 147, 153, 0.1);
  color: var(--info-color);
}

.remark-card {
  margin-bottom: 30px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .goods-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>