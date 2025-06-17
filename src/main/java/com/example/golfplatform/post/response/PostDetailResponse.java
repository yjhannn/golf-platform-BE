package com.example.golfplatform.post.response;

import com.example.golfplatform.post.domain.Post;
import java.time.LocalDateTime;

public record PostDetailResponse(
    Long id,
    String title,
    String content,
    String authorEmail,
    Post.Category category,
    LocalDateTime createdAt
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUser().getEmail(),
            post.getCategory(),
            post.getCreatedAt()
        );
    }

}
