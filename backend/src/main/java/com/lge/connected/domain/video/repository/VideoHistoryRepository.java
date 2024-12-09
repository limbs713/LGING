package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoHistoryRepository extends JpaRepository<VideoHistory, Long> {
    Optional<VideoHistory> findByUserAndVideo(User user, Video video);
}
