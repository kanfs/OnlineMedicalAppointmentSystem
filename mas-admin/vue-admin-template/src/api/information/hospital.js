import request from '@/utils/request'

const api_name = '/admin/information/hospital'

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

  save(hospital) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: hospital
    })
  },

  updateById(hospital) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: hospital
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
}
