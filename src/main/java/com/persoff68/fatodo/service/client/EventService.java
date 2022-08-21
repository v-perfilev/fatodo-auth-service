package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.model.dto.CreateEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class EventService {

    private final EventServiceClient eventServiceClient;

    public void sendWelcomeEvent(UUID userId) {
        CreateEventDTO.EventType eventType = CreateEventDTO.EventType.WELCOME;
        List<UUID> recipientIdList = Collections.singletonList(userId);
        CreateEventDTO createEventDTO = new CreateEventDTO(eventType, recipientIdList);
        eventServiceClient.addDefaultEvent(createEventDTO);
    }

}
