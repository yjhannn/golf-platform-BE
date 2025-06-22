package com.example.golfplatform.post.request;

import com.example.golfplatform.post.domain.Post;

public record PostCreateRequest(
    String title,
    String content,
    Post.Category category
) {

}
