import request from '@/utils/request'

export function getSecondHandList(params) {
  return request({
    url: '/second-hand/list',
    method: 'get',
    params
  })
}

export function getSecondHandDetail(id) {
  return request({
    url: `/second-hand/item/${id}`,
    method: 'get'
  })
}

export function createSecondHandItem(data) {
  return request({
    url: '/second-hand/products',
    method: 'post',
    data
  })
}

export function updateSecondHandItem(id, data) {
  return request({
    url: `/second-hand/${id}`,
    method: 'put',
    data
  })
}

export function buySecondHandItem(id) {
  return request({
    url: `/second-hand/${id}/buy`,
    method: 'post'
  })
}

export function getMyItems(params) {
  return request({
    url: '/second-hand/my-items',
    method: 'get',
    params
  })
}

export function getMySecondHandItems(id) {
  return request({
    url: `/second-hand/myItems/${id}`,
    method: 'get'
  })
}

export function updateSecondHandStatus(id, status) {
  return request({
    url: `/second-hand/${id}/${status}`,
    method: 'put'
  })
}

export function getSecondHandItemDetail(id) {
  return request({
    url: `/second-hand/item/${id}`,
    method: 'get'
  })
}

export function changeSecondHandItem(data) {
  return request({
    url: '/second-hand/changeItem',
    method: 'post',
    data
  })
}

export function deleteSecondHandItem(id) {
  return request({
    url: `/second-hand/deleteItem/${id}`,
    method: 'delete'
  })
}

export function getHotSecondHandItems() {
  return request({
    url: '/second-hand/hot',
    method: 'get'
  })
}

export function addSecondHandFavorite(id) {
  return request({
    url: `/second-hand/favorite/${id}`,
    method: 'post'
  })
}

export function removeSecondHandFavorite(id) {
  return request({
    url: `/second-hand/favorite/${id}`,
    method: 'delete'
  })
}

export function checkSecondHandFavorite(id) {
  return request({
    url: `/second-hand/favorite/${id}`,
    method: 'get'
  })
}
