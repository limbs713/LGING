package com.lge.connected.domain.user.dto;

import com.lge.connected.domain.user.constant.Gender;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class OnBoardingDTO {

    private String username;
    private String nickname;
    private String gender;
    private int age;
}
