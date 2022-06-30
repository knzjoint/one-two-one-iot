import request from '@/utils/request'

export async function getDataTypeList() {
  return request({
    url: '/data-type/list',
    method: 'get',
  })
}
