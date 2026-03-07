<template>
  <div class="second-hand-comments">
    <div class="main-content container">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><router-link to="/">首页</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link to="/second-hand">二手交易</router-link></el-breadcrumb-item>
        <el-breadcrumb-item><router-link :to="`/second-hand/detail/${itemId}`">商品详情</router-link></el-breadcrumb-item>
        <el-breadcrumb-item>商品评论</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">商品评论</h2>
        <p class="page-subtitle">{{ itemDetail?.title || '加载中...' }}</p>
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
                <el-button size="small" type="text" @click="toggleLike(comment)" :class="{ 'liked': comment.isLiked }">
                  <i :class="comment.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" />
                  <span>{{ comment.likeCount }} 点赞</span>
                </el-button>
                <el-button size="small" type="text" @click="replyComment(comment)">
                  <i class="el-icon-chat-line-round" />
                  <span>{{ comment.replyCount }} 回复</span>
                </el-button>
              </div>
              <!-- 回复列表 -->
              <div v-if="comment.children && comment.children.length > 0" class="replies-list">
                <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                  <div class="reply-header">
                    <img :src="cleanImageUrl(reply.userAvatar)" :alt="reply.userName" class="reply-avatar" />
                    <div class="reply-info">
                      <div class="reply-user-row">
                        <span class="reply-user">{{ reply.userName }}</span>
                        <span v-if="getReplyUserName(reply.replyUserId, comment)" class="reply-to">回复 {{ getReplyUserName(reply.replyUserId, comment) }}</span>
                      </div>
                      <div class="reply-meta">
                        <div v-if="reply.score !== undefined && reply.score !== null" class="reply-rating">
                          <i v-for="i in 5" :key="i" :class="i <= reply.score ? 'el-icon-star-on' : 'el-icon-star-off'" class="star-icon-small"></i>
                          <span class="score-text-small">{{ reply.score }}分</span>
                        </div>
                        <span class="reply-time">{{ formatTime(reply.createdAt) }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="reply-content">{{ reply.content }}</div>
                  <div class="reply-actions">
                    <el-button size="small" type="text" @click="toggleLike(reply)" :class="{ 'liked': reply.isLiked }">
                      <i :class="reply.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" />
                      <span>{{ reply.likeCount }} 点赞</span>
                    </el-button>
                    <el-button size="small" type="text" @click="replyComment(reply)">
                      <i class="el-icon-chat-line-round" />
                      <span>回复</span>
                    </el-button>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { getSecondHandDetail } from '@/api/secondHand'

const route = useRoute()
const router = useRouter()

const itemId = ref(route.params.id || route.query.itemId)
const itemDetail = ref(null)
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

// 获取商品详情
const fetchItemDetail = async () => {
  try {
    const response = await getSecondHandDetail(itemId.value)
    itemDetail.value = response.data
  } catch (error) {
    console.error('获取商品详情失败:', error)
    ElMessage.error('获取商品详情失败')
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const response = await request.post('/comment/showComments', {
      targetType: 1, // 1 表示商品
      targetId: itemId.value,
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
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    
    const response = await request.post('/comment/addComment', {
              targetType: 1, // 1 表示商品
              targetId: itemId.value,
              content: commentForm.value.content,
              rating: commentForm.value.rating,
              images: commentImages.value,
              userId: userInfo.userId || userInfo.id
            })
    
    if (response.code === 1) {
      ElMessage.success('评论成功')
      commentForm.value = { rating: 5, content: '' }
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
    
    const data = await response.json()
    if (data.code === 1 && data.data) {
      // 使用返回的URL添加到图片数组
      commentImages.value.push(data.data)
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error('上传失败: ' + (data.msg || '未知错误'))
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
  }
  
  // 清空文件输入
  event.target.value = ''
}

// 移除评论图片
const removeCommentImage = (index) => {
  commentImages.value.splice(index, 1)
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
      commentId: comment.id,
      status: status
    })
    
    if (response.code === 1) {
      comment.isLiked = !comment.isLiked
      comment.likeCount += status ? 1 : -1
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
  }
}

// 根据replyUserId查找对应的用户名
const getReplyUserName = (replyUserId, parentComment) => {
  if (!replyUserId || replyUserId === '0') {
    return null
  }
  
  // 在父评论的children中查找被回复的用户
  if (parentComment.children && parentComment.children.length > 0) {
    const repliedComment = parentComment.children.find(child => child.id === replyUserId)
    if (repliedComment) {
      return repliedComment.userName
    }
  }
  
  // 如果在children中找不到，说明回复的是父评论本身
  if (parentComment.id === replyUserId) {
    return parentComment.userName
  }
  
  return null
}

// 回复评论
const replyComment = (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/user/user/login')
    return
  }
  
  ElMessageBox.prompt('请输入回复内容:', '回复评论', {
    confirmButtonText: '提交',
    cancelButtonText: '取消',
    inputValidator: (value) => {
      if (!value || value.trim() === '') {
        return '回复内容不能为空'
      }
      if (value.length > 200) {
        return '回复内容不能超过200字'
      }
      return true
    }
  }).then(async ( { value } ) => {
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      
      const response = await request.post('/comment/addComment', {
        targetType: 1, // 1 表示商品
        targetId: itemId.value,
        content: value,
        replyId: comment.id,
        replyUserId: comment.userId,
        userId: userInfo.userId || userInfo.id,
        parentId: comment.id,
        rootId: comment.rootId === '0' ? comment.id : comment.rootId
      })
      
      if (response.code === 1) {
        ElMessage.success('回复成功')
        await fetchComments()
      } else {
        ElMessage.error(response.msg || '回复失败')
      }
    } catch (error) {
      console.error('回复失败:', error)
      ElMessage.error('回复失败')
    }
  }).catch(() => {
    // 取消回复
  })
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
  if (!itemId.value) {
    ElMessage.error('商品ID不存在')
    router.push('/second-hand')
    return
  }
  
  await fetchItemDetail()
  await fetchComments()
})
</script>

