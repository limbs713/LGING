package com.lge.connected.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.lge.connected.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserLoginRequest {
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .username(userName)
                .password(password)
                .build();
    }
}
