package com.task.service.profile.event;

import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile_stat.ProfileViewStatEntity;
import com.task.service.profile_stat.ProfileViewStatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileQueryEventHandler {

    private final ProfileViewStatCommandService profileViewStatCommandService;

    @Async
    @EventListener
    public void handleProfileQueryEvent(ProfileQueryEvent event) throws InterruptedException {
        log.info("[ProfileQueryEventHandler] handleEvent : {}", event);
        ProfileEntity profile = new ProfileEntity(event.profileId(), event.name());
        ProfileViewStatEntity profileViewStat = new ProfileViewStatEntity(profile, event.timeStamp());
        profileViewStatCommandService.save(profileViewStat);
    }
}
