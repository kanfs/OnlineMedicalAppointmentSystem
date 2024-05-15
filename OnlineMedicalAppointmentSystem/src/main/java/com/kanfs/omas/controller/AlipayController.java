package  com.kanfs.omas.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.aliyun.oss.common.comm.ServiceClient;
import com.kanfs.omas.config.AlipayConfig;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.AlipayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/mas/alipay")
public class AlipayController {
    @Resource
    private AlipayService alipayService;

    @GetMapping("/pay")
    public Result pay(@RequestParam Map requestParams) throws AlipayApiException, ServerException, FileNotFoundException {
        return Result.ok(alipayService.pay(requestParams));
    }

    /**
     * 异步回调
     */
    @PostMapping("/aliPayCallBack")
    public Object notify_url(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        // 获得返回的参数
        Map<String, String> params = alipayService.getAliParams(request.getParameterMap());
        //验证签名
        boolean flag = AlipaySignature.rsaCertCheckV1(params, AlipayConfig.ALIPAY_CERT_PATH, "utf-8", "RSA2");

        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表
        String trade_status = request.getParameter("trade_status");
        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表
        if (flag) {
            if (trade_status.equals("TRADE_SUCCESS")) {
                log.info("支付宝订单支付成功！");
                String out_trade_no = params.get("out_trade_no");
                String passback_params = params.get("passback_params");
                System.out.println("passback_params:"+ passback_params);
                // 交易流水号
                String trade_no = params.get("trade_no");
                /** 支付处理*/

            }
            //这里通知支付宝回调成功，不然一直回调
            log.info("\n=========\n支付回调成功");
            return "success";
        } else {
            return "failure";
        }
    }

    /**
     * 同步回调, 注意get请求
     */
    @RequestMapping("/returnUrl")
    public void return_url(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        // 调用方法进行验签
        Map<String, String> paramsMap = alipayService.getAliParams(request.getParameterMap());
        boolean flag = AlipaySignature.rsaCertCheckV1(paramsMap, AlipayConfig.ALIPAY_CERT_PATH, "utf-8", "RSA2");
        if (flag) {
            //跳转支付成功界面
            try {
                response.sendRedirect("https://localhost:8080/order/all");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("flag=====验签失败");
            //跳转支付失败界面

        }
    }

    @PostMapping("/query")
    public Object query(String orderNo) throws Exception {
        return alipayService.query(orderNo);
    }
}