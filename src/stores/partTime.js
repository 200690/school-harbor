import { defineStore } from 'pinia'
import { getPartTimeList, getPartTimeDetail, createPartTimeJob, updatePartTimeJob, deletePartTimeJob, applyPartTimeJob, getMyApplications } from '@/api/partTime'

export const usePartTimeStore = defineStore('partTime', {
  state: () => ({
    jobList: [],
    jobDetail: null,
    myApplications: [],
    loading: false,
    total: 0,
    currentPage: 1,
    pageSize: 10
  }),

  getters: {
    jobCount: (state) => state.total,
    getJobById: (state) => (id) => {
      return state.jobList.find(job => job.id === parseInt(id))
    }
  },

  actions: {
    async getJobListAction(params) {
      this.loading = true
      try {
        // 生成缓存键
        const cacheKey = `partTimeList_${JSON.stringify(params)}`
        const cacheTimeKey = `${cacheKey}_time`
        
        // 检查本地缓存
        const cachedData = localStorage.getItem(cacheKey)
        const cachedTime = localStorage.getItem(cacheTimeKey)
        const now = Date.now()
        const cacheExpiry = 3 * 60 * 1000 // 3分钟缓存
        
        if (cachedData && cachedTime && (now - parseInt(cachedTime)) < cacheExpiry) {
          console.log('从缓存中获取兼职列表数据')
          const parsedData = JSON.parse(cachedData)
          this.jobList = parsedData.list || []
          this.total = parsedData.total || 0
          this.currentPage = parsedData.currentPage || 1
          this.pageSize = parsedData.pageSize || 10
          return { data: parsedData }
        }
        
        // 从API获取数据
        const res = await getPartTimeList(params)
        this.jobList = res.data.list || []
        this.total = res.data.total || 0
        this.currentPage = res.data.currentPage || 1
        this.pageSize = res.data.pageSize || 10
        
        // 存入本地缓存
        const cacheData = {
          list: this.jobList,
          total: this.total,
          currentPage: this.currentPage,
          pageSize: this.pageSize
        }
        localStorage.setItem(cacheKey, JSON.stringify(cacheData))
        localStorage.setItem(cacheTimeKey, now.toString())
        console.log('兼职列表数据已存入缓存')
        
        return res
      } finally {
        this.loading = false
      }
    },

    async getJobDetailAction(id) {
      this.loading = true
      try {
        const res = await getPartTimeDetail(id)
        this.jobDetail = res.data
        return res
      } finally {
        this.loading = false
      }
    },

    async createJobAction(data) {
      return await createPartTimeJob(data)
    },

    async updateJobAction(id, data) {
      return await updatePartTimeJob(id, data)
    },

    async deleteJobAction(id) {
      return await deletePartTimeJob(id)
    },

    async applyJobAction(id) {
      return await applyPartTimeJob(id)
    },

    async getMyApplicationsAction(params) {
      this.loading = true
      try {
        const res = await getMyApplications(params)
        this.myApplications = res.data.list || []
        this.total = res.data.total || 0
        return res
      } finally {
        this.loading = false
      }
    },

    resetJobList() {
      this.jobList = []
      this.total = 0
      this.currentPage = 1
    },

    resetJobDetail() {
      this.jobDetail = null
    },

    clearCache() {
      this.jobList = []
      this.jobDetail = null
      this.myApplications = []
      this.total = 0
      this.currentPage = 1
      this.pageSize = 10
      console.log('[PartTimeStore] 已清除所有缓存')
    }
  }
})
