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
    private Float stars = 0f;
    @Builder.Default
    private Integer views = 0;
    @Builder.Default
    private Integer likes = 0;
    @Builder.Default
    private Integer comments = 0;

    @OneToOne(mappedBy = "video", fetch = FetchType.LAZY)
    private VideoGenre videoGenre;

    public void addStars(long target) {
        if (comments.equals(0)) {
            stars = (float) target;
        } else {
            stars = (stars * comments + target) / (comments + 1);
        }
        comments++;
    }

    public void minusStars(long target) {
        if (comments.equals(1)) {
            stars = 0f;
        } else {
            stars = (stars * comments - target) / (comments - 1);
        }
        comments--;
    }

    public void changeStars(int origin, int target) {
        stars = (stars * comments - origin + target) / comments;
    }

    public void addLike() {
        likes++;
    }

    public void deleteLike() {
        likes--;
    }

    public void addViews() {views++;}
}

