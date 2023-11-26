import request from '@/utils/request'

export function page(currentPage,pageSize) {
  return request({
    url: '/backsystem/products?currentPage='+currentPage+'&pageSize='+pageSize,
    method: 'get'
  })
}

export function allProducts() {
  return request({
    url: '/products/allProducts',
    method: 'get'
  })
}

export function add(product) {
  return request({
    url: '/backsystem/products',
    method: 'post',
    data: product
  })
}

export function update(product) {
  return request({
    url: '/backsystem/products',
    method: 'put',
    data: product
  })
}


export function deleteById(id) {
  return request({
    url: '/backsystem/products/delete/'+id,
    method: 'delete',
  })
}

export function deleteByIds(id) {
  return request({
    url: '/backsystem/products/deleteBatch',
    method: 'post',
    data: id,
  })
}

export function selectById(id) {
  return request({
    url: '/backsystem/products/'+id,
    method: 'get',
  })
}
