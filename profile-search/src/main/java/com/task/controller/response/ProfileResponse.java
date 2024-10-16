package com.task.controller.response;

import com.task.util.DateUtils;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileResponse {
    private Long id;
    private String name;
    private Long userId;
    private String createdAt;
    private Long viewCount;
    public ProfileResponse(Long id, String name, Long userId, LocalDateTime createdAt, Long viewCount){
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.createdAt = DateUtils.formatToKoreaTime(createdAt);
        this.viewCount = viewCount;
    }
}
