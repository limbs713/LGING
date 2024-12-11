package com.lge.connected.domain.video.application;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.comment.repository.CommentRepository;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.dto.VideoInfoDto;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;

import com.lge.connected.domain.video.exception.VideoErrorCode;
import com.lge.connected.domain.video.repository.VideoRepository;
import com.lge.connected.global.util.CustomException;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.entity.VideoHistory;
import com.lge.connected.domain.video.repository.VideoHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VideoHistoryRepository videoHistoryRepository;


    public List<VideoResponseDTO> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(video -> VideoResponseDTO.of(video))
                .collect(Collectors.toList());
    }

    public VideoInfoDto getVideoInfo(Long id){
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)

        );

        return VideoInfoDto.from(video);
    }

    public List<Comment> getAllComments(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new CustomException(VideoErrorCode.VIDEO_NOT_EXIST)
        );
        return commentRepository.findAllByVideo(video);
    }


    public Boolean addHistory(Long videoId, Long userId, int timeStamp) {
        try {
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
            );

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
            );

            VideoHistory history = VideoHistory.builder()
                    .user(user)
                    .video(video)
                    .timestamp(timeStamp)
                    .build();

            videoHistoryRepository.save(history);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public VideoHistory getHistory(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );

        return videoHistoryRepository.findByUserAndVideo(user, video).orElseThrow(
                () -> new IllegalArgumentException("해당 히스토리가 존재하지 않습니다.")
        );
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



    public Boolean updateHistory(Long videoId, Long userId, Long historyId, int timeStamp) {

        try {

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
            );

            VideoHistory videoHistory = videoHistoryRepository.findById(historyId).orElseThrow(
                    () -> new IllegalArgumentException("해당 시청 기록이 존재하지 않습니다.")
            );

            if (!videoHistory.getUser().equals(user)) {
                throw new IllegalArgumentException("해당 시청 기록에 대한 권한이 없습니다.");
            }

            videoHistory.updateTimeStamp(timeStamp);

            videoHistoryRepository.save(videoHistory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean deleteHistory(Long videoId, Long userId) {
        try {
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
            );

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
            );

            VideoHistory videoHistory = videoHistoryRepository.findByUserAndVideo(user, video).orElseThrow(
                    () -> new IllegalArgumentException("해당 시청 기록이 존재하지 않습니다.")
            );

            videoHistoryRepository.delete(videoHistory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
