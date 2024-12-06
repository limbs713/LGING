package com.lge.connected.domain.user.dto;

import com.lge.connected.domain.user.constant.Role;
import com.lge.connected.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSignupRequest {

    private String loginId;
    private String password;

    public User toEntity(String password) {
        return User.builder()
                .loginId(loginId)
                .role(Role.TYPE1.getText())
                .password(password)
                .build();
    }
}
