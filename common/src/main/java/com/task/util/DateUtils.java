package com.task.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;


public class DateUtils {
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // LocalDateTime을 한국 시간으로 변환하고 지정된 형식으로 반환하는 메서드
    public static String formatToKoreaTime(LocalDateTime dateTime) {
        // LocalDateTime을 한국 시간으로 변환
        ZonedDateTime koreaTime = dateTime.atZone(ZoneId.of("Asia/Seoul"));
        // 원하는 형식으로 포맷팅
        return koreaTime.format(YYYY_MM_DD_HH_MM_SS_FORMATTER);
    }
}
