package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoGenreRepository extends JpaRepository<VideoGenre, Long> {

    @Query("SELECT vg FROM VideoGenre vg JOIN vg.video v WHERE vg.video = :video")
    Optional<VideoGenre> findByVideo(@Param("video") Video video);

    @Query("SELECT vg FROM VideoGenre vg WHERE vg.video.id = :videoId")
    Optional<VideoGenre> findByVideoId(@Param("videoId") Long videoId);
}
