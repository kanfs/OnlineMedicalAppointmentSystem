import request from '@/utils/request'

/*
登录日志管理相关的API请求函数
*/
const api_name = '/admin/file'

export default {

  delete(fileUrl) {
    return request({
      url: `${api_name}`,
      method: 'delete',
      params: fileUrl
    })
  }
}
