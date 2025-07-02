package com.example.golfplatform.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AverageScore {
    UNDER_72("72타 이하"), BETWEEN_72_80("72~80타"), BETWEEN_81_90("81~90타"), BETWEEN_91_100("91~100타"), OVER_100("100타 이상");

    private final String description;

    AverageScore(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static AverageScore from(String value) {
        for (AverageScore score : AverageScore.values()) {
            if (score.description.equals(value)) {
                return score;
            }
        }
        throw new IllegalArgumentException("잘못된 평균 타수입니다: " + value);
    }

}
