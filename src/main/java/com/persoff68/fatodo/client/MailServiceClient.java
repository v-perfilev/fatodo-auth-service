package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.ActivationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service", primary = false)
public interface MailServiceClient {

    @GetMapping(value = "/api/mail/activation", consumes = MediaType.APPLICATION_JSON_VALUE)
    void activate(@RequestBody ActivationDTO activationDTO);

}
