package com.task.service;



import com.task.controller.response.ProfileResponse;
import com.task.service.profile.ProfileQueryService;
import com.task.service.profile.event.ProfileQueryEvent;
import com.task.service.user.UserCommandService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;
// 행위 검증이유 퍼사드 패턴이므로 정상동작만 확인하면 될거라고 생각했습니다
@ExtendWith(MockitoExtension.class)
class ProfileApplicationServiceTest {
    @InjectMocks
    ProfileApplicationService profileApplicationService;
    @Mock
    ProfileQueryService profileQueryService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    UserCommandService userCommandService;

    

    @Test
    public void getProfileAndSendEvent_호출시_조회수로그를_저장한다(){
        // given
        ProfileResponse response = new ProfileResponse(1L, "홍길동", 1L,
            LocalDateTime.now(), 1L);
        when(profileQueryService.getById(any())).thenReturn(response);
        // when
        profileApplicationService.getProfileAndSendEvent(1L);
        // then
        verify(profileQueryService, times(1)).getById(any(Long.class));
        verify(eventPublisher, times(1)).publishEvent(any(ProfileQueryEvent.class));
    }

    @Test
    public void getAllByCondition_호출시_프로필목록을보낸다(){
        // given & when
        profileApplicationService.getAllByCondition(any());
        // then
        verify(profileQueryService, times(1)).getAllByCondition(any());
    }

    @Test
    public void addPoint_포인트를_적립한다(){
        // given & when
        profileApplicationService.addPoint(any());
        // then
        verify(userCommandService, times(1)).addPoint(any());
    }



}