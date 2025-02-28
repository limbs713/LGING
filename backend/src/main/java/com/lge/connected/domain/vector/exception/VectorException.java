package com.lge.connected.domain.vector.exception;

import com.lge.connected.global.util.ResponseException;

public class VectorException extends ResponseException {

    public VectorException(VectorErrorCode vectorErrorCode) {
        super(vectorErrorCode.getMessage(), vectorErrorCode.getHttpStatus());
    }

}
