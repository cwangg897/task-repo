package com.task.infrastructure.profile;

import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProfileRepositoryCustom {
    ProfileResponse searchById(Long id);

    PageResult<ProfileResponse> getAllByCondition(Pageable pageable);
}
