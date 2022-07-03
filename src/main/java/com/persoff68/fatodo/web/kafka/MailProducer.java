package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPasswordMail;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class MailProducer implements MailServiceClient {

    private final KafkaTemplate<String, ActivationMail> activationMailKafkaTemplate;

    private final KafkaTemplate<String, ResetPasswordMail> resetPasswordMailKafkaTemplate;

    public void sendActivationCode(ActivationMail activationMail) {
        activationMailKafkaTemplate.send("mail_auth", "activation", activationMail);
    }

    public void sendResetPasswordCode(ResetPasswordMail resetPasswordMail) {
        resetPasswordMailKafkaTemplate.send("mail_auth", "reset-password", resetPasswordMail);
    }

}
