package com.task.service;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.QProfileEntity;
import com.task.infrastructure.profile_stat.ProfileViewStatEntity;
import com.task.service.profile.ProfileQueryService;
import com.task.service.profile_stat.ProfileViewStatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 퍼사드 패턴
@Service
@RequiredArgsConstructor
public class ProfileApplicationService {

    private final ProfileQueryService profileQueryService;
    private final ProfileViewStatCommandService profileViewStatCommandService;

    public ProfileResponse getById(Long id) {
        ProfileResponse response = profileQueryService.getById(id);
        ProfileViewStatEntity viewStatEntity = new ProfileViewStatEntity(new ProfileEntity(
            response.getId(),
            response.getName()));
        profileViewStatCommandService.save(viewStatEntity); // 이벤트로 빼기
        return response;
    }

    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileQueryService.getAllByCondition(pageable);
    }
}
