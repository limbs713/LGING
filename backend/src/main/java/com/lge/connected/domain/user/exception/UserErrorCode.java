package com.lge.connected.domain.user.exception;

import com.lge.connected.domain.user.dto.error.UserErrorCodeResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode {

    NICKNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    LOGINID_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 로그인ID입니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일 계정입니다."),
    INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "잘못된 회원정보입니다."),
    INVALID_PASSWORD_INFO(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    NICKNAME_OVER_LENGTH(HttpStatus.NOT_FOUND, "닉네임이 12자 초과입니다"),
    NICKNAME_SPACE_EXIST(HttpStatus.NOT_FOUND, "닉네임에 공백이 있습니다"),
    NICKNAME_SYMBOL_EXIST(HttpStatus.NOT_FOUND, "닉네임에 특수문자가 있습니다"),
    USERNAME_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증한 성명과 유저의 이름이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;


    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public UserErrorCodeResponse toResponse() {
        return new UserErrorCodeResponse(this.httpStatus, this.message);
    }
}
