package com.task.service.profile.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


public record ProfileQueryEvent(Long profileId, String name, LocalDateTime timeStamp) { }
