import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/user/user/register',
    method: 'post',
    data
  })
}

export function getUserInfo(id) {
  return request({
    url: `/user/user/info/${id}`,
    method: 'get'
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/user/user/update',
    method: 'post',
    data
  })
}

export function changePassword(data) {
  return request({
    url: '/user/user/password',
    method: 'put',
    data
  })
}

export function logout() {
  return request({
    url: '/user/user/logout',
    method: 'post'
  })
}