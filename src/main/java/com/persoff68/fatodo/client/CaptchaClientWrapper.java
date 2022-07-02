package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.CaptchaRequestDTO;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CaptchaClientWrapper implements CaptchaClient {

    @Qualifier("feignCaptchaClient")
    private final CaptchaClient captchaClient;

    @Override
    public CaptchaResponseDTO sendVerificationRequest(CaptchaRequestDTO captchaRequestDTO) {
        try {
            return captchaClient.sendVerificationRequest(captchaRequestDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
