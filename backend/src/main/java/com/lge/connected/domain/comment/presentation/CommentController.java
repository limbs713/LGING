package com.lge.connected.domain.comment.presentation;

import com.lge.connected.domain.comment.application.CommentService;
import com.lge.connected.domain.comment.dto.RequestCommentDto;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.global.util.SecurityUtils;
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

    @PostMapping("/{videoId}")
    public ResponseEntity<String> addCommentOnVideo(@PathVariable Long videoId,
                                                    @RequestBody RequestCommentDto commentDto) {
        commentService.addCommentByVideo(videoId, commentDto);
        return ResponseEntity.ok("Failed to add comment");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.ok("Comment deleted successfully");
        }

        return ResponseEntity.badRequest().body("Failed to delete comment");
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @RequestBody RequestCommentDto commentDto) {
        Long userId = SecurityUtils.getCurrentMemberId();
        commentService.updateComment(commentId, commentDto, userId);
        return ResponseEntity.ok("Comment updated successfully");

    }


}
