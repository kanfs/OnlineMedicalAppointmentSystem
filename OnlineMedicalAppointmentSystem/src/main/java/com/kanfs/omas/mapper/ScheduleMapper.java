package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
* @author 方盛
* @description 针对表【schedule】的数据库操作Mapper

* @Entity com.kanfs.omas.pojo.Schedule
*/
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    List<Schedule> selectSchedules( Date date, Long departmentId);
}




