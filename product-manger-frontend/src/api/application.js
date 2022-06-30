import request from '@/utils/request'

export async function getPage(params) {
  return request({
    url: '/application-token/page',
    method: 'get',
    params,
  })
}

export async function addApplication(data) {
  return request({
    url: '/application-token/add-application',
    method: 'post',
    data,
  })
}

export async function deleteApplicationToken(id) {
  return request({
    url: '/application-token/' + id,
    method: 'delete',
  })
}
