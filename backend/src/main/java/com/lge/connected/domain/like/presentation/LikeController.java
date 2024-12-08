package com.lge.connected.domain.like.presentation;

import com.lge.connected.domain.like.application.LikeService;
import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.global.config.jwt.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {
    private final LikeService likeService;

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
}
