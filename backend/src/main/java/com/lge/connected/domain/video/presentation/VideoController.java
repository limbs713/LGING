package com.lge.connected.domain.video.presentation;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.application.VideoService;
import com.lge.connected.domain.video.dto.VideoInfoDto;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<VideoInfoDto> getVideoInfo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getVideoInfo(videoId));
    }

    @GetMapping("/{videoId}/comment")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getAllComments(videoId));
    }

    @GetMapping("/view-top5")
    public ResponseEntity<List<VideoResponseDTO>> getTop5ViewedVideos() {
        return ResponseEntity.ok(videoService.getTop5ViewedVideos());
    }

    @PostMapping("/{videoId}/view")
    public ResponseEntity<VideoResponseDTO> addVideoViews(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.addVideoViews(videoId));
    }

    @PostMapping("/{videoId}/history/{userId}")
    public ResponseEntity<Boolean> addHistory(@PathVariable Long videoId, @PathVariable Long userId,
                                              @RequestBody int timeStamp) {
        return ResponseEntity.ok(videoService.addHistory(videoId, userId, timeStamp));
    }

    @DeleteMapping("/{videoId}/history/{userId}")
    public ResponseEntity<Boolean> deleteHistory(@PathVariable Long videoId, @PathVariable Long userId) {
        return ResponseEntity.ok(videoService.deleteHistory(videoId, userId));
    }

    @PatchMapping("/{videoId}/history/{historyId}/{userId}")
    public ResponseEntity<Boolean> updateHistory(@PathVariable Long videoId, @PathVariable Long historyId,
                                                 @PathVariable Long userId, @RequestBody int timeStamp) {
        return ResponseEntity.ok(videoService.updateHistory(videoId, userId, historyId, timeStamp));
    }

    @GetMapping("/{videoId}/history/{userId}")
    public ResponseEntity<VideoHistory> getHistory(@PathVariable Long videoId, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(videoService.getHistory(videoId, userId));
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}
