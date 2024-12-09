package com.lge.connected.domain.comment.application;

import com.lge.connected.domain.comment.dto.RequestCommentDto;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.comment.repository.CommentRepository;
import com.lge.connected.domain.video.repository.VideoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;

    public List<Comment> getAllCommentsByVideo(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
        );
        return commentRepository.findAllByVideo(video);
    }

    public Boolean addCommentByVideo(Long videoId, RequestCommentDto commentDto) {
        try {
            Comment comment = commentDto.toEntity();
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다.")
            );
            video.addStars(comment.getRating());
            commentRepository.save(comment);
            videoRepository.save(video);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteComment(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
            );
            Video video = comment.getVideo();
            video.minusStars(comment.getRating());
            videoRepository.save(video);
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Comment updateComment(Long commentId, RequestCommentDto commentDto, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.getUser().getId().equals(userId)) {
            Video video = comment.getVideo();
            video.changeStars(comment.getRating(), commentDto.getRating());
            comment.update(commentDto);
            videoRepository.save(video);
            return commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("해당 댓글을 수정할 권한이 없습니다.");
        }
    }
}
