<template>
  <div class="part-time-comments">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/part-time">校园兼职</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link :to="`/part-time/detail/${jobId}`">兼职详情</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>兼职评论</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">兼职评论</h2>
        <p class="page-subtitle">{{ jobDetail?.title || '加载中...' }}</p>
      </div>

      <!-- 评论区域 -->
      <div class="comments-section">
        <!-- 评论表单 -->
        <div class="comment-form" v-if="isLoggedIn">
          <h3 class="section-title">发表评论</h3>
          <el-form :model="commentForm" :rules="rules" ref="formRef" label-width="80px">
            <el-form-item label="评分" prop="rating">
              <el-rate v-model="commentForm.rating" :max="5" show-score />
            </el-form-item>
            <el-form-item label="评论内容" prop="content">
              <el-input type="textarea" v-model="commentForm.content" placeholder="请输入您的评论" :rows="4" />
            </el-form-item>
            <el-form-item label="上传图片">
              <div class="image-uploader">
                <div v-for="(image, index) in commentImages" :key="index" class="uploaded-image">
                  <img :src="image" alt="评论图片" />
                  <div class="image-actions">
                    <el-button type="danger" size="small" @click="removeCommentImage(index)">删除</el-button>
                  </div>
                </div>
                <div class="upload-btn-container">
                  <input
                    type="file"
                    ref="fileInput"
                    style="display: none"
                    accept="image/*"
                    @change="handleCommentImageUpload"
                  />
                  <el-button type="primary" @click="triggerFileInput">点击上传</el-button>
                  <div class="upload-tip">
                    只能上传jpg/png文件，且不超过5MB
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitComment">提交评论</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <h3 class="section-title">全部评论 ({{ totalComments }})</h3>
          <div v-if="comments.length === 0" class="empty-state">
            <el-empty description="暂无评论" />
          </div>
          <div v-else class="comment-items">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <!-- 一级评论 -->
              <div class="comment-main">
                <div class="comment-header">
                  <div class="user-info">
                    <img :src="cleanImageUrl(comment.userAvatar)" :alt="comment.userName" class="user-avatar" />
                    <div class="user-details">
                      <span class="user-name">{{ comment.userName }}</span>
                      <div class="comment-meta">
                        <div v-if="comment.score !== undefined && comment.score !== null" class="comment-rating">
                          <i v-for="i in 5" :key="i" :class="i <= comment.score ? 'el-icon-star-on' : 'el-icon-star-off'" class="star-icon"></i>
                          <span class="score-text">{{ comment.score }}分</span>
                        </div>
                        <span class="comment-time">{{ formatTime(comment.createdAt) }}</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="comment-content">
                  <p>{{ comment.content }}</p>
                  <!-- 评论图片 -->
                  <div v-if="comment.images && comment.images.length > 0" class="comment-images">
                    <div class="image-item" v-for="(img, index) in comment.images" :key="index">
                      <img :src="cleanImageUrl(img)" :alt="'评论图片 ' + (index + 1)" class="comment-image" />
                    </div>
                  </div>
                </div>
                <div class="comment-actions">
                  <el-button size="small" type="text" @click="toggleLike(comment)">
                    <i :class="comment.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" />
                    <span>{{ comment.likeCount }} 点赞</span>
                  </el-button>
                  <el-button size="small" type="text" @click="showReplyForm(comment)">
                    <i class="el-icon-chat-line-round" />
                    <span>{{ comment.replyCount }} 回复</span>
                  </el-button>
                </div>
              </div>
              
              <!-- 回复列表 -->
              <div v-if="comment.children && comment.children.length > 0" class="reply-list">
                <div v-for="(reply, index) in comment.children" :key="reply.id" class="reply-item" v-show="index < 2 || expandedComments[comment.id]">
                  <div class="reply-header">
                    <div class="user-info">
                      <img :src="cleanImageUrl(reply.userAvatar)" :alt="reply.userName" class="user-avatar-small" />
                      <div class="user-details">
                        <span class="user-name">{{ reply.userName }}</span>
                        <span v-if="reply.replyUserName" class="reply-info">
                          回复 <span class="reply-user">@{{ reply.replyUserName }}</span>
                        </span>
                        <span class="comment-time">{{ formatTime(reply.createdAt) }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="reply-content">
                    <p>{{ reply.content }}</p>
                  </div>
                  <div class="reply-actions">
                    <el-button size="small" type="text" @click="toggleLike(reply)">
                      <i :class="reply.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" />
                      <span>{{ reply.likeCount }} 点赞</span>
                    </el-button>
                    <el-button size="small" type="text" @click="showReplyForm(reply)">
                      <i class="el-icon-chat-line-round" />
                      <span>回复</span>
                    </el-button>
                  </div>
                  
                  <!-- 子评论的回复表单 -->
                  <div v-if="activeReplyCommentId === reply.id" class="reply-form">
                    <el-input
                      v-model="replyForm.content"
                      type="textarea"
                      :rows="3"
                      :placeholder="`回复 ${reply.userName}...`"
                    />
                    <div class="reply-form-actions">
                      <el-button size="small" @click="cancelReply">取消</el-button>
                      <el-button size="small" type="primary" @click="submitReply(reply)">提交回复</el-button>
                    </div>
                  </div>
                </div>
                <!-- 展开/折叠按钮 -->
                <div v-if="comment.children.length > 2" class="expand-toggle">
                  <el-button size="small" type="text" @click="toggleExpand(comment.id)">
                    <i :class="expandedComments[comment.id] ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                    <span>{{ expandedComments[comment.id] ? '收起回复' : `展开更多回复 (${comment.children.length - 2})` }}</span>
                  </el-button>
                </div>
              </div>
              
              <!-- 回复表单 -->
              <div v-if="activeReplyCommentId === comment.id" class="reply-form">
                <el-input
                  v-model="replyForm.content"
                  type="textarea"
                  :rows="3"
                  :placeholder="`回复 ${comment.userName}...`"
                />
                <div class="reply-form-actions">
                  <el-button size="small" @click="cancelReply">取消</el-button>
                  <el-button size="small" type="primary" @click="submitReply(comment)">提交回复</el-button>
                </div>
              </div>
            </div>
          </div>
          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalComments"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { getPartTimeDetail } from '@/api/partTime'

const route = useRoute()
const router = useRouter()

const jobId = ref(route.params.id || route.query.targetId)
const jobDetail = ref(null)
const comments = ref([])
const totalComments = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 评论表单
const commentForm = ref({
  rating: 5,
  content: ''
})

// 评论图片
const commentImages = ref([])
const fileInput = ref(null)

// 回复相关
const activeReplyCommentId = ref(null)
const replyForm = ref({
  content: ''
})

// 展开/折叠状态
const expandedComments = ref({})

// 切换展开/折叠状态
const toggleExpand = (commentId) => {
  expandedComments.value[commentId] = !expandedComments.value[commentId]
}

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { min: 1, max: 500, message: '评论内容长度在 1 到 500 之间', trigger: 'blur' }
  ]
}

