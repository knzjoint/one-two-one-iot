import request from '@/utils/request'

export async function getClientThings(id) {
  return request({
    url: '/things/list/' + id,
    method: 'get',
  })
}

export async function addThings(data) {
  return request({
    url: '/things/add',
    method: 'post',
    data,
  })
}

export async function controlThings(data) {
  return request({
    url: '/things/control-things',
    method: 'post',
    data,
  })
}

export async function deleteThings(id) {
  return request({
    url: '/things/' + id,
    method: 'delete',
  })
}

export async function copyThingsTopic(id) {
  return request({
    url: '/things/copy-topic/' + id,
    method: 'get',
  })
}
