package com.example.golfplatform.user.request;

import com.example.golfplatform.user.domain.AverageScore;
import com.example.golfplatform.user.domain.PreferredRegion;

public record AdditionalInfoRequest(
    String phoneNumber,
    String email,
    PreferredRegion preferredRegion,
    AverageScore averageScore
) {

}
