import request from '@/utils/request'

export function getPartTimeList(params) {
  return request({
    url: '/part-time/list',
    method: 'get',
    params
  })
}

export function getPartTimeDetail(id) {
  return request({
    url: `/part-time/item/${id}`,
    method: 'get'
  })
}

export function createPartTimeJob(data) {
  return request({
    url: '/part-time',
    method: 'post',
    data
  })
}

export function updatePartTimeJob(id, data) {
  return request({
    url: `/part-time/${id}`,
    method: 'put',
    data
  })
}

export function deletePartTimeJob(id) {
  return request({
    url: `/part-time/${id}`,
    method: 'delete'
  })
}

export function applyPartTimeJob(id) {
  return request({
    url: `/part-time/${id}/apply`,
    method: 'post'
  })
}

export function getMyApplications(params) {
  return request({
    url: '/part-time/my-applications',
    method: 'get',
    params
  })
}
