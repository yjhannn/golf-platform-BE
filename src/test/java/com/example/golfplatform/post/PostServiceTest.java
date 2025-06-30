package com.example.golfplatform.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.domain.Post.Category;
import com.example.golfplatform.post.repository.PostRepository;
import com.example.golfplatform.post.request.PostCreateRequest;
import com.example.golfplatform.post.request.PostUpdateRequest;
import com.example.golfplatform.post.service.PostService;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .nickname("테스터")
            .profileImageUrl("image.png")
            .build();
    }

    @Test
    @DisplayName("게시물 등록 성공")
    void createPost() {
        // given
        PostCreateRequest request = new PostCreateRequest("제목", "내용", Category.FREE);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when
        postService.createPost(request);

        // then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getContent()).isEqualTo("내용");
        assertThat(saved.getCategory()).isEqualTo(Category.FREE);
        assertThat(saved.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void updatePost() {
        // given
        Post post = Post.builder()
            .id(1L)
            .user(user)
            .title("기존제목")
            .content("기존내용")
            .category(Category.FREE)
            .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostUpdateRequest request = new PostUpdateRequest("수정제목", "수정내용", Category.QUESTION);

        // when
        postService.updatePost(1L, request);

        // then
        assertThat(post.getTitle()).isEqualTo("수정제목");
        assertThat(post.getContent()).isEqualTo("수정내용");
        assertThat(post.getCategory()).isEqualTo(Category.QUESTION);
    }

    @Test
    @DisplayName("게시물 상세조회 성공")
    void getPostDetails() {
        // given
        Post post = Post.builder()
            .id(1L)
            .user(user)
            .title("제목")
            .content("내용")
            .category(Category.FREE)
            .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        var result = postService.getPostById(1L);

        // then
        assertThat(result.title()).isEqualTo("제목");
        assertThat(result.content()).isEqualTo("내용");
    }

    @Test
    @DisplayName("게시물 삭제 성공")
    void deletePost() {
        // given
        Post post = Post.builder()
            .id(1L)
            .user(user)
            .title("삭제할 제목")
            .content("삭제할 내용")
            .category(Category.REVIEW)
            .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        verify(postRepository, times(1)).delete(post);
        assertThat(postRepository.findAll()).hasSize(0);
    }

}
