<template>
  <div class="register">
    <!-- 注册表单 -->
    <div class="register-container">
      <div class="register-form-wrapper">
        <h2 class="register-title">用户注册</h2>
        <el-form :model="registerForm" :rules="rules" ref="registerForm" label-width="80px" class="register-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="registerForm.password" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input type="password" v-model="registerForm.confirmPassword" placeholder="请确认密码" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <div class="register-actions">
              <el-checkbox v-model="agreeTerms">我已阅读并同意<a href="#" class="terms-link">用户协议</a>和<a href="#" class="terms-link">隐私政策</a></el-checkbox>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="register-btn" @click="handleRegister" :loading="loading">注册</el-button>
          </el-form-item>
          <el-form-item>
            <div class="login-link-container">
              已有账号？<router-link to="/user/login" class="login-link">立即登录</router-link>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '@/api/user'

export default {
  name: 'RegisterView',
  components: {
  },
  data() {
    return {
      registerForm: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      agreeTerms: false,
      loading: false,
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    },
    async handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid && this.agreeTerms) {
          this.loading = true;
          try {
            // 准备注册数据，移除confirmPassword字段
            const registerData = {
              username: this.registerForm.username,
              phone: this.registerForm.phone,
              password: this.registerForm.password
            };
            
            await register(registerData);
            this.$message.success('注册成功，请登录');
            // 注册成功后跳转到登录页
            this.$router.push('/user/user/login');
          } catch (error) {
            console.error('注册失败:', error);
          } finally {
            this.loading = false;
          }
        } else if (!this.agreeTerms) {
          this.$message.error('请阅读并同意用户协议和隐私政策');
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
.register {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.register-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 60px;
  padding: 40px 0;
  background-color: #f5f7fa;
}

.register-form-wrapper {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 400px;
}

.register-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
  text-align: center;
}

.register-form {
  margin-top: 20px;
}

.terms-link {
  color: var(--primary-color);
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.register-btn {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}

.login-link-container {
  text-align: center;
  margin-top: 16px;
}

.login-link {
  color: var(--primary-color);
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-form-wrapper {
    width: 90%;
    padding: 20px;
  }
}
</style>