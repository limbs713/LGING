package com.lge.connected.domain.video.exception;

import com.lge.connected.domain.video.dto.error.VideoErrorCodeResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum VideoErrorCode {

    INVALID_VIDEO_INFO(HttpStatus.BAD_REQUEST, "잘못된 비디오 정보입니다."),
    VIDEO_OVER_REQUEST(HttpStatus.BAD_REQUEST, "존재하는 비디오의 개수보다 많은 개수를 요청했습니다"),
    VIDEO_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 비디오입니다."),
    INVALID_VIDEO_IMAGE(HttpStatus.BAD_REQUEST, "잘못된 이미지 파일입니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다"),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;


    VideoErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public VideoErrorCodeResponse toResponse() {
        return new VideoErrorCodeResponse(this.httpStatus, this.message);
    }
}
