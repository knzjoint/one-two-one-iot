import request from '@/utils/request'

export async function getTopicList(productId) {
  return request({
    url: '/topic/list/' + productId,
    method: 'get',
  })
}

export async function getDeviceTopicList(deviceId) {
  return request({
    url: '/topic/list/device/' + deviceId,
    method: 'get',
  })
}

export async function getPage(params) {
  return request({
    url: '/product/page',
    method: 'get',
    params,
  })
}

export async function addTopic(data) {
  return request({
    url: '/topic/add-topic',
    method: 'post',
    data,
  })
}

export async function deleteTopicOne(id) {
  return request({
    url: '/topic/' + id,
    method: 'delete',
  })
}

export async function updateTopic(id, data) {
  return request({
    url: '/topic/' + id,
    method: 'put',
    data
  })
}
