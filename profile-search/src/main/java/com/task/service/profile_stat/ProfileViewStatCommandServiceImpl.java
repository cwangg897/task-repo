package com.task.service.profile_stat;

import com.task.infrastructure.profile_stat.ProfileViewStatEntity;
import com.task.infrastructure.profile_stat.ProfileViewStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileViewStatCommandServiceImpl implements ProfileViewStatCommandService {

    private final ProfileViewStatRepository profileViewStatRepository;

    @Override
    public ProfileViewStatEntity save(ProfileViewStatEntity profileViewStat) {
        log.info("save daily stats : {}", profileViewStat);
        return profileViewStatRepository.save(profileViewStat);
    }
}
