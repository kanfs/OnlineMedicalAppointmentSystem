package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.pojo.Order;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.OrderService;
import com.kanfs.omas.service.ScheduleService;
import com.kanfs.omas.service.WeixinService;
import com.kanfs.omas.utils.DozerUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ScheduleService scheduleService;

    @Resource
    private WeixinService weixinService;

    @Autowired
    private DozerUtil dozerUtil;

    // 生成微信扫描二维码
    @GetMapping("/mas/order/createNative")
    public  Result createNative(@RequestParam Map<String, Object> params) throws Exception {
        Map map = weixinService.createNative(params);
        return Result.ok(map);
    }

    // 查询支付状态
    @GetMapping("/mas/order/queryOrderStatus/{orderId}")
    public Result queryOrderStatus(@PathVariable("orderId") int orderId) {
        Map result = weixinService.queryOrderStatus(orderId);
        if ( result == null)
        {
            return Result.fail().message("支付出错");
        }
        if ( result.get("trade_state").equals("SUCCESS"))
        {
            orderService.confirm(orderId, (String)result.get("transaction_id"));
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }

    @GetMapping("/mas/order/save")
    public Result saveOrder(@RequestParam Map<String, Object> params){
        // 获取排班已被预约数量
         int cnt = (int) orderService.countOrder(Integer.parseInt((String)params.get("scheduleId")));
         cnt++;
        Order order = dozerUtil.map(params, Order.class);
        int availableNum = scheduleService.getById(order.getScheduleId()).getAvailableNum();
        if(availableNum<=0){
            return Result.fail("预约失败，预约数量超过排班可预约数量");
        }else{
            scheduleService.update(new LambdaUpdateWrapper<Schedule>().set(Schedule::getAvailableNum,availableNum-1).eq(Schedule::getId,order.getScheduleId()));
            order.setStatus(0);
            order.setNumber(cnt);
            order.setCreationTime(new Date());
            return Result.ok(orderService.saveOrder(order));
        }

    }

    @GetMapping("/mas/order/get/{id}")
    public Result getById(@PathVariable int id){
        // 获取排班已被预约数量
        return Result.ok(orderService.getOrderById(id));
    }

    @GetMapping("/mas/order/right")
    public Result check(@RequestParam Map<String, Object> params){
        int userId = orderService.getUserId(Integer.parseInt((String) params.get("orderId")));
        return Result.ok(userId == Integer.parseInt((String) params.get("userId")));
    }

    @GetMapping("/mas/order/cancel/{id}")
    public Result cancel(@PathVariable int id){
        if(weixinService.refund(id)) return Result.ok().message("退款成功");
        else return Result.ok(orderService.cancel((id)));
    }

    @GetMapping("/mas/order/confirm/{id}")
    public Result confirm(@PathVariable int id){
        return Result.ok(orderService.confirm(id, ""));
    }

    @GetMapping("/mas/order/list")
    public Result list(@RequestParam Map<String, Object> params){
        int page = Integer.parseInt((String) params.get("page"));
        int limit = Integer.parseInt((String) params.get("limit"));
        return Result.ok(orderService.getOrderList(new Page<OrderVo>(page, limit), params));
    }



    @GetMapping("/mas/order/delete/{id}")
    public Result delete(@PathVariable int id){
        return Result.ok(orderService.deleteOrder(id));
    }


}
