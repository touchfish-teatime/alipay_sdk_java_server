package com.sendroids.alipayserver.controller;

import com.alipay.easysdk.factory.Factory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @PostMapping("/callback")
    public void callBack(
            @RequestParam Map<String, String> data
    ) throws Exception {
        var sign = Factory.Payment.Common().verifyNotify(data);

        if (sign) {
            // todo update order status
        } else {
            // todo log.warn
        }
    }

}
