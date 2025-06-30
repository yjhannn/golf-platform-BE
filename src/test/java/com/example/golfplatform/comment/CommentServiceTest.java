package com.example.golfplatform.comment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.golfplatform.comment.domain.Comment;
import com.example.golfplatform.comment.repository.CommentRepository;
import com.example.golfplatform.comment.request.CommentRequest;
import com.example.golfplatform.comment.request.CommentUpdateRequest;
import com.example.golfplatform.comment.service.CommentService;
import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.repository.PostRepository;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder()
            .email("test@naver.com")
            .nickname("tester")
            .build());

        post = postRepository.save(Post.builder()
            .title("테스트 제목")
            .content("내용")
            .category(Post.Category.FREE)
            .user(user)
            .build());
    }

    @Test
    @DisplayName("댓글 등록 성공")
    void createComment_success() {
        CommentRequest request = new CommentRequest("댓글 내용");
        commentService.createComment(user.getId(), post.getId(), request);

        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
        assertThat(comment.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_success() {
        Comment comment = commentRepository.save(Comment.builder()
            .content("원래 댓글")
            .user(user)
            .post(post)
            .build());

        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글");
        commentService.updateComment(user.getId(), comment.getId(), request);

        Comment updated = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        Comment comment = commentRepository.save(Comment.builder()
            .content("삭제할 댓글")
            .user(user)
            .post(post)
            .build());

        commentService.deleteComment(comment.getId(), user.getId());

        Optional<Comment> deleted = commentRepository.findById(comment.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자 아님")
    void updateComment_fail() {
        User another = userRepository.save(User.builder()
            .email("another@naver.com")
            .nickname("다른사람")
            .build());

        Comment comment = commentRepository.save(Comment.builder()
            .content("원래 댓글")
            .user(user)
            .post(post)
            .build());

        assertThrows(RuntimeException.class, () ->
            commentService.updateComment(another.getId(), comment.getId(), new CommentUpdateRequest("수정 시도")));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 작성자 아님")
    void deleteComment_fail() {
        User another = userRepository.save(User.builder()
            .email("another@naver.com")
            .nickname("다른사람")
            .build());

        Comment comment = commentRepository.save(Comment.builder()
            .content("삭제 시도 댓글")
            .user(user)
            .post(post)
            .build());

        assertThrows(RuntimeException.class, () ->
            commentService.deleteComment(comment.getId(), another.getId()));
    }
}
