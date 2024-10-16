package com.task.controller;

import com.task.ApiResponse;
import com.task.controller.request.PointUpdate;
import com.task.service.ProfileApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/users")
public class UserController {

    private final ProfileApplicationService profileApplicationService;

    @PatchMapping("/points")
    public ResponseEntity<ApiResponse<String>> addPoint(@RequestBody PointUpdate request){
        profileApplicationService.addPoint(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok("포인트 추가 성공"));
    }

}
