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
    url: `/message/user/info/${id}`,
    method: 'get'
  })
}

export function getUserCenter(id) {
  return request({
    url: `/message/user/center/${id}`,
    method: 'get'
  })
}

export function getUserDetail(id) {
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

// 关注相关API
export function getMyFollows(data) {
  return request({
    url: '/user/follow/myFollow',
    method: 'post',
    data
  })
}

export function unfollowUser(userId) {
  return request({
    url: `/user/follow/unfollow/${userId}`,
    method: 'post'
  })
}

// 黑名单相关API
export function getUserBlacklist(data) {
  return request({
    url: '/user/blacklist/user/list',
    method: 'post',
    data
  })
}

export function getItemBlacklist(data) {
  return request({
    url: '/user/blacklist/item/list',
    method: 'post',
    data
  })
}

export function unblockUser(blockUserId) {
  return request({
    url: `/user/blacklist/user/unblock/${blockUserId}`,
    method: 'post'
  })
}

export function unblockItem(itemId) {
  return request({
    url: `/user/blacklist/item/remove/${itemId}`,
    method: 'post'
  })
}

export function blockUser(userId) {
  return request({
    url: `/user/blacklist/user/add/${userId}`,
    method: 'post'
  })
}

// 评价相关API
export function getMyEvaluations(data) {
  return request({
    url: '/user/evaluation/my',
    method: 'post',
    data
  })
}

// 评论相关API
export function showMyComments(type, data) {
  return request({
    url: `/comment/showMyComments/${type}`,
    method: 'post',
    data
  })
}