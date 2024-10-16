package com.task.feign;

import com.task.domain.PointAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userClient", url = "${user.url}")
public interface UserClient {

    @PatchMapping("/api/v1/users/points")
    void addPoint(@RequestBody PointAddRequest request);

}
