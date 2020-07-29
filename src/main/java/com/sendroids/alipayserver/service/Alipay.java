package com.sendroids.alipayserver.service;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;

import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class Alipay {

    {
        Factory.setOptions(getOptions());
    }

    private Config getOptions() {
        final var config = new Config();
        config.protocol = "https";
        // 正式环境
        // config.gatewayHost = "openapi.alipay.com";
        // 测试网关
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";

        config.appId = "2021000116673110";

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = readStringFromFile("keys/private_key.txt");
        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = readStringFromFile("keys/pub_key.txt");

        log.info("private key : {}", config.merchantPrivateKey);
        log.info("public key : {}", config.alipayPublicKey);

        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<--
        // 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        // config.alipayCertPath = "<--
        // 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt
        // -->";

        // 可设置异步通知接收服务地址（可选）
        config.notifyUrl = "http://localhost:8080/alipay/callback";

        // 可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = "";

        return config;
    }

    private String readStringFromFile(String uri) {
        try {
            return Files.readAllLines(
                    Path.of(
                            new ClassPathResource(uri).getFile().getAbsolutePath()
                    )
            )
                    .stream()
                    .findAny()
                    .orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public AlipayTradeRefundResponse refund(String outTradeNo, String refundAmount) throws Exception {
        return Factory.Payment.Common().refund(outTradeNo, refundAmount);
    }

    public AlipayTradeAppPayResponse createAppTradeOrder() throws Exception {
        return Factory.Payment.App().pay("subject", System.currentTimeMillis() + "", "0.01");


    }

    public void createWapTradeOrder() throws Exception {
        Factory.Payment.Wap().pay("subject", System.currentTimeMillis() + "", "0.01", "quitUrl", "returnUrl");
    }

    public AlipayTradeQueryResponse queryTrade(String outTradeNo) throws Exception {
        return Factory.Payment.Common().query(outTradeNo);
    }

}
