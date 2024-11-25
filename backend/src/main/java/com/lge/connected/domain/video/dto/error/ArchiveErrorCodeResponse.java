package com.lge.connected.domain.video.dto.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ArchiveErrorCodeResponse {
    private final HttpStatus status;
    private final String message;

    public ArchiveErrorCodeResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
    }
}
