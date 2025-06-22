package com.example.golfplatform.post.request;

import com.example.golfplatform.post.domain.Post;

public record PostUpdateRequest(
    String title,
    String content,
    Post.Category category
) {

}
