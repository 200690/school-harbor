<template>
  <div class="part-time-detail" :class="{ 'banned-mode': isPublisherBanned }">
    <!-- 服务提供者被封禁提示 - 全屏显示 -->
    <div v-if="isPublisherBanned" class="banned-notice">
      <div class="banned-content">
        <div class="error-code">403</div>
        <div class="error-icon">
          <i class="el-icon-warning-outline"></i>
        </div>
        <h2 class="error-title">服务提供者已封禁</h2>
        <p class="error-description">
          抱歉，该服务提供者已被封禁。<br>
          无法查看相关信息，请返回首页继续浏览。
        </p>
        <div class="error-actions">
          <button @click="goHome" class="btn btn-primary">
            <i class="el-icon-house"></i> 返回首页
          </button>
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
    
    <!-- 正常内容 -->
    <div v-else class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <!-- 根据导航来源显示不同的面包屑 -->
        <template v-if="isFromMyPublish">
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/publish">我的发布</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>兼职详情</el-breadcrumb-item>
        </template>
        <template v-else-if="isFromMyApplications">
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/center">个人中心</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/user/user/applications">我的申请</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>兼职详情</el-breadcrumb-item>
        </template>
        <template v-else>
          <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
          <el-breadcrumb-item><router-link to="/part-time">校园兼职</router-link></el-breadcrumb-item>
          <el-breadcrumb-item>兼职详情</el-breadcrumb-item>
        </template>
      </el-breadcrumb>

      <!-- 兼职详情卡片 -->
      <div class="detail-card">
        <!-- 兼职基本信息 -->
        <div class="job-header">
          <h1 class="job-title">{{ jobDetail.title }}</h1>
          <div class="job-meta">
            <span class="meta-item"><i class="el-icon-s-flag"></i> {{ jobDetail.employer }}</span>
            <span class="meta-item"><i class="el-icon-s-position"></i> {{ jobDetail.location }}</span>
            <span class="meta-item"><i class="el-icon-time"></i> {{ jobDetail.workTime }}</span>
            <span class="meta-item"><i class="el-icon-money"></i> {{ jobDetail.salaryDesc }}</span>
          </div>
          <div class="job-tags">
            <!-- 发布者信息 -->
            <router-link :to="`/user/profile/${jobDetail.publisherId}?from=part-time-detail&jobId=${jobId}`" class="publisher-info-mini">
              <el-avatar :size="30" :src="jobDetail.img || jobDetail.avatar" class="publisher-avatar-mini">
                {{ jobDetail.username ? jobDetail.username.charAt(0) : '用' }}
              </el-avatar>
              <span class="publisher-name">{{ jobDetail.username }}</span>
            </router-link>
            
            <!-- 兼职类型标签 -->
            <span class="tag tag-primary">{{ getTypeName(jobDetail.type) }}</span>
            <span class="tag tag-success">状态: {{ jobDetail.statusName || jobDetail.status }}</span>
            <span class="tag tag-warning">信誉分: {{ jobDetail.creditScore }}</span>
            <span class="tag tag-info">浏览: {{ jobDetail.viewCount }}</span>
            <span class="tag tag-info">申请: {{ jobDetail.applicantCount }}</span>
          </div>
        </div>

        <!-- 兼职详情内容 -->
        <div class="job-content">
          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-document"></i> 职位描述</h3>
            <div class="content-text">{{ jobDetail.description }}</div>
          </div>

          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-user"></i> 任职要求</h3>
            <div class="content-text">{{ jobDetail.requirements }}</div>
          </div>

          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-time"></i> 发布时间</h3>
            <p class="content-text">{{ jobDetail.publishTime }}</p>
          </div>

          <div class="content-section">
            <h3 class="section-title"><i class="el-icon-message"></i> 联系方式</h3>
            <div class="contact-info">
              <p><strong>联系人：</strong>{{ jobDetail.contactPerson }}</p>
              <p><strong>联系电话：</strong>{{ jobDetail.contactPhone }}</p>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="job-actions">
          <!-- 发布者操作 -->
          <template v-if="jobDetail.isPublisher">
            <el-button type="primary" size="large" class="apply-btn" @click="editJob">
              <i class="el-icon-edit"></i> 编辑职位
            </el-button>
            <el-button size="large" class="favorite-btn" @click="manageApplications">
              <i class="el-icon-s-operation"></i> 管理申请
            </el-button>
            <el-button size="large" class="share-btn" @click="shareJob">
              <i class="el-icon-share"></i> 分享职位
            </el-button>
          </template>
          
          <!-- 普通用户操作 -->
          <template v-else>
            <el-button 
              :type="jobDetail.applicable ? 'primary' : 'danger'" 
              size="large" 
              class="apply-btn" 
              @click="jobDetail.applicable ? showApplyDialog() : cancelApplication()"
            >
              <i :class="jobDetail.applicable ? 'el-icon-check' : 'el-icon-close'"></i> {{ jobDetail.applicable ? '立即申请' : '取消申请' }}
            </el-button>
            <el-button type="primary" size="large" class="favorite-btn" @click="toggleFavorite">
              <i :class="jobDetail.isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              {{ jobDetail.isFavorite ? '已收藏' : '收藏' }}
            </el-button>
            <el-button size="large" class="share-btn" @click="shareJob">
              <i class="el-icon-share"></i> 分享职位
            </el-button>
            <el-button type="info" size="large" class="comment-btn" @click="viewComments">
              <i class="el-icon-chat-dot-round"></i> 查看评论
            </el-button>
            <el-button type="danger" size="large" class="report-btn" @click="reportJob">
              <i class="el-icon-warning-outline"></i> 举报
            </el-button>
          </template>
        </div>
      </div>

      <!-- 推荐兼职 -->
      <div class="recommended-jobs">
        <h3 class="section-title">推荐兼职</h3>
        <div class="job-list">
          <div class="list-item" v-for="job in jobDetail.topViewJobs" :key="job.id">
            <div class="job-info">
              <h4 class="job-title">{{ job.title }}</h4>
              <div class="job-meta">
                <span class="meta-item"><i class="el-icon-s-flag"></i> {{ job.employer }}</span>
                <span class="meta-item"><i class="el-icon-s-position"></i> {{ job.location }}</span>
                <span class="meta-item"><i class="el-icon-time"></i> {{ job.workTime }}</span>
              </div>
              <div class="job-description">{{ job.description }}</div>
              <div class="job-tags">
                <span class="tag tag-primary">{{ job.typeName || getTypeName(job.type) }}</span>
                <span class="tag tag-success">薪资: {{ job.salaryDesc }}</span>
              </div>
            </div>
            <router-link :to="`/item/${job.id}`" class="btn btn-primary">查看详情</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 申请弹窗 -->
    <el-dialog
      title="申请兼职"
      v-model="applyDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form :model="applyForm" label-width="80px">
        <el-form-item label="简历内容" required>
          <el-input
            v-model="applyForm.resume"
            type="textarea"
            :rows="8"
            placeholder="请输入您的简历内容，包括个人基本信息、教育背景、工作经历等"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input
            v-model="applyForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="submitApplication">立即申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getPartTimeDetail, applyPartTimeJob, addPartTimeFavorite, removePartTimeFavorite, cancelPartTimeApplication } from '@/api/partTime'
