import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
  withCredentials: true
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data

    if (res.code !== 1) {
      ElMessage.error({ message: res.msg || '请求失败', duration: 1500 })

      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        window.location.href = '/user/user/login'
      }

      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res.data
    }
  },
  error => {
    console.error('响应错误:', error)

    if (error.response) {
      switch (error.response.status) {
        case 400:
          ElMessage.error({ message: '请求参数错误', duration: 1500 })
          break
        case 401:
          ElMessage.error({ message: '未授权，请重新登录', duration: 1500 })
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          window.location.href = '/user/user/login'
          break
        case 403:
          ElMessage.error({ message: '拒绝访问', duration: 1500 })
          break
        case 404:
          ElMessage.error({ message: '请求的资源不存在', duration: 1500 })
          break
        case 500:
          ElMessage.error({ message: '服务器错误', duration: 1500 })
          break
        case 502:
          ElMessage.error({ message: '网关错误', duration: 1500 })
          break
        case 503:
          ElMessage.error({ message: '服务不可用', duration: 1500 })
          break
        case 504:
          ElMessage.error({ message: '网关超时', duration: 1500 })
          break
        default:
          ElMessage.error({ message: `请求失败: ${error.response.status}`, duration: 1500 })
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error({ message: '请求超时', duration: 1500 })
    } else {
      ElMessage.error({ message: '网络错误，请检查网络连接', duration: 1500 })
    }

    return Promise.reject(error)
  }
)

export default request
