package com.persoff68.fatodo.config;

import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPasswordMail;
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
    public NewTopic activationNewTopic() {
        return KafkaUtils.buildTopic("mail_activation", partitions);
    }

    @Bean
    public NewTopic resetPasswordNewTopic() {
        return KafkaUtils.buildTopic("mail_resetPassword", partitions);
    }

    @Bean
    public KafkaTemplate<String, ActivationMail> activationMailKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, ResetPasswordMail> resetPasswordMailKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

}
