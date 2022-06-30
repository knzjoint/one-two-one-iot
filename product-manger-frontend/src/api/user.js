import request from '@/utils/request'
import { tokenName } from '@/config'

export async function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  })
}

export async function socialLogin(data) {
  return request({
    url: '/socialLogin',
    method: 'post',
    data,
  })
}

export function getUserInfo(accessToken) {
  //此处为了兼容mock.js使用data传递accessToken，如果使用mock可以走headers
  return request({
    url: '/user/user-info',
    method: 'get',
    data: {
      [tokenName]: accessToken,
    },
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'get',
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data,
  })
}

export function registerCode(data) {
  return request({
    url: '/user/register/code',
    method: 'post',
    data,
  })
}
