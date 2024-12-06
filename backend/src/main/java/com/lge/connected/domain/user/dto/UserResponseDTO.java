package com.lge.connected.domain.user.dto;


import com.lge.connected.domain.user.constant.Gender;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDTO {
    private Long userId;
    private String loginId;
    private String nickname;
    private String username;
    private Gender gender;
    private int age;
}
