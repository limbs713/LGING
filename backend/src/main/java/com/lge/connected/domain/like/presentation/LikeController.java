package com.lge.connected.domain.like.presentation;

import com.lge.connected.domain.like.application.LikeService;
import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.global.config.jwt.SecurityUtils;
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

    @GetMapping("/{videoId}")
    public ResponseEntity<Like> getLikeByVideoId(@PathVariable Long videoId) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(likeService.getLikeByVideoId(videoId,userId));
    }

    @PostMapping("/{videoId}")
    public ResponseEntity<Boolean> addLikeByVideoId(@PathVariable Long videoId) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(likeService.addLikeByVideoId(videoId, userId));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Boolean> deleteLikeByVideoId(@PathVariable Long videoId) {
        Long userId = SecurityUtils.getCurrentMemberId();
        likeService.deleteLikeByVideoId(videoId, userId);
        return ResponseEntity.ok(likeService.deleteLikeByVideoId(videoId, userId));
    }

    @GetMapping("/{userId}/target-group")
    public ResponseEntity<List<VideoResponseDTO>> getTargetGroupVideos(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getTargetGroupVideos(userId));
    }
}