import { usePartTimeStore } from '@/stores/partTime'

export default {
  name: 'PartTimeDetailView',
  data() {
    return {
      jobId: null,
      jobDetail: {
        id: null,
        title: '',
        employer: '',
        location: '',
        workTime: '',
        salaryDesc: '',
        salaryUnit: '',
        type: null,
        typeName: '',
        description: '',
        requirements: '',
        contactPerson: '',
        contactPhone: '',
        publisherId: null,
        status: null,
        statusName: '',
        publishTime: '',
        viewCount: 0,
        applicantCount: 0,
        creditScore: 0,
        username: '',
        img: '',
        avatar: '',
        isFavorite: false,
        applicable: true,
        isPublisher: false,
        topViewJobs: []
      },

      loading: false,
      isFromMyPublish: false,
      isFromMyApplications: false,
      isPublisherBanned: false,
      bannedMessage: '',
      // 申请弹窗相关
      applyDialogVisible: false,
      applyForm: {
        resume: '',
        remark: ''
      }
    }
  },
  computed: {
    partTimeStore() {
      return usePartTimeStore()
    }
  },
  created() {
    this.jobId = this.$route.params.id
    this.fetchJobDetail()
    // 监听用户被封禁事件
    window.addEventListener('user-banned', this.handleUserBanned)
    // 监听清除缓存事件
    window.addEventListener('clear-cache', this.handleClearCache)
  },
  beforeUnmount() {
    // 移除事件监听
    window.removeEventListener('user-banned', this.handleUserBanned)
    window.removeEventListener('clear-cache', this.handleClearCache)
  },
  watch: {
    '$route.params.id'(newId) {
      this.jobId = newId
      this.fetchJobDetail()
    }
  },
  methods: {
    async fetchJobDetail() {
      this.loading = true
      try {
        console.log('正在获取兼职详情，ID:', this.jobId)
        
        // 检查是否从"我的发布"页面导航过来
        this.isFromMyPublish = this.$route.query.from === 'myPublish'
        // 检查是否从"我的申请"页面导航过来
        this.isFromMyApplications = this.$route.query.from === 'myApplications'
        
        // 不使用缓存，每次都从后端获取最新数据，确保能检测到服务提供者封禁状态
        console.log('从后端获取兼职详情（不使用缓存）')
        try {
          const response = await getPartTimeDetail(this.jobId)
          console.log('获取到的兼职详情数据:', response.data)
          this.jobDetail = response.data
          this.isPublisherBanned = false
        } catch (error) {
          console.error('获取兼职详情失败:', error)
          console.log('错误详情:', {
            message: error.message,
            response: error.response,
            status: error.response?.status
          })
          
          // 检查是否是服务提供者被封禁的错误（HTTP 403 或 消息包含"用户状态异常"）
          const isBanned = error.response?.status === 403 || 
                          (error.message && error.message.includes('用户状态异常'))
          
          if (isBanned) {
            console.log('服务提供者被封禁')
            this.isPublisherBanned = true
            console.log('isPublisherBanned 设置为:', this.isPublisherBanned)
            this.bannedMessage = '用户状态异常'
            // 清空jobDetail数据，确保不显示任何内容
            this.jobDetail = {
              id: null,
              title: '',
              employer: '',
              location: '',
              workTime: '',
              salaryDesc: '',
              salaryUnit: '',
              type: null,
              typeName: '',
              description: '',
              requirements: '',
              contactPerson: '',
              contactPhone: '',
              publisherId: null,
              status: null,
              statusName: '',
              publishTime: '',
              viewCount: 0,
              applicantCount: 0,
              creditScore: 0,
              username: '',
              img: '',
              avatar: '',
              isFavorite: false,
              applicable: true,
              isPublisher: false
            }
            console.log('jobDetail 已清空')
            // 触发事件通知App.vue隐藏导航栏和页脚
            window.dispatchEvent(new CustomEvent('publisher-banned'))
            // 使用 nextTick 确保 DOM 更新
            this.$nextTick(() => {
              console.log('DOM 已更新，isPublisherBanned:', this.isPublisherBanned)
            })
          } else {
            this.$message.error('获取兼职详情失败')
          }
        }
        
        console.log('当前职位状态:', {
          isFavorite: this.jobDetail.isFavorite,
          applicable: this.jobDetail.applicable,
          isPublisher: this.jobDetail.isPublisher,
          isPublisherBanned: this.isPublisherBanned
        })
      } catch (error) {
        console.error('获取兼职详情失败:', error)
        console.log('错误详情:', {
          message: error.message,
          response: error.response,
          status: error.response?.status
        })
        
        // 检查是否是服务提供者被封禁的错误（HTTP 403 或 消息包含"用户状态异常"）
        const isBanned = error.response?.status === 403 || 
                        (error.message && error.message.includes('用户状态异常'))
        
        if (isBanned) {
          console.log('服务提供者被封禁')
          this.isPublisherBanned = true
          this.bannedMessage = '用户状态异常'
          // 清空jobDetail数据，确保不显示任何内容
          this.jobDetail = {
            id: null,
            title: '',
            employer: '',
            location: '',
            workTime: '',
            salaryDesc: '',
            salaryUnit: '',
            type: null,
            typeName: '',
            description: '',
            requirements: '',
            contactPerson: '',
            contactPhone: '',
            publisherId: null,
            status: null,
            statusName: '',
            publishTime: '',
            viewCount: 0,
            applicantCount: 0,
            creditScore: 0,
            username: '',
            img: '',
            avatar: '',
            isFavorite: false,
            applicable: true,
            isPublisher: false
          }
          console.log('jobDetail 已清空')
          // 触发事件通知App.vue隐藏导航栏和页脚
          window.dispatchEvent(new CustomEvent('publisher-banned'))
          // 使用 nextTick 确保 DOM 更新
          this.$nextTick(() => {
            console.log('DOM 已更新，isPublisherBanned:', this.isPublisherBanned)
          })
        } else {
          this.$message.error('获取兼职详情失败')
        }
      } finally {
        this.loading = false
      }
    },
    async toggleFavorite() {
      try {
        if (this.jobDetail.isFavorite) {
          await removePartTimeFavorite(this.jobId)
          this.$message.success('已取消收藏')
          this.jobDetail.isFavorite = false
        } else {
          await addPartTimeFavorite(this.jobId)
          this.$message.success('收藏成功')
          this.jobDetail.isFavorite = true
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        this.$message.error('操作失败，请重试')
      }
    },
    showApplyDialog() {
      console.log('showApplyDialog called')
      // 重置表单
      this.applyForm = {
        resume: '',
        remark: ''
      }
      // 显示弹窗
      this.applyDialogVisible = true
      console.log('applyDialogVisible:', this.applyDialogVisible)
      // 强制DOM更新
      this.$nextTick(() => {
        console.log('DOM updated, applyDialogVisible:', this.applyDialogVisible)
      })
    },
    
    handleCancel() {
      // 清空表单内容
      this.applyForm = {
        resume: '',
        remark: ''
      }
      // 隐藏弹窗
      this.applyDialogVisible = false
    },
    
    async submitApplication() {
      // 验证简历内容
      if (!this.applyForm.resume || this.applyForm.resume.trim() === '') {
        this.$message.error('请输入简历内容')
        return
      }
      
      try {
        // 从localStorage获取用户信息
        const userInfoStr = localStorage.getItem('userInfo')
        const userInfo = userInfoStr ? JSON.parse(userInfoStr) : {}
        
        // 构建请求数据
        const applicationData = {
          partTimeId: this.jobId,
          resume: this.applyForm.resume,
          remark: this.applyForm.remark,
          userName: userInfo.username || '',
          phone: userInfo.phone || ''
        }
        
        await applyPartTimeJob(applicationData)
        this.$message.success('申请成功！请等待雇主联系')
        // 关闭弹窗
        this.applyDialogVisible = false
        // 更新申请状态，禁用申请按钮
        this.jobDetail.applicable = false
      } catch (error) {
        console.error('申请兼职失败:', error)
        this.$message.error('申请兼职失败')
      }
    },
    
    async cancelApplication() {
      try {
        await cancelPartTimeApplication(this.jobId)
        this.$message.success('已取消申请')
        // 更新申请状态，启用申请按钮
        this.jobDetail.applicable = true
      } catch (error) {
        console.error('取消申请失败:', error)
        this.$message.error('取消申请失败')
      }
    },
    shareJob() {
      this.$message.info('分享功能开发中...')
    },
    
    viewComments() {
      if (!this.jobDetail || !this.jobDetail.id) {
        this.$message.error('无法获取职位信息')
        return
      }
      
      // 跳转到兼职评论页面
      this.$router.push({
        path: '/part-time/comments',
        query: {
          targetId: this.jobDetail.id
        }
      })
    },
    

    handleUserBanned() {
      console.log('收到用户被封禁事件')
      this.isPublisherBanned = true
      this.bannedMessage = '用户状态异常'
      // 清空jobDetail数据，确保不显示任何内容
      this.jobDetail = {
        id: null,
        title: '',
        employer: '',
        location: '',
        workTime: '',
        salaryDesc: '',
        salaryUnit: '',
        type: null,
        typeName: '',
        description: '',
        requirements: '',
        contactPerson: '',
        contactPhone: '',
        publisherId: null,
        status: null,
        statusName: '',
        publishTime: '',
        viewCount: 0,
        applicantCount: 0,
        creditScore: 0,
        username: '',
        img: '',
        avatar: '',
        isFavorite: false,
        applicable: true,
        isPublisher: false
      }
      // 触发事件通知App.vue隐藏导航栏和页脚
      window.dispatchEvent(new CustomEvent('publisher-banned'))
    },
    handleClearCache() {
      console.log('收到清除缓存事件')
      this.isPublisherBanned = false
      this.jobDetail = {
        id: null,
        title: '',
        employer: '',
        location: '',
        workTime: '',
        salaryDesc: '',
        salaryUnit: '',
        type: null,
        typeName: '',
        description: '',
        requirements: '',
        contactPerson: '',
        contactPhone: '',
        publisherId: null,
        status: null,
        statusName: '',
        publishTime: '',
        viewCount: 0,
        applicantCount: 0,
        creditScore: 0,
        username: '',
        img: '',
        avatar: '',
        isFavorite: false,
        applicable: true,
        isPublisher: false
      }
      this.partTimeStore.clearCache()
    },
    getTypeName(type) {
      // 类型映射：1-校内, 2-校外, 3-实习
      const typeMap = {
        1: '校内',
        2: '校外',
        3: '实习'
      }
      return typeMap[type] || '其他'
    },
    goBack() {
      this.$router.back()
    },
    goHome() {
      this.$router.push('/')
    },
    editJob() {
      this.$router.push(`/item/edit/${this.jobId}`)
    },
    // 举报兼职
    reportJob() {
      if (!this.jobDetail || !this.jobDetail.id) {
        this.$message.error('无法获取兼职信息')
        return
      }
      
      // 跳转到举报页面
      this.$router.push({
        path: '/report',
        query: {
          targetId: this.jobDetail.id,
          targetType: 'part-time',
          targetTitle: this.jobDetail.title
        }
      })
    },
    manageApplications() {
      console.log('跳转到管理申请页面，jobId:', this.jobId)
      
      // 发送 POST 请求
      fetch('http://localhost:8080/api/part-time/apply/applyMy', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify({
          id: this.jobId,
          pageNum: 1,
          pageSize: 10
        })
      })
      .then(response => response.json())
      .then(data => {
        console.log('获取申请列表成功:', data)
        this.$router.push(`/manage/applications?jobId=${this.jobId}`)
      })
      .catch(error => {
        console.error('获取申请列表失败:', error)
      })
    }
  }
}
</script>

