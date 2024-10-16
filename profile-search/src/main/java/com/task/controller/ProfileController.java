package com.task.controller;

import com.task.ApiResponse;
import com.task.PageResult;
import com.task.controller.request.PointUpdate;
import com.task.controller.response.ProfileResponse;
import com.task.service.ProfileApplicationService;
import com.task.service.profile.ProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/profiles")

public class ProfileController {

    private final ProfileApplicationService profileApplicationService;

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getById(@PathVariable("id") Long id){
        ProfileResponse response = profileApplicationService.getProfileAndSendEvent(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }

    // 목록조회(페이지네이션) - 이름 가나다 순, 조회 수 순, 등록 최신 순
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ProfileResponse>>> getAllByCondition(
        @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable
    ){
        PageResult<ProfileResponse> response = profileApplicationService.getAllByCondition(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }

    @PatchMapping("/points")
    public ResponseEntity<ApiResponse<String>> addPoint(@RequestBody PointUpdate request){
        profileApplicationService.addPoint(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok("포인트 추가 성공"));
    }
}
