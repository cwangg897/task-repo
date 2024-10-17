package com.task.util;



import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    public void UTC_시간이들어오면_한국시간으로_포멧변경() throws Exception {
        // given
        LocalDateTime utcDateTime = LocalDateTime.of(2024, 10, 17, 0, 0, 0);
        // when
        String koreaTimeString = DateUtils.formatToKoreaTime(utcDateTime);
        LocalDateTime koreaTimeToLocalTime = LocalDateTime.parse(koreaTimeString,
            DateUtils.YYYY_MM_DD_HH_MM_SS_FORMATTER);
        // then 다시 UTC시간으로 바꿔도 같은지
        Assertions.assertThat(koreaTimeToLocalTime).isEqualTo(utcDateTime);
    }

}