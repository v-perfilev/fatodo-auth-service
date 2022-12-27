package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.FeedbackMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailServiceClientWrapper implements MailServiceClient {

    @Qualifier("feignMailServiceClient")
    private final MailServiceClient mailServiceClient;

    @Override
    public void sendActivationCode(ActivationMailDTO activationMailDTO) {
        try {
            mailServiceClient.sendActivationCode(activationMailDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordMailDTO resetPasswordMailDTO) {
        try {
            mailServiceClient.sendResetPasswordCode(resetPasswordMailDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendFeedback(FeedbackMailDTO feedbackMailDTO) {
        try {
            mailServiceClient.sendFeedback(feedbackMailDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
