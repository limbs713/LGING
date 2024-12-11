package com.lge.connected.domain.comment.dto;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequestCommentDto {
    @Min(1)
    @Max(5)
    private int rating;
    private String content;

    public Comment toEntity(Video video, User user){
        return Comment.builder()
                .rating(rating)
                .content(content)
                .user(user)
                .video(video)
                .build();
    }
}
