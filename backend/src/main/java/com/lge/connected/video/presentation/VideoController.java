package com.lge.connected.video.presentation;

import com.lge.connected.user.entity.Comment;
import com.lge.connected.user.entity.Video;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lge.connected.video.application.VideoService;
import com.lge.connected.video.dto.ResponseVideoDto;

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

//    @PostMapping("/{id}/comment")
//    public ResponseEntity<String> addComment(@PathVariable Long id) {
//        if (videoService.addComment(id)) {
//            return ResponseEntity.ok("Comment added successfully");
//        }
//
//        return ResponseEntity.badRequest().body("Failed to add comment");
//    }


}
