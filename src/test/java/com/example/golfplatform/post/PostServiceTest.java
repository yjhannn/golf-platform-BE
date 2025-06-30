package com.example.golfplatform.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.repository.PostRepository;
import com.example.golfplatform.post.request.PostCreateRequest;
import com.example.golfplatform.post.request.PostUpdateRequest;
import com.example.golfplatform.post.response.PostDetailResponse;
import com.example.golfplatform.post.service.PostService;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Long savedUserId;

    @BeforeEach
    void setup() {
        User user = User.builder()
            .email("test@example.com")
            .nickname("testuser")
            .build();
        savedUserId = userRepository.save(user).getId();
    }

    @Test
    @DisplayName("게시물 등록 성공")
    void createPost() {
        PostCreateRequest request = new PostCreateRequest("제목", "내용", Post.Category.FREE);
        postService.createPost(request, savedUserId);

        Post saved = postRepository.findAll().get(0);
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getUser().getId()).isEqualTo(savedUserId);
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void updatePost() {
        Post post = Post.builder()
            .title("old")
            .content("oldContent")
            .category(Post.Category.FREE)
            .user(userRepository.findById(savedUserId).orElseThrow())
            .build();
        Long postId = postRepository.save(post).getId();

        PostUpdateRequest update = new PostUpdateRequest("new", "newContent", Post.Category.QUESTION);
        postService.updatePost(postId, update, savedUserId);

        Post updated = postRepository.findById(postId).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("new");
    }

    @Test
    @DisplayName("게시물 상세조회 성공")
    void getPostDetails() {
        Post post = Post.builder()
            .title("detail")
            .content("some content")
            .category(Post.Category.REVIEW)
            .user(userRepository.findById(savedUserId).orElseThrow())
            .build();
        Long postId = postRepository.save(post).getId();

        PostDetailResponse response = postService.getPostById(postId);

        assertThat(response.title()).isEqualTo("detail");
        assertThat(response.content()).isEqualTo("some content");
    }

    @Test
    @DisplayName("게시물 삭제 성공")
    void deletePost() {
        Post post = Post.builder()
            .title("delete")
            .content("deleteContent")
            .category(Post.Category.FREE)
            .user(userRepository.findById(savedUserId).orElseThrow())
            .build();
        Long postId = postRepository.save(post).getId();

        postService.deletePost(postId, savedUserId);

        assertThat(postRepository.findById(postId)).isEmpty();
    }

    @Test
    @DisplayName("게시물 수정 권한 없을 때 예외 발생")
    void getExceptionByUnauthorizedAccessToUpdatePost() {
        User otherUser = userRepository.save(
            User.builder().email("other@example.com").nickname("other").build()
        );
        Post post = Post.builder()
            .title("title")
            .content("content")
            .category(Post.Category.FREE)
            .user(otherUser)
            .build();
        Long postId = postRepository.save(post).getId();

        PostUpdateRequest update = new PostUpdateRequest("updated", "updatedContent", Post.Category.REVIEW);

        assertThatThrownBy(() -> postService.updatePost(postId, update, savedUserId))
            .isInstanceOf(com.example.golfplatform.exception.UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 조회 예외 발생")
    void getExceptionByWrongPostId() {
        assertThatThrownBy(() -> postService.getPostById(9999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("해당 게시글은 존재하지 않습니다.");
    }
}
