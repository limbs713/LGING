package com.lge.connected.video.repository;

import com.lge.connected.user.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
