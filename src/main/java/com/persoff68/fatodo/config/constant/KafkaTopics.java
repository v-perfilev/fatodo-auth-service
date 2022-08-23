package com.persoff68.fatodo.config.constant;

import lombok.Getter;

public enum KafkaTopics {
    EVENT("event"),
    MAIL_AUTH("mail_auth"),
    WS("ws");

    @Getter
    private final String value;

    KafkaTopics(String value) {
        this.value = value;
    }

}
