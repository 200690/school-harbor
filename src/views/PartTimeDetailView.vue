<template>
  <div class="part-time-detail">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/part-time">校园兼职</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>兼职详情</el-breadcrumb-item>
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
            <span class="tag tag-primary">{{ jobDetail.typeName }}</span>
            <span class="tag tag-success">状态: {{ jobDetail.statusName }}</span>
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
          <el-button type="primary" size="large" class="apply-btn" @click="applyForJob">
            <i class="el-icon-check"></i> 立即申请
          </el-button>
          <el-button size="large" class="share-btn" @click="shareJob">
            <i class="el-icon-share"></i> 分享职位
          </el-button>
        </div>
      </div>

      <!-- 推荐兼职 -->
      <div class="recommended-jobs">
        <h3 class="section-title">推荐兼职</h3>
        <div class="job-list">
          <div class="list-item" v-for="job in recommendedJobs" :key="job.id">
            <div class="job-info">
              <h4 class="job-title">{{ job.title }}</h4>
              <div class="job-meta">
                <span class="meta-item"><i class="el-icon-s-flag"></i> {{ job.employer }}</span>
                <span class="meta-item"><i class="el-icon-s-position"></i> {{ job.location }}</span>
                <span class="meta-item"><i class="el-icon-time"></i> {{ job.workTime }}</span>
              </div>
              <div class="job-description">{{ job.description }}</div>
              <div class="job-tags">
                <span class="tag tag-primary">{{ job.typeName }}</span>
                <span class="tag tag-success">薪资: {{ job.salaryDesc }}</span>
              </div>
            </div>
            <router-link :to="`/part-time/detail/${job.id}`" class="btn btn-primary">查看详情</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPartTimeDetail, applyPartTimeJob } from '@/api/partTime'
import { usePartTimeStore } from '@/stores/partTime'

export default {
  name: 'PartTimeDetailView',
  data() {
    return {
      jobId: null,
      jobDetail: {
        id: 1,
        title: '校园超市收银员',
        employer: '校园超市',
        location: '校内',
        workTime: '周末 9:00-18:00',
        salaryDesc: '15元/小时',
        salaryUnit: '元/小时',
        type: 1,
        typeName: '校内兼职',
        description: '负责超市收银工作',
        requirements: '责任心强',
        contactPerson: '张经理',
        contactPhone: '138****8888',
        publisherId: 1,
        status: 1,
        statusName: '招聘中',
        publishTime: '2026-02-20 23:59:52',
        viewCount: 120,
        applicantCount: 5,
        creditScore: 85
      },
      recommendedJobs: [
        {
          id: 2,
          title: '图书馆整理员',
          employer: '校图书馆',
          location: '校内',
          workTime: '周一至周五 18:00-21:00',
          salaryDesc: '12元/小时',
          type: '校内兼职',
          description: '负责图书整理、上架等工作'
        },
        {
          id: 3,
          title: '社团活动策划',
          employer: '学生会',
          location: '校内',
          workTime: '弹性时间',
          salaryDesc: '200元/次',
          type: '校内兼职',
          description: '协助策划和组织社团活动'
        },
        {
          id: 4,
          title: '家教',
          employer: '个人',
          location: '校外',
          workTime: '周末 2小时',
          salaryDesc: '50元/小时',
          type: '校外兼职',
          description: '初中数学家教'
        }
      ],
      loading: false
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
        
        const cachedJob = this.partTimeStore.getJobById(this.jobId)
        if (cachedJob) {
          console.log('从缓存中获取到兼职数据:', cachedJob)
          this.jobDetail = cachedJob
        } else {
          console.log('缓存中未找到，从后端获取兼职详情')
          const response = await getPartTimeDetail(this.jobId)
          console.log('获取到的兼职详情数据:', response.data)
          this.jobDetail = response.data
        }
      } catch (error) {
        console.error('获取兼职详情失败:', error)
        this.$message.error('获取兼职详情失败')
      } finally {
        this.loading = false
      }
    },
    async applyForJob() {
      try {
        await applyPartTimeJob(this.jobId)
        this.$message.success('申请成功！请等待雇主联系')
      } catch (error) {
        console.error('申请兼职失败:', error)
        this.$message.error('申请兼职失败')
      }
    },
    shareJob() {
      this.$message.info('分享功能开发中...')
    }
  }
}
</script>

<style scoped lang="scss">
.part-time-detail {
  min-height: 100vh;
  padding: 80px 0 20px;
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
}
</style>
