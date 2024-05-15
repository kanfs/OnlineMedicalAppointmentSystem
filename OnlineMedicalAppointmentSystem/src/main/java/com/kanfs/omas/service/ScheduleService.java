package com.kanfs.omas.service;

import com.kanfs.omas.Vo.ScheduleVo;
import com.kanfs.omas.Vo.ScheduleWeekdayVo;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
* @author 方盛
* @description 针对表【schedule】的数据库操作Service
*/
public interface ScheduleService extends IService<Schedule> {

    ScheduleWeekdayVo scheduleWeekday(Long page, Long limit, Long departmentId);

    List<ScheduleVo> findScheduleVo(Date weekDay, Long departmentId);

    List<ScheduleVo> findFreeDoctorList(Date weekDay, Long departmentId);
}
