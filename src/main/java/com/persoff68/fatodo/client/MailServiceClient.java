package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPasswordMail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service", primary = false, qualifiers = {"feignMailServiceClient"})
public interface MailServiceClient {

    @GetMapping(value = "/api/mails/activation", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendActivationCode(@RequestBody ActivationMail activationMail);

    @GetMapping(value = "/api/mails/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendResetPasswordCode(@RequestBody ResetPasswordMail resetPasswordMail);

}
