package com.artinus.common.response.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("D-0", "OK"),
    ERROR("D-99", "관리자에게 문의해주세요."),
    INVALID_PARAMETER("D-01", "입력값이 올바르지 않습니다."),
    ALREADY_SIGNUP("D-02", "이미 회원가입된 계정입니다."),
    USER_NOT_FOUND("D-03", "가입된 계정이 없습니다."),
    NOT_FOUND("D-04", "데이터를 찾을 수 없습니다."),
    ALREADY_REQUEST("D-05", "이미 처리된 요청입니다."),
    CSRNG_ROLL_BACK("D-06", "[csrng.net] ROLL BACK 요청"),
    SUBSCRIBE_UNIQUE("D-07", "이미 구독이 되어있습니다."),
    ILLEGAL_TOKEN_ERROR("D-995", "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN_ERROR("D-996", "토큰이 만료되었습니다."),
    EXPIRED_TOKEN_ERROR("D-997", "토큰이 만료되었습니다."),
    INVALID_TOKEN_ERROR("D-998", "토큰이 올바르지 않습니다."),
    SECURITY_ERROR("D-999", "인증에 실패하였습니다.");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
