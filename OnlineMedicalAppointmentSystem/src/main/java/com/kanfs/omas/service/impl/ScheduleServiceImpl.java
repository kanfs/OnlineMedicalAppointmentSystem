package com.kanfs.omas.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.ScheduleVo;
import com.kanfs.omas.Vo.ScheduleWeekdayRo;
import com.kanfs.omas.Vo.ScheduleWeekdayVo;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.service.DoctorService;
import com.kanfs.omas.service.ScheduleService;
import com.kanfs.omas.mapper.ScheduleMapper;
import com.kanfs.omas.utils.DateWeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 方盛
* @description 针对表【schedule】的数据库操作Service实现
*/
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService{
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private DoctorService doctorService;

    @Override
    public ScheduleWeekdayVo scheduleWeekday(Long page, Long limit, Long departmentId) {
        // 查询排班周期
        Integer cycle = 10;
        // 根据当前时间计算后面排班日期范围的排版日期
        List<Date> workDateList = new ArrayList<>();
        for (int i = 0; i <= cycle; i++) {
            workDateList.add(
                    Date.from(
                            LocalDate.now()
                                    .plusDays(i)
                                    .atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                    )
            );
        }

        // 根据分页条件计算需要查询的日期
        int pageSize = page.intValue();
        int limitSize = limit.intValue();
        List<Date> dateList = new ArrayList<>(workDateList.subList((pageSize - 1) * limitSize,
                Math.min(((pageSize - 1) * limitSize + limitSize), workDateList.size())
        ));

        // 只查询有效的日期
        List<Schedule> scheduleList = new ArrayList<>();
        for(Date date : dateList)
            scheduleList.addAll(scheduleMapper.selectSchedules(date, departmentId));

        Map<String, List<Schedule>> scheduleMap = scheduleList.stream()
                .collect(
                        Collectors.groupingBy(
                                s -> s.getWorkDate().toString()
                        )
                );


        List<ScheduleWeekdayRo> scheduleWeekdayRos = dateList.stream()
                .map(d -> {
                    List<Schedule> schedules = scheduleMap.get(d.toString());
                    // 删选出没有停止的数据
                    List<Schedule> collect = Collections.emptyList();
                    if (CollUtil.isNotEmpty(schedules)) {
                        collect = schedules.stream()
                                .filter(s -> s.getStatus() != false)
                                .collect(Collectors.toList());
                    }

                    return ScheduleWeekdayRo.builder()
                            .workDate(d)
                            .weekday(DateWeekUtil.dateToWeek(
                                            d.toInstant()
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate()
                                    )
                            )
                            .signalSourceStr(
                                    CollUtil.isEmpty(schedules) ? "未安排号源" :
                                            CollUtil.isEmpty(collect) ?
                                                    "停诊" :
                                                    "剩余可预约数" + collect.stream()
                                                            .mapToInt(Schedule::getAvailableNum)
                                                            .sum()

                            )
                            .build();
                })
                .collect(Collectors.toList());


        return ScheduleWeekdayVo.builder()
                .total(Long.parseLong(workDateList.size() + ""))
                .page(page)
                .limit(Long.parseLong(dateList.size() + ""))
                .scheduleWeekdayRoList(scheduleWeekdayRos)
                .build();
    }

    @Override
    public List<ScheduleVo> findScheduleVo(Date weekDay, Long departmentId) {
        // 根据日期查询所有的排班信息
        List<Schedule> scheduleList = scheduleMapper.selectSchedules(weekDay, departmentId);;

        if (CollUtil.isEmpty(scheduleList)) {
            return Collections.emptyList();
        }

        List<Long> doctorIds = scheduleList.stream()
                .map(Schedule::getDoctorId)
                .collect(Collectors.toList());

        // 查询医师信息，封装Map
        Map<Long, Doctor> doctorMap = doctorService.list(new LambdaQueryWrapper<Doctor>().in(Doctor::getId, doctorIds))
                .stream()
                .collect(Collectors.toMap(Doctor::getId, Function.identity()));


        return scheduleList.stream()
                .map(s -> ScheduleVo.builder()
                        .id(s.getId())
                        .doctorId(s.getDoctorId())
                        .doctorName(doctorMap.get(s.getDoctorId()).getName())
                        .grade(doctorMap.get(s.getDoctorId()).getGrade())
                        .title(doctorMap.get(s.getDoctorId()).getTitle())
                        .doctorGoodat(doctorMap.get(s.getDoctorId()).getGoodat())
                        .availableNum(s.getAvailableNum())
                        .workDate(weekDay)
                        .status(s.getStatus())
                        .price(s.getPrice())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleVo> findFreeDoctorList(Date weekDay, Long departmentId) {
        // 先查询这个科室门诊下面是否有医生
        List<Doctor> doctorList = doctorService.list(
                new LambdaQueryWrapper<Doctor>().eq(Doctor::getDepartmentId, departmentId)
        );

        if (CollUtil.isEmpty(doctorList))
            return Collections.emptyList();


        // 先根据日期和科室门诊信息查询已经排班的医生信息
        List<Schedule> scheduleList = scheduleMapper.selectSchedules(weekDay, departmentId);

        Set<Long> existsDoctorIds = new HashSet<>();
        if (CollUtil.isNotEmpty(scheduleList)) {
            existsDoctorIds = scheduleList.stream()
                    .map(Schedule::getDoctorId)
                    .collect(Collectors.toSet());
        }

        // 查询医生并排除已经排班的医生信息
        Set<Long> finalExistsDoctorIds = existsDoctorIds;
        List<Doctor> doctors = doctorList.stream()
                .filter(d -> !finalExistsDoctorIds.contains(d.getId()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(doctors))
            return Collections.emptyList();

        Random random = new Random();
        return doctors.stream()
                .map(d -> ScheduleVo.builder()
                        .doctorId(d.getId())
                        .doctorName(d.getName())
                        .grade(d.getGrade())
                        .title(d.getTitle())
                        .workDate(weekDay)
                        .doctorGoodat(d.getGoodat())
                        .status(true)
                        .availableNum(30)
                        .price(50.0)
                        .build())
                .collect(Collectors.toList());
    }
}




