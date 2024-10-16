package com.task.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.ApiException;
import com.task.ErrorType;
import com.task.TossPaymentAcceptFailResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class TossErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public TossErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            TossPaymentAcceptFailResponse errorResponse = objectMapper.readValue(body, TossPaymentAcceptFailResponse.class);
            throw new TossException( errorResponse.getCode(), errorResponse.getMessage(), response.status());
        } catch (IOException e) {
            log.error("[Toss] PG승인 에러 code={}, request={}, methodKey={}, errorMessage={}", response.status(), response.request(), methodKey, e.getMessage());
            throw new ApiException("토스 PG승인 에러", ErrorType.EXTERNAL_API_ERROR, HttpStatus.valueOf(response.status()));
        }
    }
}
