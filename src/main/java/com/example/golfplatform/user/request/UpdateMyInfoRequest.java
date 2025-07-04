package com.example.golfplatform.user.request;

public record UpdateMyInfoRequest(
    String phoneNumber,
    String preferredRegion,
    String averageScore
) {

}
