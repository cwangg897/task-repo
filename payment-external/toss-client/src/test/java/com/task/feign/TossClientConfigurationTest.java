package com.task.feign;

import static org.junit.jupiter.api.Assertions.*;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Collection;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TossClientConfigurationTest {

    TossClientConfiguration configuration;

    @BeforeEach
    void setup(){
        configuration = new TossClientConfiguration();
    }
    @Test
    public void requestInterceptor_header_key_값들이_적용된다() throws Exception {
        // given
        String secretKey = "secert";
        RequestTemplate template = new RequestTemplate();
        // when
        RequestInterceptor interceptor = configuration.requestInterceptor(secretKey);
        interceptor.apply(template);;
        // then
        Map<String, Collection<String>> headers = template.headers();
        Assertions.assertThat(headers.containsKey("Authorization")).isTrue();
    }

}