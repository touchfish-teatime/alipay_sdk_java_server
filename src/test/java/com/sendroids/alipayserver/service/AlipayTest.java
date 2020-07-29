package com.sendroids.alipayserver.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AlipayTest {

    @Autowired
    Alipay alipay;

    @Test
    void refund() throws Exception {
        var result = alipay.refund("1596010117110","0.01");
        log.info("refund trade : {}",result.httpBody);
    }

    @Test
    void createAppTradeOrder() throws Exception {
        var trade = alipay.createAppTradeOrder();

        log.info("you had created trade : {} ",trade.body);
        Arrays.stream(
                URLDecoder.decode(trade.body, StandardCharsets.UTF_8).split("&")
        ).forEach(s->log.info(s));
    }

    @Test
    void createWapTradeOrder() {
    }

    @Test
    void queryTrade() throws Exception {
        var trade = alipay.queryTrade("1596010117110");
        log.info("you got a trade : {}",trade.body);
    }
}