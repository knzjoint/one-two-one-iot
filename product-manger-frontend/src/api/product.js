import request from '@/utils/request'

export async function getProductOne(id) {
  return request({
    url: '/product/' + id,
    method: 'get',
  })
}

export async function getProductList() {
  return request({
    url: '/product/list',
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

export async function addProduct(data) {
  return request({
    url: '/product/add-product',
    method: 'post',
    data,
  })
}

export async function deleteProduct(id) {
  return request({
    url: '/product/' + id,
    method: 'delete',
  })
}
