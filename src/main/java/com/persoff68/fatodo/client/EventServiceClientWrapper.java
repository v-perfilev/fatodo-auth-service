package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.CreateEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventServiceClientWrapper implements EventServiceClient {

    @Qualifier("feignEventServiceClient")
    private final EventServiceClient eventServiceClient;

    @Override
    public void addDefaultEvent(CreateEventDTO createEventDTO) {
        try {
            eventServiceClient.addDefaultEvent(createEventDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
