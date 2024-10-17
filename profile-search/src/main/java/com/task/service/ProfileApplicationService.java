package com.task.service;

import com.task.PageResult;
import com.task.controller.request.PointUpdate;
import com.task.controller.response.ProfileResponse;
import com.task.service.profile.ProfileQueryService;
import com.task.service.profile.event.ProfileQueryEvent;
import com.task.service.user.UserCommandService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// 퍼사드 패턴
@Service
@RequiredArgsConstructor
public class ProfileApplicationService {

    private final ProfileQueryService profileQueryService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserCommandService userCommandService;


    public ProfileResponse getProfileAndSendEvent(Long id) {
        ProfileResponse response = profileQueryService.getById(id);  // 캐시에서 조회
        eventPublisher.publishEvent(new ProfileQueryEvent(id, response.getName(), response.getUserId(),LocalDateTime.now())); // 향후 kafka로 변경
        return response;
    }

    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileQueryService.getAllByCondition(pageable);
    }

    public void addPoint(PointUpdate request) {
        userCommandService.addPoint(request);
    }
}
