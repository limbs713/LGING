package com.lge.connected.global;

import com.lge.connected.domain.vector.exception.VectorErrorCode;

public class CustomException extends ResponseException {

//    public CustomException(MemberErrorCode memberErrorCode) {
//        super(memberErrorCode.getMessage(), memberErrorCode.getHttpStatus());
//    }
//
//    public CustomException(MemberMoodErrorCode memberMoodErrorCode) {
//        super(memberMoodErrorCode.getMessage(), memberMoodErrorCode.getHttpStatus());
//    }
//
//    public CustomException(MemberGenreErrorCode memberMoodErrorCode) {
//        super(memberMoodErrorCode.getMessage(), memberMoodErrorCode.getHttpStatus());
//    }
//
//    public CustomException(VenueErrorCode venueErrorCode) {
//        super(venueErrorCode.getMessage(), venueErrorCode.getHttpStatus());
//    }
//
//    public CustomException(VenueGenreErrorCode venueGenreErrorCode) {
//        super(venueGenreErrorCode.getMessage(), venueGenreErrorCode.getHttpStatus());
//    }
//
//    public CustomException(VenueMoodErrorCode venueMoodErrorCode) {
//        super(venueMoodErrorCode.getMessage(), venueMoodErrorCode.getHttpStatus());
//    }
//
//    public CustomException(HeartbeatErrorCode heartbeatErrorCode) {
//        super(heartbeatErrorCode.getMessage(), heartbeatErrorCode.getHttpStatus());
//    }
//
//    public CustomException(ArchiveErrorCode archiveErrorCode) {
//        super(archiveErrorCode.getMessage(), archiveErrorCode.getHttpStatus());
//    }

    public CustomException(VectorErrorCode vectorErrorCode){
        super(vectorErrorCode.getMessage(), vectorErrorCode.getHttpStatus());
    }

//    public CustomException(SearchErrorCode searchErrorCode){
//        super(searchErrorCode.getMessage(), searchErrorCode.getHttpStatus());
//    }


}
