package com.task.service.profile.event;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile_stat.ProfileViewStatEntity;
import com.task.infrastructure.user.UserEntity;
import com.task.infrastructure.user.UserRepository;
import com.task.service.profile_stat.ProfileViewStatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileQueryEventHandler {

    private final ProfileViewStatCommandService profileViewStatCommandService;
    private final UserRepository userRepository;

    @Async
    @EventListener
    public void handleProfileQueryEvent(ProfileQueryEvent event) throws InterruptedException {
        log.info("[ProfileQueryEventHandler] handleEvent : {}", event);
        UserEntity user = userRepository.findById(event.userId())
            .orElseThrow(() -> new ApiException("user-search에서 user ID " + event.userId() + " 찾을 수 없습니다",
                ErrorType.NO_RESOURCE
                , HttpStatus.NOT_FOUND));
        ProfileEntity profile = new ProfileEntity(event.profileId(), event.name(), user);
        ProfileViewStatEntity profileViewStat = new ProfileViewStatEntity(profile, event.timeStamp());
        profileViewStatCommandService.save(profileViewStat);
    }
}
