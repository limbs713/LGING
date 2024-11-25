package com.lge.connected.domain.video.application;


import java.util.List;
import java.util.stream.Collectors;

import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Comment;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.exception.VideoErrorCode;
import com.lge.connected.domain.video.repository.CommentRepository;
import com.lge.connected.domain.video.repository.VideoRepository;
import com.lge.connected.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;

    public List<VideoResponseDTO> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(video -> VideoResponseDTO.of(video))
                .collect(Collectors.toList());
    }

    public Video getVideoInfo(Long id){
        return videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)
        );
    }

    public List<Comment> getAllComments(Long id) {
        return commentRepository.findAllByVideoId(id);
    }

//    public booloan addComment(Long videoId) {
//        Comment comment = Comment.builder()
//                        .
//        commentRepository.save(comment);
//        return true;
//    }
}
