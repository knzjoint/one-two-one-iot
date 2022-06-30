import request from '@/utils/request'

export async function getWorkFlowNode(id) {
  return request({
    url: '/things-linkage/work-flow/' + id,
    method: 'get',
  })
}

export async function updateRules(id, data) {
  return request({
    url: '/things-linkage/update-rules/' + id,
    method: 'put',
    data,
  })
}


export async function getThingsLinkage(id) {
  return request({
    url: '/things-linkage/' + id,
    method: 'get',
  })
}

/**
 * 获取venues列表
 */
export async function getVenuesThingsLinkage(params) {
  return request({
    url: '/things-linkage/page',
    method: 'get',
    params,
  })
}

/**
 * 添加一个规则场景
 */
export async function addRules(data) {
  return request({
    url: '/things-linkage/add',
    method: 'post',
    data,
  })
}

/**
 * 删除一个联动场景
 * @param id
 * @returns {Promise<void>}
 */
export async function deleteLinkage(id) {
  return request({
    url: '/things-linkage/' + id,
    method: 'delete',
  })
}
