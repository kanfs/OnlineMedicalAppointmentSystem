import request from '@/utils/request'

const api_name = '/admin/user/patient'

export default {
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
    authPatient(id) {
        return request({
            url: `${api_name}/auth/${id}`,
            method: 'get',
        })
    }
}