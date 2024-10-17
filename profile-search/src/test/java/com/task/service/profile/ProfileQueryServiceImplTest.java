package com.task.service.profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import com.task.infrastructure.user.UserEntity;
import com.task.infrastructure.user.UserRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ProfileQueryServiceImplTest {

    @Autowired
    ProfileQueryService profileQueryService;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    ProfileRepository profileRepository;

    @Test
    public void 프로필상세조회_정상상황에서는_Circuit의_상태가_CLOSED이고_레디스_캐시에서_데이터를가져온다() {
        // given
        Long profileId = 1L;
        ProfileResponse mockResponse = new ProfileResponse(profileId, "홍길동", 1L,
            LocalDateTime.now(), 1L);
        when(profileRepository.searchById(any())).thenReturn(Optional.of(mockResponse));
        // when
        profileQueryService.getById(profileId);
        // then
        verify(profileRepository, times(1)).searchById(profileId);
    }


    @Test
    public void 프로필목록조회_정상상황에서는_Circuit의_상태가_CLOSED이고_레디스_캐시에서_데이터를가져온다() {
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
        when(profileRepository.getAllByCondition(pageable)).thenReturn(profileList);
        // when
        profileQueryService.getAllByCondition(pageable);
        // then
        verify(profileRepository, times(1)).getAllByCondition(pageable);
    }


}

