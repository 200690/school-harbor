<template>
  <div class="login">
    <!-- 登录表单 -->
    <div class="login-container">
      <div class="login-form-wrapper">
        <h2 class="login-title">用户登录</h2>
        <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px" class="login-form">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="loginForm.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <div class="login-actions">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <router-link to="/user/user/register" class="register-link">立即注册</router-link>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { login, getUserInfo } from '@/api/user'
import { useUserStore } from '@/stores/user'

export default {
  name: 'LoginView',
  components: {
  },
  data() {
    return {
      loginForm: {
        phone: '',
        password: ''
      },
      rememberMe: false,
      loading: false,
      rules: {
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            const response = await login(this.loginForm);
            const { token, userId, username, role } = response.data || {};
            
            // 存储token和用户信息
            if (token) {
              localStorage.setItem('token', token);
              
              // 获取用户详细信息
              try {
                const userInfoResponse = await getUserInfo(userId);
                const userInfo = userInfoResponse.data;
                // 存储完整的用户信息
                localStorage.setItem('userInfo', JSON.stringify({
                  userId,
                  username,
                  role,
                  phone: userInfo.phone || '',
                  ...userInfo
                }));
              } catch (error) {
                console.error('获取用户信息失败:', error);
                // 如果获取用户信息失败，至少存储基本信息
                localStorage.setItem('userInfo', JSON.stringify({ userId, username, role }));
              }
              
              // 获取黑名单数据
              const userStore = useUserStore();
              try {
                await userStore.getUserBlacklist();
                await userStore.getItemBlacklist();
                console.log('黑名单数据加载成功');
              } catch (error) {
                console.error('获取黑名单数据失败:', error);
              }
            }
            
            this.$message.success('登录成功');
            // 登录成功后刷新页面，确保导航栏状态更新
            window.location.href = '/';
            // this.$router.push('/');
          } catch (error) {
            console.error('登录失败:', error);
          } finally {
            this.loading = false;
          }
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    }
  }
}
</script>

<style scoped lang="scss">
.login {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.login-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 60px;
  padding: 40px 0;
  background-color: #f5f7fa;
}

.login-form-wrapper {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 400px;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
  text-align: center;
}

.login-form {
  margin-top: 20px;
}

.login-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.register-link {
  color: var(--primary-color);
  text-decoration: none;
  transition: color 0.3s;
  
  &:hover {
    color: #66B1FF;
  }
}

.login-btn {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-form-wrapper {
    width: 90%;
    padding: 20px;
  }
}
</style>