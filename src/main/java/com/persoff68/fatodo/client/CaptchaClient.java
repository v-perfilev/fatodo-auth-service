package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.CaptchaRequestDTO;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "captcha-service",
        url = "https://www.google.com/recaptcha/api/siteverify",
        fallbackFactory = CaptchaFallbackFactory.class
)
public interface CaptchaClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CaptchaResponseDTO sendVerificationRequest(@RequestBody CaptchaRequestDTO captchaRequestDTO);

}
