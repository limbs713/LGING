package com.lge.connected.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVectorResponseDTO {
    Long userId;
    Long vectorId;
    String email;
    String nickname;
    String username;
    String vectorString;
}
