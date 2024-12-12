package com.lge.connected.domain.user.dto;


import com.lge.connected.domain.user.constant.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserResponseDTO {
    private Long userId;
    private String loginId;
    private String nickname;
    private String username;
    private Gender gender;
    private int age;
}
