package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.CaptchaRequestDTO;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "https://www.google.com/recaptcha/api/siteverify", primary = false)
public interface CaptchaClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    CaptchaResponseDTO sendVerificationRequest(@RequestBody CaptchaRequestDTO captchaRequestDTO);

}
