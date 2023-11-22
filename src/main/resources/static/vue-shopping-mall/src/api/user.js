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

export function update(user) {
  return request({
    url: '/backsystem/users',
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

export function deleteByIds(id) {
  return request({
    url: '/backsystem/users/deleteBatch',
    method: 'post',
    data: id,
  })
}

export function selectById(id) {
  return request({
    url: '/backsystem/users/'+id,
    method: 'get',
  })
}

