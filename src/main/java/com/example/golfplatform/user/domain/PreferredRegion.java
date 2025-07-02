package com.example.golfplatform.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum PreferredRegion {
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    GYEONGSANG("경상도"),
    CHUNGCHEONG("충청도"),
    JEOLLA("전라도"),
    JEJU("제주도"),
    OVERSEAS("해외");

    private final String description;

    PreferredRegion(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PreferredRegion from(String value) {
        for (PreferredRegion region : PreferredRegion.values()) {
            if (region.description.equals(value)) {
                return region;
            }
        }
        throw new IllegalArgumentException("잘못된 지역입니다: " + value);
    }
}
