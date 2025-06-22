package com.example.golfplatform.post.response;

import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.domain.Post.Category;
import java.time.LocalDateTime;

public record PostListResponse(
    Long id,
    String title,
    String email,
    Post.Category category,
    LocalDateTime createdAt
) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
            post.getId(),
            post.getTitle(),
            post.getUser().getEmail(),
            post.getCategory(),
            post.getCreatedAt()
        );
    }

}
