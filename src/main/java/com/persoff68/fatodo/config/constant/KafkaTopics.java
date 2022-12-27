package com.persoff68.fatodo.config.constant;

import lombok.Getter;

public enum KafkaTopics {
    EVENT("event"),
    MAIL_AUTH("mail_auth"),
    MAIL_FEEDBACK("mail_feedback");

    @Getter
    private final String value;

    KafkaTopics(String value) {
        this.value = value;
    }

}
