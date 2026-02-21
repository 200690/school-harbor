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

export function deleteSecondHandItem(id) {
  return request({
    url: `/second-hand/${id}`,
    method: 'delete'
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
