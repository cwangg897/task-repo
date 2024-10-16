package com.task.service.profile;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.controller.request.PointUpdate;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandServiceImpl implements ProfileCommandService{

    private final ProfileRepository profileRepository;

    @Override
    public void addPoint(PointUpdate request) {
        ProfileEntity profile = profileRepository.findByIdWithPessimisticLock(Long.valueOf(request.getUserId()))
            .orElseThrow(() ->
                new ApiException("profiles-search에서 ID " + request.getUserId() + " 찾을 수 없습니다",
                    ErrorType.NO_RESOURCE
                    , HttpStatus.NOT_FOUND));
        profile.addPoint(Long.valueOf(request.getAmount()));
    }
}
