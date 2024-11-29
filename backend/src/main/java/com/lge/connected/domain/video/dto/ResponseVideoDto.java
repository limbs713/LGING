package com.lge.connected.domain.video.dto;

import com.lge.connected.domain.user.entity.Video;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseVideoDto {
    private Long id;
    private String title;
    private String source;
    private String thumbnail;

    public static ResponseVideoDto of(Video video) {
        return ResponseVideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .source(video.getSource())
                .thumbnail(video.getThumbnail())
                .build();
    }
}
