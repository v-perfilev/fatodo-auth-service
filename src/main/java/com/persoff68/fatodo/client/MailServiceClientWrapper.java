package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ActivationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class MailServiceClientWrapper implements MailServiceClient {

    @Qualifier("mailServiceClient")
    private final MailServiceClient mailServiceClient;

    @Override
    public void activate(ActivationDTO activationDTO) {
        try {
            mailServiceClient.activate(activationDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
