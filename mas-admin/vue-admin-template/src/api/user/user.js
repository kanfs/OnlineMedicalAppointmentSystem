import request from '@/utils/request'

const api_name = '/admin/user/user'

export default {
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  getPageListWithPatients(page, limit, searchObj) {
    return request({
      url: `${api_name}/patient/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },

  save(user) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data:  user
    })
  },

  updateById( user) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data:  user
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
}
