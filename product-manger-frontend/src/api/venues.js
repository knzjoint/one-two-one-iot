import request from '@/utils/request'

export async function getVenuesList(id) {
  return request({
    url: '/venues/list',
    method: 'get',
  })
}

export async function addVenues(data) {
  return request({
    url: '/venues/add',
    method: 'post',
    data,
  })
}

export async function getVenuesThings(id) {
  return request({
    url: '/venues/things/' + id,
    method: 'get',
  })
}

export async function deleteVenues(id) {
  return request({
    url: '/venues/' + id,
    method: 'delete',
  })
}
