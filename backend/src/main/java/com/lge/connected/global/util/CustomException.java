package com.lge.connected.global.util;

import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.exception.UserGenreErrorCode;
import com.lge.connected.domain.vector.exception.VectorErrorCode;
import com.lge.connected.domain.video.exception.ArchiveErrorCode;
import com.lge.connected.domain.video.exception.VideoErrorCode;
import com.lge.connected.domain.video.exception.VideoGenreErrorCode;

public class CustomException extends ResponseException {

    public CustomException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage(), userErrorCode.getHttpStatus());
    }

    public CustomException(UserGenreErrorCode memberMoodErrorCode) {
        super(memberMoodErrorCode.getMessage(), memberMoodErrorCode.getHttpStatus());
    }

    public CustomException(VideoErrorCode videoErrorCode) {
        super(videoErrorCode.getMessage(), videoErrorCode.getHttpStatus());
    }

    public CustomException(VideoGenreErrorCode videoGenreErrorCode) {
        super(videoGenreErrorCode.getMessage(), videoGenreErrorCode.getHttpStatus());
    }
//
//    public CustomException(VenueMoodErrorCode venueMoodErrorCode) {
//        super(venueMoodErrorCode.getMessage(), venueMoodErrorCode.getHttpStatus());
//    }
//
//    public CustomException(HeartbeatErrorCode heartbeatErrorCode) {
//        super(heartbeatErrorCode.getMessage(), heartbeatErrorCode.getHttpStatus());
//    }
//
    public CustomException(ArchiveErrorCode archiveErrorCode) {
        super(archiveErrorCode.getMessage(), archiveErrorCode.getHttpStatus());
    }

    public CustomException(VectorErrorCode vectorErrorCode){
        super(vectorErrorCode.getMessage(), vectorErrorCode.getHttpStatus());
    }

//    public CustomException(SearchErrorCode searchErrorCode){
//        super(searchErrorCode.getMessage(), searchErrorCode.getHttpStatus());
//    }


}
