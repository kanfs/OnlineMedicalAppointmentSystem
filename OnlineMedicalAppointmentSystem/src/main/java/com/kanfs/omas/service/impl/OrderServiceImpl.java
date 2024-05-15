package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.pojo.Order;
import com.kanfs.omas.pojo.Schedule;
import com.kanfs.omas.service.OrderService;
import com.kanfs.omas.mapper.OrderMapper;
import com.kanfs.omas.service.ScheduleService;
import com.kanfs.omas.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【order】的数据库操作Service实现

*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    ScheduleService scheduleService;

    @Override
    public int countOrder(int scheduleId) {
        return orderMapper.countOrder(scheduleId);
    }

    @Override
    public int saveOrder(Order order) {
        if (orderMapper.getId(order) != null)
            return 0;
        orderMapper.saveOrder(order);
        return orderMapper.getId(order);
    }

    @Override
    public OrderVo getOrderById(int id) {
        return orderMapper.getOrderById(id);
    }

    @Override
    public int getUserId(int orderId) {
        return orderMapper.getUserId(orderId);
    }

    @Override
    public boolean cancel(int id) {
        // 修改排班和其他订单信息
        int scheduleId = orderMapper.getOrderById(id).getScheduleId();
        int availableNum = scheduleService.getById(scheduleId).getAvailableNum();
        scheduleService.update(new LambdaUpdateWrapper<Schedule>().set(Schedule::getAvailableNum,availableNum+1).eq(Schedule::getId,scheduleId));
        int number = orderMapper.getOrderById(id).getNumber();
        List<Order> orders = orderMapper.getOrdersByScheduleId(scheduleId);
        for (Order order : orders) {
            if ( number<order.getNumber() && order.getStatus()<2 )
                orderMapper.updateNumber(order.getId(), order.getNumber()-1);
        }
        return orderMapper.cancel(id)==1;
    }

    @Override
    public int confirm(int id, String transaction_id) {
        return orderMapper.confirm(id, transaction_id);
    }

    @Override
    public int abort(int id) {
        int scheduleId = orderMapper.getOrderById(id).getScheduleId();
        int availableNum = scheduleService.getById(scheduleId).getAvailableNum();
        scheduleService.update(new LambdaUpdateWrapper<Schedule>().set(Schedule::getAvailableNum,availableNum+1).eq(Schedule::getId,scheduleId));
        int number = orderMapper.getOrderById(id).getNumber();
        List<Order> orders = orderMapper.getOrdersByScheduleId(scheduleId);

        for (Order order : orders) {
            if ( number<order.getNumber() && order.getStatus()<2 )
                orderMapper.updateNumber(order.getId(), order.getNumber()-1);
        }
        return orderMapper.abort(id);
    }

    @Override
    public List<OrderVo> getOrderList(Page orderPage, Map<String, Object> params) {
        return orderMapper.selectPageList(orderPage, params);
    }

    @Override
    public int deleteOrder(int id) {
        return orderMapper.deleteOrder(id);
    }

    @Override
    public List<OrderVo> getTodayOrder() {
        return orderMapper.getTodayOrder();
    }

    @Override
    public List<Order> getTimeoutOrder(Date time) {
        return orderMapper.getTimeoutOrder(time);
    }

}




