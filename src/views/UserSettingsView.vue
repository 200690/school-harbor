<template>
  <div class="user-settings">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/user/center">个人中心</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>账号设置</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">账号设置</h2>
      </div>

      <!-- 设置分类 -->
      <div class="settings-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="账号安全" name="security">
            <!-- 账号安全设置 -->
            <div class="settings-section">
              <div class="setting-item">
                <div class="setting-label">
                  <h4>修改密码</h4>
                  <p>定期修改密码可以提高账号安全性</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="changePassword">修改密码</el-button>
                </div>
              </div>
              <div class="setting-item">
                <div class="setting-label">
                  <h4>绑定手机号</h4>
                  <p>已绑定：{{ userInfo.phone }}</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="bindPhone">更换手机号</el-button>
                </div>
              </div>
              <div class="setting-item">
                <div class="setting-label">
                  <h4>绑定邮箱</h4>
                  <p>已绑定：{{ userInfo.email }}</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="bindEmail">更换邮箱</el-button>
                </div>
              </div>
              <div class="setting-item">
                <div class="setting-label">
                  <h4>登录设备管理</h4>
                  <p>查看和管理登录过的设备</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="manageDevices">管理设备</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="隐私设置" name="privacy">
            <!-- 隐私设置 -->
            <div class="settings-section">
              <div class="setting-item">
                <div class="setting-label">
                  <h4>个人信息可见性</h4>
                  <p>控制谁可以看到你的个人信息</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="privacySettings">设置</el-button>
                </div>
              </div>
              <div class="setting-item">
                <div class="setting-label">
                  <h4>消息通知设置</h4>
                  <p>控制接收哪些类型的消息通知</p>
                </div>
                <div class="setting-action">
                  <el-button type="primary" @click="notificationSettings">设置</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="通用设置" name="general">
            <!-- 通用设置 -->
            <div class="settings-section">
              <div class="setting-item">
                <div class="setting-label">
                  <h4>语言设置</h4>
                  <p>选择系统语言</p>
                </div>
                <div class="setting-action">
                  <el-select v-model="language" placeholder="选择语言">
                    <el-option label="简体中文" value="zh-CN"></el-option>
                    <el-option label="English" value="en-US"></el-option>
                  </el-select>
                </div>
              </div>
              <div class="setting-item">
                <div class="setting-label">
                  <h4>主题设置</h4>
                  <p>选择系统主题</p>
                </div>
                <div class="setting-action">
                  <el-radio-group v-model="theme">
                    <el-radio label="light">浅色</el-radio>
                    <el-radio label="dark">深色</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="账号管理" name="account">
            <!-- 账号管理 -->
            <div class="settings-section">
              <div class="setting-item danger">
                <div class="setting-label">
                  <h4>注销账号</h4>
                  <p>注销后账号将无法恢复，请谨慎操作</p>
                </div>
                <div class="setting-action">
                  <el-button type="danger" @click="deleteAccount">注销账号</el-button>
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElConfirm } from 'element-plus'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const activeTab = ref('security')
const language = ref('zh-CN')
const theme = ref('light')
const userInfo = ref({})

// 修改密码
const changePassword = () => {
  ElMessage.info('跳转到修改密码页面')
  // 这里应该导航到修改密码页面
}

// 绑定手机号
const bindPhone = () => {
  ElMessage.info('跳转到绑定手机号页面')
  // 这里应该导航到绑定手机号页面
}

// 绑定邮箱
const bindEmail = () => {
  ElMessage.info('跳转到绑定邮箱页面')
  // 这里应该导航到绑定邮箱页面
}

// 管理设备
const manageDevices = () => {
  ElMessage.info('跳转到设备管理页面')
  // 这里应该导航到设备管理页面
}

// 隐私设置
const privacySettings = () => {
  ElMessage.info('跳转到隐私设置页面')
  // 这里应该导航到隐私设置页面
}

// 通知设置
const notificationSettings = () => {
  ElMessage.info('跳转到通知设置页面')
  // 这里应该导航到通知设置页面
}

// 注销账号
const deleteAccount = () => {
  ElConfirm('确定要注销账号吗？注销后账号将无法恢复！', '注销账号', {
    confirmButtonText: '确定注销',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    ElMessage.success('账号注销申请已提交，将在3个工作日内处理')
    // 这里应该实现账号注销逻辑
  }).catch(() => {
    // 取消操作
  })
}

onMounted(async () => {
  // 从 store 获取用户信息
  await userStore.getUserInfoAction()
  userInfo.value = userStore.userInfo
})
</script>

<style scoped lang="scss">
.user-settings {
  min-height: 100vh;
  padding: 20px 0;
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

.settings-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.settings-section {
  margin-top: 20px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s;
  
  &:last-child {
    border-bottom: none;
  }
  
  &:hover {
    background-color: #f5f7fa;
  }
  
  &.danger:hover {
    background-color: #fef0f0;
  }
}

.setting-label {
  flex: 1;
}

.setting-label h4 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 5px 0;
}

.setting-label p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.setting-action {
  margin-left: 30px;
  flex-shrink: 0;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }
  
  .settings-tabs {
    padding: 15px;
  }
  
  .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
    padding: 15px;
  }
  
  .setting-action {
    margin-left: 0;
    width: 100%;
  }
  
  .setting-action el-select {
    width: 100%;
  }
}
</style>