package com.lge.connected.domain.comment.dto;

import com.lge.connected.domain.comment.entity.Comment;
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
    private int stars;
    private String content;

    public Comment toEntity(){
        return Comment.builder()
                .stars(stars)
                .content(content)
                .build();
    }
}
