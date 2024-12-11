package com.lge.connected.domain.like.presentation;

import com.lge.connected.domain.like.application.LikeService;
import com.lge.connected.domain.like.entity.Like;
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

    @PostMapping("/{videoId}/{userId}")
    public ResponseEntity<Boolean> addLikeByVideoId(@PathVariable Long videoId, @PathVariable Long userId) {
        return ResponseEntity.ok(likeService.addLikeByVideoId(videoId, userId));
    }

    @GetMapping("/{videoId}/{userId}")
    public ResponseEntity<Like> getLikeByVideoId(@PathVariable Long videoId, @PathVariable Long userId) {
        try{
            return ResponseEntity.ok(likeService.getLikeByVideoId(videoId, userId));
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    @DeleteMapping("/{videoId}/{userId}")
    public ResponseEntity<Boolean> deleteLikeByVideoId(@PathVariable Long videoId, @PathVariable Long userId) {
        return ResponseEntity.ok(likeService.deleteLikeByVideoId(videoId, userId));
    }

    @GetMapping("/{userId}/target-group")
    public ResponseEntity<List<VideoResponseDTO>> getTargetGroupVideos(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getTargetGroupVideos(userId));
    }
}
