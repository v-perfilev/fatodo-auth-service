package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPasswordMail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailServiceClientWrapper implements MailServiceClient {

    @Qualifier("feignMailServiceClient")
    private final MailServiceClient mailServiceClient;

    @Override
    public void sendActivationCode(ActivationMail activationMail) {
        try {
            mailServiceClient.sendActivationCode(activationMail);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordMail resetPasswordMail) {
        try {
            mailServiceClient.sendResetPasswordCode(resetPasswordMail);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
