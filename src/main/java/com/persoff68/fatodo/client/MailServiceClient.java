package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignMailServiceClient"})
public interface MailServiceClient {

    @GetMapping(value = "/api/mails/activation", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendActivationCode(@RequestBody ActivationMailDTO activationMailDTO);

    @GetMapping(value = "/api/mails/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendResetPasswordCode(@RequestBody ResetPasswordMailDTO resetPasswordMailDTO);

}
