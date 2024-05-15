package com.kanfs.omas.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.service.DepartmentService;
import com.kanfs.omas.service.DoctorService;
import com.kanfs.omas.service.HospitalService;
import com.kanfs.omas.service.ScheduleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@Slf4j
public class ScheduleHandler {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ScheduleService scheduleService;

    /**
     * 指定每天24:00生成预约周期后一天的预约挂号信息
     * <p>
     * 如果有生成失败的，要记录信息，通知手动添加
     *
     * @return
     */
    @XxlJob(value = "ScheduleGenerationHandler")
    public ReturnT timedGeneration() {

        return ReturnT.SUCCESS;
    }

    /**
     * 将已过期的排版状态修改——每天晚上两点执行
     *
     * @return
     */
    @XxlJob(value = "TimedOutScheduleStatusModification")
    public ReturnT timedOutScheduleStatusModification() {
        List<Schedule> scheduleList = scheduleService.list(
                new LambdaQueryWrapper<Schedule>()
                        .lt(Schedule::getWorkDate, Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        .eq(Schedule::getStatus, true)
        );

        if (CollUtil.isNotEmpty(scheduleList)) {
            for(Schedule schedule : scheduleList) schedule.setStatus(false);
            scheduleService.updateBatchById(scheduleList);

        }

        return ReturnT.SUCCESS;
    }

}

