package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.user.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
