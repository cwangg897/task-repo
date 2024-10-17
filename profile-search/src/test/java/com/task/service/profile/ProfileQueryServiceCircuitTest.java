package com.task.service.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class ProfileQueryServiceCircuitTest {

    @Autowired
    ProfileQueryService profileQueryService;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    ProfileRepository profileRepository;

    @Test
    public void 프로필상세조회_circuit이_open되면_DB에직접요청한다() {
        // given
        Long profileId = 1L;
        ProfileResponse mockResponse = new ProfileResponse(profileId, "홍길동", 1L,
            LocalDateTime.now(), 1L);
        when(profileRepository.searchById(any()))
            .thenThrow(new ApiException("테스트 예외", ErrorType.UNKNOWN_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR))
            .thenReturn(Optional.of(mockResponse));  // 첫 번째 호출 시 반환 // 두 번째 호출 시 예외 발생

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(1)
            .minimumNumberOfCalls(1)
            .failureRateThreshold(50)
            .build();
        circuitBreakerRegistry.circuitBreaker("ProfileGetById", config);
        // when
        profileQueryService.getById(profileId);
        // then
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream()
            .findFirst().get();
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        verify(profileRepository, times(2)).searchById(profileId);
    }


    @Test
    public void 프로필목록조회_circuit이_open되면_DB에직접요청한다() {
        // given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        ProfileResponse mockResponse1 = new ProfileResponse(1L, "홍길동1", 1L, LocalDateTime.now(),
            1L);
        ProfileResponse mockResponse2 = new ProfileResponse(2L, "짱구2", 2L, LocalDateTime.now(),
            10L);
        List<ProfileResponse> responseList = List.of(mockResponse1, mockResponse2);
        PageResult<ProfileResponse> profileList = new PageResult<>(
            Long.valueOf(pageable.getOffset()).intValue(),
            pageable.getPageSize(), 200, responseList);


        when(profileRepository.getAllByCondition(any()))
            .thenThrow(new RuntimeException("Redis connection error"))
            .thenReturn(profileList);  // 첫 번째 호출 시 반환 // 두 번째 호출 시 예외 발생

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(1)
            .minimumNumberOfCalls(1)
            .failureRateThreshold(50)
            .build();
        circuitBreakerRegistry.circuitBreaker("GetAllByCondition", config);
        // when
        profileQueryService.getAllByCondition(pageable);
        // then
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream()
            .findFirst().get();

        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        verify(profileRepository, times(2)).getAllByCondition(pageable);
    }
}
