package com.altinus.common.response.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("D-0", "OK"),
    ERROR("D-99", "관리자에게 문의해주세요."),
    INVALID_PARAMETER("D-01", "입력값이 올바르지 않습니다."),
    ALREADY_SIGNUP("D-02", "이미 회원가입된 계정입니다."),
    USER_NOT_FOUND("D-03", "가입된 계정이 없습니다."),
    NOT_FOUND("D-04", "데이터를 찾을 수 없습니다."),
    LIMIT_EXCEEDED("D-05", "최대 3개의 옵션만 추가할 수 있습니다."),
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
