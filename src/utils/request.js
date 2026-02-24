import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 监听localStorage变化，追踪token清除原因
const originalSetItem = localStorage.setItem
const originalRemoveItem = localStorage.removeItem

localStorage.setItem = function (key, value) {
  console.log(`[localStorage] SET ${key} = ${value.substring(0, 50)}${value.length > 50 ? '...' : ''}`)
  console.trace('localStorage.setItem 调用栈')
  return originalSetItem.call(this, key, value)
}

localStorage.removeItem = function (key) {
  console.log(`[localStorage] REMOVE ${key}`)
  console.trace('localStorage.removeItem 调用栈')
  return originalRemoveItem.call(this, key)
}

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
  withCredentials: true
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `${token}`
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
      if (res.code === 401) {
        console.error('[Token清除] 响应码401 - token过期')
        console.error('[Token清除] 响应数据:', res)
        ElMessage.error({ message: res.msg || '请求失败', duration: 1500 })
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        // 触发自定义事件通知组件更新登录状态
        window.dispatchEvent(new CustomEvent('token-expired'))

        // 获取当前路径
        const currentPath = router.currentRoute.value.path

        // 非登录注册页面且不是首页、兼职、二手交易页面时，强制跳转到登录页
        if (!currentPath.startsWith('/user/user/login') &&
          !currentPath.startsWith('/user/user/register') &&
          currentPath !== '/index' &&
          currentPath !== '/part-time' &&
          currentPath !== '/second-hand') {
          router.push('/user/user/login')
        }
      } else if (res.code === 0 && res.msg === '用户状态异常') {
        console.error('[用户状态异常] 用户已被封禁')
        // 不显示错误消息，让组件自己处理
        // 不清除token，因为可能是服务提供者被封禁，而不是当前用户
        // 触发自定义事件通知组件用户被封禁
        window.dispatchEvent(new CustomEvent('user-banned'))
      } else {
        ElMessage.error({ message: res.msg || '请求失败', duration: 1500 })
      }

      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)

    if (error.response) {
      switch (error.response.status) {
        case 400:
          ElMessage.error({ message: '请求参数错误', duration: 1500 })
          break
        case 401: {
          console.error('[Token清除] HTTP状态码401 - 未授权')
          console.error('[Token清除] 错误响应:', error.response)
          ElMessage.error({ message: '登录已过期，请重新登录', duration: 1500 })
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          // 触发自定义事件通知组件更新登录状态
          window.dispatchEvent(new CustomEvent('token-expired'))
          
          // 获取当前路径
          const currentPath = router.currentRoute.value.path
          
          // 非登录注册页面且不是首页、兼职、二手交易页面时，强制跳转到登录页
          if (!currentPath.startsWith('/user/user/login') && 
              !currentPath.startsWith('/user/user/register') &&
              currentPath !== '/index' &&
              currentPath !== '/part-time' &&
              currentPath !== '/second-hand') {
            router.push('/user/user/login')
          }
          break
        }
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
