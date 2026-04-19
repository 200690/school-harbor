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
    url: `/part-time/job/${id}`,
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

export function applyPartTimeJob(data) {
  return request({
    url: '/part-time/apply',
    method: 'post',
    data
  })
}

export function getMyApplications(params) {
  return request({
    url: '/part-time/my-applications',
    method: 'get',
    params
  })
}

export function getMyApplicationsById(data) {
  return request({
    url: '/part-time/apply/my-applications',
    method: 'post',
    data
  })
}

export function publishPartTimeJob(data) {
  return request({
    url: '/part-time/newJob',
    method: 'post',
    data
  })
}

export function getMyPartTimeJobs(data) {
  return request({
    url: '/part-time/myJobs',
    method: 'post',
    data
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

export function getRecommendedPartTimeJobs() {
  return request({
    url: '/part-time/recommended',
    method: 'get'
  })
}

export function addPartTimeFavorite(partTimeId) {
  return request({
    url: `/part-time/favorite/addFavorite/${partTimeId}`,
    method: 'post'
  })
}

export function removePartTimeFavorite(id) {
  return request({
    url: `/part-time/favorite/removeFavorite/${id}`,
    method: 'delete'
  })
}

export function checkPartTimeFavorite(id) {
  return request({
    url: `/part-time/favorite/${id}`,
    method: 'get'
  })
}

export function getMyPartTimeFavorites(data) {
  return request({
    url: '/part-time/favorite/MyFavorites',
    method: 'post',
    data
  })
}

export function cancelPartTimeApplication(partTimeId) {
  return request({
    url: `/part-time/apply/cancel/${partTimeId}`,
    method: 'post'
  })
}

export function getAllPartTimeJobs(data) {
  return request({
    url: '/part-time/getAll',
    method: 'post',
    data
  })
}

export function deletePartTimeItem(id) {
  return request({
    url: `/part-time/deleteItem/${id}`,
    method: 'delete'
  })
}
