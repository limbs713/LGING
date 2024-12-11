package com.lge.connected.domain.user.dto;

import com.lge.connected.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    UserInfoResponseDto {
    private Long id;
    private String username;
    private String gender;
    private int age;
    private boolean isOnboarding;

    public static UserInfoResponseDto of(User user){
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .gender(user.getGender().getText())
                .age(user.getAge())
                .isOnboarding(user.isOnboarding())
                .build();
    }
}
