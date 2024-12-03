package com.lge.connected.domain.video.presentation;

import com.lge.connected.domain.video.application.VideoService;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Comment;
import com.lge.connected.domain.video.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("/{id}/comment")
//    public ResponseEntity<String> addComment(@PathVariable Long id) {
//        if (videoService.addComment(id)) {
//            return ResponseEntity.ok("Comment added successfully");
//        }
//
//        return ResponseEntity.badRequest().body("Failed to add comment");
//    }




}
