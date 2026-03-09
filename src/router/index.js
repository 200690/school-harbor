import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PartTimeListView from '../views/PartTimeListView.vue'
import PartTimeDetailView from '../views/PartTimeDetailView.vue'
import SecondHandListView from '../views/SecondHandListView.vue'
import SecondHandDetailView from '../views/SecondHandDetailView.vue'
import ChatView from '../views/ChatView.vue'

const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    name: 'home',
    component: HomeView
  },
  {
    path: '/part-time',
    name: 'part-time',
    component: PartTimeListView
  },
  {
    path: '/item/:id',
    name: 'part-time-detail',
    component: PartTimeDetailView
  },
  {
    path: '/item/edit/:id',
    name: 'part-time-edit',
    component: () => import('../views/PartTimeEditView.vue')
  },
  {
    path: '/part-time/comments',
    name: 'part-time-comments',
    component: () => import('../views/PartTimeCommentsView.vue')
  },
  {
    path: '/second-hand',
    name: 'second-hand',
    component: SecondHandListView
  },
  {
    path: '/second-hand/detail/:id',
    name: 'second-hand-detail',
    component: SecondHandDetailView
  },
  {
    path: '/second-hand/comments/:id',
    name: 'second-hand-comments',
    component: () => import('../views/SecondHandCommentsView.vue')
  },
  {
    path: '/second-hand/edit/:id',
    name: 'second-hand-edit',
    component: () => import('../views/SecondHandEditView.vue')
  },
  {
    path: '/chat',
    name: 'chat',
    component: ChatView
  },
  {
    path: '/user/user/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/user/user/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/user/user/center',
    name: 'user-center',
    component: () => import('../views/UserCenterView.vue')
  },
  {
    path: '/user/user/info',
    name: 'user-info',
    component: () => import('../views/UserInfoView.vue')
  },
  {
    path: '/user/user/publish',
    name: 'user-publish',
    component: () => import('../views/UserPublishView.vue')
  },
  {
    path: '/user/user/applications',
    name: 'user-applications',
    component: () => import('../views/UserApplicationsView.vue')
  },
  {
    path: '/manage/applications',
    name: 'manage-applications',
    component: () => import('../views/ManageApplicationsView.vue')
  },
  {
    path: '/user/user/purchases',
    name: 'user-purchases',
    component: () => import('../views/UserPurchasesView.vue')
  },
  {
    path: '/user/user/messages',
    name: 'user-messages',
    component: () => import('../views/UserMessagesView.vue')
  },
  {
    path: '/user/user/evaluations',
    name: 'user-evaluations',
    component: () => import('../views/UserEvaluationsView.vue')
  },
  {
    path: '/user/user/favorites',
    name: 'user-favorites',
    component: () => import('../views/UserFavoritesView.vue')
  },
  {
    path: '/user/user/settings',
    name: 'user-settings',
    component: () => import('../views/UserSettingsView.vue')
  },
  {
    path: '/user/user/follows',
    name: 'user-follows',
    component: () => import('../views/UserFollowsView.vue')
  },
  {
    path: '/user/user/blacklist',
    name: 'user-blacklist',
    component: () => import('../views/UserBlacklistView.vue')
  },
  {
    path: '/user/user/report/:id',
    name: 'user-report',
    component: () => import('../views/UserReportView.vue')
  },
  {
    path: '/user/user/after-sales/:id',
    name: 'user-after-sales',
    component: () => import('../views/UserAfterSalesView.vue')
  },
  {
    path: '/user/profile/:id',
    name: 'user-profile',
    component: () => import('../views/UserProfileView.vue')
  },
  {
    path: '/user/admin',
    name: 'user-admin',
    component: () => import('../views/UserAdminView.vue')
  },
  {
    path: '/user/admin/order/:id',
    name: 'admin-order-detail',
    component: () => import('../views/AdminOrderDetailView.vue')
  },
  {
    path: '/user/admin/user/:id',
    name: 'admin-user-detail',
    component: () => import('../views/AdminUserDetailView.vue')
  },
  {
    path: '/user/admin/goods/:id',
    name: 'admin-goods-detail',
    component: () => import('../views/AdminGoodsDetailView.vue')
  },
  {
    path: '/user/admin/dispute/:id',
    name: 'admin-dispute-detail',
    component: () => import('../views/AdminDisputeDetailView.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('../views/NotFoundView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 配置滚动行为，每次导航到新页面时滚动到顶部
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  console.log('[路由守卫] 检查登录状态:', {
    to: to.path,
    from: from.path,
    token: token ? '存在' : '不存在'
  })

  // 如果访问需要登录的页面，检查是否有token
  if (to.path.startsWith('/user/user/') && to.path !== '/user/user/login' && to.path !== '/user/user/register') {
    if (!token) {
      console.log('[路由守卫] 未登录，跳转到登录页面')
      next('/user/user/login')
      return
    } else {
      console.log('[路由守卫] 已登录，允许访问')
    }
  }

  next()
})

export default router
