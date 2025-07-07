package com.example.golfplatform.user.request;

import com.example.golfplatform.user.domain.AverageScore;
import com.example.golfplatform.user.domain.PreferredRegion;

public record UpdateMyInfoRequest(
    String phoneNumber,
    String preferredRegion,
    String averageScore
) {
    public PreferredRegion toPreferredRegion() {
        return PreferredRegion.from(preferredRegion);
    }

    public AverageScore toAverageScore() {
        return AverageScore.from(averageScore);
    }
}
