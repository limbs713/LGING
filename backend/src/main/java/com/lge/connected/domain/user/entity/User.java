package com.lge.connected.domain.user.entity;

import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.user.constant.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String nickname;
    private String loginId;
    private String password;
    private Gender gender;
    private int age;
    private String role;
    private Long latestArchiveId;

    @Builder.Default
    private boolean isOnboarding = false;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;


    public void saveLatestArchiveId(Long latestArchiveId) {this.latestArchiveId = latestArchiveId;}

    public void saveOnBoarding(String username, String nickname, String gender, int age) {
        this.username = username;
        this.nickname = nickname;
        this.gender = Gender.fromText(gender);
        this.isOnboarding = true;
        this.age = age;
    }
}
