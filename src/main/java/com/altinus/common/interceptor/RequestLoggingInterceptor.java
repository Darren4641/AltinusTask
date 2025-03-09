package com.altinus.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = (request.getQueryString() != null) ? "?" + request.getQueryString() : "";

        // 요청 파라미터 추출
        Map<String, String[]> parameterMap = request.getParameterMap();
        String parameters = parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining(", "));

        log.info("[URL:" + uri + queryString + "] [METHOD:" + method + "] [PARAMETERS:{" + parameters + "}] [STATUS:" + response.getStatus() + "]");

        return true; // 요청 처리를 계속 진행
    }
}