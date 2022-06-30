import request from '@/utils/request'

export async function getDeviceList() {
  return request({
    url: '/device/list',
    method: 'get',
  })
}

export async function getPage(params) {
  return request({
    url: '/device/page',
    method: 'get',
    params,
  })
}

export async function addDevice(data) {
  return request({
    url: '/device/add-device',
    method: 'post',
    data,
  })
}

export async function deleteDevice(id) {
  return request({
    url: '/device/' + id,
    method: 'delete',
  })
}
