package com.kanfs.omas.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanfs.omas.pojo.Function;
import jakarta.servlet.http.HttpServletRequest;

import java.io.FileNotFoundException;
import java.rmi.ServerException;
import java.util.Map;

public interface AlipayService {
    /**
     * 获得支付页面
     *
     * @param aliPayReq @return
     * @param request
     * @return
     */
    // String aliPayLine(AliPayReq aliPayReq, HttpServletRequest request) throws AlipayApiException, ServerException, FileNotFoundException;

    /**
     * 获取异步返回的参数
     *
     * @param requestParams
     * @return
     */
    Map<String, String> pay(Map requestParams) throws ServerException, AlipayApiException, FileNotFoundException;

//    /**
//     * 异步通知
//     * @param request
//     * @param response
//     * @return
//     */
//    Object notify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException;
    public Map<String, String> getAliParams(Map requestParams);
    /**
     * 查看订单
     *
     * @return
     */
    String query(String orderNo) throws AlipayApiException, ServerException, FileNotFoundException;
}



