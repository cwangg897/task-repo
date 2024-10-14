package com.task.service.profile;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
@RequiredArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    @Cacheable(value = "profile", key = "#id")
    @Override
    public ProfileResponse getById(Long id) {
        return profileRepository.searchById(id)
            .orElseThrow(()->
                new ApiException("profiles-search에서 ID "+ id + " 찾을 수 없습니다", ErrorType.NO_RESOURCE
            , HttpStatus.NOT_FOUND));
    }

    @Cacheable(cacheNames = "profiles", value = "profiles", key =
        "'page_' + #pageable.getOffset() + "
            + "'_size_' + #pageable.getPageSize() + '_sort_' + #pageable.sort.toString()")
    @Override
    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        return profileRepository.getAllByCondition(pageable);
    }
}
