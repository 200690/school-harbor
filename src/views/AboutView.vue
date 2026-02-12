<template>
  <div class="about">
    <div class="about-container">
      <div class="about-header">
        <h1 class="page-title">关于我们</h1>
        <p class="about-description">校园港湾是专为在校大学生设计的综合服务平台，提供校园兼职和二手交易服务</p>
      </div>

      <div class="about-content">
        <div class="about-section">
          <h2 class="section-title">平台介绍</h2>
          <div class="intro-card">
            <p>校园港湾致力于为大学生提供安全、便捷的校园生活服务。我们专注于两大核心业务：</p>
            <ul class="feature-list">
              <li><i class="el-icon-s-finance"></i> 校园兼职：提供丰富的兼职机会，帮助大学生赚取零花钱</li>
              <li><i class="el-icon-s-goods"></i> 二手交易：方便快捷的校园二手交易平台</li>
              <li><i class="el-icon-shield"></i> 安全保障：严格审核，确保交易安全</li>
              <li><i class="el-icon-service"></i> 贴心服务：客服支持</li>
            </ul>
          </div>
        </div>

        <div class="about-section">
          <h2 class="section-title">联系我们</h2>
          <div class="contact-card">
            <div class="contact-info">
              <div class="contact-item">
                <i class="el-icon-message"></i>
                <div>
                  <h4>邮箱</h4>
                  <p>zhanglonglong@createrna.com</p>
                </div>
              </div>
              <div class="contact-item">
                <i class="el-icon-phone"></i>
                <div>
                  <h4>电话</h4>
                  <p>15737349990</p>
                </div>
              </div>
              <div class="contact-item">
                <i class="el-icon-location"></i>
                <div>
                  <h4>地址</h4>
                  <p>上海第二工业大学</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="about-section">
          <h2 class="section-title">扫码联系</h2>
          <div class="qr-cards">
            <div class="qr-card">
              <div class="qr-image">
                <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/其他/QQ.png" alt="QQ二维码" />
              </div>
              <h4>QQ客服</h4>
              <p>扫码添加QQ好友</p>
            </div>
            <div class="qr-card">
              <div class="qr-image">
                <img src="https://zll-java-ai.oss-cn-beijing.aliyuncs.com/其他/微信.png" alt="微信二维码" />
              </div>
              <h4>微信客服</h4>
              <p>扫码添加微信好友</p>
            </div>
          </div>
        </div>

        <div class="about-section">
          <h2 class="section-title">欢迎反馈</h2>
          <div class="feedback-card">
            <p>我们非常重视您的意见和建议，如果您在使用过程中遇到任何问题或有任何建议，欢迎通过以下方式联系我们：</p>
            <div class="feedback-actions">
              <el-button type="primary" icon="el-icon-edit" @click="showFeedbackDialog">在线反馈</el-button>
              <el-button icon="el-icon-chat-line-round">在线客服</el-button>
            </div>
            <div class="feedback-note">
              <i class="el-icon-info"></i>
              <p>我们会认真对待每一条反馈，并在24小时内给予回复</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="feedbackDialogVisible" title="在线反馈" width="500px">
      <el-form :model="feedbackForm" label-width="80px">
        <el-form-item label="反馈类型">
          <el-select v-model="feedbackForm.type" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="suggestion"></el-option>
            <el-option label="问题反馈" value="bug"></el-option>
            <el-option label="其他" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="feedbackForm.contact" placeholder="请输入您的联系方式"></el-input>
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input type="textarea" v-model="feedbackForm.content" :rows="4" placeholder="请详细描述您的问题或建议"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="feedbackDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFeedback">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AboutView',
  data() {
    return {
      feedbackDialogVisible: false,
      feedbackForm: {
        type: '',
        contact: '',
        content: ''
      }
    }
  },
  methods: {
    showFeedbackDialog() {
      this.feedbackDialogVisible = true
    },
    submitFeedback() {
      if (!this.feedbackForm.type || !this.feedbackForm.content) {
        this.$message.warning('请填写完整的反馈信息')
        return
      }
      this.$message.success('感谢您的反馈，我们会尽快处理')
      this.feedbackDialogVisible = false
      this.feedbackForm = {
        type: '',
        contact: '',
        content: ''
      }
    }
  }
}
</script>

<style scoped lang="scss">
.about {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.about-container {
  flex: 1;
  padding: 80px 20px 40px;
}

.about-header {
  text-align: center;
  margin-bottom: 40px;
}

.about-description {
  font-size: 16px;
  color: var(--text-color-secondary);
  margin-top: 10px;
}

.about-content {
  max-width: 1000px;
  margin: 0 auto;
}

.about-section {
  margin-bottom: 40px;
}

.intro-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  padding: 30px;
}

.intro-card p {
  font-size: 15px;
  color: var(--text-color-secondary);
  line-height: 1.8;
  margin-bottom: 20px;
}

.feature-list {
  list-style: none;
  padding: 0;
}

.feature-list li {
  display: flex;
  align-items: center;
  padding: 12px 0;
  font-size: 15px;
  color: var(--text-color-primary);
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.feature-list i {
  font-size: 20px;
  color: var(--primary-color);
  margin-right: 12px;
  width: 24px;
}

.contact-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  padding: 30px;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background-color: #f9fafc;
  border-radius: 8px;
  transition: all 0.3s;
  
  &:hover {
    background-color: #f0f7ff;
    transform: translateY(-2px);
  }
}

.contact-item i {
  font-size: 32px;
  color: var(--primary-color);
  margin-right: 16px;
}

.contact-item h4 {
  font-size: 14px;
  color: var(--text-color-secondary);
  margin-bottom: 4px;
}

.contact-item p {
  font-size: 16px;
  color: var(--text-color-primary);
  font-weight: 500;
}

.qr-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 500px;
  margin: 0 auto;
}

.qr-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  padding: 30px;
  text-align: center;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--box-shadow-hover);
  }
}

.qr-image {
  width: 180px;
  height: 180px;
  margin: 0 auto 20px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qr-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qr-card h4 {
  font-size: 18px;
  color: var(--text-color-primary);
  margin-bottom: 8px;
}

.qr-card p {
  font-size: 14px;
  color: var(--text-color-secondary);
}

.feedback-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  padding: 30px;
}

.feedback-card p {
  font-size: 15px;
  color: var(--text-color-secondary);
  line-height: 1.8;
  margin-bottom: 20px;
}

.feedback-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.feedback-note {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f0f7ff;
  border-radius: 8px;
  border-left: 4px solid var(--primary-color);
}

.feedback-note i {
  font-size: 20px;
  color: var(--primary-color);
  margin-right: 12px;
}

.feedback-note p {
  font-size: 14px;
  color: var(--text-color-primary);
  margin: 0;
}

@media (max-width: 768px) {
  .about-container {
    padding: 70px 15px 30px;
  }
  
  .about-header {
    margin-bottom: 30px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .about-description {
    font-size: 14px;
  }
  
  .intro-card,
  .contact-card,
  .feedback-card {
    padding: 20px;
  }
  
  .contact-info {
    grid-template-columns: 1fr;
  }
  
  .qr-cards {
    grid-template-columns: 1fr;
    max-width: 250px;
  }
  
  .qr-card {
    padding: 20px;
  }
  
  .qr-image {
    width: 150px;
    height: 150px;
  }
  
  .feedback-actions {
    flex-direction: column;
  }
  
  .feedback-actions .el-button {
    width: 100%;
  }
}

  .footer-content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>
