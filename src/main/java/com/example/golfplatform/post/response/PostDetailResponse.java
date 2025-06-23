package com.example.golfplatform.post.response;

import com.example.golfplatform.comment.response.CommentResponse;
import com.example.golfplatform.post.domain.Post;
import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
    Long id,
    String title,
    String content,
    String authorEmail,
    Post.Category category,
    LocalDateTime createdAt,
    List<CommentResponse> comments
) {

}
