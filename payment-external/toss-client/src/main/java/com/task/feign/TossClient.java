package com.task.feign;

import com.task.TossPaymentAcceptRequest;
import com.task.TossPaymentAcceptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tossClient", url = "https://api.tosspayments.com/v1/payments", configuration = TossClientConfiguration.class)
public interface TossClient {
    @PostMapping("/confirm")
    TossPaymentAcceptResponse approvePayment(@RequestBody TossPaymentAcceptRequest request);
}