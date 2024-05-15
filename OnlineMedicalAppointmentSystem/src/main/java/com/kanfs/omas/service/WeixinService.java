package com.kanfs.omas.service;

import java.util.Map;

public interface WeixinService {
    Map createNative(Map<String, Object> params) throws Exception;

    Map queryOrderStatus(int orderId);

    Boolean refund(int orderId);
}
