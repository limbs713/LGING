package com.lge.connected.domain.comment.entity;

import com.lge.connected.domain.comment.dto.RequestCommentDto;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.global.util.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(value = 5)
    @Min(value = 1)
    private int rating;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY   )
    @JoinColumn(name = "video_id")
    private Video video;

    public void update(RequestCommentDto commentDto) {
        rating = commentDto.getRating();
        content = commentDto.getContent();
    }
}
