<template>
  <div class="part-time-list">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <h2 class="page-title">校园兼职</h2>
      
      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <div class="filter-row">
          <div class="search-box">
            <input 
              type="text" 
              placeholder="搜索兼职职位" 
              class="search-input"
              v-model="searchKeyword"
            />
            <button class="search-btn" @click="handleSearch">
              <i class="el-icon-search"></i> 搜索
            </button>
          </div>
          
          <div class="filter-options">
            <el-select v-model="filterType" placeholder="兼职类型" class="filter-select">
              <el-option label="全部" value=""></el-option>
              <el-option label="校内兼职" value="1"></el-option>
              <el-option label="校外兼职" value="2"></el-option>
              <el-option label="实习" value="3"></el-option>
            </el-select>
            
            <el-select v-model="filterCreditScore" placeholder="信誉分" class="filter-select">
              <el-option label="全部" value=""></el-option>
              <el-option label="差 (0-60)" value="0-60"></el-option>
              <el-option label="中等 (61-80)" value="61-80"></el-option>
              <el-option label="优秀 (81-100)" value="81-100"></el-option>
            </el-select>
            
            <el-select v-model="sortField" placeholder="排序字段" class="filter-select">
              <el-option label="发布时间" value="publishTime"></el-option>
              <el-option label="浏览量" value="viewCount"></el-option>
              <el-option label="申请人数" value="applicantCount"></el-option>
              <el-option label="信誉分" value="creditScore"></el-option>
            </el-select>
            
            <button class="sort-toggle-btn" @click="toggleSort">
              <i :class="sortOrder === 'desc' ? 'el-icon-sort-down' : 'el-icon-sort-up'"></i>
              {{ sortOrder === 'desc' ? '降序' : '升序' }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- 兼职列表 -->
      <div class="job-list">
        <div class="list-item" v-for="job in jobs" :key="job.id">
          <div class="job-info">
            <h3 class="job-title">{{ job.title }}</h3>
            <div class="job-meta">
              <span class="meta-item">
                <i class="el-icon-s-flag"></i>
                <router-link :to="`/user/profile/${job.publisherId || 1}`" class="employer-link">
                  {{ job.employer }}
                </router-link>
              </span>
              <span class="meta-item"><i class="el-icon-s-position"></i> {{ job.location }}</span>
              <span class="meta-item"><i class="el-icon-time"></i> {{ job.workTime }}</span>
            </div>
            <p class="job-description">{{ job.description }}</p>
            <div class="job-tags">
              <span class="tag tag-primary">{{ job.typeName || getJobTypeText(job.type) }}</span>
              <span class="tag tag-success">薪资: {{ job.salaryDesc }}</span>
              <span class="tag tag-info">{{ job.publishTime }}</span>
              <span class="tag tag-warning">浏览: {{ job.viewCount }}</span>
              <span class="tag tag-info">申请: {{ job.applicantCount }}</span>
            </div>
          </div>
          <div class="job-actions">
            <router-link :to="`/item/${job.id}`" class="btn btn-primary">查看详情</router-link>
            
            <!-- 发布者操作 -->
            <template v-if="job.isPublisher">
              <button class="btn btn-warning" @click="manageApplications(job)">管理申请</button>
            </template>
            
            <!-- 普通用户操作 -->
            <template v-else>
              <button 
                :class="job.applicable ? 'btn btn-success' : 'btn btn-danger'" 
                @click="job.applicable ? showApplyDialog(job.id) : cancelApplication(job.id)"
              >
                {{ job.applicable ? '立即申请' : '取消申请' }}
              </button>
              <button 
                class="btn btn-secondary" 
                @click="toggleFavorite(job)"
              >
                <i :class="job.isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                {{ job.isFavorite ? '已收藏' : '收藏' }}
              </button>
            </template>
          </div>
        </div>
      </div>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalJobs"
        >
        </el-pagination>
      </div>
      
      <!-- 发布兼职按钮 -->
      <div class="publish-btn-container">
        <router-link to="/item/edit/new" class="btn btn-primary publish-btn">
          <i class="el-icon-plus"></i> 发布兼职
        </router-link>
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
import { applyPartTimeJob, addPartTimeFavorite, removePartTimeFavorite, cancelPartTimeApplication } from '@/api/partTime'
import { usePartTimeStore } from '@/stores/partTime'

export default {
  name: 'PartTimeListView',
  components: {
  },
  data() {
    return {
      searchKeyword: '',
      filterType: '',
      filterCreditScore: '',
      sortField: 'publishTime',
      sortOrder: 'desc',
      currentPage: 1,
      pageSize: 10,
      loading: false,
      // 申请弹窗相关
      applyDialogVisible: false,
      currentJobId: null,
      applyForm: {
        resume: '',
        remark: ''
      }
    }
  },
  computed: {
    partTimeStore() {
      return usePartTimeStore()
    },
    jobs() {
      return this.partTimeStore.jobList
    },
    totalJobs() {
      return this.partTimeStore.total
    }
  },
  mounted() {
    // 手动刷新页面时，清除缓存并重新获取数据
    this.clearCache()
    
    // 从路由查询参数中获取 type 参数
    if (this.$route.query.type) {
      const typeParam = this.$route.query.type
      // 将 internship 转换为对应的 filterType 值 3（实习）
      if (typeParam === 'internship') {
        this.filterType = '3'
      }
    }
    
    this.fetchPartTimeList()
  },
  methods: {
    // 清除与兼职列表相关的所有缓存
    clearCache() {
      console.log('清除兼职列表缓存')
      // 遍历localStorage中的所有键，删除所有以partTimeList_开头的键
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)
        if (key && key.startsWith('partTimeList_')) {
          localStorage.removeItem(key)
          // 同时删除对应的时间戳键
          const timeKey = `${key}_time`
          localStorage.removeItem(timeKey)
          i-- // 因为删除了一个键，所以索引需要减1
        }
      }
    },
    async fetchPartTimeList() {
      this.loading = true
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          keyword: this.searchKeyword,
          sortField: this.sortField,
          sortOrder: this.sortOrder
        }
        
        if (this.filterType) {
          params.types = [parseInt(this.filterType)]
        }
        
        if (this.filterCreditScore) {
          const range = this.filterCreditScore.split('-')
          if (range.length === 2) {
            params.creditScore = parseInt(range[0])
          }
        }
        
        await this.partTimeStore.getJobListAction(params)
      } catch (error) {
        console.error('获取兼职列表失败:', error)
        this.$message.error('获取兼职列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchPartTimeList()
    },
    toggleSort() {
      this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc'
      this.fetchPartTimeList()
    },
    showApplyDialog(jobId) {
      // 保存当前 jobId
      this.currentJobId = jobId
      // 重置表单
      this.applyForm = {
        resume: '',
        remark: ''
      }
      // 显示弹窗
      this.applyDialogVisible = true
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
          partTimeId: this.currentJobId,
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
        const job = this.jobs.find(j => j.id === this.currentJobId)
        if (job) {
          job.applicable = false
        }
      } catch (error) {
        console.error('申请兼职失败:', error)
        this.$message.error('申请兼职失败')
      }
    },
    
    async applyJob(jobId) {
      // 这个方法现在不再直接使用，而是通过 showApplyDialog 显示弹窗
      // 保留此方法以保持兼容性
      this.showApplyDialog(jobId)
    },
    
    async cancelApplication(jobId) {
      try {
        await cancelPartTimeApplication(jobId)
        this.$message.success('已取消申请')
        // 更新申请状态，启用申请按钮
        const job = this.jobs.find(j => j.id === jobId)
        if (job) {
          job.applicable = true
        }
      } catch (error) {
        console.error('取消申请失败:', error)
        this.$message.error('取消申请失败')
      }
    },
    async toggleFavorite(job) {
      try {
        if (job.isFavorite) {
          await removePartTimeFavorite(job.id)
          this.$message.success('已取消收藏')
          job.isFavorite = false
        } else {
          await addPartTimeFavorite(job.id)
          this.$message.success('收藏成功')
          job.isFavorite = true
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        this.$message.error('操作失败，请重试')
      }
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.fetchPartTimeList()
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.fetchPartTimeList()
    },
    
    getJobTypeText(type) {
      const typeMap = {
        1: '校内兼职',
        2: '校外兼职',
        3: '实习'
      }
      return typeMap[type] || type
    },

    // 管理申请
    manageApplications(job) {
      console.log('跳转到管理申请页面，jobId:', job.id)
      
      // 发送 POST 请求
      fetch('http://localhost:8080/api/part-time/apply/applyMy', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify({
          id: job.id,
          pageNum: 1,
          pageSize: 10
        })
      })
      .then(response => response.json())
      .then(data => {
        console.log('获取申请列表成功:', data)
        this.$router.push(`/manage/applications?jobId=${job.id}`)
      })
      .catch(error => {
        console.error('获取申请列表失败:', error)
      })
    }
  }
}
</script>

