# 1. 系统需求分析与设计的前端架构设计

## 1.1 项目概述
"校园港湾"是一个面向高校学生的综合性服务平台，主要提供校园兼职招聘和二手商品交易两大核心功能。前端采用现代化的 Vue 3 技术栈，旨在为学生用户提供流畅、安全、易用的交互体验。

## 1.2 系统需求分析
基于对现有代码的深入分析，系统主要包含以下功能需求：

### 1.2.1 用户功能需求
- **用户认证**：注册、登录、退出、Token 管理
- **个人信息管理**：个人资料编辑、头像上传、密码修改
- **兼职招聘模块**：兼职信息浏览、搜索、筛选、排序、申请、收藏、发布、编辑、管理申请
- **二手交易模块**：商品浏览、搜索、筛选、排序、购买、收藏、发布、编辑、评论
- **用户中心**：个人主页、发布记录、申请记录、购买记录、收藏列表、关注列表、黑名单、消息通知、评价记录
- **聊天功能**：实时一对一聊天（基于 WebSocket）
- **支付功能**：账户余额充值、交易支付
- **信誉系统**：信誉分展示、等级评定

### 1.2.2 管理功能需求
- **用户管理**：用户列表、封禁/解封、信誉分调整
- **兼职管理**：兼职信息审核、上下架、违规处理
- **二手商品管理**：商品信息审核、上下架、违规处理
- **举报管理**：举报信息处理、违规判定
- **数据统计**：平台数据概览

### 1.2.3 非功能需求
- **性能**：页面加载速度、响应时间、数据缓存
- **安全性**：XSS 防护、CSRF 防护、Token 安全、输入验证
- **可维护性**：代码结构清晰、组件复用、文档完整
- **可扩展性**：模块化设计、易于新增功能
- **兼容性**：支持主流浏览器、移动端适配

## 1.3 前端架构设计原则
1. **组件化**：基于 Vue 3 的组合式 API，实现高内聚、低耦合的组件设计
2. **状态集中管理**：使用 Pinia 进行全局状态管理，确保数据流清晰可预测
3. **路由分层**：基于 Vue Router 4 实现路由懒加载、路由守卫
4. **API 抽象**：统一请求拦截、响应处理、错误处理
5. **样式模块化**：采用 SCSS 预处理器，实现样式隔离与复用
6. **性能优化**：代码分割、懒加载、图片优化、请求缓存
7. **安全防护**：输入输出过滤、Token 自动刷新、防重复提交

## 1.4 技术栈选型
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | ^3.2.13 | 核心框架 |
| Vue Router 4 | ^4.0.3 | 路由管理 |
| Pinia | ^3.0.4 | 状态管理 |
| Element Plus | ^2.13.2 | UI 组件库 |
| Axios | ^1.13.5 | HTTP 客户端 |
| SockJS-client | ^1.6.1 | WebSocket 客户端 |
| STOMP.js | ^2.3.3 | WebSocket 消息协议 |
| Sass (SCSS) | ^1.97.3 | CSS 预处理器 |
| Vue CLI | ~5.0.0 | 构建工具 |

## 1.5 目录结构设计
```
src/
├── api/                    # API 接口封装
│   ├── partTime.js        # 兼职相关 API
│   ├── secondHand.js      # 二手交易相关 API
│   └── user.js            # 用户相关 API
├── assets/                # 静态资源
│   └── logo.png
├── components/            # 公共组件
│   ├── BackToTop.vue      # 返回顶部
│   ├── Footer.vue         # 页脚
│   └── Navbar.vue         # 导航栏
├── router/                # 路由配置
│   └── index.js
├── stores/                # Pinia 状态管理
│   ├── app.js             # 应用全局状态
│   ├── partTime.js        # 兼职模块状态
│   ├── secondHand.js      # 二手模块状态
│   ├── user.js            # 用户模块状态
│   └── index.js           # Store 统一导出
├── styles/                # 全局样式
│   └── index.scss
├── utils/                 # 工具函数
│   └── request.js         # Axios 请求封装
├── views/                 # 页面视图组件
│   ├── HomeView.vue       # 首页
│   ├── PartTimeListView.vue      # 兼职列表
│   ├── PartTimeDetailView.vue    # 兼职详情
│   ├── SecondHandListView.vue    # 二手列表
│   ├── SecondHandDetailView.vue  # 二手详情
│   ├── UserCenterView.vue        # 用户中心
│   ├── LoginView.vue             # 登录
│   ├── RegisterView.vue          # 注册
│   ├── AdminView.vue             # 管理后台
│   └── ... (共 30+ 个视图)
├── App.vue                # 根组件
└── main.js                # 应用入口
```

