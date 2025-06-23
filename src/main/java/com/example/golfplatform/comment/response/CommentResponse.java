package com.example.golfplatform.comment.response;

import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    String nickname,
    String content,
    LocalDateTime createdAt
) {

}
