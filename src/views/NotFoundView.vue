<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-icon">
        <i class="el-icon-warning-outline"></i>
      </div>
      <h2 class="error-title">页面未找到</h2>
      <p class="error-description">
        抱歉，您访问的页面不存在或已被移除。<br>
        请检查URL是否正确，或者返回首页继续浏览。
      </p>
      <div class="error-actions">
        <router-link to="/" class="btn btn-primary">
          <i class="el-icon-house"></i> 返回首页
        </router-link>
        <button @click="goBack" class="btn btn-secondary">
          <i class="el-icon-back"></i> 返回上一页
        </button>
      </div>
      <div class="quick-links">
        <p>您可能在找：</p>
        <div class="links">
          <router-link to="/part-time" class="link-item">
            <i class="el-icon-s-finance"></i> 校园兼职
          </router-link>
          <router-link to="/second-hand" class="link-item">
            <i class="el-icon-s-goods"></i> 二手交易
          </router-link>
          <router-link to="/user/user/center" class="link-item">
            <i class="el-icon-user"></i> 个人中心
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFoundView',
  methods: {
    goBack() {
      if (window.history.length > 1) {
        this.$router.go(-1)
      } else {
        this.$router.push('/')
      }
    }
  }
}
</script>

<style scoped lang="scss">
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.not-found-content {
  text-align: center;
  color: #fff;
  max-width: 600px;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 20px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.error-icon {
  font-size: 60px;
  margin-bottom: 20px;
  opacity: 0.9;
}

.error-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.error-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 32px;
  opacity: 0.9;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border-radius: 20px;
  font-size: 16px;
  text-decoration: none;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
  font-weight: 500;
}

.btn-primary {
  background-color: #fff;
  color: #667eea;
  
  &:hover {
    background-color: #f0f0f0;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }
}

.btn-secondary {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.3);
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }
}

.quick-links {
  margin-top: 40px;
  padding-top: 40px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  
  p {
    font-size: 14px;
    margin-bottom: 16px;
    opacity: 0.9;
  }
}

.links {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.link-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
  }
}

@media (max-width: 768px) {
  .error-code {
    font-size: 80px;
  }
  
  .error-icon {
    font-size: 40px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-description {
    font-size: 14px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .links {
    flex-direction: column;
    align-items: center;
  }
  
  .link-item {
    width: 100%;
    justify-content: center;
  }
}
</style>
