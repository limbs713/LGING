package com.lge.connected.domain.user.dto.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserGenreErrorCodeResponse {
    private HttpStatus status;
    private String message;

    public UserGenreErrorCodeResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
    }
}
