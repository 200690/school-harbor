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
    path: '/user/center',
    name: 'user-center',
    component: () => import('../views/UserCenterView.vue')
  },
  {
    path: '/user/info',
    name: 'user-info',
    component: () => import('../views/UserInfoView.vue')
  },
  {
    path: '/user/publish',
    name: 'user-publish',
    component: () => import('../views/UserPublishView.vue')
  },
  {
    path: '/user/applications',
    name: 'user-applications',
    component: () => import('../views/UserApplicationsView.vue')
  },
  {
    path: '/user/purchases',
    name: 'user-purchases',
    component: () => import('../views/UserPurchasesView.vue')
  },
  {
    path: '/user/messages',
    name: 'user-messages',
    component: () => import('../views/UserMessagesView.vue')
  },
  {
    path: '/user/evaluations',
    name: 'user-evaluations',
    component: () => import('../views/UserEvaluationsView.vue')
  },
  {
    path: '/user/favorites',
    name: 'user-favorites',
    component: () => import('../views/UserFavoritesView.vue')
  },
  {
    path: '/user/settings',
    name: 'user-settings',
    component: () => import('../views/UserSettingsView.vue')
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
  routes
})

export default router
