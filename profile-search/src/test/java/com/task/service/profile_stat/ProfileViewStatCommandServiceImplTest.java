package com.task.service.profile_stat;

import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile_stat.ProfileViewStatEntity;
import com.task.infrastructure.user.UserEntity;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProfileViewStatCommandServiceImplTest {

    @Autowired
    private ProfileViewStatCommandService profileViewStatCommandService;


    @Test
    public void 프로필_조회_로그를_저장한다() throws Exception {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 10, 17, 9, 0, 0);
        UserEntity user = new UserEntity("홍길동");
        ProfileEntity profile = new ProfileEntity(1L, "홍길동_프로필이름", user);
        ProfileViewStatEntity profileViewStat = new ProfileViewStatEntity(profile, time);
        // when
        ProfileViewStatEntity save = profileViewStatCommandService.save(profileViewStat);
        // then
        Assertions.assertThat(save.getProfileEntity().getName()).isEqualTo(profileViewStat.getProfileEntity().getName());
    }

}