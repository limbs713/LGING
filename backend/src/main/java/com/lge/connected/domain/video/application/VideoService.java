package com.lge.connected.domain.video.application;

import com.lge.connected.domain.user.entity.Video;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lge.connected.domain.video.dto.ResponseVideoDto;
import com.lge.connected.domain.video.repository.VideoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;

    public List<ResponseVideoDto> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(video -> ResponseVideoDto.of(video))
                .collect(Collectors.toList());
    }

    public Video getVideoInfo(Long id){
        return videoRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
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
