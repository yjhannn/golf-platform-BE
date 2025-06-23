package com.example.golfplatform.comment.controller;

import com.example.golfplatform.comment.request.CommentRequest;
import com.example.golfplatform.comment.request.CommentUpdateRequest;
import com.example.golfplatform.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal Long userId, @PathVariable Long postId,
        @RequestBody CommentRequest request) {
        commentService.createComment(userId, postId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Long userId, @PathVariable Long commentId,
        @RequestBody CommentUpdateRequest request) {
        commentService.updateComment(userId, commentId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Long userId, @PathVariable Long postId) {
        commentService.deleteComment(userId, postId);
        return ResponseEntity.ok().build();
    }

}
