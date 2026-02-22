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
    url: `/part-time/deleteJob/${id}`,
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

export function publishPartTimeJob(data) {
  return request({
    url: '/part-time/newJob',
    method: 'post',
    data
  })
}

export function getMyPartTimeJobs(id) {
  return request({
    url: `/part-time/myJobs/${id}`,
    method: 'get'
  })
}

export function updatePartTimeStatus(id, status) {
  return request({
    url: `/part-time/${id}/${status}`,
    method: 'put'
  })
}

export function getPartTimeJobDetail(id) {
  return request({
    url: `/part-time/job/${id}`,
    method: 'get'
  })
}

export function changePartTimeJob(data) {
  return request({
    url: '/part-time/changeJob',
    method: 'post',
    data
  })
}
