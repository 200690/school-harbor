import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PartTimeListView from '../views/PartTimeListView.vue'
import PartTimeDetailView from '../views/PartTimeDetailView.vue'
import SecondHandListView from '../views/SecondHandListView.vue'
import SecondHandDetailView from '../views/SecondHandDetailView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/part-time',
    name: 'part-time',
    component: PartTimeListView
  },
  {
    path: '/part-time/detail/:id',
    name: 'part-time-detail',
    component: PartTimeDetailView
  },
  {
    path: '/part-time/edit/:id',
    name: 'part-time-edit',
    component: () => import('../views/PartTimeEditView.vue')
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
    path: '/second-hand/edit/:id',
    name: 'second-hand-edit',
    component: () => import('../views/SecondHandEditView.vue')
  },
  {
    path: '/user/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/user/register',
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

export default router
