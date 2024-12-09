package com.lge.connected.domain.video.presentation;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.application.VideoService;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.dto.ResponseVideoDto;
import com.lge.connected.domain.video.entity.Video;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<Video> getVideoInfo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getVideoInfo(videoId));
    }

    @GetMapping("/{videoId}/comment")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long videoId){
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
   
}
