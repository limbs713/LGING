package com.lge.connected.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ArchiveResponseDTO {

    private Long archiveId;
    private Long userId;
    private List<String> preferenceList;
    private LocalDateTime updatedAt;
}

