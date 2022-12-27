package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.FeedbackMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class MailProducer implements MailServiceClient {

    private final KafkaTemplate<String, ActivationMailDTO> activationMailKafkaTemplate;

    private final KafkaTemplate<String, ResetPasswordMailDTO> resetPasswordMailKafkaTemplate;

    private final KafkaTemplate<String, FeedbackMailDTO> feedbackMailKafkaTemplate;

    public void sendActivationCode(ActivationMailDTO activationMailDTO) {
        activationMailKafkaTemplate.send(KafkaTopics.MAIL_AUTH.getValue(), "activation", activationMailDTO);
    }

    public void sendResetPasswordCode(ResetPasswordMailDTO resetPasswordMailDTO) {
        resetPasswordMailKafkaTemplate.send(KafkaTopics.MAIL_AUTH.getValue(), "reset-password", resetPasswordMailDTO);
    }

    public void sendFeedback(FeedbackMailDTO feedbackMailDTO) {
        feedbackMailKafkaTemplate.send(KafkaTopics.MAIL_FEEDBACK.getValue(), feedbackMailDTO);
    }

}
