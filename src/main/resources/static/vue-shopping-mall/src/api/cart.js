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

  export function getProdsByCartId(cartId) {
    return request({
      url: '/cart/get-products/'+cartId,
      method: 'get',
    })
  }

  export function updateQuantity(cartItem) {
    return request({
      url: '/cart/updateQuantity',
      method: 'post',
      data: cartItem
    })
  }

  export function deleteById(cartID,productID) {
    return request({
      url: '/cart/deleteCartItem/'+cartID+'/'+productID,
      method: 'delete',
    })
  }