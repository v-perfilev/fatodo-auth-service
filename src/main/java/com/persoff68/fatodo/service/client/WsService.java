package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.constant.WsEventType;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class WsService {

    private final WsServiceClient wsServiceClient;

    public void sendWelcomeEvent(UUID userId) {
        WsEventDTO wsEventDTO = new WsEventDTO(List.of(userId), WsEventType.WELCOME);
        wsServiceClient.sendEvent(wsEventDTO);
    }

}
