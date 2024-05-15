package com.kanfs.omas.config;


import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo
 */
@Configuration
public class AlipayConfig {
    /**
     * 应用公钥证书路径
     */
    public static final String APP_CERT_PATH = "static/cert/appCertPublicKey_2021004139601603.crt";
    /**
     * 支付宝公钥证书路径
     */
    public static final String ALIPAY_CERT_PATH = "static/cert/alipayCertPublicKey_RSA2.crt";
    /**
     * 支付宝根证书路径
     */
    public static final String ALIPAY_ROOT_CERT_PATH = "static/cert/alipayRootCert.crt";

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
     */
    public static String app_id = "";

    /**
     * 私钥，您的PKCS8格式RSA2私钥(应用私钥)
     */
    public static String merchant_private_key = "";

    /**
     * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问（可以使用natapp内容穿透完成外网设置）
     */
    public static String notify_url = "http://st555kt1752.vicp.fun:80/mas/alipay/aliPayCallBack";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static String return_url = "http://st555kt1752.vicp.fun:80/mas/alipay/returnUrl";

    /**
     * 签名方式  固定
     */
    public static String sign_type = "RSA2";

    /**
     * 字符编码格式  固定
     */
    public static String charset = "utf-8";

    /**
     * 支付宝网关   开发环境
     */
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

}



