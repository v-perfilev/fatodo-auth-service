package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.model.dto.CaptchaRequestDTO;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.service.exception.CaptchaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaClient captchaClient;
    private final AppProperties appProperties;

    public void captchaCheck(String token) {
        CaptchaRequestDTO captchaRequestDTO = new CaptchaRequestDTO();
        captchaRequestDTO.setSecret(appProperties.getAuth().getCaptchaSecret());
        captchaRequestDTO.setResponse(token);
        CaptchaResponseDTO captchaResponseDTO = captchaClient.sendVerificationRequest(captchaRequestDTO);
        if (!captchaResponseDTO.isSuccess()) {
            throw new CaptchaException();
        }
    }

}
