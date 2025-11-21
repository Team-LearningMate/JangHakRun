package com.learning_mate.janghakrun.global.error;

public record ErrorResponse(
    int httpStatus,
    String code,
    String message
) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }
}

