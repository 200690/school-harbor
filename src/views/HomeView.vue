<template>
  <div class="home">
    <!-- 主要内容 -->
    <div class="main-content">
      <!-- 轮播图 -->
      <el-carousel height="300px" class="carousel">
        <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
          <div class="carousel-item">
            <div class="carousel-content">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
              <router-link :to="item.link" class="btn btn-primary">立即查看</router-link>
            </div>
            <div class="carousel-image">
              <img :src="item.image" :alt="item.title" />
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      
      <!-- 分类入口 -->
      <div class="categories container">
        <h3 class="section-title">服务分类</h3>
        <div class="category-list">
          <router-link to="/part-time" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.partTime" alt="校园兼职" />
            </div>
            <span>校园兼职</span>
          </router-link>
          <router-link to="/second-hand" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.secondHand" alt="二手交易" />
            </div>
            <span>二手交易</span>
          </router-link>
          <router-link to="/part-time?type=internship" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.internship" alt="实习机会" />
            </div>
            <span>实习机会</span>
          </router-link>
          <router-link to="/second-hand?category=textbook" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.textbook" alt="教材教辅" />
            </div>
            <span>教材教辅</span>
          </router-link>
          <router-link to="/second-hand?category=electronics" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.electronics" alt="电子产品" />
            </div>
            <span>电子产品</span>
          </router-link>
          <router-link to="/second-hand?category=life" class="category-item">
            <div class="category-icon">
              <img :src="serviceIcons.life" alt="生活用品" />
            </div>
            <span>生活用品</span>
          </router-link>
        </div>
      </div>
      
      <!-- 推荐兼职 -->
      <div class="recommended-part-time container">
        <div class="section-header">
          <h3 class="section-title">推荐兼职</h3>
          <router-link to="/part-time" class="more-link">查看更多 <i class="el-icon-arrow-right"></i></router-link>
        </div>
        <div class="part-time-list">
          <div class="list-item" v-for="job in recommendedJobs" :key="job.id">
            <div class="job-info">
              <h4 class="job-title">{{ job.title }}</h4>
              <div class="job-meta">
                <span class="meta-item"><i class="el-icon-s-flag"></i> {{ job.employer }}</span>
                <span class="meta-item"><i class="el-icon-s-position"></i> {{ job.location }}</span>
                <span class="meta-item"><i class="el-icon-time"></i> {{ job.workTime }}</span>
              </div>
              <div class="job-tags">
                <span class="tag tag-primary">{{ job.type }}</span>
                <span class="tag tag-success">薪资: {{ job.salary }}</span>
              </div>
            </div>
            <router-link :to="`/item/${job.id}`" class="btn btn-primary">查看详情</router-link>
          </div>
        </div>
      </div>
      
      <!-- 推荐二手物品 -->
      <div class="recommended-second-hand container">
        <div class="section-header">
          <h3 class="section-title">热门二手</h3>
          <router-link to="/second-hand" class="more-link">查看更多 <i class="el-icon-arrow-right"></i></router-link>
        </div>
        <div class="second-hand-list">
          <div class="list-item" v-for="item in recommendedItems" :key="item.id">
            <div class="item-image">
              <img :src="item.image" :alt="item.title" />
            </div>
            <div class="item-info">
              <h4 class="item-title">{{ item.title }}</h4>
              <p class="item-description">{{ item.description }}</p>
              <div class="item-meta">
                <span class="price">¥{{ item.price }}</span>
                <span class="location"><i class="el-icon-s-position"></i> {{ item.location }}</span>
              </div>
            </div>
            <router-link :to="`/second-hand/detail/${item.id}`" class="btn btn-primary">查看详情</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  components: {
  },
  data() {
    return {
      // 服务分类图标
      serviceIcons: {
        partTime: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/校园兼职.png',
        secondHand: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/二手交易.png',
        internship: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/实习机会.png',
        textbook: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/教材教辅.png',
        electronics: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/电子产品.png',
        life: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/service/生活用品.png'
      },
      // 轮播图数据
      carouselItems: [
        {
          title: '寻找校园兼职，赚取零花钱',
          description: '海量校园兼职机会，安全可靠，时间灵活',
          link: '/part-time',
          image: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/兼职横幅.png'
        },
        {
          title: '二手交易，让闲置物品找到新主人',
          description: '校园内的二手交易平台，方便快捷，性价比高',
          link: '/second-hand',
          image: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/二手横幅.png'
        },
        {
          title: '实习机会，为未来铺路',
          description: '优质实习岗位，提升专业技能，积累工作经验',
          link: '/part-time?type=internship',
          image: 'https://zll-java-ai.oss-cn-beijing.aliyuncs.com/school-harbor/web/实习横幅.png'
        }
      ],
      // 推荐兼职数据
      recommendedJobs: [
        {
          id: 1,
          title: '校园超市收银员',
          employer: '校园超市',
          location: '校内',
          workTime: '周末 9:00-18:00',
          type: '兼职',
          salary: '15元/小时'
        },
        {
          id: 2,
          title: '图书馆整理员',
          employer: '校图书馆',
          location: '校内',
          workTime: '周一至周五 18:00-21:00',
          type: '兼职',
          salary: '12元/小时'
        },
        {
          id: 3,
          title: '社团活动策划',
          employer: '学生会',
          location: '校内',
          workTime: '弹性时间',
          type: '兼职',
          salary: '200元/次'
        }
      ],
      // 推荐二手物品数据
      recommendedItems: [
        {
          id: 1,
          title: '大学英语四级词汇书',
          description: '全新，未使用，附带光盘',
          price: 20,
          location: '东区宿舍',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20for%20college%20students&image_size=square'
        },
        {
          id: 2,
          title: '笔记本电脑',
          description: '联想小新Pro 13，九成新，配置良好',
          price: 3500,
          location: '西区宿舍',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lenovo%20laptop%20computer%20for%20students&image_size=square'
        },
        {
          id: 3,
          title: '篮球',
          description: '斯伯丁篮球，八成新，无损坏',
          price: 80,
          location: '南区宿舍',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=basketball%20for%20students&image_size=square'
        }
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  margin: 10px auto;
  padding-bottom: 60px;
}

.main-content {
  flex: 1;
  margin-top: 60px;
}

/* 轮播图样式 */
.carousel {
  margin-bottom: 40px;
  width: 100%;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

.carousel-item {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  overflow: hidden;
}

.carousel-content {
  flex: 1;
  padding-right: 40px;
  color: #fff;
  max-width: 50%;
}

.carousel-content h2 {
  font-size: 32px;
  margin-bottom: 16px;
  font-weight: bold;
}

.carousel-content p {
  font-size: 16px;
  margin-bottom: 24px;
  line-height: 1.6;
  opacity: 0.95;
}

.carousel-content .btn {
  display: inline-block;
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 24px;
  background-color: #fff;
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;

  &:hover {
    background-color: #764ba2;
    color: #fff;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }
}

.carousel-image {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 50%;
}

.carousel-image img {
  max-width: 100%;
  max-height: 260px;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 分类入口样式 */
.categories {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.category-list {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.category-item {
  flex: 1;
  min-width: 150px;
  text-align: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  text-decoration: none;
  color: #333;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  }
}

.category-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  overflow: hidden;
  background-color: rgba(64, 158, 255, 0.05);
}

.category-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 推荐兼职样式 */
.recommended-part-time {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.more-link {
  color: var(--primary-color);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
  
  &:hover {
    color: #66B1FF;
  }
}

.part-time-list {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.part-time-list .list-item {
  flex: 1;
  min-width: 300px;
  display: flex;
  flex-direction: column;
}

.job-info {
  flex: 1;
  margin-bottom: 16px;
}

.job-title {
  font-size: 18px;
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

.job-tags {
  margin-top: 12px;
}

/* 推荐二手物品样式 */
.recommended-second-hand {
  margin-bottom: 40px;
}

.second-hand-list {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.second-hand-list .list-item {
  flex: 1;
  min-width: 300px;
  display: flex;
  flex-direction: column;
}

.item-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 12px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.list-item:hover .item-image img {
  transform: scale(1.05);
}

.item-info {
  flex: 1;
  margin-bottom: 16px;
}

.item-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.item-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.4;
}

.item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: var(--danger-color);
}

.location {
  font-size: 14px;
  color: #666;
}

/* 页脚样式使用全局样式 */

/* 响应式设计 */
@media (max-width: 768px) {
  .carousel {
    height: 200px !important;
  }
  
  .carousel-content {
    padding: 0 20px;
  }
  
  .carousel-content h2 {
    font-size: 20px;
  }
  
  .carousel-content p {
    font-size: 14px;
  }
  
  .category-list {
    justify-content: center;
  }
  
  .category-item {
    min-width: 120px;
    padding: 15px;
  }
  
  .part-time-list,
  .second-hand-list {
    flex-direction: column;
  }
}
</style>