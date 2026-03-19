<template>
  <div class="user-seller-after-sales">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/user/publish">我的发布</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>卖家售后</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">卖家售后</h2>
      </div>

      <!-- 售后内容 -->
      <div class="after-sales-content">
        <div v-if="loading" class="loading-state">
          <el-empty description="加载中..." />
        </div>
        <div v-else-if="error" class="error-state">
          <el-empty description="获取售后信息失败" />
        </div>
        <div v-else class="after-sales-details">
          <h3 class="section-title">售后详情</h3>
          <div class="detail-card">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="商品ID">{{ itemId }}</el-descriptions-item>
              <el-descriptions-item label="售后状态">{{ afterSalesStatus }}</el-descriptions-item>
              <el-descriptions-item label="申请时间">{{ applyTime }}</el-descriptions-item>
              <el-descriptions-item label="处理时间">{{ handleTime }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <h3 class="section-title">处理操作</h3>
          <div class="action-card">
            <el-form :model="form" label-width="100px">
              <el-form-item label="处理结果">
                <el-radio-group v-model="form.result">
                  <el-radio label="1">同意退款</el-radio>
                  <el-radio label="0">拒绝退款</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="处理说明">
                <el-input
                  v-model="form.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入处理说明"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitHandle">提交处理</el-button>
                <el-button @click="goBack">返回</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

const itemId = ref(route.params.id || '')
const loading = ref(false)
const error = ref(false)
const afterSalesStatus = ref('处理中')
const applyTime = ref('2026-03-19 10:00:00')
const handleTime = ref('')

const form = ref({
  result: '',
  description: ''
})

const submitHandle = () => {
  if (!form.value.result) {
    ElMessage.error('请选择处理结果')
    return
  }
  
  if (!form.value.description) {
    ElMessage.error('请输入处理说明')
    return
  }
  
  ElMessageBox.confirm('确定要提交处理结果吗？', '提交确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用API提交处理结果
    ElMessage.success('处理结果已提交')
    setTimeout(() => {
      goBack()
    }, 1500)
  }).catch(() => {
    // 取消操作
  })
}

const goBack = () => {
  router.push('/user/user/publish')
}

onMounted(() => {
  if (!itemId.value) {
    ElMessage.error('缺少商品ID')
    error.value = true
    return
  }
  
  // 这里应该调用API获取售后信息
  loading.value = true
  setTimeout(() => {
    loading.value = false
    // 模拟数据
    afterSalesStatus.value = '处理中'
    applyTime.value = '2026-03-19 10:00:00'
  }, 1000)
})
</script>

<style scoped lang="scss">
.user-seller-after-sales {
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

.after-sales-content {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.loading-state,
.error-state {
  padding: 60px 0;
  text-align: center;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-card {
  margin-bottom: 30px;
}

.action-card {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .after-sales-content {
    padding: 15px;
  }
}
</style>