## 1.6 状态管理设计
采用 Pinia 进行模块化状态管理，每个核心功能模块对应一个 Store：

### 1.6.1 User Store (`stores/user.js`)
- **状态**：`token`, `userInfo`, `userFollows`, `userBlacklist`, `itemBlacklist`, `userPosts`, `userApplications`, `userPurchases`, `userFavorites`, `userMessages`, `recentActivities`
- **Getters**：`userId`, `username`, `avatar`, `unreadMessageCount`
- **Actions**：`loginAction`, `registerAction`, `getUserInfoAction`, `updateUserInfoAction`, `logoutAction`, `checkLogin`, `getUserPosts`, `addFavorite`, `removeFavorite`, `followUser`, `unfollowUser`, `blockUser`, `unblockUser`

### 1.6.2 PartTime Store (`stores/partTime.js`)
- **状态**：`jobList`, `jobDetail`, `total`, `pages`
- **Actions**：`getJobListAction`, `getJobDetailAction`, `applyJobAction`, `cancelApplicationAction`, `addFavoriteAction`, `removeFavoriteAction`

### 1.6.3 SecondHand Store (`stores/secondHand.js`)
- **状态**：`itemList`, `itemDetail`, `total`, `pages`
- **Actions**：`getItemListAction`, `getItemDetailAction`, `publishItemAction`, `editItemAction`, `deleteItemAction`, `addFavoriteAction`, `removeFavoriteAction`

### 1.6.4 App Store (`stores/app.js`)
- **状态**：全局配置、主题、加载状态等

## 1.7 路由设计
### 1.7.1 路由配置 (`router/index.js`)
- **路由模式**：HTML5 History 模式 (`createWebHistory`)
- **路由懒加载**：使用 `() => import()` 动态导入页面组件
- **路由守卫**：全局前置守卫检查登录状态
- **滚动行为**：导航后滚动到顶部

### 1.7.2 路由分类
- **公开路由**：首页 (`/`)、兼职列表 (`/part-time`)、二手列表 (`/second-hand`)、详情页、登录 (`/user/user/login`)、注册 (`/user/user/register`)
- **用户路由**：以 `/user/user/` 开头的路由，需要登录访问
- **管理路由**：以 `/admin/` 开头的路由，需要管理员权限
- **聊天路由**：`/chat`，需要登录访问
- **支付路由**：`/payment`，需要登录访问
- **404 路由**：`/:pathMatch(.*)*` 匹配所有未定义路由

### 1.7.3 路由守卫实现
```javascript
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  // 访问需要登录的页面，检查是否有 token
  if (to.path.startsWith('/user/user/') && to.path !== '/user/user/login' && to.path !== '/user/user/register') {
    if (!token) {
      next('/user/user/login')
      return
    }
  }
  next()
})
```

## 1.8 API 层设计
### 1.8.1 请求封装 (`utils/request.js`)
- **基础配置**：设置 `baseURL: '/api'`、`timeout: 15000`、`withCredentials: true`
- **请求拦截器**：自动添加 `Authorization` 头部（Token）
- **响应拦截器**：
  - 统一处理响应码：`code === 1` 成功，其他为错误
  - 401 状态码：Token 过期，清除本地存储并跳转登录页
  - 错误消息统一提示（使用 Element Plus 的 `ElMessage`）
  - 自定义事件触发：`token-expired`、`user-banned`

