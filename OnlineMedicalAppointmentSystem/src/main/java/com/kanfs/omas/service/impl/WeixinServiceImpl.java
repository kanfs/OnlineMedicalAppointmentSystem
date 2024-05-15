package com.kanfs.omas.service.impl;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.kanfs.omas.Vo.OrderVo;
import com.kanfs.omas.mapper.OrderMapper;
import com.kanfs.omas.service.OrderService;
import com.kanfs.omas.service.WeixinService;
import com.kanfs.omas.utils.ConstantWxPropertiesUtil;
import com.kanfs.omas.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WeixinServiceImpl implements WeixinService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map queryOrderStatus(int orderId) {
        try {
            OrderVo order = orderService.getOrderById(orderId);

            Map params = new HashMap();
            params.put("appid", ConstantWxPropertiesUtil.APPID);
            params.put("mch_id", ConstantWxPropertiesUtil.PARTNER);
            params.put("out_trade_no", "37373737373737"+orderId);
            params.put("nonce_str", WXPayUtil.generateNonceStr());

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(params, ConstantWxPropertiesUtil.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();

            String xml = httpClient.getContent();
            Map result = WXPayUtil.xmlToMap(xml);
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 微信退款操作
    @Override
    public Boolean refund(int orderId) {
        try {
            OrderVo order = orderService.getOrderById(orderId);
            if(order!=null && order.getStatus()==1) {
                // 创建一个Date对象，表示今天
                Date today = new Date();

                // 创建一个Calendar对象，表示今天
                Calendar todayCalendar = Calendar.getInstance();
                todayCalendar.setTime(today);

                // 创建一个Date对象，表示order.getWorkDate()
                Date workDate = order.getWorkDate();

                // 创建一个Calendar对象，表示order.getWorkDate()
                Calendar workDateCalendar = Calendar.getInstance();
                workDateCalendar.setTime(workDate);

                // 比较两个日期
                if (workDateCalendar.after(todayCalendar)) {
                    // 如果order.getWorkDate()晚于今天，则退款
                    Map params = new HashMap();
                    params.put("appid", ConstantWxPropertiesUtil.APPID);
                    params.put("mch_id", ConstantWxPropertiesUtil.PARTNER);
                    params.put("nonce_str", WXPayUtil.generateNonceStr());
                    params.put("transaction_id", order.getTransactionId());
                    params.put("out_trade_no", "37373737373737" + orderId);
                    params.put("out_refund_no", "3337373737373737" + orderId);
                    params.put("total_fee", "1");
                    params.put("refund_fee", "1");

                    HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
                    httpClient.setXmlParam(WXPayUtil.generateSignedXml(params, ConstantWxPropertiesUtil.PARTNERKEY));
                    httpClient.setCert(true);
                    httpClient.setHttps(true);
                    httpClient.setCertPassword(ConstantWxPropertiesUtil.PARTNER);
                    httpClient.post();

                    String xml = httpClient.getContent();
                    Map result = WXPayUtil.xmlToMap(xml);
                    if ( result.get("result_code").equals("SUCCESS")) {
                        orderService.cancel(orderId);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Map createNative(Map<String, Object> params) throws Exception {
        Map payMap = (Map)redisTemplate.opsForValue().get(params.get("orderId"));
        if(payMap!= null) return payMap;
        else
        {
            // 根据orderId获取订单信息
            // 把参数转换为xml格式，使用商户key进行加密
            Map map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtil.APPID);
            map.put("mch_id", ConstantWxPropertiesUtil.PARTNER);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = params.get("workDate")+" 就诊"+params.get("departmentName");
            map.put("body", body);
            map.put("out_trade_no", "37373737373737"+params.get("orderId"));
            map.put("total_fee", "1");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", "http://st555kt1752.vicp.fun:80/");
            map.put("trade_type", "NATIVE");
            // 调用微信生成二维码接口 httpClient
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, ConstantWxPropertiesUtil.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();
            // 获取返回结果
            String xml = httpClient.getContent();

            Map<String, String> result = WXPayUtil.xmlToMap(xml);
            if ( result.get("result_code")!=null )
                redisTemplate.opsForValue().set(params.get("orderId"), result, 120, TimeUnit.MINUTES);
            return result;
        }
    }
}
