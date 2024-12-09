package com.lge.connected.domain.video.application;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.comment.repository.CommentRepository;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;

import com.lge.connected.domain.video.exception.VideoErrorCode;
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
    private final UserRepository userRepository;

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

    @Transactional
    public void addStar(int stars, Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST));
        video.addStars(stars);
        videoRepository.save(video);
    }

    @Transactional
    public void changeStar(int prev, int curr, Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)
        );
        video.changeStars(prev, curr);
        videoRepository.save(video);
    }

    public List<VideoResponseDTO> getTop5ViewedVideos() {
        List<Video> videos = videoRepository.sortByViewCount();
        return videos.stream()
                .map(VideoResponseDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public VideoResponseDTO addVideoViews(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST));
        video.addViews();
        videoRepository.save(video);
        return VideoResponseDTO.of(video);
    }




}
