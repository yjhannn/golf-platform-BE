package com.example.golfplatform.review.request;

import java.time.LocalDateTime;
import org.antlr.v4.runtime.misc.NotNull;

public record ReviewCreateRequest(
    @NotNull Long userId,
    @NotNull Long golfCourseId,
    int rating,
    String title,
    String content,
    LocalDateTime visitedAt,
    String imageUrl
) {

}
