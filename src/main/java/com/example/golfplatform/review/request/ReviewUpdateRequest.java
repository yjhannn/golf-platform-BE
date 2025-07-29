package com.example.golfplatform.review.request;

import java.time.LocalDateTime;

public record ReviewUpdateRequest(
    int rating,
    String title,
    String content,
    LocalDateTime visitedAt,
    String imageUrl
) {

}
