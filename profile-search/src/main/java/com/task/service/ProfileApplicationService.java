package com.task.service;

import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.service.profile.ProfileQueryService;
import com.task.service.profile.event.ProfileQueryEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// 퍼사드 패턴
@Service
@RequiredArgsConstructor
public class ProfileApplicationService {

    private final ProfileQueryService profileQueryService;
    private final ApplicationEventPublisher eventPublisher;

    public ProfileResponse getProfileAndSendEvent(Long id) {
        ProfileResponse response = profileQueryService.getById(id);  // 캐시에서 조회
        eventPublisher.publishEvent(new ProfileQueryEvent(id, response.getName(), LocalDateTime.now())); // 향후 kafka로 변경
        return response;
    }

    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileQueryService.getAllByCondition(pageable);
    }

}
