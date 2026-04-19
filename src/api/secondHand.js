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

export function getMySecondHandItems(data) {
  return request({
    url: '/second-hand/myItems',
    method: 'post',
    data
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

export function addSecondHandFavorite(itemId) {
  return request({
    url: `/second-hand/favorite/addFavorite/${itemId}`,
    method: 'post'
  })
}

export function removeSecondHandFavorite(id) {
  return request({
    url: `/second-hand/favorite/removeFavorite/${id}`,
    method: 'delete'
  })
}

export function checkSecondHandFavorite(id) {
  return request({
    url: `/second-hand/favorite/${id}`,
    method: 'get'
  })
}

export function getMySecondHandFavorites(data) {
  return request({
    url: '/second-hand/favorite/MyFavorites',
    method: 'post',
    data
  })
}

export function getMyOrders(data) {
  return request({
    url: '/second-hand/order/myOrders',
    method: 'post',
    data
  })
}

export function confirmReceipt(id) {
  return request({
    url: `/second-hand/order/confirmReceipt/${id}`,
    method: 'post'
  })
}

export function cancelOrder(id) {
  return request({
    url: `/second-hand/order/cancelOrder/${id}`,
    method: 'post'
  })
}

export function cancelTradeItem(id) {
  return request({
    url: `/second-hand/order/cancelItem/${id}`,
    method: 'post'
  })
}

// 消息相关接口
export function sendMessage(data) {
  return request({
    url: '/second-hand/messages/send',
    method: 'post',
    data
  })
}

export function getMessageList(params) {
  return request({
    url: '/second-hand/messages/list',
    method: 'get',
    params
  })
}

export function getChatHistory(otherUserId, currentUserId, page = 1, size = 20) {
  return request({
    url: `/second-hand/messages/chat/${otherUserId}`,
    method: 'get',
    params: {
      page,
      size,
      currentUserId
    }
  })
}

export function getUnreadCount() {
  return request({
    url: '/second-hand/messages/unread/count',
    method: 'get'
  })
}

export function markAsRead(senderId) {
  return request({
    url: `/second-hand/messages/read/${senderId}`,
    method: 'put'
  })
}

export function deleteMessage(messageId) {
  return request({
    url: `/second-hand/messages/${messageId}`,
    method: 'delete'
  })
}

export function getAllSecondHandItems(data) {
  return request({
    url: '/second-hand/getAll',
    method: 'post',
    data
  })
}

export function saveRefuseMessage(id, text) {
  return request({
    url: `/message/save/${id}/${text}`,
    method: 'post'
  })
}

export function refundOrder(id) {
  return request({
    url: `/second-hand/order/refund/${id}`,
    method: 'post'
  })
}
