package com.persoff68.fatodo.config;

import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.FeedbackMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import com.persoff68.fatodo.model.dto.event.EventDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.partitions}")
    private int partitions;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return KafkaUtils.buildKafkaAdmin(bootstrapAddress);
    }

    @Bean
    public NewTopic mailAuthNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.MAIL_AUTH.getValue(), partitions);
    }

    @Bean
    public NewTopic mailFeedbackNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.MAIL_FEEDBACK.getValue(), partitions);
    }

    @Bean
    public NewTopic eventNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.EVENT.getValue(), partitions);
    }

    @Bean
    public KafkaTemplate<String, ActivationMailDTO> activationMailKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, ResetPasswordMailDTO> resetPasswordMailKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, FeedbackMailDTO> feedbackMailKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, EventDTO> eventKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

}
