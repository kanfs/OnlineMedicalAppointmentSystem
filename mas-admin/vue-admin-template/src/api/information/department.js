import request from '@/utils/request'

const api_name = '/admin/information/department'

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

  save(department) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data:  department
    })
  },

  updateById( department) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data:  department
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  getDepartmentNames(hospitalName){
    return request({
      url: `${api_name}/names`,
      method: 'get',
      params: {hospital:hospitalName}
    })
  },
}
