package com.lge.connected.domain.like.repository;

import com.lge.connected.domain.like.entity.Like;
import com.lge.connected.domain.user.constant.Gender;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndVideo(User user, Video video);

    @Query("SELECT l FROM Like l WHERE l.user = :user AND l.video = :video")
    Optional<Like> findByUserVideo(@Param("user") User user, @Param("video") Video video);

    @Query("SELECT l FROM Like l WHERE l.user = :user ORDER BY l.createdAt DESC")
    List<Like> findByUser(@Param("user") User user);

    @Query("SELECT l.video FROM Like l WHERE l.user.gender = :gender AND l.user.age BETWEEN :minAge AND :maxAge GROUP BY l.video ORDER BY COUNT(l.video) DESC LIMIT 5")
    List<Video> findMostLikedVideosByAgeAndGender(@Param("gender") Gender gender, @Param("minAge") int minAge, @Param("maxAge") int maxAge);

}
