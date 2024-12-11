package com.lge.connected.domain.comment.presentation;

import com.lge.connected.domain.comment.application.CommentService;
import com.lge.connected.domain.comment.dto.RequestCommentDto;
import com.lge.connected.domain.comment.entity.Comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{videoId}")
    public ResponseEntity<List<Comment>> getAllCommentsByVideo(@PathVariable Long videoId) {
        return ResponseEntity.ok(commentService.getAllCommentsByVideo(videoId));
    }

    @PostMapping("/add/{videoId}/{userId}")
    public ResponseEntity<Boolean> addCommentOnVideo(@PathVariable Long videoId, @PathVariable Long userId,
                                                    @RequestBody RequestCommentDto commentDto) {
        return ResponseEntity.ok(commentService.addCommentByVideo(videoId, userId, commentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

    @PostMapping("/update/{userId}/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                 @RequestBody RequestCommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDto, userId));

    }

}
