package com.task.feign;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.ApiException;
import com.task.ErrorType;
import com.task.TossPaymentAcceptFailResponse;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class TossErrorDecoderTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TossErrorDecoder errorDecoder;

    @Test
    public void 에러디코더에서_에러발생시_ApiException_예외가_발생한다() throws Exception {
        // given
        String code = "ERROR_CODE";
        String message = "Error occurred";
        String responseBody = String.format("{\"code\":\"%s\",\"message\":\"%s\"}", code, message);

        Response.Body body = mock(Response.Body.class);
        given(body.asInputStream()).willReturn(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)));
        Response response = Response.builder()
            .status(400)
            .request(Request.create(HttpMethod.POST, "test", new HashMap<>() ,new byte[0], StandardCharsets.UTF_8))
            .body(body)
            .build();

        given(objectMapper.readValue(any(String.class), eq(TossPaymentAcceptFailResponse.class)))
            .willReturn(new TossPaymentAcceptFailResponse(code, message));
        // when
        // then
        TossException exception = assertThrows(TossException.class, () -> {
            errorDecoder.decode("testMethodKey", response);
        });
        Assertions.assertThat(code).isEqualTo(exception.getCode());
        Assertions.assertThat(message).isEqualTo(exception.getMessage());
    }

    @Test
    public void 에러디코에서_메시지_파싱시_에러발생() throws Exception {
        // Given
        Response.Body body = mock(Response.Body.class);
        Response response = Response.builder()
            .status(500)
            .request(Request.create(HttpMethod.POST, "test", new HashMap<>() ,new byte[0], StandardCharsets.UTF_8))
            .body(body)
            .build();
        when(body.asInputStream()).thenThrow(new IOException("Toss메시지 IO Exception"));
        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> {
            errorDecoder.decode("testMethodKey", response);
        });
        System.out.println(exception);
        Assertions.assertThat(exception.getErrorMessage()).isEqualTo("토스 PG승인 메시지 파싱 에러");
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.EXTERNAL_API_ERROR);
        Assertions.assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Response.Body responseBody = mock(Response.Body.class);
     *         ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]); // 빈 InputStream
     *         Response response = Response.builder()
     *                 .status(400)
     *                 .request(Request.create(Request.HttpMethod.GET, "testUrl", Request.Body.empty(), null))
     *                 .body(responseBody)
     *                 .build();
     *
     *         when(responseBody.asInputStream()).thenReturn(inputStream);
     *         when(objectMapper.readValue(any(ByteArrayInputStream.class), eq(NaverErrorResponse.class)))
     *                 .thenReturn(new NaverErrorResponse("error!!", "SE03"));
     *
     *         // When & Then
     *         ApiException exception = assertThrows(ApiException.class, () -> {
     *             errorDecoder.decode("testMethodKey", response);
     *         });
     *
     *         // Verify
     *         assertEquals("error!!", exception.getErrorMessage());
     *         assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
     *         assertEquals(ErrorType.EXTERNAL_API_ERROR, exception.getErrorType());
     *
     *         // Mockito interactions 확인
     *         verify(responseBody, times(1)).asInputStream();
     *         verify(objectMapper, times(1)).readValue(any(ByteArrayInputStream.class), eq(NaverErrorResponse.class));
     */

}