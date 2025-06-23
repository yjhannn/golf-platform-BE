package com.example.golfplatform.comment.service;

import com.example.golfplatform.comment.domain.Comment;
import com.example.golfplatform.comment.repository.CommentRepository;
import com.example.golfplatform.comment.request.CommentRequest;
import com.example.golfplatform.exception.UnauthorizedAccessException;
import com.example.golfplatform.post.domain.Post;
import com.example.golfplatform.post.repository.PostRepository;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public void createComment(Long userId, Long postId, CommentRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException(("유저를 찾을 수 없습니다.")));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        Comment comment = Comment.builder()
            .user(user)
            .post(post)
            .content(request.content())
            .build();
        commentRepository.save(comment);
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
