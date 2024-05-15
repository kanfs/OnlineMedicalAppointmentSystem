package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Patient;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.PatientService;
import com.kanfs.omas.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@CrossOrigin
@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private DozerUtil dozerUtil;

    @GetMapping("/mas/patient/list")
    public Map<String, Object> getPatientList(@RequestParam Map params){
        Map<String, Object> result = patientService.getPatientList(params);
        return result;
    }

    @GetMapping("/mas/patient/register")
    public Map<String, Object> registerPatient(@RequestParam Map params){
        Map<String, Object> result = patientService.registerPatient(params);
        return result;
    }

    @GetMapping("/mas/patient/update")
    public Map<String, Object> updatePatient(@RequestParam Map params){
        Map<String, Object> result = patientService.updatePatient(params);
        return result;
    }

    @GetMapping("/mas/patient/info/{id}")
    public Map<String, Object> getPatientById(@PathVariable int id){
        Map<String, Object> result = patientService.getPatientById(id);
        return result;
    }

    @GetMapping("/mas/patient/delete/{id}")
    public Map<String, Object> deletePatient(@PathVariable int id){
        Map<String, Object> result = patientService.deletePatient(id);
        return result;
    }

    //@PreAuthorize("hasAuthority('bnt.patient.list')")
    @GetMapping("/mas/admin/user/patient/get/{id}")
    public Result getById(@PathVariable long id){
        return Result.ok(patientService.getById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.patient.add')")
    @PostMapping("/mas/admin/user/patient/save")
    public Result save(@RequestBody Map<String, Object> params){
        System.out.println(params);
        try {
            System.out.println(params);
            Date birthdate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse((String) params.remove("birthdate"));
            params.put("birthdate", birthdate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Patient patient = dozerUtil.map(params, Patient.class);
        patient.setCreationTime(new Date());
        return Result.ok(patientService.save(patient));
    }

    //@PreAuthorize("hasAuthority('bnt.patient.update')")
    @PostMapping("/mas/admin/user/patient/update")
    public Result update(@RequestBody Map<String, Object> params){
        try {
            System.out.println(params);
            Date creationTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse((String) params.remove("creationTime"));
            params.put("creationTime", creationTime);
            Date birthdate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse((String) params.remove("birthdate"));
            params.put("birthdate", birthdate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Patient patient = dozerUtil.map(params, Patient.class);
        return Result.ok(patientService.updateById(patient));
    }


    //@PreAuthorize("hasAuthority('bnt.patient.remove')")
    @DeleteMapping("/mas/admin/user/patient/remove/{id}")
    public Result remove(@PathVariable long id){
        return Result.ok(patientService.removeById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.patient.update')")
    @GetMapping("/mas/admin/user/patient/auth/{id}")
    public Result authPatient(@PathVariable int id){

        return Result.ok(patientService.authPatient(id));
    }
}
