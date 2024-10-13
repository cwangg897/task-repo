package com.task;

import java.util.List;
import lombok.Builder;

// Spring Data Jpa에서는 응답기본객체를 지원해주지만 Mybatis나 다른db나 mapper를
// 사용할 때는 이용하지못하기때문에 공통 포맷을 만들었습니다.
@Builder
public record PageResult<T>(int page, int size, long totalElements, List<T> contents) {
}
