package com.task.service.profile;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
@RequiredArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    @Override
    public ProfileResponse getById(Long id) {
        return profileRepository.searchById(id)
            .orElseThrow(()->
                new ApiException("profiles-search에서 ID "+ id + " 찾을 수 없습니다", ErrorType.NO_RESOURCE
            , HttpStatus.NOT_FOUND));
    }

    @Override
    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileRepository.getAllByCondition(pageable);
    }
}
