package com.persoff68.fatodo.model.constant;

import lombok.Getter;

public enum KafkaTopics {
    EVENT_ADD("event_add"),
    MAIL_AUTH("mail_auth");

    @Getter
    private final String value;

    KafkaTopics(String value) {
        this.value = value;
    }

}
