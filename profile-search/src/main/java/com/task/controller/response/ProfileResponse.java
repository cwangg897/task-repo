package com.task.controller.response;

import java.time.LocalDateTime;



public record ProfileResponse(Long id, String name, LocalDateTime createdAt, Long viewCount) {
}