<style scoped lang="scss">
.part-time-detail {
  min-height: 100vh;
  padding: 80px 0 20px;
}

.part-time-detail.banned-mode {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
}

.banned-notice {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.banned-content {
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

.breadcrumb {
  margin-bottom: 20px;
}

.detail-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 30px;
}

.job-header {
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 30px;
}

.job-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.job-meta {
  margin-bottom: 16px;
}

.meta-item {
  display: inline-block;
  margin-right: 20px;
  font-size: 14px;
  color: #666;
}

.meta-item i {
  margin-right: 6px;
  color: #409EFF;
}

.job-tags {
  margin-top: 16px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

/* 迷你发布者信息 */
.publisher-info-mini {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-right: 15px;
}

.publisher-avatar-mini {
  border: 1px solid #e0e0e0;
}

.publisher-name {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  margin-right: 10px;
  font-weight: 500;
}

.tag-primary {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

.tag-success {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67C23A;
}

.tag-warning {
  background-color: rgba(230, 162, 60, 0.1);
  color: #E6A23C;
}

.job-content {
  margin-bottom: 30px;
}

.content-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

.section-title i {
  margin-right: 8px;
  color: #409EFF;
}

.content-text {
  font-size: 15px;
  color: #666;
  line-height: 1.8;
}

.contact-info {
  background-color: #f9fafc;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
}

.contact-info p {
  margin-bottom: 8px;
  font-size: 14px;
}

.job-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.apply-btn {
  min-width: 200px;
}

.share-btn {
  min-width: 150px;
}

.recommended-jobs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.job-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.list-item {
  background-color: #f9fafc;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  gap: 16px;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.list-item .job-title {
  font-size: 16px;
  margin-bottom: 12px;
}

.list-item .job-meta {
  margin-bottom: 12px;
}

.list-item .job-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.btn {
  display: inline-block;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  text-decoration: none;
  z-index: 1;
  position: relative;
}

.btn-primary {
  background-color: #409EFF;
  color: #fff;
  
  &:hover {
    background-color: #66B1FF;
  }
}

@media (max-width: 768px) {
  .detail-card,
  .recommended-jobs {
    padding: 20px;
  }
  
  .job-title {
    font-size: 20px;
  }
  
  .meta-item {
    display: block;
    margin-bottom: 8px;
  }
  
  .job-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .job-list {
    grid-template-columns: 1fr;
  }
  
  .job-tags {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .publisher-info-mini {
    margin-right: 0;
    margin-bottom: 5px;
  }
}
</style>
