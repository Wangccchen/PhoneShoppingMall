import request from '@/utils/request'

export function addProduct(cartItem) {
    return request({
      url: '/cart/addProduct',
      method: 'post',
      data: cartItem
    })
  }

export function getCartId(userId) {
    return request({
      url: '/cart/get-cart-id/'+userId,
      method: 'get',
    })
  }