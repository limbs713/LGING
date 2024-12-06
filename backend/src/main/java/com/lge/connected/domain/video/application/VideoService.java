package com.lge.connected.domain.video.application;

import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.dto.ResponseVideoDto;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.Comment;
import com.lge.connected.domain.video.exception.VideoErrorCode;
import com.lge.connected.domain.video.repository.CommentRepository;
import com.lge.connected.domain.video.repository.VideoRepository;
import com.lge.connected.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)
        );
        return commentRepository.findAllByVideo(video);
    }

    public void addStar(int stars, Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST));
        video.addStars(stars);
        videoRepository.save(video);
    }

    public void changeStar(int prev, int curr, Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)
        );
        video.changeStars(prev, curr);
        videoRepository.save(video);
    }

}
