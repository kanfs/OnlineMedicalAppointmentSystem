package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.HospitalService;
import com.kanfs.omas.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin()
@RestController
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DozerUtil dozerUtil;

    @GetMapping("/mas/hospital/basic-info")
    public ResponseEntity<Map<String, Object>> getBasicInformation(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hospitalService.getBasicInfo());
    }

    @GetMapping("/mas/hospital/names")
    public ResponseEntity<List<String>> getHospitalName(@RequestParam("province") String province, @RequestParam("city") String city){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hospitalService.getHospitalName(province, city));
    }

    @GetMapping("/mas/hospital/vague-name")
    public ResponseEntity<List<String>> getHospitalByName(@RequestParam Map<String, Object> params){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hospitalService.getHospitalByName(params));
    }

    @GetMapping("/mas/hospital/name")
    public ResponseEntity<Long> getHospitalIdByName(@RequestParam("name") String name){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hospitalService.getHospitalIdByName(name));
    }

    //@PreAuthorize("hasAuthority('bnt.hospital.list')")
    @GetMapping("/mas/admin/information/hospital/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map<String, Object> searchObj){
        return Result.ok(hospitalService.selectPage(new Page<Hospital>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.hospital.list')")
    @GetMapping("/mas/admin/information/hospital/get/{id}")
    public Result getById(@PathVariable long id){
        return Result.ok(hospitalService.getById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.hospital.add')")
    @PostMapping("/mas/admin/information/hospital/save")
    public Result save(@RequestBody Map<String, Object> params){
        Hospital hospital = dozerUtil.map(params, Hospital.class);
        return Result.ok(hospitalService.save(hospital));
    }

    //@PreAuthorize("hasAuthority('bnt.hospital.update')")
    @PostMapping("/mas/admin/information/hospital/update")
    public Result update(@RequestBody Map<String, Object> params){
        Hospital hospital = dozerUtil.map(params, Hospital.class);
        return Result.ok(hospitalService.updateById(hospital));
    }

    //@PreAuthorize("hasAuthority('bnt.hospital.remove')")
    @DeleteMapping("/mas/admin/information/hospital/remove/{id}")
    public Result remove(@PathVariable long id){
        return Result.ok(hospitalService.removeById(id));
    }

}
