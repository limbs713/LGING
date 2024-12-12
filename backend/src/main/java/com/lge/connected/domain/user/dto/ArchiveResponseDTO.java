package com.lge.connected.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ArchiveResponseDTO {

    private Long archiveId;
    private Long userId;
    private List<String> preferenceList;
    private LocalDateTime updatedAt;
}

