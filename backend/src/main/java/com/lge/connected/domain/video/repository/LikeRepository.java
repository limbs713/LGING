package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Like;
import com.lge.connected.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l FROM Like l WHERE l.user = :user AND l.video = :video")
    Optional<Like> findByUserVideo(@Param("user") User user, @Param("video") Video video);

    @Query("SELECT l FROM Like l WHERE l.user = :user ORDER BY l.createdAt DESC")
    List<Like> findByUser(@Param("user") User user);

    void deleteByUser(User user);

}
