package com.lge.connected.domain.bookmark.repository;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUserId(Long userId);

<<<<<<< Updated upstream
    @Query("SELECT B.video FROM Bookmark B WHERE B.user = :user ORDER BY B.updatedAt DESC")
    List<Video> findBookmarkedVideosByUser(@Param("user") User user);
    Optional<Bookmark> findByVideoAndUser(Video video, User user);
}
