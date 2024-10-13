package com.task.controller.response;

import com.task.util.DateUtils;
import java.time.LocalDateTime;
import lombok.Getter;


@Getter
public class ProfileResponse {
    private final Long id;
    private final String name;
    private final String createdAt;
    private final Long viewCount;
    public ProfileResponse(Long id, String name, LocalDateTime createdAt, Long viewCount){
        this.id = id;
        this.name = name;
        this.createdAt = DateUtils.formatToKoreaTime(createdAt);
        this.viewCount = viewCount;
    }
}