<style scoped lang="scss">
.second-hand-comments {
  min-height: 100vh;
  padding: 80px 0 20px;
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
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.comments-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.comment-form {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
}

/* 图片上传器样式 */
.image-uploader {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 10px;
}

.uploaded-image {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f9fafc;
  border: 1px solid #e4e7ed;
}

.uploaded-image img {
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
  display: flex;
  justify-content: center;
  padding: 4px;
  opacity: 0;
  transition: opacity 0.3s;
}

.uploaded-image:hover .image-actions {
  opacity: 1;
}

.upload-btn-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 120px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
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
}

.comment-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
  margin-bottom: 12px;
}

/* 评论图片样式 */
.comment-images {
  display: flex;
  gap: 12px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.image-item {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f9fafc;
}

.comment-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-actions {
  padding-left: 60px;
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
  display: flex;
  gap: 20px;
}

.comment-actions .el-button.liked,
.reply-actions .el-button.liked {
  color: #F56C6C;
  
  i {
    animation: likeAnimation 0.3s ease-in-out;
  }
  
  &:hover {
    color: #F78989;
    background-color: rgba(245, 108, 108, 0.1);
  }
}

@keyframes likeAnimation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}

.replies-list {
  margin-top: 16px;
  padding-left: 60px;
  border-left: 2px solid #f0f0f0;
  padding-left: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reply-item {
  padding: 16px;
  background-color: #f9fafc;
  border-radius: 6px;
  margin-bottom: 12px;
}

.reply-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.reply-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.reply-info {
  flex: 1;
}

.reply-user-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.reply-user {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.reply-to {
  font-size: 12px;
  color: #999;
}

.reply-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.reply-rating {
  display: flex;
  align-items: center;
  gap: 2px;
}

.star-icon-small {
  font-size: 12px;
  color: #F56C6C;
}

.star-icon-small.el-icon-star-off {
  color: #DCDFE6;
}

.score-text-small {
  font-size: 11px;
  color: #F56C6C;
  margin-left: 2px;
}

.reply-time {
  font-size: 12px;
  color: #999;
}

.reply-content {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
  padding-left: 44px;
}

.reply-actions {
  padding-left: 44px;
  display: flex;
  gap: 20px;
}

.reply-actions .el-button {
  font-size: 12px;
  padding: 2px 8px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .comments-section {
    padding: 20px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .comment-item {
    padding: 16px;
  }
  
  .user-avatar {
    width: 40px;
    height: 40px;
  }
  
  .comment-content,
  .comment-actions {
    padding-left: 52px;
  }
  
  .replies-list {
    padding-left: 16px;
  }
}
</style>