### 1.8.2 API 模块化
- **用户模块** (`api/user.js`)：`login`, `register`, `getUserInfo`, `updateUserInfo`, `changePassword`, `logout`, `getMyFollows`, `unfollowUser`, `getUserBlacklist`, `getItemBlacklist`, `blockUser`, `unblockUser`, `getMyEvaluations`, `showMyComments`, `getMyMessage`
- **兼职模块** (`api/partTime.js`)：`getPartTimeList`, `getPartTimeDetail`, `applyPartTimeJob`, `cancelPartTimeApplication`, `addPartTimeFavorite`, `removePartTimeFavorite`
- **二手模块** (`api/secondHand.js`)：`getSecondHandList`, `getSecondHandDetail`, `publishSecondHand`, `editSecondHand`, `deleteSecondHand`, `addSecondHandFavorite`, `removeSecondHandFavorite`

## 1.9 组件化设计
### 1.9.1 组件分类
- **布局组件**：`Navbar`, `Footer`, `BackToTop`
- **页面组件**：位于 `views/` 目录，每个路由对应一个页面组件
- **业务组件**：可根据需要提取可复用的业务组件（如 `JobCard`, `ItemCard`, `CommentList`）

### 1.9.2 组件通信
- **Props/Events**：父子组件通信
- **Provide/Inject**：跨层级组件通信
- **Pinia Store**：跨组件状态共享
- **Event Bus**：使用 Vue 3 的 `mitt` 或自定义事件（谨慎使用）

### 1.9.3 组件设计原则
- **单一职责**：每个组件只负责一个特定功能
- **可复用性**：提取通用逻辑为可复用组件或组合式函数
- **可测试性**：组件逻辑与视图分离，便于单元测试

## 1.10 样式方案
### 1.10.1 预处理器
- 使用 SCSS 语法，支持变量、嵌套、混合、函数等高级特性
- 全局样式文件：`styles/index.scss`

### 1.10.2 样式组织
- **全局样式**：重置样式、字体、颜色变量、工具类
- **组件样式**：使用 `<style scoped lang="scss">` 实现样式作用域隔离
- **主题变量**：定义 CSS 变量或 SCSS 变量统一管理主题色

### 1.10.3 响应式设计
- 使用媒体查询实现移动端适配
- 采用 Flexbox 和 Grid 布局
- Element Plus 组件库内置响应式支持

## 1.11 性能优化策略
### 1.11.1 加载性能
- **路由懒加载**：分割代码包，按需加载页面组件
- **组件懒加载**：使用 `defineAsyncComponent` 异步加载大型组件
- **图片懒加载**：使用 `loading="lazy"` 或第三方库
- **资源压缩**：Gzip/Brotli 压缩、图片优化

### 1.11.2 运行时性能
- **虚拟列表**：长列表使用虚拟滚动（如 `vue-virtual-scroller`）
- **数据缓存**：合理使用 `localStorage` 缓存 API 响应数据（如首页推荐数据缓存 3 分钟）
- **防抖节流**：搜索输入、窗口调整等高频事件使用防抖节流
- **计算属性缓存**：Vue 计算属性自动缓存

### 1.11.3 构建优化
- **代码分割**：Webpack SplitChunks 配置
- **Tree Shaking**：移除未使用代码
- **预加载/预获取**：使用 `<link rel="preload">` 或 `webpackPreload`/`webpackPrefetch`

## 1.12 安全考虑
### 1.12.1 输入验证
- 前端表单验证（使用 Element Plus 表单验证规则）
- 敏感字符过滤（防止 XSS）
- 文件上传类型限制

### 1.12.2 输出编码
- 使用 Vue 的文本插值自动转义 HTML
- 避免使用 `v-html`，必要时进行净化处理

### 1.12.3 Token 安全
- Token 存储在 `localStorage`，设置合适的过期时间
- 请求自动携带 Token
- Token 过期自动跳转登录页
- 退出登录清除 Token

