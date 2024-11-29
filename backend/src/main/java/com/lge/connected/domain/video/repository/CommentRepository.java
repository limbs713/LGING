package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByVideoId(Long VideoId);
}
