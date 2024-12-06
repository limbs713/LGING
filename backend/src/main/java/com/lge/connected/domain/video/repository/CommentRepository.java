package com.lge.connected.domain.video.repository;

import java.util.List;
import com.lge.connected.domain.video.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByVideoId(Long VideoId);
}
