package com.artinus.common.response;

import com.artinus.common.response.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
        String resultCode,
        String message,
        boolean success,
        T data
) {

    /**
     * 요청 성공
     * @param data
     */
    public BaseResponse(T data) {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), true, data);
    }

    /**
     * 요청 실패
     * @param resultCode
     * @param data
     */
    public BaseResponse(ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), resultCode == ResultCode.SUCCESS, data);
    }

}