### 1.12.4 CSRF 防护
- 使用 `withCredentials: true` 携带 Cookie
- 后端配合设置 SameSite 属性

## 1.13 错误处理与日志
### 1.13.1 错误类型
- **网络错误**：请求超时、连接失败
- **API 错误**：后端返回错误码（如 401、403、500）
- **前端错误**：JavaScript 运行时错误、组件渲染错误

### 1.13.2 错误处理策略
- **全局错误捕获**：Vue 的 `errorHandler`、`onErrorCaptured`
- **API 错误统一处理**：在 `request.js` 拦截器中处理
- **用户友好提示**：使用 `ElMessage` 显示错误信息，避免暴露技术细节
- **错误日志**：开发环境输出详细日志，生产环境收集错误上报

### 1.13.3 日志管理
- 使用 `console.log`/`console.error` 分级输出
- 生产环境可接入 Sentry 等错误监控平台

## 1.14 构建与部署
### 1.14.1 开发环境
- `npm run serve`：启动开发服务器，支持热重载
- 代理配置：`vue.config.js` 中配置 `/api` 代理到后端服务器

### 1.14.2 生产环境
- `npm run build`：构建生产版本，输出到 `dist/` 目录
- 环境变量：通过 `.env.development` 和 `.env.production` 管理环境配置
- CI/CD：可集成 GitLab CI、Jenkins 等自动化部署

### 1.14.3 部署策略
- **静态资源托管**：Nginx、Apache、CDN
- **路由 History 模式**：配置 Nginx `try_files` 规则
- **版本管理**：文件哈希命名，支持长期缓存
- **监控告警**：接入应用性能监控（APM）和错误监控

# 2. 核心功能模块实现的前端模块设计实现

## 2.1 用户认证模块
### 2.1.1 功能描述
- 用户注册、登录、退出
- Token 管理与自动续期
- 登录状态持久化

### 2.1.2 实现要点
- **登录页面** (`LoginView.vue`)：表单验证、登录请求、错误处理
- **注册页面** (`RegisterView.vue`)：表单验证、注册请求、跳转登录
- **Token 存储**：登录成功后将 Token 和用户信息存入 `localStorage`
- **路由守卫**：保护需要登录的路由，未登录跳转到登录页
- **自动检查登录状态**：应用启动时从 `localStorage` 恢复登录状态

### 2.1.3 代码示例
```javascript
// stores/user.js - 登录 Action
async loginAction(loginForm) {
  try {
    const res = await login(loginForm)
    this.setToken(res.data.token)
    this.setUserInfo(res.data.userInfo)
    return res
  } catch (error) {
    // 模拟登录成功（开发环境）
    this.setToken('mock-token-' + Date.now())
    this.setUserInfo(this.userInfo)
    return { data: { token: 'mock-token-' + Date.now(), userInfo: this.userInfo } }
  }
}
```

## 2.2 兼职招聘模块
### 2.2.1 功能描述
- 兼职信息列表展示（搜索、筛选、排序、分页）
- 兼职详情查看
- 兼职申请（填写简历、提交申请）
- 兼职收藏/取消收藏
- 兼职发布与编辑（发布者功能）
- 申请管理（发布者查看、处理申请）

### 2.2.2 组件设计
- **兼职列表页面** (`PartTimeListView.vue`)：搜索框、筛选器、排序按钮、列表项、分页组件
- **兼职详情页面** (`PartTimeDetailView.vue`)：详细信息展示、申请按钮、收藏按钮、评论列表
- **兼职编辑页面** (`PartTimeEditView.vue`)：表单编辑、图片上传、富文本编辑
- **申请管理页面** (`ManageApplicationsView.vue`)：申请列表、状态筛选、操作按钮

### 2.2.3 状态管理
- **PartTime Store** 管理兼职列表、详情、分页数据
- 列表页与详情页共享 Store 状态，减少重复请求
- 使用缓存策略：列表数据缓存 3 分钟，减少服务器压力

