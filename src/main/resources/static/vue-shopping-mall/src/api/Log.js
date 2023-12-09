import request from '@/utils/request'

export function logPage(currentPage,pageSize) {
    return request({
      url: '/backsystem/logs?currentPage='+currentPage+'&pageSize='+pageSize,
      method: 'get'
    })
  }