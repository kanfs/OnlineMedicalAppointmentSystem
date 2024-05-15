import request from '@/utils/request'

const api_name = '/order'

export default {

  getPageList(data){
    return request({
      url: `${api_name}/list`,
      method: 'GET',
      params: data
    })
  },
  getById(id){
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get',
    })
  },
  
  save(order){
    return request({
      url: `${api_name}/save`,
      method: 'GET',
      data: order
    })
  },
  delete(id){
    return request({
      url: `${api_name}/delete/${id}`,
      method: 'get',
    })
  },
  // 修改订单状态
  confirm(id){
    return request({
      url: `${api_name}/confirm/${id}`,
      method: 'get',
    })
  },
  cancel(id){
    return request({
      url: `${api_name}/cancel/${id}`,
      method: 'get',
    })
  },
}
