package com.lge.connected.domain.comment.repository;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.entity.Video;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long id);

    List<Comment> findAllByVideo(Video video);
}
