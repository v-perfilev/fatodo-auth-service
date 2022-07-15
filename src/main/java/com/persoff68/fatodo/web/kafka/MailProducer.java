package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class MailProducer implements MailServiceClient {

    private static final String MAIL_AUTH_TOPIC = "mail_auth";

    private final KafkaTemplate<String, ActivationMailDTO> activationMailKafkaTemplate;

    private final KafkaTemplate<String, ResetPasswordMailDTO> resetPasswordMailKafkaTemplate;

    public void sendActivationCode(ActivationMailDTO activationMailDTO) {
        activationMailKafkaTemplate.send(MAIL_AUTH_TOPIC, "activation", activationMailDTO);
    }

    public void sendResetPasswordCode(ResetPasswordMailDTO resetPasswordMailDTO) {
        resetPasswordMailKafkaTemplate.send(MAIL_AUTH_TOPIC, "reset-password", resetPasswordMailDTO);
    }

}
