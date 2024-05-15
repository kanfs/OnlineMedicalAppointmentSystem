package com.kanfs.omas.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.mapper.PatientMapper;
import com.kanfs.omas.mapper.UserMapper;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.pojo.Patient;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.service.UserService;
import com.kanfs.omas.utils.EmailUtil;
import com.kanfs.omas.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* @author 方盛
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RandomUtil randomUtil;
    @Autowired
    private EmailUtil emailUtils;

    @Override
    public Integer count(Map<String, Object> params) {
        return userMapper.count(params);
    }

    @Override
    public List<User> getUsers(Map<String, Object> params) {
        return userMapper.findByPage(params);
    }

    @Override
    public Map<String, Object> getUserInfoById(int id) {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = userMapper.findUserById(id);
        if( user != null)
        {
            result.put("code", 200);
            result.put("emailAddress", user.getEmail());
            result.put("phoneNumber", user.getPhoneNumber());
            result.put("name", user.getName());
            result.put("headUrl", user.getHeadUrl());
        }else{
            result.put("code", 1);
            result.put("msg", "获取用户失败");
        }
        return result;
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    @Override
    public Map<String, Object> updateUserInfo(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        int id = Integer.parseInt((String)map.get("userId")) ;
        String password = (String) map.get("password");
        User user = userMapper.findUserById(id);
        if( user!=null && user.getPassword().equals(password) ) // 通过身份验证
        {
//            String newPassword = (String) map.get("newPassword");
//            String newName = (String) map.get("name");
//            String newPhoneNumber = (String) map.get("phoneNumber");
            if (userMapper.updateUserInfo(map)>0)
            {
                result.put("code", 0);
                result.put("msg", "修改成功");
            }else{
                result.put("code", 1);
                result.put("msg", "修改失败");
            }
        }else{
            result.put("code", 2);
            result.put("msg", "密码错误");
        }
        return result;
    }

    @Override
    public Map<String, Object> registerUser(Map map) {
        String phoneNumber = (String) map.get("phoneNumber");
        String name = (String) map.get("name");
        String sex = (String) map.get("sex");
        String password = (String) map.get("password");
        String email = (String) map.get("email");
        String province = (String) map.get("province");
        String city = (String) map.get("city");
        String testCode = (String) map.get("testCode");
        // map对象result保存查询结果
        Map<String, Object> result = new HashMap<String, Object>();
        // map对象params封装查询条件
        Map<String, Object> params = new HashMap<String, Object>();
        //判断验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(email);
        if (StrUtil.isEmpty(redisCode) || !testCode.equals(redisCode)) {
            result.put("code", 2);
            result.put("msg", "验证码错误");
        }else{
            // 数据库查询邮箱号是否已注册
            User user = userMapper.findUserByEmail(email);
            if(user!=null)
            {
                result.put("code", 1);
                result.put("msg", String.format("该邮箱:%s已被注册",email));
            }else{
                result.put("code", 0);
                result.put("msg", "注册成功");
                // 向数据库中添加一个用户对象
                User regUser = new User();
                regUser.setName(name);
                regUser.setPassword(password);
                regUser.setPhoneNumber(phoneNumber);
                regUser.setSex(sex);
                regUser.setEmail(email);
                regUser.setAddress(province+" "+city);
                regUser.setRegDate(new Date());
                userMapper.insertUser(regUser);
                System.out.println("register user successfully");
            }
        }
        return result;
    }

    @Override
    public void sendCode(String email) {
        //从redis中获取验证码，如果获取到，返回OK
        //key：手机号   value：验证码
        String code = redisTemplate.opsForValue().get(email);
        if (StrUtil.isNotEmpty(code)) return;

        //如果从redis中获取不到，生成验证码，通过整合阿里云短信服务进行发送
        code = randomUtil.getCheckCode();
        //调用service方法，判断是电话还是邮箱，使用不同的通知方式
        boolean isSend = false;
            //说明是邮箱
        isSend = this.sendEmail(email, code);

        //生成验证码放到redis里面，设置有效时间
        if (isSend)
            //将验证码存入redis，1分钟内有效
            redisTemplate.opsForValue().set(email, code, 1, TimeUnit.MINUTES);
         else throw new RuntimeException("验证码发送失败");
         return ;
    }

    /**
     * 使用邮箱发送验证码
     */
    private boolean sendEmail(String email, String code) {
        boolean i = emailUtils.sendEmail(email, "OMAS智能预约平台",
                "你本次注册的验证码是：" + code + "\r\n注意：有效时间1分钟！");
        return i;
    }

    @Override
    public Map<String, Object> selectPage(Page<User> userPage, Map<String, Object> searchObj) {
        Map<String, Object> res = new HashMap<>();
        res.put("records", userMapper.selectPageList(userPage, searchObj));
        res.put("total", userMapper.getTotalRowCount());
        return res;
    }

    @Override
    public Map<String, Object> selectPageWithPatients(Page<User> userPage, Map<String, Object> searchObj) {
        Map<String, Object> res = new HashMap<>();
        List<User> users = userMapper.selectPageList(userPage, searchObj);
        List<Map<String, Object>> records = new ArrayList<>();
        for (User user : users){

            Map<String, Object> record = new HashMap<>();
            record.put("id", user.getId());
            record.put("name", user.getName());
            record.put("password", user.getPassword());
            record.put("sex", user.getSex());
            record.put("phoneNumber", user.getPhoneNumber());
            record.put("address", user.getAddress());
            record.put("email", user.getEmail());
            record.put("regDate", user.getRegDate());
            record.put("patientList", patientMapper.selectList(new QueryWrapper<Patient>().eq("userId", user.getId())));
            records.add(record);
        }
        res.put("records", records);
        res.put("total", userMapper.getTotalRowCount());
        return res;
    }
}




