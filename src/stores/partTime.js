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
    jobCount: (state) => state.total
  },

  actions: {
    async getJobListAction(params) {
      this.loading = true
      try {
        const res = await getPartTimeList(params)
        this.jobList = res.data.list || []
        this.total = res.data.total || 0
        this.currentPage = res.data.currentPage || 1
        this.pageSize = res.data.pageSize || 10
        return res
      } catch (error) {
        throw error
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
      } catch (error) {
        throw error
      } finally {
        this.loading = false
      }
    },

    async createJobAction(data) {
      try {
        const res = await createPartTimeJob(data)
        return res
      } catch (error) {
        throw error
      }
    },

    async updateJobAction(id, data) {
      try {
        const res = await updatePartTimeJob(id, data)
        return res
      } catch (error) {
        throw error
      }
    },

    async deleteJobAction(id) {
      try {
        const res = await deletePartTimeJob(id)
        return res
      } catch (error) {
        throw error
      }
    },

    async applyJobAction(id) {
      try {
        const res = await applyPartTimeJob(id)
        return res
      } catch (error) {
        throw error
      }
    },

    async getMyApplicationsAction(params) {
      this.loading = true
      try {
        const res = await getMyApplications(params)
        this.myApplications = res.data.list || []
        this.total = res.data.total || 0
        return res
      } catch (error) {
        throw error
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
    }
  }
})
