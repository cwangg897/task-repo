package com.task.infrastructure.profile;

import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProfileRepositoryCustom {
    Optional<ProfileResponse> searchById(Long id);

    PageResult<ProfileResponse> getAllByCondition(Pageable pageable);
}