// 计算属性：用户是否登录
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token')
})

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取兼职详情
const fetchJobDetail = async () => {
  try {
    const response = await getPartTimeDetail(jobId.value)
    jobDetail.value = response.data
  } catch (error) {
    console.error('获取兼职详情失败:', error)
    ElMessage.error('获取兼职详情失败')
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const response = await request.post('/comment/showComments', {
      targetType: 0, // 0 表示兼职
      targetId: jobId.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      sortType: 1 // 按时间倒序
    })
    
    comments.value = response?.data?.list || []
    totalComments.value = parseInt(response?.data?.total) || 0
  } catch (error) {
    console.error('获取评论列表失败:', error)
    ElMessage.error('获取评论列表失败')
  }
}

// 提交评论
const submitComment = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/user/user/login')
    return
  }
  
  try {
    // 构建评论数据
    const commentData = {
      targetType: 0, // 0 表示兼职
      targetId: jobId.value,
      content: commentForm.value.content,
      score: commentForm.value.rating,
      images: commentImages.value
    }
    
    const response = await request.post('/comment/addComment', commentData)
    
    if (response.code === 1) {
      ElMessage.success('评论成功')
      // 清空表单
      commentForm.value = {
        rating: 5,
        content: ''
      }
      commentImages.value = [] // 清空图片数组
      await fetchComments()
    } else {
      ElMessage.error(response.msg || '评论失败')
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage.error('提交评论失败')
  }
}

