package com.altinus.common.exception;

import com.altinus.common.response.enums.ResultCode;

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