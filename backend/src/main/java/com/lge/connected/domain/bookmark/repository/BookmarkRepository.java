package com.lge.connected.domain.bookmark.repository;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByVideoIdAndUserId(Long videoId, Long userId);

    List<Bookmark> findAllByUserId(Long userId);
}
