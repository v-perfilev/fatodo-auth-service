package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class EventService {

    private final EventServiceClient eventServiceClient;

    public void sendWelcomeEvent(UUID userId) {
        List<UUID> userIdList = List.of(userId);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.WELCOME);
        eventServiceClient.addEvent(eventDTO);
    }

}