### 2.2.4 代码示例
```javascript
// PartTimeListView.vue - 加载兼职列表
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
    
    await this.partTimeStore.getJobListAction(params)
  } catch (error) {
    console.error('获取兼职列表失败:', error)
    this.$message.error('获取兼职列表失败')
  } finally {
    this.loading = false
  }
}
```

## 2.3 二手交易模块
### 2.3.1 功能描述
- 二手商品列表展示（搜索、筛选、排序、分页）
- 商品详情查看（图片轮播、商品描述、卖家信息）
- 商品购买（加入购物车、直接购买）
- 商品收藏/取消收藏
- 商品发布与编辑（卖家功能）
- 商品评论与评价
- 交易聊天（与卖家沟通）

### 2.3.2 组件设计
- **二手列表页面** (`SecondHandListView.vue`)：搜索框、筛选器、商品卡片、分页组件
- **商品详情页面** (`SecondHandDetailView.vue`)：图片展示、商品信息、卖家信息、购买按钮、评论区域
- **商品编辑页面** (`SecondHandEditView.vue`)：表单编辑、多图上传、分类选择
- **评论页面** (`SecondHandCommentsView.vue`)：评论列表、评论发表、回复功能

### 2.3.3 状态管理
- **SecondHand Store** 管理商品列表、详情、分页数据
- 商品详情页独立状态，支持快速切换
- 评论数据单独管理，支持分页加载

### 2.3.4 代码示例
```javascript
// SecondHandListView.vue - 搜索与筛选
handleSearch() {
  this.currentPage = 1
  this.fetchSecondHandList()
},

toggleSort() {
  this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc'
  this.fetchSecondHandList()
}
```

## 2.4 用户中心模块
### 2.4.1 功能描述
- 个人主页展示（基本信息、信誉分、统计数据）
- 我的发布（兼职、二手商品）
- 我的申请（兼职申请记录）
- 我的购买（二手商品购买记录）
- 我的收藏（兼职、商品收藏）
- 我的关注（关注的用户）
- 黑名单管理（用户黑名单、商品黑名单）
- 消息通知（系统消息、交易消息）
- 我的评价（收到的评价、给出的评价）
- 账户设置（个人信息编辑、密码修改、隐私设置）

### 2.4.2 组件设计
- **用户中心首页** (`UserCenterView.vue`)：个人信息卡片、余额卡片、功能导航、最近活动
- **我的发布页面** (`UserPublishView.vue`)：发布列表、状态筛选、编辑删除操作
- **我的申请页面** (`UserApplicationsView.vue`)：申请列表、状态跟踪、取消申请
- **我的购买页面** (`UserPurchasesView.vue`)：购买记录、订单状态、确认收货
- **消息通知页面** (`UserMessagesView.vue`)：消息列表、标记已读、批量删除
- **黑名单页面** (`UserBlacklistView.vue`)：用户黑名单、商品黑名单、取消拉黑

### 2.4.3 状态管理
- **User Store** 集中管理所有用户相关数据
- 各子页面通过 Store 获取对应数据，保持数据一致性
- 使用本地缓存减少重复请求，提高响应速度

### 2.4.4 代码示例
```javascript
// UserCenterView.vue - 获取用户中心数据
onMounted(async () => {
  try {
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const userId = storedUserInfo.userId || storedUserInfo.id || 6
    
    const response = await getUserCenter(userId)
    
    if (response.data) {
      userInfo.value = response.data
      userStore.setUserInfo(response.data)
      recentActivities.value = generateRecentActivities(response.data.tags)
    }
  } catch (error) {
    console.error('获取用户中心数据失败:', error)
    // 降级处理：使用本地存储数据
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (storedUserInfo && storedUserInfo.username) {
      userInfo.value = storedUserInfo
    }
    recentActivities.value = userStore.recentActivities
  }
})
```

