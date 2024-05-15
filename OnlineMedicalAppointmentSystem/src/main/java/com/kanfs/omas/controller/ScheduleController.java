package com.kanfs.omas.controller;

import cn.hutool.core.date.format.DateParser;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kanfs.omas.Vo.ScheduleVo;
import com.kanfs.omas.Vo.ScheduleWeekdayVo;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.DoctorService;
import com.kanfs.omas.service.ScheduleService;
import com.kanfs.omas.utils.DozerUtil;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/mas/admin/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DozerUtil dozerUtil;

    @GetMapping("/schedule-all/{departmentId}")
    public Result scheduleAll(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam("workDate")  Date workDate,
            @RequestParam("availableNum")  String availableNum,
            @RequestParam("price")  String price,
            @PathVariable("departmentId") Long departmentId) throws ParseException {

        List<ScheduleVo> doctors = scheduleService.findFreeDoctorList(workDate, departmentId);
        for (ScheduleVo doctor:doctors)
            scheduleService.save(
                    Schedule.builder().
                            doctorId(doctor.getDoctorId()).
                            workDate(workDate).
                            availableNum(Integer.parseInt(availableNum)).
                            price(Double.parseDouble(price)).
                            status(true).
                            creationTime(new Date()).
                            build()
            );
        return Result.ok();
    }

    @GetMapping("/weekday")
    public Result scheduleWeekday(
            @RequestParam("page")  Long page,
            @RequestParam("limit")  Long limit,
            @RequestParam("departmentId")  Long departmentId) {
        ScheduleWeekdayVo scheduleWeekdayVo = scheduleService.scheduleWeekday(page, limit, departmentId);
        return Result.ok(scheduleWeekdayVo);
    }

    @GetMapping("/list")
    public Result scheduleList(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam("weekDay") Date weekDay,
            @RequestParam("departmentId")  Long departmentId) {
        List<ScheduleVo> scheduleVo = scheduleService.findScheduleVo(weekDay, departmentId);
        return Result.ok(scheduleVo);
    }

    //@PreAuthorize("hasAuthority('bnt.schedule.list')")
    @GetMapping("/free-doctor")
    public Result getFreeDoctorList(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam("weekDay") Date weekDay,
            @RequestParam("departmentId")  Long departmentId) {
        List<ScheduleVo> doctors = scheduleService.findFreeDoctorList(weekDay, departmentId);
        return Result.ok(doctors);
    }

    //@PreAuthorize("hasAuthority('bnt.schedule.add')")
    @PostMapping("/save")
    public Result save(@RequestBody Map<String, Object> params) {
        try {
            params.put("workDate", new SimpleDateFormat("yyyy-MM-dd").parse((String) params.remove("workDate")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Schedule schedule = dozerUtil.map(params, Schedule.class);
        schedule.setCreationTime(new Date());
        return Result.ok(scheduleService.save(schedule));
    }

    //@PreAuthorize("hasAuthority('bnt.schedule.remove')")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable int id) {
        return Result.ok(scheduleService.removeById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.schedule.update')")
    @PostMapping("/update")
    public Result update(@RequestBody ScheduleVo scheduleVo) {
        Schedule schedule = new Schedule(scheduleVo.getId(), scheduleVo.getDoctorId(), scheduleVo.getWorkDate(), scheduleVo.getAvailableNum(), scheduleVo.getStatus(), scheduleVo.getPrice(), new Date());
        return Result.ok(scheduleService.updateById(schedule));
    }
}
