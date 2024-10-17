package com.task.service.profile;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;


    // 서킷 적용하기
    @CircuitBreaker(name = "ProfileGetById", fallbackMethod = "getByIdFallBack")
    @Cacheable(value = "profile", key = "#id")
    @Override
    public ProfileResponse getById(Long id) {
        return profileRepository.searchById(id)
            .orElseThrow(()->
                new ApiException("profiles-search에서 ID "+ id + " 찾을 수 없습니다", ErrorType.NO_RESOURCE
            , HttpStatus.NOT_FOUND));
    }

    // 로컬 캐시에 담기 - 서비스마다 로컬 캐시에 담긴 데이터가 다를 수 있다는 점을 주의해야 한다.
    // 그런데 이는 db에 영향을 주어 전체적인 서비스에 영향을 줄 수 있기때문에 redis cluster나 sentinal로 고가용성을 해두기 때문에 이 시간동안 버티는 용도
    public ProfileResponse getByIdFallBack(Long id, Throwable throwable){
        log.warn("[ProfileQueryServiceImpl - getById] Circuit Breaker is open! Fallback to DB search.");
        return profileRepository.searchById(id)
            .orElseThrow(()->
                new ApiException("profiles-search에서 ID:"+ id + " 찾을 수 없습니다", ErrorType.NO_RESOURCE
                    , HttpStatus.NOT_FOUND));
    }


    @CircuitBreaker(name = "GetAllByCondition", fallbackMethod = "getAllByConditionFallBack")
    @Cacheable(cacheNames = "profiles", value = "profiles", key =
        "'page_' + #pageable.getOffset() + "
            + "'_size_' + #pageable.getPageSize() + '_sort_' + #pageable.sort.toString()")
    @Override
    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileRepository.getAllByCondition(pageable);
    }
    public PageResult<ProfileResponse> getAllByConditionFallBack(Pageable pageable, Throwable throwable){
        log.warn("[ProfileQueryServiceImpl - getAllByConditionFallBack] Circuit Breaker is open! Fallback to DB search.");
        return profileRepository.getAllByCondition(pageable);
    }
}
