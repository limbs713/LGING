package com.lge.connected.domain.video.application;

import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.comment.repository.CommentRepository;
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

    public Video getVideoInfo(Long id) {
        return videoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );
    }

    public List<Comment> getAllComments(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );
        return commentRepository.findAllByVideo(video);
    }

    public void addStar(int stars, Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다."));
        video.addStars(stars);
        videoRepository.save(video);
    }

    public void changeStar(int prev, int curr, Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );
        video.changeStars(prev, curr);
        videoRepository.save(video);
    }
}
