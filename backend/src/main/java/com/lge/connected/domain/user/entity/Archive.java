package com.lge.connected.domain.user.entity;

import com.lge.connected.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archiveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_genre_id")
    private UserGenre userGenre;

    public void updateArchive(UserGenre userGenre){
        this.userGenre = userGenre;
        updateTheUpdatedAt(LocalDateTime.now());
    }

    public void updateToGetHistory(LocalDateTime updatedAt){
        updateTheUpdatedAt(updatedAt);
    }

}
