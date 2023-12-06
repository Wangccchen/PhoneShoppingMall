import request from '@/utils/request'

export function page(currentPage,pageSize) {
  return request({
    url: '/backsystem/users?currentPage='+currentPage+'&pageSize='+pageSize,
    method: 'get'
  })
}

export function add(user) {
  return request({
    url: '/backsystem/users',
    method: 'post',
    data: user
  })
}

export function register(user) {
  return request({
    url: '/users',
    method: 'post',
    data: user
  })
}

export function update(user) {
  return request({
    url: '/backsystem/users',
    method: 'put',
    data: user
  })
}

export function updateInfo(user) {
  return request({
    url: '/users',
    method: 'put',
    data: user
  })
}

export function deleteById(id) {
  return request({
    url: '/backsystem/users/delete/'+id,
    method: 'delete',
  })
}

export function cancel(id) {
  return request({
    url: '/users/delete/'+id,
    method: 'delete',
  })
}

export function deleteByIds(id) {
  return request({
    url: '/backsystem/users/deleteBatch',
    method: 'post',
    data: id,
  })
}

export function selectById(id) {
  return request({
    url: '/backsystem/users',
    method: 'get',
    params:{
      id:id
    }
  })
}

export function getInfoByName(name) {
  return request({
    url: '/users/getByUsername',
    method: 'get',
    params:{
      username:name
    }
  })
}
