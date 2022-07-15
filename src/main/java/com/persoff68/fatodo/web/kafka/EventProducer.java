package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.model.constant.KafkaTopics;
import com.persoff68.fatodo.model.dto.CreateEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class EventProducer implements EventServiceClient {

    private final KafkaTemplate<String, CreateEventDTO> eventKafkaTemplate;

    public void addDefaultEvent(CreateEventDTO createEventDTO) {
        eventKafkaTemplate.send(KafkaTopics.EVENT_ADD.getValue(), "default", createEventDTO);
    }

}
