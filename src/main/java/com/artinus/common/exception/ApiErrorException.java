package com.artinus.common.exception;

import com.artinus.common.response.enums.ResultCode;

public class ApiErrorException extends RuntimeException {
    private final ResultCode resultCode;

    public ApiErrorException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}