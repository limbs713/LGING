package com.lge.connected.video.repository;

import com.lge.connected.user.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByVideoId(Long VideoId);
}
