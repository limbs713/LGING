package com.lge.connected.domain.video.presentation;

import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.comment.entity.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lge.connected.domain.video.application.VideoService;
import com.lge.connected.domain.video.dto.ResponseVideoDto;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<ResponseVideoDto>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoInfo(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.getVideoInfo(id));
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long id){
        return ResponseEntity.ok(videoService.getAllComments(id));
    }
}
