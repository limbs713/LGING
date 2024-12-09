package com.lge.connected.domain.video.dto;

import com.lge.connected.domain.video.entity.Video;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class VideoResponseDTO {
    private Long videoId;
    private String title;
    private String subtitle;
    private String source;
    private String thumbnail;
    private String description;
    private List<String> tagList;
    private Integer likes;
    private Integer comments;
    private Integer views;
    private Boolean doesLike;

    public static VideoResponseDTO of(Video video) {
        return VideoResponseDTO.builder()
                .videoId(video.getId())
                .title(video.getTitle())
                .subtitle(video.getSubtitle())
                .source(video.getSource())
                .thumbnail(video.getThumbnail())
                .description(video.getDescription())
                .build();
    }
}

