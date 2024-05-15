package com.kanfs.omas.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.*;
import com.kanfs.omas.config.AlipayConfig;
import com.kanfs.omas.service.AlipayService;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {
    /**
     * 证书模式   获取文件路径
     *
     * @return
     * @throws FileNotFoundException
     * @throws ServerException
     */
    public String queryPath() throws FileNotFoundException, ServerException {
        String basePath = ResourceUtils.getURL("classpath:").getPath();
        basePath = this.initCert(basePath);
        return basePath;
    }

    /**
     * 证书模式 初始化证书文件
     *
     * @param basePath
     * @return
     * @throws ServerException
     */
    private String initCert(String basePath) throws ServerException {
        if (basePath.contains("jar!")) {
            if (basePath.startsWith("file:")) {
                basePath = basePath.replace("file:", "");
            }
            String appCertPath = AlipayConfig.APP_CERT_PATH;
            this.checkAndCopyCert(basePath, appCertPath);
            String alipayCertPath = AlipayConfig.ALIPAY_CERT_PATH;
            this.checkAndCopyCert(basePath, alipayCertPath);
            String alipayRootCertPath = AlipayConfig.ALIPAY_ROOT_CERT_PATH;
            this.checkAndCopyCert(basePath, alipayRootCertPath);
        }
        return basePath;
    }

    /**
     * 证书模式下需要
     *
     * @param basePath
     * @param path
     * @throws ServerException
     * @description 查找在该文件路径下是否已经存在这个文件，如果不存在，则拷贝文件
     */
    private void checkAndCopyCert(String basePath, String path) throws ServerException {
        InputStream certInputStream = null;
        OutputStream certOutputStream = null;
        String certPath = basePath + path;
        File certFile = new File(certPath);
        File parentFile = certFile.getParentFile();
        parentFile.mkdirs();
        try {
            if (!certFile.exists()) {
                certInputStream = ClassUtils.getDefaultClassLoader().getResourceAsStream(path);
                certOutputStream = new FileOutputStream(certFile);
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = certInputStream.read(buf)) != -1) {
                    certOutputStream.write(buf, 0, bytesRead);
                }
                certInputStream.close();
                certOutputStream.close();
            }
        } catch (IOException e) {
            throw new ServerException(e.getMessage());
        }
    }
    /**
     * 构建client
     *
     * @return
     * @throws AlipayApiException
     * @throws ServerException
     * @throws FileNotFoundException
     */
    public AlipayClient initAlipayClient() throws AlipayApiException, ServerException, FileNotFoundException {
        String basePath = this.queryPath();
        // 构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        // 设置网关地址
        certAlipayRequest.setServerUrl(AlipayConfig.gatewayUrl);
        // 设置应用Id
        certAlipayRequest.setAppId(AlipayConfig.app_id);
        // 设置应用私钥
        certAlipayRequest.setPrivateKey(AlipayConfig.merchant_private_key);
        // 设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        // 设置字符集
        certAlipayRequest.setCharset(AlipayConfig.charset);
        // 设置签名类型
        certAlipayRequest.setSignType(AlipayConfig.sign_type);
        // 设置应用公钥证书路径
        certAlipayRequest.setCertPath(basePath + AlipayConfig.APP_CERT_PATH);
        // 设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(basePath + AlipayConfig.ALIPAY_CERT_PATH);
        // 设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(basePath + AlipayConfig.ALIPAY_ROOT_CERT_PATH);
        // 构造client
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        return alipayClient;
    }

    @Override
    public Map<String, String> getAliParams(Map requestParams) {
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    @Override
    public Map<String, String> pay(Map requestParams) throws ServerException, AlipayApiException, FileNotFoundException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = this.initAlipayClient();

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(AlipayConfig.notify_url);
        //同步跳转地址，仅支持http/https
        request.setReturnUrl(AlipayConfig.return_url);
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", requestParams.get("orderNo"));
        //订单标题，不可使用特殊符号
        bizContent.put("subject", "医生挂号预约");
        //支付金额，最小值0.01元
        bizContent.put("total_amount", 0.01);


        //电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("qr_pay_mode", "4");
        bizContent.put("qrcode_width", 300);

        request.setBizContent(bizContent.toString());
        AlipayTradePrecreateResponse response = alipayClient.certificateExecute(request, "POST");

        String data = response.getBody();
        System.out.println(response);

        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return null;
    }

    @Override
    public String query(String orderNo) throws AlipayApiException, ServerException, FileNotFoundException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = this.initAlipayClient();

        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        /**请求接口*/
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderNo);
        alipayRequest.setBizModel(model);
        /**转换格式*/
        String result = alipayClient.certificateExecute(alipayRequest).getBody().replace('\"', '\'').replace('\n', ' ');
        JSONObject jsonObject = JSONObject.parseObject(result, JSONObject.class);
        JSONObject resp = (JSONObject) jsonObject.get("alipay_trade_query_response");
        String code = (String) resp.get("code");
        String status = "1";
        if ("40004".equals(code)) {
            status = "3";
        }
        String trade_status = (String) resp.get("trade_status");
        //支付成功||交易结束，不可退款
        if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
            status = "2";
            //交易创建，等待买家付款
        } else if ("WAIT_BUYER_PAY".equals(trade_status)) {
            status = "1";
            //未付款交易超时关闭，或支付完成后全额退款
        } else if ("TRADE_CLOSED".equals(trade_status)) {
            status = "3";
        }
        return status;
    }

}