package com.lge.connected.domain.user.entity;

import com.lge.connected.domain.user.constant.Gender;
import com.lge.connected.domain.video.entity.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();


    public void saveLatestArchiveId(Long latestArchiveId) {this.latestArchiveId = latestArchiveId;}

    public void saveOnBoarding(String username, String nickname, String gender, int age) {
        this.username = username;
        this.nickname = nickname;
        this.gender = Gender.fromText(gender);
        this.age = age;
    }
}
