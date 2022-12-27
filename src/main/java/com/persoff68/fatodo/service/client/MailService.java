package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.FeedbackMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class MailService {

    private final MailServiceClient mailServiceClient;

    public void sendActivationCode(UserPrincipal userPrincipal, UUID activationCode) {
        ActivationMailDTO activationMailDTO = new ActivationMailDTO(userPrincipal, activationCode);
        mailServiceClient.sendActivationCode(activationMailDTO);
    }

    public void sendResetPasswordCode(UserPrincipal userPrincipal, UUID resetPasswordCode) {
        ResetPasswordMailDTO resetPasswordMailDTO = new ResetPasswordMailDTO(userPrincipal, resetPasswordCode);
        mailServiceClient.sendResetPasswordCode(resetPasswordMailDTO);
    }

    public void sendFeedback(String name, String email, String message) {
        FeedbackMailDTO feedbackMailDTO = new FeedbackMailDTO(name, email, message);
        mailServiceClient.sendFeedback(feedbackMailDTO);
    }

}
