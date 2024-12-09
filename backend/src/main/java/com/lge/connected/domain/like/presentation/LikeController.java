package com.lge.connected.domain.like.presentation;

import com.lge.connected.domain.like.application.LikeService;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{userId}/{videoId}")
    public ResponseEntity<String> addLikeByVideoId(@PathVariable Long userId, @PathVariable Long videoId) {
        likeService.addLikeByVideoId(videoId, userId);
        return ResponseEntity.ok("Like added successfully");
    }

    @DeleteMapping("/{userId}/{videoId}")
    public ResponseEntity<String> deleteLikeByVideoId(@PathVariable Long userId, @PathVariable Long videoId) {
        likeService.deleteLikeByVideoId(videoId, userId);
        return ResponseEntity.ok("Like deleted successfully");
    }

    @GetMapping("/{userId}/target-group")
    public ResponseEntity<List<VideoResponseDTO>> getTargetGroupVideos(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getTargetGroupVideos(userId));
    }
}