## 2.5 聊天模块
### 2.5.1 功能描述
- 实时一对一聊天
- 消息发送与接收（文本、图片、表情）
- 聊天记录查看
- 在线状态显示
- 未读消息计数

### 2.5.2 技术实现
- **WebSocket 连接**：使用 SockJS + STOMP 协议
- **消息队列**：STOMP 订阅/发布模式
- **心跳检测**：保持连接活跃
- **断线重连**：网络异常时自动重连

### 2.5.3 组件设计
- **聊天页面** (`ChatView.vue`)：联系人列表、聊天窗口、消息输入框
- **聊天卡片组件**：单个聊天会话展示
- **消息气泡组件**：消息内容展示（区分发送/接收）

### 2.5.4 状态管理
- 聊天状态独立管理，不与其他 Store 耦合
- 消息记录本地存储，支持离线查看
- 未读计数全局同步（通过 User Store）

## 2.6 支付模块
### 2.6.1 功能描述
- 账户余额充值（金额选择、支付方式）
- 交易支付（兼职报酬支付、商品购买支付）
- 支付记录查询
- 退款处理

### 2.6.2 组件设计
- **充值页面** (`PaymentView.vue`)：充值金额选择、支付方式选择、确认支付
- **支付记录页面**：支付历史列表、状态筛选
- **支付弹窗组件**：通用支付弹窗，支持多种支付场景

### 2.6.3 安全考虑
- 支付密码验证（前端加密传输）
- 防重复提交（按钮禁用、请求锁）
- 支付结果校验（前端验证 + 后端回调）

### 2.6.4 代码示例
```javascript
// 充值处理
const handleRecharge = async () => {
  if (!rechargeAmount.value || isNaN(rechargeAmount.value) || parseFloat(rechargeAmount.value) <= 0) {
    ElMessage.error('请输入有效的充值金额')
    return
  }
  
  try {
    const amount = parseFloat(rechargeAmount.value)
    const userId = userInfo.value?.userId || userInfo.value?.id || JSON.parse(localStorage.getItem('userInfo') || '{}').userId
    
    await request({
      url: '/user/balance/recharge',
      method: 'post',
      data: { amount, userId }
    })
    
    // 更新本地余额
    if (userInfo.value) {
      userInfo.value.balance = (userInfo.value.balance || 0) + amount
    }
    ElMessage.success(`充值成功，金额：¥${amount}`)
    rechargeDialogVisible.value = false
    rechargeAmount.value = ''
  } catch (error) {
    console.error('充值失败:', error)
    ElMessage.error('充值失败，请稍后重试')
  }
}
```

## 2.7 管理员后台模块
### 2.7.1 功能描述
- 用户管理：用户列表、封禁/解封、信誉分调整
- 兼职管理：兼职审核、上下架、违规处理
- 二手商品管理：商品审核、上下架、违规处理
- 举报管理：举报列表、处理结果反馈
- 数据统计：平台数据仪表盘

### 2.7.2 组件设计
- **管理后台首页** (`AdminView.vue`)：功能导航、数据概览
- **用户管理页面** (`AdminUsersView.vue`)：用户列表、搜索、批量操作
- **兼职管理页面** (`AdminPartTimeView.vue`)：兼职列表、审核操作、详情查看
- **二手管理页面** (`AdminSecondHandView.vue`)：商品列表、审核操作、详情查看
- **举报管理页面** (`AdminReportsView.vue`)：举报列表、处理流程、结果反馈

### 2.7.3 权限控制
- 路由级权限：管理员路由需要特殊权限才能访问
- 组件级权限：敏感操作按钮根据权限显示/隐藏
- API 级权限：前端传递用户角色，后端验证

## 2.8 信誉与评价系统
### 2.8.1 功能描述
- 信誉分展示与计算规则说明
- 评价发布（对兼职雇主、二手卖家）
- 评价查看（收到的评价、给出的评价）
- 信誉分变动记录

### 2.8.2 组件设计
- **信誉分展示组件**：圆形进度条、等级标签
- **评价列表组件**：评价卡片、评分星星、回复功能
- **评价发布弹窗**：评分选择、评价内容、提交按钮

