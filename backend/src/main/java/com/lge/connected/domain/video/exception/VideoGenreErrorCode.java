package com.lge.connected.domain.video.exception;

import com.lge.connected.domain.video.dto.error.VideoGenreErrorCodeResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum VideoGenreErrorCode {

    INVALID_VIDEO_GENRE_INFO(HttpStatus.BAD_REQUEST, "잘못된 비디오 장르 정보입니다."),
    VIDEO_GENRE_NOT_EXIST(HttpStatus.NOT_FOUND, "비디오 장르가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;


    VideoGenreErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public VideoGenreErrorCodeResponse toResponse() {
        return new VideoGenreErrorCodeResponse(this.httpStatus, this.message);
    }
}
