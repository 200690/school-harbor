<template>
  <div class="part-time-list">
    <!-- 主要内容 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <h2 class="page-title">校园兼职</h2>
      
      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <div class="search-box">
          <input 
            type="text" 
            placeholder="搜索兼职职位" 
            class="search-input"
            v-model="searchKeyword"
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">
            <i class="el-icon-search"></i>
          </button>
        </div>
        
        <div class="filter-options">
          <el-select v-model="filterType" placeholder="兼职类型" class="filter-select">
            <el-option label="全部" value=""></el-option>
            <el-option label="校内兼职" value="campus"></el-option>
            <el-option label="校外兼职" value="off-campus"></el-option>
            <el-option label="实习" value="internship"></el-option>
          </el-select>
          
          <el-select v-model="filterSalary" placeholder="薪资范围" class="filter-select">
            <el-option label="全部" value=""></el-option>
            <el-option label="10元/小时以下" value="<10"></el-option>
            <el-option label="10-15元/小时" value="10-15"></el-option>
            <el-option label="15-20元/小时" value="15-20"></el-option>
            <el-option label="20元/小时以上" value=">20"></el-option>
          </el-select>
          
          <el-select v-model="filterTime" placeholder="工作时间" class="filter-select">
            <el-option label="全部" value=""></el-option>
            <el-option label="周末" value="weekend"></el-option>
            <el-option label="工作日" value="weekday"></el-option>
            <el-option label="弹性时间" value="flexible"></el-option>
          </el-select>
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
                <router-link :to="`/user/profile/${job.employerId || 1}`" class="employer-link">
                  {{ job.employer }}
                </router-link>
              </span>
              <span class="meta-item"><i class="el-icon-s-position"></i> {{ job.location }}</span>
              <span class="meta-item"><i class="el-icon-time"></i> {{ job.workTime }}</span>
            </div>
            <p class="job-description">{{ job.description }}</p>
            <div class="job-tags">
              <span class="tag tag-primary">{{ job.type }}</span>
              <span class="tag tag-success">薪资: {{ job.salary }}</span>
              <span class="tag tag-info">{{ job.publishTime }}</span>
            </div>
          </div>
          <div class="job-actions">
            <router-link :to="`/part-time/detail/${job.id}`" class="btn btn-primary">查看详情</router-link>
            <button class="btn btn-success" @click="applyJob(job.id)">立即申请</button>
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
        <router-link to="/part-time/publish" class="btn btn-primary publish-btn">
          <i class="el-icon-plus"></i> 发布兼职
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { getPartTimeList, applyPartTimeJob } from '@/api/partTime'

export default {
  name: 'PartTimeListView',
  components: {
  },
  data() {
    return {
      searchKeyword: '',
      filterType: '',
      filterSalary: '',
      filterTime: '',
      currentPage: 1,
      pageSize: 10,
      totalJobs: 0,
      jobs: [],
      loading: false
    }
  },
  mounted() {
    // 组件创建时获取兼职列表
    this.fetchPartTimeList();
  },
  methods: {
    async fetchPartTimeList() {
      this.loading = true;
      try {
        // 构建查询参数
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          keyword: this.searchKeyword,
          type: this.filterType,
          workTime: this.filterTime
        };
        
        // 处理薪资范围筛选
        if (this.filterSalary) {
          const salaryRange = this.filterSalary.split('-');
          if (salaryRange.length === 2) {
            params.salaryMin = parseInt(salaryRange[0]);
            params.salaryMax = parseInt(salaryRange[1]);
          } else if (this.filterSalary.startsWith('<')) {
            params.salaryMax = parseInt(this.filterSalary.substring(1));
          } else if (this.filterSalary.startsWith('>')) {
            params.salaryMin = parseInt(this.filterSalary.substring(1));
          }
        }
        
        const response = await getPartTimeList(params);
        const { records, total } = response.data;
        this.jobs = records;
        this.totalJobs = total;
      } catch (error) {
        console.error('获取兼职列表失败:', error);
        this.$message.error('获取兼职列表失败');
      } finally {
        this.loading = false;
      }
    },
    handleSearch() {
      // 重置页码并搜索
      this.currentPage = 1;
      this.fetchPartTimeList();
    },
    async applyJob(jobId) {
      try {
        await applyPartTimeJob(jobId);
        this.$message.success('申请成功，请等待审核');
      } catch (error) {
        console.error('申请兼职失败:', error);
        this.$message.error('申请兼职失败');
      }
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.fetchPartTimeList();
    },
    handleCurrentChange(current) {
      this.currentPage = current;
      this.fetchPartTimeList();
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

.search-box {
  margin-bottom: 20px;
}

.filter-options {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-select {
  width: 200px;
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
  min-width: 120px;
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

/* 响应式设计 */
@media (max-width: 768px) {
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
    margin-bottom: 10px;
  }
  
  .filter-options {
    flex-direction: column;
  }
}
</style>