<style scoped lang="scss">
.part-time-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 80px;
}

.main-content {
  flex: 1;
  padding: 20px 0;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

/* 搜索和筛选样式 */
.search-filter {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.filter-row {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 300px;
}

.search-input {
  width: 100%;
  padding: 12px 120px 12px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;

  &:focus {
    outline: none;
    border-color: #409EFF;
  }
}

.search-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  padding: 8px 16px;
  background-color: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;

  &:hover {
    background-color: #66B1FF;
  }
}

.filter-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-select {
  width: 160px;
}

.sort-toggle-btn {
  padding: 8px 20px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;

  &:hover {
    color: #409EFF;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }

  &:active {
    transform: scale(0.98);
  }
}

/* 兼职列表样式 */
.job-list {
  margin-bottom: 30px;
}

.list-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.job-info {
  flex: 1;
  margin-right: 20px;
}

.job-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
}

.job-meta {
  margin-bottom: 12px;
}

.meta-item {
  display: inline-block;
  margin-right: 16px;
  font-size: 14px;
  color: #666;
}

.employer-link {
  color: #409EFF;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.job-description {
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

.job-tags {
  margin-top: 12px;
}

.job-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width: 140px;
}

.job-actions .btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
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

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

/* 发布按钮样式 */
.publish-btn-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.publish-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 24px;
  font-size: 16px;
}

/* 按钮样式 */
.btn-success {
  background-color: #67C23A;
  color: #fff;
  
  &:hover:not(:disabled) {
    background-color: #85ce61;
  }
  
  &:disabled {
    background-color: #c0c4cc;
    cursor: not-allowed;
  }
}

.btn-secondary {
  background-color: #409EFF;
  color: #fff;
  
  &:hover {
    background-color: #66B1FF;
  }
}

.btn-warning {
  background-color: #E6A23C;
  color: #fff;
  
  &:hover {
    background-color: #ebb563;
  }
}

.btn-danger {
  background-color: #F56C6C;
  color: #fff;
  
  &:hover {
    background-color: #f78989;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    min-width: 100%;
  }
  
  .list-item {
    flex-direction: column;
  }
  
  .job-info {
    margin-right: 0;
    margin-bottom: 16px;
  }
  
  .job-actions {
    flex-direction: row;
    justify-content: flex-end;
    min-width: auto;
  }
  
  .filter-select {
    width: 100%;
  }
  
  .filter-options {
    flex-direction: column;
  }
}
</style>