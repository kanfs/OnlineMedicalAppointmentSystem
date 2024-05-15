package com.kanfs.omas.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.mapper.OrderMapper;
import com.kanfs.omas.pojo.Message;
import com.kanfs.omas.pojo.Order;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.service.*;
import com.kanfs.omas.utils.EmailUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @date 2023/5/15
 */
@Component
@Slf4j
public class OrderHandler {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderMapper orderMapper;


    /**
     * 检测超时未支付订单，自动取消预约
     * <p>
     * 订单超过两个小时
     *
     * @return
     */
    @XxlJob(value = "orderTimeoutCancel")
    public ReturnT orderTimeoutCancel() {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, -2);
//            Date date = c.getTime();
//            Timestamp timestamp = new Timestamp(date.getTime());
//            String formattedTimestamp = timestamp.toString().substring(0, 19);
//            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedTimestamp);
            List<Order> orderList = orderService.getTimeoutOrder(c.getTime());

            // 处理超过两个小时还没有支付的订单
            if (CollUtil.isNotEmpty(orderList)) {
                // 修改订单状态
                orderList.forEach(o -> {
                    o.setStatus(3);
                    orderService.abort(o.getId());

                    // 修改排班预约数
                    Schedule schedule = scheduleService.getById(o.getScheduleId());
                    schedule.setAvailableNum(schedule.getAvailableNum() + 1);
                    scheduleService.updateById(schedule);


                    // 给用户发送订单自动取消的消息通知
                    User user = userService.getById(patientService.getById(o.getPatientId()).getUserId());

                    // 用户认证之后进行消息通知
                    String title = "订单"
                            + o.getId()
                            + "超时取消通知";
                    String content = "尊敬的用户，您在OMAS智能医院预约挂号系统的订单"
                            + o.getId()
                            + "（"
                            + doctorService.getDoctorById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getHospitalName()
                            + "-"
                            + doctorService.getDoctorById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getDepartmentName()
                            + "/ 患者:"
                            + patientService.getById(o.getPatientId()).getName()
                            + "- 医生:"
                            + doctorService.getById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getName()
                            + "（"
                            + o.getCreationTime()
                            + "))由于超过两小时未支付，订单已于"
                            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
                            + "自动取消。";

                    // 判断账号是邮箱还是手机号
                    sendMsg(user, title, content);
                });
            }

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.FAIL;
        }
    }

    /**
     * 订单超时未支付提醒
     * 订单超过一个小数未支付提醒，只发送一次提醒
     *
     * @return
     */
    @XxlJob(value = "unpaidReminder")
    public ReturnT unpaidReminder() {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, -1);
//            Date date = c.getTime();
//            Timestamp timestamp = new Timestamp(date.getTime());
//            String formattedTimestamp = timestamp.toString().substring(0, 19);
//            Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedTimestamp);
            List<Order> orderList = orderService.getTimeoutOrder(c.getTime());

            // 处理超过一个小时还没有支付的订单，发送一次消息提醒
            if (CollUtil.isNotEmpty(orderList)) {
                // 修改订单状态
                orderList.forEach(o -> {
                    // 给用户发送订单自动取消的消息通知
                    User user = userService.getById(patientService.getById(o.getPatientId()).getUserId());

                    // 用户认证之后进行消息通知
                    String title = "订单"
                            + o.getId()
                            + "为未支付通知";
                    String content = "尊敬的用户，您在OMAS智能医院预约挂号系统的订单"
                            + o.getId()
                            + "（"
                            + doctorService.getDoctorById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getHospitalName()
                            + "-"
                            + doctorService.getDoctorById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getDepartmentName()
                            + "/ 患者:"
                            + patientService.getById(o.getPatientId()).getName()
                            + "- 医生:"
                            + doctorService.getById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getName()
                            + "（"
                            + o.getCreationTime()
                            + "))由于已经超过一个小时未支付，请尽快前往系统支付，若超过两个小时将自动取消订单";

                    // 查询是否已经发送通知
                    List<Message> messageList = messageService.list(
                            new LambdaQueryWrapper<Message>()
                                    .eq(Message::getTitle, title)
                    );
                    if (CollUtil.isEmpty(messageList)) {
                        // 判断账号是邮箱还是手机号
                        sendMsg(user, title, content);
                    }
                });
            }

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.FAIL;
        }
    }

    /**
     * 检测用户成功预定的订单，设置就诊提醒
     * 查询当天需要就诊的用户信息，每天早上6点提醒就诊
     *
     * @return
     */
    @XxlJob(value = "orderVisitReminder")
    public ReturnT orderVisitReminder() {

        List<OrderVo> orderList = orderService.getTodayOrder();

        if (CollUtil.isNotEmpty(orderList)) {
            orderList.forEach(o -> {
                // 给用户发送订单自动取消的消息通知
                User user = userService.getById(patientService.getById(o.getPatientId()).getUserId());

                // 用户认证之后进行消息通知
                String title = "预约就诊通知";
                String content = "尊敬的用户，您在OMAS智能医院预约挂号系统的订单"
                        + o.getId()
                        + "（"
                        + doctorService.getDoctorById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getDepartmentName()
                        + "/"
                        + patientService.getById(o.getPatientId()).getName()
                        + "-"
                        + doctorService.getById(scheduleService.getById(o.getScheduleId()).getDoctorId()).getName()
                        + "（"
                        + o.getCreationTime()
                        + "))在今日需要取号就诊，请及时前往医院取号" +
                        "超时未就诊将视作爽约，系统不会进行退费处理.";
                sendMsg(user, title, content);
                orderMapper.abort(o.getId());
            });
        }

        return ReturnT.SUCCESS;
    }


    private void sendMsg(User user, String title, String content) {
        // 判断账号是邮箱还是手机号
        if (user.getEmail().contains("@")) {
            // 邮箱
            emailUtil.sendEmail(user.getEmail(), content, title);
        }else {
            // TODO 手机号，发送短信

        }


        // 先将消息写入数据库
        messageService.save(
                Message.builder()
                        .userId(user.getId())
                        .title(title)
                        .content(content)
                        .isSee(false)
                        .creationTime(new Date())
                        .build()
        );
    }

}

