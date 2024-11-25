package com.lge.connected.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ArchiveDTO {

    private Long archiveId;
    private Long userId;
    private List<String> userGenreList;
    private LocalDateTime updatedAt;
}
