import request from '@/utils/request'

const api_name = '/admin/system/admin'

export default {

  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get',
      params: id
    })
  },

  save(admin) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: admin
    })
  },

  updateById(admin) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: admin
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  //更新用户状态
  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  },

  getUserInfo(){
    return request({
      url: `${api_name}/get`,
      method: 'get'
    })
  }
}