// 清理图片URL（去除多余的引号和反引号）
const cleanImageUrl = (url) => {
  if (!url) return '/default-avatar.png'
  
  let cleanedUrl = url.toString()
  // 去除多余的反引号和引号
  cleanedUrl = cleanedUrl.replace(/`/g, '').replace(/"/g, '').trim()
  
  return cleanedUrl || '/default-avatar.png'
}

// 触发文件输入框
const triggerFileInput = () => {
  fileInput.value?.click()
}

// 处理评论图片上传
const handleCommentImageUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件')
    return
  }
  
  // 检查文件大小
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过5MB')
    return
  }
  
  // 上传文件
  try {
    const uploadFormData = new FormData()
    uploadFormData.append('file', file)
    
    // 获取token
    const token = localStorage.getItem('token')
    
    const response = await fetch('http://localhost:8080/api/oss/upload', {
      method: 'POST',
      body: uploadFormData,
      headers: {
        'Authorization': token ? token : ''
      }
    })
    
    if (!response.ok) {
      throw new Error('上传失败')
    }
    
    const result = await response.json()
    
    if (result.code === 1) {
      // 上传成功，添加到图片数组
      commentImages.value.push(result.data)
    } else {
      ElMessage.error(result.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传图片失败:', error)
    ElMessage.error('上传图片失败')
  }
}

// 移除评论图片
const removeCommentImage = (index) => {
  commentImages.value.splice(index, 1)
}

// 显示回复表单
const showReplyForm = (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/user/user/login')
    return
  }
  activeReplyCommentId.value = comment.id
  replyForm.value.content = ''
}

// 取消回复
const cancelReply = () => {
  activeReplyCommentId.value = null
  replyForm.value.content = ''
}

// 提交回复
const submitReply = async (parentComment) => {
  if (!replyForm.value.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  try {
    const replyData = {
      targetType: 0, // 0 表示兼职
      targetId: jobId.value,
      content: replyForm.value.content,
      parentId: parentComment.id,
      rootId: parentComment.rootId === '0' ? parentComment.id : parentComment.rootId,
      replyUserId: parentComment.userId,
      replyUserName: parentComment.userName
    }
    
    const response = await request.post('/comment/addComment', replyData)
    
    if (response.code === 1) {
      ElMessage.success('回复成功')
      activeReplyCommentId.value = null
      replyForm.value.content = ''
      await fetchComments()
    } else {
      ElMessage.error(response.msg || '回复失败')
    }
  } catch (error) {
    console.error('提交回复失败:', error)
    ElMessage.error('提交回复失败')
  }
}

// 切换点赞状态
const toggleLike = async (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/user/user/login')
    return
  }
  
  try {
    const status = comment.isLiked ? 0 : 1
    const response = await request.post('/comment/commentLike/like', {
      commentId: Number(comment.id),
      status: status
    })
    
    if (response.code === 1) {
      comment.isLiked = !comment.isLiked
      comment.likeCount += status === 1 ? 1 : -1
      ElMessage.success(status === 1 ? '点赞成功' : '取消点赞成功')
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchComments()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchComments()
}

onMounted(async () => {
  await fetchJobDetail()
  await fetchComments()
})
</script>

<style scoped lang="scss">
.part-time-comments {
  min-height: 100vh;
  padding: 80px 0 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
  
  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin-bottom: 10px;
  }
  
  .page-subtitle {
    font-size: 14px;
    color: #666;
  }
}

.comments-section {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.comment-form {
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
  
  .section-title {
    font-size: 18px;
    font-weight: 500;
    color: #333;
    margin-bottom: 20px;
  }
  
  .image-uploader {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    
    .uploaded-image {
      position: relative;
      width: 120px;
      height: 120px;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      overflow: hidden;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      
      .image-actions {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0, 0, 0, 0.6);
        padding: 4px;
        text-align: center;
        
        .el-button {
          padding: 2px 8px;
          font-size: 12px;
        }
      }
    }
    
    .upload-btn-container {
      width: 120px;
      height: 120px;
      border: 1px dashed #d9d9d9;
      border-radius: 4px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409eff;
        background: #ecf5ff;
      }
      
      .upload-tip {
        font-size: 12px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }
}

.comments-list {
  margin-top: 30px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.comment-items {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.user-info {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  flex: 1;
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.comment-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.comment-rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

.star-icon {
  font-size: 14px;
  color: #F56C6C;
}

.star-icon.el-icon-star-off {
  color: #DCDFE6;
}

.score-text {
  font-size: 12px;
  color: #F56C6C;
  margin-left: 4px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  margin-bottom: 15px;
  padding-left: 60px;
  
  p {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
    margin: 0;
  }
  
  .comment-images {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: 12px;
    
    .image-item {
      width: 80px;
      height: 80px;
      border-radius: 4px;
      overflow: hidden;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.comment-actions {
  padding-left: 60px;
  margin-top: 10px;
  
  .el-button {
    margin-right: 16px;
  }
}

.reply-list {
  margin-top: 20px;
  margin-left: 60px;
  padding-left: 20px;
  border-left: 2px solid #e4e7ed;
}

.reply-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.expand-toggle {
  text-align: center;
  padding: 8px 0;
  border-top: 1px dashed #e4e7ed;
  margin-top: 8px;
  
  .el-button {
    color: #409eff;
    font-size: 13px;
    
    &:hover {
      color: #66b1ff;
    }
    
    i {
      margin-right: 4px;
    }
  }
}

.reply-header {
  margin-bottom: 8px;
  
  .user-avatar-small {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .reply-info {
    font-size: 12px;
    color: #666;
    margin-left: 8px;
    
    .reply-user {
      color: #409eff;
      font-weight: 500;
    }
  }
}

.reply-content {
  padding-left: 44px;
  
  p {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
    margin: 0;
  }
}

.reply-actions {
  padding-left: 44px;
  margin-top: 8px;
}

.reply-form {
  margin-top: 16px;
  margin-left: 60px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  
  .reply-form-actions {
    margin-top: 12px;
    text-align: right;
    
    .el-button {
      margin-left: 8px;
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .part-time-comments {
    padding: 60px 0 20px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .comments-section {
    padding: 16px;
  }
  
  .comment-item {
    padding: 15px;
  }
  
  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .comment-content {
    padding-left: 42px;
  }
  
  .comment-actions {
    padding-left: 42px;
  }
  
  .user-avatar {
    width: 32px;
    height: 32px;
  }
  
  .reply-list {
    margin-left: 42px;
    padding-left: 12px;
  }
  
  .reply-form {
    margin-left: 42px;
  }
  
  .pagination {
    margin-top: 20px;
  }
}
</style>