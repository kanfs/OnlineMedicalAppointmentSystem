package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.mapper.DepartmentMapper;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.DepartmentService;
import com.kanfs.omas.utils.DozerUtil;
import com.kanfs.omas.utils.MedicalConsultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    private DozerUtil dozerUtil;

    @GetMapping("/mas/department/names")
    public ResponseEntity<List<String>> getDepartmentNames(@RequestParam("hospital") String hospitalName){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(departmentService.getDepartmentNames(hospitalName));
    }

    @GetMapping("/mas/admin/information/department/names")
    public Result getDepartmentNames_(@RequestParam("hospital") String hospitalName){
        return Result.ok(departmentService.getDepartmentNames(hospitalName));
    }

    @GetMapping("/mas/department/names/{departmentId}")
    public Result getNames(@PathVariable long departmentId){
        return Result.ok(departmentService.getNames(departmentId));
    }

    //@PreAuthorize("hasAuthority('bnt.department.list')")
    @GetMapping("/mas/admin/information/department/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map<String, Object> searchObj){
        return Result.ok(departmentService.selectPage(new Page<Department>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.department.list')")
    @GetMapping("/mas/admin/information/department/get/{id}")
    public Result getById(@PathVariable long id){
        return Result.ok(departmentService.getById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.department.add')")
    @PostMapping("/mas/admin/information/department/save")
    public Result save(@RequestBody Map<String, Object> params){
        Department department = dozerUtil.map(params, Department.class);
        return Result.ok(departmentService.save(department));
    }

    //@PreAuthorize("hasAuthority('bnt.department.update')")
    @PostMapping("/mas/admin/information/department/update")
    public Result update(@RequestBody Map<String, Object> params){
        Department department = dozerUtil.map(params, Department.class);
        return Result.ok(departmentService.updateById(department));
    }

    //@PreAuthorize("hasAuthority('bnt.department.remove')")
    @DeleteMapping("/mas/admin/information/department/remove/{id}")
    public Result remove(@PathVariable long id){
        return Result.ok(departmentService.removeById(id));
    }

    @GetMapping("/mas/admin/schedule/departments/{hospitalId}")
    public Result getByHospitalId(@PathVariable long hospitalId){
        return Result.ok(departmentService.list(new QueryWrapper<Department>().eq("hospitalId", hospitalId)));
    }

}
