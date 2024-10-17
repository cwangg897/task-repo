package com.task.service.profile.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.infrastructure.user.UserEntity;
import com.task.infrastructure.user.UserRepository;
import com.task.service.profile_stat.ProfileViewStatCommandService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ProfileQueryEventHandlerTest {

    @InjectMocks
    ProfileQueryEventHandler eventHandler;

    @Mock
    ProfileViewStatCommandService profileViewStatCommandService;

    @Mock
    UserRepository userRepository;

    @Test
    public void 프로필조회_이벤트를_받아_저장한다() throws Exception {
        // given
        UserEntity user = new UserEntity(1L, "홍길동");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ProfileQueryEvent event = new ProfileQueryEvent(1L, "테스트", 1L, LocalDateTime.now());
        // when
        eventHandler.handleProfileQueryEvent(event);
        // then
        verify(userRepository, times(1)).findById(anyLong());
        verify(profileViewStatCommandService, times(1)).save(any());
    }

}