package com.lge.connected.domain.like.application;

import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.domain.like.repository.LikeRepository;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    public void addLikeByVideoId(Long videoId, Long userId) {
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
    }

    public void deleteLikeByVideoId(Long videoId, Long userId) {
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
        videoRepository.save(video);
        likeRepository.delete(like);
    }
}
