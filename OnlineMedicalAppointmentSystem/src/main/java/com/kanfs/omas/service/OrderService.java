package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【order】的数据库操作Service

*/
public interface OrderService extends IService<Order> {

    int countOrder(int scheduleId);

    int saveOrder(Order order);

    OrderVo getOrderById(int id);

    int getUserId(int orderId);

    boolean cancel(int id);

    int confirm(int id, String transaction_id);

    int abort(int id);

    List<OrderVo> getOrderList(Page orderPage, Map<String, Object> params);

    int deleteOrder(int id);

    List<OrderVo> getTodayOrder();

    List<Order> getTimeoutOrder(Date time);


}
