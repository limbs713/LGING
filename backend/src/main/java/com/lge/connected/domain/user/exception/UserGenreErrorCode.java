package com.lge.connected.domain.user.exception;

import com.lge.connected.domain.user.dto.error.UserGenreErrorCodeResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserGenreErrorCode {

    USER_GENRE_OVER_REQUEST(HttpStatus.BAD_REQUEST, "존재하는 선호 장르 벡터보다 요청 수가 많습니다"),
    USER_GENRE_ONLY_ONE(HttpStatus.BAD_REQUEST, "선호 장르 벡터가 1개밖에 없어서 삭제할 수 없습니다"),
    USER_GENRE_NOT_EXIST(HttpStatus.NOT_FOUND, "선호 장르가 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;


    UserGenreErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public UserGenreErrorCodeResponse toResponse() {
        return new UserGenreErrorCodeResponse(this.httpStatus, this.message);
    }
}
