package com.altinus.subscription.infrastructure.external.service;


import com.altinus.common.exception.ApiErrorException;
import com.altinus.common.response.enums.ResultCode;
import com.altinus.subscription.infrastructure.external.dto.CsrngResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsrngExternalService {
    private final RestTemplate restTemplate;

    @Value("${altinus.external-api}")
    private String altinusAPIUrl;

    public List<CsrngResponseDto> fetchRandomData() {
        try {
            log.info("Calling external API: {}", altinusAPIUrl);

            ResponseEntity<List<CsrngResponseDto>> response = restTemplate.exchange(
                    altinusAPIUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CsrngResponseDto>>() {}
            );

            if(response.getBody() == null || response.getBody().size() == 0) {
                throw new ApiErrorException(ResultCode.ERROR);
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("Csrn api failed {}", e);
            throw new ApiErrorException(ResultCode.ERROR);
        }
    }
}
