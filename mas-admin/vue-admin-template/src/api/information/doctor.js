import request from '@/utils/request'

const api_name = '/admin/information/doctor'

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
      method: 'get'
    })
  },

  save(doctor) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data:  doctor
    })
  },

  updateById( doctor) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data:  doctor
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
}
