package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.Message;
import com.kanfs.omas.pojo.Patient;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.service.MessageService;
import com.kanfs.omas.service.PatientService;
import com.kanfs.omas.mapper.PatientMapper;
import com.kanfs.omas.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【patient】的数据库操作Service实现
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
    implements PatientService{
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EmailUtil emailUtil;
    @Override
    public Map<String, Object> getPatientList(Map params) {
        int userId = Integer.parseInt((String) params.get("userId"));
        Map<String, Object> result = new HashMap<>();
        List<Patient> patientList = patientMapper.getPatientsByUserId(userId);
        result.put("patientList", patientList);
        return result;
    }

    @Override
    public Map<String, Object> registerPatient(Map map) {
        int userId = Integer.parseInt((String) map.get("userId"));
        String identifyCardNumber = (String) map.get("identifyCardNumber");
        String phone = (String) map.get("phone");
        String name = (String) map.get("name");
        boolean sex = ((String) map.get("sex")).equals("1");
        String province = (String) map.get("province");
        String city = (String) map.get("city");
        Date birthdate;
        try {
            birthdate = (new SimpleDateFormat("yyyyMMdd")).parse(identifyCardNumber.substring(6,14));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // map对象result保存查询结果
        Map<String, Object> result = new HashMap<String, Object>();
        // map对象params封装查询条件
        Map<String, Object> params = new HashMap<String, Object>();
        // 数据库查询身份证号或手机号是否已注册
        Patient patient = patientMapper.findUserByIdentifyCardNumber(identifyCardNumber);
        if(patient!=null)
        {
            result.put("code", 1);
            result.put("msg", String.format("该身份证号:%s已被使用",identifyCardNumber));
        }else{
            result.put("code", 0);
            result.put("msg", "提交成功");
            // 向数据库中添加一个用户对象
            Patient regPatient = new Patient();
            regPatient.setUserId(userId);
            regPatient.setName(name);
            regPatient.setIdentifyCardNumber(identifyCardNumber);
            regPatient.setBirthdate(birthdate);
            regPatient.setPhone(phone);
            regPatient.setSex(sex);
            regPatient.setStatus(false);
            regPatient.setAddress(province+" "+city);
            regPatient.setCreationTime(new Date());
            patientMapper.insertPatient(regPatient);
        }
        return result;
    }

    @Override
    public Map<String, Object> updatePatient(Map map) {
        map.put("sex", ((String)map.remove("sex")).equals("1"));
        map.put("address", ((String) map.get("province"))+" "+((String) map.get("city"))) ;
        try {
            map.put("actualBirthdate",(new SimpleDateFormat("yyyyMMdd")).parse(((String) map.get("identifyCardNumber")).substring(6,14)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // map对象result保存查询结果
        Map<String, Object> result = new HashMap<String, Object>();
        if (patientMapper.updatePatient(map) > 0){
            result.put("code", 0);
            result.put("msg", "提交成功");
        }else{
            result.put("code", 1);
            result.put("msg", "提交失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> getPatientById(int id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Patient patient = patientMapper.getPatientById(id);
        if( patient!= null){
            result.put("name", patient.getName());
            result.put("identifyCardNumber", patient.getIdentifyCardNumber());
            result.put("sex", patient.getSex());
            result.put("birthdate", patient.getBirthdate().toString());
            result.put("phone", patient.getPhone());
            result.put("address", patient.getAddress());
            result.put("province", patient.getAddress().split(" ")[0]);
            result.put("city", patient.getAddress().split(" ")[1]);
            result.put("status", patient.getStatus());
            result.put("code", 0);
            result.put("msg", "获取患者成功");
        }else{
            result.put("code", 1);
            result.put("msg", "获取患者失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> deletePatient(int id) {
        Map<String, Object> result = new HashMap<String, Object>();
        int influencedRows = patientMapper.deletePatientById(id);
        if ( influencedRows > 0) result.put("code", 0);
        else{
            result.put("code", 1);
            result.put("msg", "删除失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> authPatient(int id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Patient patient = patientMapper.getPatientById(id);
        boolean status = patient.getStatus()  ? false : true ;
        int influencedRows = patientMapper.updatePatientStatusById(id, status);
        if ( influencedRows > 0) {
            result.put("code", 200);
            result.put("message", "认证成功");
        }
        // 用户认证之后进行消息通知
        String title = "就诊人认证审核"
                + (patient.getStatus() ? "通过" : "不通过")
                + "通知";
        String content = "尊敬的用户，您在在线医疗平台的就诊人："
                +patient.getName()
                +"认证信息已于"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd mm:hh:ss"))
                + "审核"
                + (patient.getStatus()  ? "通过" : "不通过。请前往系统检查信息并重新提交！如有问题联系医院管理员。");

        User user = userService.getById(patient.getUserId());
        // 发送短信通知
        emailUtil.sendEmail(user.getEmail(), title, content);
        // 将消息写入数据库
        messageService.saveMessage(
                Message.builder()
                        .userId(patient.getUserId())
                        .title(title)
                        .content(content)
                        .isSee(false)
                        .creationTime(new Date())
                        .build()
        );
        return result;
    }
}




