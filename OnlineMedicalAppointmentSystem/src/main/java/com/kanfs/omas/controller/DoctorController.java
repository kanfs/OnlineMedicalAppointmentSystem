package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.DoctorService;
import com.kanfs.omas.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    @Autowired
    private DozerUtil dozerUtil;

    @GetMapping("/mas/doctor/matched-doctors")
    public Map<String, Object> getMatchedDoctors(@RequestParam Map<String, Object> params){
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Object> doctorMap = doctorService.getMatchedDoctors(params);
        res.put("count", doctorMap.remove("count"));
        List<Map<String, Object>> doctorList = new ArrayList<Map<String, Object>>();
        for( Map.Entry<String, Object> entry: doctorMap.entrySet())
            doctorList.add((Map<String, Object>)entry.getValue());
        res.put("doctorList", doctorList);
        return res;
    }

    @GetMapping("/mas/department/doctor/{departmentId}")
    public Result getDepartmentDoctors(@PathVariable long departmentId){
        return Result.ok(doctorService.list(new QueryWrapper<Doctor>().eq("departmentId", departmentId).orderByDesc("score")));
    }

    @GetMapping("/mas/doctor/info/{id}")
    public Map<String, Object> getDoctorInfo(@PathVariable long id){
        Map<String, Object> res = doctorService.getDoctorInfo(id);
        return res;
    }

    //@PreAuthorize("hasAuthority('bnt.doctor.list')")
    @GetMapping("/mas/admin/information/doctor/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map<String, Object> searchObj){
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Object> doctorMap = doctorService.selectPage(new Page<Doctor>(page, limit), searchObj);
        res.put("total", doctorMap.remove("count"));
        List<Map<String, Object>> doctorList = new ArrayList<Map<String, Object>>();
        for( Map.Entry<String, Object> entry: doctorMap.entrySet())
            doctorList.add((Map<String, Object>)entry.getValue());
        res.put("records", doctorList);
        return Result.ok(res);
    }

    //@PreAuthorize("hasAuthority('bnt.doctor.list')")
    @GetMapping("/mas/admin/information/doctor/get/{id}")
    public Result getById(@PathVariable long id){
        return Result.ok(doctorService.getDoctorById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.doctor.add')")
    @PostMapping("/mas/admin/information/doctor/save")
    public Result save(@RequestBody Map<String, Object> params){
        return Result.ok(doctorService.saveDoctor(params));
    }

    //@PreAuthorize("hasAuthority('bnt.doctor.update')")
    @PostMapping("/mas/admin/information/doctor/update")
    public Result update(@RequestBody Map<String, Object> params){

        return Result.ok(doctorService.updateDoctorById(params));
    }

    //@PreAuthorize("hasAuthority('bnt.doctor.remove')")
    @DeleteMapping("/mas/admin/information/doctor/remove/{id}")
    public Result remove(@PathVariable long id){
        return Result.ok(doctorService.removeById(id));
    }
}
