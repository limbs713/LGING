package com.lge.connected.domain.like.repository;

import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndVideo(User user, Video video);
}
