package com.task.service.user;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.controller.request.PointUpdate;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.user.UserEntity;
import com.task.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;

    @Override
    public void addPoint(PointUpdate request) {
        try {
            UserEntity user = userRepository.findByIdWithPessimisticLock(request.getUserId())
                .orElseThrow(() ->
                    new ApiException("user ID " + request.getUserId() + " 찾을 수 없습니다",
                        ErrorType.NO_RESOURCE
                        , HttpStatus.NOT_FOUND));
            user.addPoint(Long.valueOf(request.getAmount()));
        }catch (Exception e){
            String message = String.format("[UserCommandServiceImpl] - addPoint 포인트 지급 실패 userId: %s, amount: %s",
                request.getUserId(), request.getAmount());
            throw new ApiException(message, ErrorType.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
