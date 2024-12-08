package com.lge.connected.domain.video.presentation;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.application.VideoService;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoHistory;
import com.lge.connected.global.config.jwt.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoResponseDTO>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<Video> getVideoInfo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getVideoInfo(videoId));
    }

    @GetMapping("/{videoId}/comment")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long videoId){
        return ResponseEntity.ok(videoService.getAllComments(videoId));
    }

    @PostMapping("/{id}/history")
    public ResponseEntity<Boolean> addHistory(@PathVariable Long id, @RequestBody int timeStamp) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(videoService.addHistory(id, userId, timeStamp));
    }

    @DeleteMapping("/{id}/history")
    public ResponseEntity<Boolean> deleteHistory(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(videoService.deleteHistory(id, userId));
    }

    @PatchMapping("/{id}/history/{historyId}")
    public ResponseEntity<Boolean> updateHistory(@PathVariable Long id, @PathVariable Long historyId, @RequestBody int timeStamp) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(videoService.updateHistory(id, userId, historyId, timeStamp));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<VideoHistory> getHistory(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(videoService.getHistory(id, userId));
    }
}
