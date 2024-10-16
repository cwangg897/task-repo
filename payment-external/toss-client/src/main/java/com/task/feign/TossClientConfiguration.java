package com.task.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TossClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(
        @Value("${toss.pg.secret-key}") String secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        return requestTemplate -> requestTemplate.header("Authorization", authorizations)
        .header("Content-Type", "application/json");
    }


    @Bean
    public TossErrorDecoder tossErrorDecoder(ObjectMapper objectMapper) {
        return new TossErrorDecoder(objectMapper);
    }
}
