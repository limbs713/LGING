package com.lge.connected.domain.video.entity;

import com.lge.connected.domain.vector.entity.Vector;
import com.lge.connected.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoGenre extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoGenreId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Transient
    private Vector genreVector;

    @Lob
    private String genreVectorString;

    public void updateGenreVector(Vector vector) {
        this.genreVector = vector;
        this.genreVectorString = vector.getElements().toString();
    }

    public Vector getGenreVector() {
        if (genreVector == null && genreVectorString != null) {
            List<Double> elements = List.of(genreVectorString.replace("[", "").replace("]", "").split(","))
                    .stream().map(String::trim).map(Double::parseDouble).collect(Collectors.toList());
            genreVector = new Vector(elements);
        }
        return genreVector;
    }

}
