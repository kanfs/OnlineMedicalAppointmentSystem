package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.DepartmentVo;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【order】的数据库操作Mapper

* @Entity com.kanfs.omas.pojo.Order
*/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    int countOrder(int scheduleId);

    int saveOrder(Order order);

    Integer getId(Order order);

    OrderVo getOrderById(int id);

    int getUserId(int orderId);

    int cancel(int id);

    int confirm(int id, String transaction_id);

    int abort(int id);

    int updateNumber(int id, int number);

    List<Order> getOrdersByScheduleId(int scheduleId);

    List<OrderVo> selectPageList(Page orderPage, Map<String, Object> params);

    int getTotalRowCount();

    int deleteOrder(int id);

    List<OrderVo> getTodayOrder();

    List<Order> getTimeoutOrder(Date time);
}




