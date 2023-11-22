import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/back/login',
    method: 'post',
    data
  })
}