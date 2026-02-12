import { defineStore } from 'pinia'
import { login, register, getUserInfo, updateUserInfo, changePassword, logout } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null') || {
      id: 1,
      username: '小明',
      phone: '138****8888',
      email: 'xiaoming@example.com',
      avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20friendly%20student&image_size=square',
      registerTime: '2026-01-01',
      balance: 0
    },
    isLoggedIn: !!localStorage.getItem('token') || true, // 默认登录状态
    // 用户相关数据
    userPosts: [
      {
        id: 1,
        title: '大学英语四级词汇书',
        type: 'second-hand',
        status: 'published',
        publishTime: '2026-02-09'
      },
      {
        id: 2,
        title: '校园超市收银员兼职',
        type: 'part-time',
        status: 'expired',
        publishTime: '2026-02-01'
      }
    ],
    userApplications: [
      {
        id: 1,
        jobTitle: '校园超市收银员',
        status: 'approved',
        applyTime: '2026-02-10'
      },
      {
        id: 2,
        jobTitle: '图书馆整理员',
        status: 'pending',
        applyTime: '2026-02-08'
      }
    ],
    userPurchases: [
      {
        id: 1,
        itemTitle: '篮球',
        price: 80,
        purchaseTime: '2026-02-05',
        status: 'completed'
      }
    ],
    userFavorites: [
      {
        id: 1,
        itemId: 2,
        itemTitle: '笔记本电脑',
        type: 'second-hand',
        favoriteTime: '2026-02-10'
      }
    ],
    userMessages: [
      {
        id: 1,
        title: '兼职申请通过',
        content: '您的校园超市收银员兼职申请已通过',
        time: '2026-02-10 09:15',
        read: false
      },
      {
        id: 2,
        title: '商品已售出',
        content: '您发布的大学英语四级词汇书已售出',
        time: '2026-02-09 16:30',
        read: true
      }
    ],
    recentActivities: [
      {
        id: 1,
        type: 'application',
        title: '申请了兼职：校园超市收银员',
        time: '2026-02-10 10:30'
      },
      {
        id: 2,
        type: 'publish',
        title: '发布了二手物品：大学英语四级词汇书',
        time: '2026-02-09 15:20'
      },
      {
        id: 3,
        type: 'message',
        title: '收到消息：您的兼职申请已通过',
        time: '2026-02-08 09:15'
      }
    ]
  }),

  getters: {
    userId: (state) => state.userInfo?.id || null,
    username: (state) => state.userInfo?.username || '',
    avatar: (state) => state.userInfo?.avatar || '',
    phone: (state) => state.userInfo?.phone || '',
    email: (state) => state.userInfo?.email || '',
    unreadMessageCount: (state) => state.userMessages.filter(msg => !msg.read).length
  },

  actions: {
    setToken(token) {
      this.token = token
      this.isLoggedIn = !!token
      if (token) {
        localStorage.setItem('token', token)
      } else {
        localStorage.removeItem('token')
      }
    },

    setUserInfo(userInfo) {
      this.userInfo = userInfo
      if (userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
      } else {
        localStorage.removeItem('userInfo')
      }
    },

    async loginAction(loginForm) {
      try {
        const res = await login(loginForm)
        this.setToken(res.data.token)
        this.setUserInfo(res.data.userInfo)
        return res
      } catch (error) {
        // 模拟登录成功
        this.setToken('mock-token-' + Date.now())
        this.setUserInfo(this.userInfo)
        return { data: { token: 'mock-token-' + Date.now(), userInfo: this.userInfo } }
      }
    },

    async registerAction(registerForm) {
      try {
        const res = await register(registerForm)
        return res
      } catch (error) {
        // 模拟注册成功
        return { data: { success: true } }
      }
    },

    async getUserInfoAction() {
      try {
        const res = await getUserInfo()
        this.setUserInfo(res.data)
        return res
      } catch (error) {
        // 模拟获取用户信息成功
        return { data: this.userInfo }
      }
    },

    async updateUserInfoAction(data) {
      try {
        const res = await updateUserInfo(data)
        await this.getUserInfoAction()
        return res
      } catch (error) {
        // 模拟更新用户信息成功
        this.userInfo = { ...this.userInfo, ...data }
        this.setUserInfo(this.userInfo)
        return { data: { success: true } }
      }
    },

    async changePasswordAction(data) {
      try {
        const res = await changePassword(data)
        return res
      } catch (error) {
        // 模拟修改密码成功
        return { data: { success: true } }
      }
    },

    async logoutAction() {
      try {
        await logout()
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.setToken('')
        this.setUserInfo(null)
      }
    },

    checkLogin() {
      if (!this.token) {
        return false
      }
      return true
    },

    // 模拟获取用户发布的信息
    async getUserPosts() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.userPosts })
        }, 300)
      })
    },

    // 模拟获取用户申请的信息
    async getUserApplications() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.userApplications })
        }, 300)
      })
    },

    // 模拟获取用户购买的信息
    async getUserPurchases() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.userPurchases })
        }, 300)
      })
    },

    // 模拟获取用户收藏的信息
    async getUserFavorites() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.userFavorites })
        }, 300)
      })
    },

    // 模拟获取用户消息
    async getUserMessages() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.userMessages })
        }, 300)
      })
    },

    // 模拟获取最近活动
    async getRecentActivities() {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ data: this.recentActivities })
        }, 300)
      })
    },

    // 模拟添加收藏
    async addFavorite(itemId, itemTitle, type) {
      return new Promise((resolve) => {
        setTimeout(() => {
          const newFavorite = {
            id: Date.now(),
            itemId,
            itemTitle,
            type,
            favoriteTime: new Date().toISOString().split('T')[0]
          }
          this.userFavorites.push(newFavorite)
          resolve({ data: { success: true } })
        }, 300)
      })
    },

    // 模拟取消收藏
    async removeFavorite(itemId) {
      return new Promise((resolve) => {
        setTimeout(() => {
          this.userFavorites = this.userFavorites.filter(fav => fav.itemId !== itemId)
          resolve({ data: { success: true } })
        }, 300)
      })
    },

    // 模拟标记消息为已读
    async markMessageAsRead(messageId) {
      return new Promise((resolve) => {
        setTimeout(() => {
          const message = this.userMessages.find(msg => msg.id === messageId)
          if (message) {
            message.read = true
          }
          resolve({ data: { success: true } })
        }, 300)
      })
    }
  }
})
