package com.lge.connected.domain.video.entity;

import com.lge.connected.global.util.BaseTimeEntity;
import jakarta.persistence.*;
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
public class Video extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subtitle;
    private String source;
    private String description;
    private String thumbnail;

    @Builder.Default
    private Integer views = 0;
    @Builder.Default
    private Integer likes = 0;
    @Builder.Default
    private Integer comments = 0;

    @OneToOne(mappedBy = "video")
    private VideoGenre videoGenre;


}

