import request from '@/utils/request'

const api_name = ''

export default {
  userList(doctorId) {
    return request({
      url: `${api_name}/user/list/${doctorId}`,
      method: 'GET'
    })
  },
  textList(doctorId, userId){
    return request({
      url: `${api_name}/doctor-text/${doctorId}/${userId}`,
      method: 'GET'
    })
  },
  saveText(data){
    return request({
      url: `${api_name}/text-user`,
      method: 'GET',
      params: data
    })
  }
}