### 2.8.3 状态管理
- 用户信誉分在 User Store 中管理
- 评价数据单独存储，按需加载
- 评价统计信息（平均分、评价数量）实时更新

## 2.9 消息与通知系统
### 2.9.1 功能描述
- 系统通知（审核结果、违规警告）
- 交易消息（申请状态更新、购买确认）
- 聊天消息（实时聊天）
- 未读消息计数
- 消息分类与筛选

### 2.9.2 技术实现
- **实时消息**：WebSocket 推送
- **系统消息**：HTTP 轮询或 WebSocket
- **本地存储**：消息记录本地缓存
- **推送通知**：浏览器 Notification API（用户授权后）

### 2.9.3 组件设计
- **消息列表页面** (`UserMessagesView.vue`)：消息分类、标记已读、批量删除
- **消息详情组件**：消息内容展示、相关操作
- **通知徽章组件**：未读计数显示，悬浮提示

## 2.10 缓存与数据管理
### 2.10.1 缓存策略
- **API 响应缓存**：首页推荐数据缓存 3 分钟，减少服务器压力
- **本地数据缓存**：用户信息、配置数据长期缓存
- **图片缓存**：使用浏览器缓存机制，配置合适缓存头

### 2.10.2 数据同步
- **乐观更新**：用户操作后立即更新 UI，再发送请求
- **错误回滚**：请求失败时恢复原始状态
- **数据过期**：缓存数据设置过期时间，定期清理

### 2.10.3 离线支持
- **本地存储**：关键数据（用户信息、聊天记录）本地持久化
- **操作队列**：网络恢复后同步离线操作（如消息发送）
- **状态提示**：网络状态检测，离线时显示提示

### 2.10.4 代码示例
```javascript
// 首页数据缓存实现
const loadData = async () => {
  const cacheExpiry = 3 * 60 * 1000 // 3分钟缓存
  const now = Date.now()
  
  // 检查兼职数据缓存
  const jobsCacheKey = 'homeRecommendedJobs'
  const jobsCacheTimeKey = 'homeRecommendedJobs_time'
  const cachedJobs = localStorage.getItem(jobsCacheKey)
  const cachedJobsTime = localStorage.getItem(jobsCacheTimeKey)
  
  if (cachedJobs && cachedJobsTime && (now - parseInt(cachedJobsTime)) < cacheExpiry) {
    console.log('从缓存中获取推荐兼职数据')
    recommendedJobs.value = JSON.parse(cachedJobs)
  } else {
    // 从API获取数据
    const jobsRes = await getPartTimeList({...})
    if (jobsRes.data && jobsRes.data.list) {
      recommendedJobs.value = jobsRes.data.list.map(job => ({...}))
      // 存入缓存
      localStorage.setItem(jobsCacheKey, JSON.stringify(recommendedJobs.value))
      localStorage.setItem(jobsCacheTimeKey, now.toString())
    }
  }
}
```

## 2.11 总结
前端模块设计实现了校园港湾平台的所有核心功能，每个模块都遵循组件化、状态集中管理、API 抽象的设计原则。通过合理的目录结构、清晰的代码组织、完善的错误处理和性能优化，确保了应用的可维护性、可扩展性和用户体验。

关键设计亮点：
1. **模块化 Store 设计**：每个核心功能模块有独立的 Store，状态管理清晰
2. **统一的 API 层**：请求拦截、响应处理、错误提示统一封装
3. **组件化开发**：页面组件与业务组件分离，提高复用性
4. **性能优化**：路由懒加载、数据缓存、图片优化等多方面优化
5. **安全防护**：输入验证、Token 管理、XSS 防护等多层安全措施
6. **响应式设计**：全面支持移动端，提供良好的跨设备体验

通过以上设计，前端应用能够高效、稳定地支持校园兼职与二手交易平台的日常运营，为用户提供安全、便捷、流畅的服务体验。