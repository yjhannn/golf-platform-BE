package com.example.golfplatform.user.response;

import com.example.golfplatform.user.domain.User;

public record MyInfoResponse(
    String nickname,
    String email,
    String phoneNumber,
    String preferredRegion,
    String averageScore
) {
}
