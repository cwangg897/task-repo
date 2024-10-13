package com.task.service.profile;

import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import org.springframework.data.domain.Pageable;

public interface ProfileQueryService {

    ProfileResponse getById(Long id);

    PageResult<ProfileResponse> getAllByCondition(Pageable pageable);

}
