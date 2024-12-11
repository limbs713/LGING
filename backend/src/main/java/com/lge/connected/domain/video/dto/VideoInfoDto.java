package com.lge.connected.domain.video.dto;


import com.lge.connected.domain.video.entity.Video;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VideoInfoDto {
    private Long id;
    private String title;
    private String subtitle;
    private String source;
    private String description;
    private String thumbnail;

    public static VideoInfoDto from(Video video){
        return VideoInfoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .subtitle(video.getSubtitle())
                .source(video.getSource())
                .description(video.getDescription())
                .thumbnail(video.getThumbnail())
                .build();
    }
}
