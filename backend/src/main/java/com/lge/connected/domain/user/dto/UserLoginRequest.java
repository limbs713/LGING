package com.lge.connected.domain.user.dto;

import com.lge.connected.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserLoginRequest {
    private String loginId;
    private String password;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .build();
    }
}
