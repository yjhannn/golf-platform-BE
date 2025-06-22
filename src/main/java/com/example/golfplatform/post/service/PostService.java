package com.example.golfplatform.post.service;

import com.example.golfplatform.exception.UnauthorizedAccessException;
import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.repository.PostRepository;
import com.example.golfplatform.post.request.PostCreateRequest;
import com.example.golfplatform.post.request.PostUpdateRequest;
import com.example.golfplatform.post.response.PostDetailResponse;
import com.example.golfplatform.post.response.PostListResponse;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostListResponse> getAllPosts() {
        return postRepository.findAll().stream()
            .map(PostListResponse::from)
            .collect(Collectors.toList());
    }

    public PostDetailResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        return PostDetailResponse.from(post);
    }

    public void createPost(PostCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException(("유저를 찾을 수 없습니다.")));
        Post post = Post.builder()
            .user(user)
            .title(request.title())
            .content(request.content())
            .category(request.category())
            .build();
        postRepository.save(post);
    }

    public void updatePost(Long id, PostUpdateRequest request, Long userId) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 이미 존재하지 않습니다."));
        if(!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("수정 권한이 없습니다.");
        }
        post.update(request.title(), request.content(), request.category());
    }

    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 이미 존재하지 않습니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

}
