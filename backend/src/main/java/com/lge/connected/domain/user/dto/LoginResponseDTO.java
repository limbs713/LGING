package com.lge.connected.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDTO {
    private Long userId;
    private String loginId;
    private String token;
}