import request from '@/utils/request'

const api_name = '/admin/schedule'

export default {
  scheduleAll(workDate, departmentId, availableNum, price){
    return request({
      url: `${api_name}/schedule-all/${departmentId}?workDate=${workDate}&availableNum=${availableNum}&price=${price}`,
      method: 'GET',
    })
  },
  findDepartments(hospitalId) {
    return request({
      url: `${api_name}/departments/${hospitalId}`,
      method: 'GET'
    })
  },
  scheduleWeekday(data){
    return request({
      url: `${api_name}/weekday`,
      method: 'GET',
      params: data
    })
  },
  scheduleList(data){
    return request({
      url: `${api_name}/list`,
      method: 'GET',
      params: data
    })
  },
  getFreeDoctorList(data){
    return request({
      url: `${api_name}/free-doctor`,
      method: 'GET',
      params: data
    })
  },
  // 排班
  save(schedule){
    return request({
      url: `${api_name}/save`,
      method: 'POST',
      data: schedule
    })
  },
  delete(id){
    return request({
      url: `${api_name}/delete/${id}`,
      method: 'delete',
    })
  },
  // 修改排班状态
  updateSchedule(schedule){
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: schedule
    })
  }
}
