package com.lge.connected.domain.like.application;

import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.domain.like.repository.LikeRepository;
import com.lge.connected.domain.user.constant.Gender;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;
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
public class LikeService {
    private final LikeRepository likeRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Transactional
    public Boolean addLikeByVideoId(Long videoId, Long userId) {
        try {
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
            );

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
            );

            Like like = Like.builder()
                    .user(user)
                    .video(video)
                    .build();

            video.addLike();
            videoRepository.save(video);
            likeRepository.save(like);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean deleteLikeByVideoId(Long videoId, Long userId) {
        try {
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
            );

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
            );

            Like like = likeRepository.findByUserAndVideo(user, video).orElseThrow(
                    () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다.")
            );

            video.deleteLike();
            likeRepository.delete(like);
            videoRepository.save(video);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Like getLikeByVideoId(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        return likeRepository.findByUserAndVideo(user, video).orElseThrow(
                () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다.")
        );
    }

    public List<VideoResponseDTO> getTargetGroupVideos(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        int age = user.getAge();
        Gender gender = user.getGender();

        int minAge = (age / 10) * 10;
        int maxAge = minAge + 9;

        List<Video> videos = likeRepository.findMostLikedVideosByAgeAndGender(gender, minAge, maxAge);

        return videos.stream()
                .map(VideoResponseDTO::of)
                .collect(Collectors.toList());
    }
}
