package com.example.golfplatform.user;

import com.example.golfplatform.user.request.AdditionalInfoRequest;
import com.example.golfplatform.user.response.MyInfoResponse;
import com.example.golfplatform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    @PostMapping("/profile")
    public ResponseEntity<Void> updateAdditionUserInfo(@AuthenticationPrincipal Long userId,
        @RequestBody AdditionalInfoRequest request) {
        userService.addProfile(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getUserProfile(@AuthenticationPrincipal Long userId) {
        MyInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

}
