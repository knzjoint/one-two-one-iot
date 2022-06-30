import request from '@/utils/request'

export async function getTopicList(productId) {
  return request({
    url: '/product-topic/list/' + productId,
    method: 'get',
  })
}

export async function getPage(params) {
  return request({
    url: '/product-topic/page',
    method: 'get',
    params,
  })
}

export async function addTopic(data) {
  return request({
    url: '/product-topic/add-topic',
    method: 'post',
    data,
  })
}

export async function deleteTopicOne(id) {
  return request({
    url: '/product-topic/' + id,
    method: 'delete',
  })
}

export async function updateTopic(id, data) {
  return request({
    url: '/product-topic/' + id,
    method: 'put',
    data
  })
}
