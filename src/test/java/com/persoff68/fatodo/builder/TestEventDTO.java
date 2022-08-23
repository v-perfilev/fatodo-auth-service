package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.constant.WsEventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestEventDTO extends EventDTO {

    @Builder
    TestEventDTO(List<UUID> userIdList, EventType type, Object payload) {
        super(userIdList, type, payload);
    }

    public static TestEventDTOBuilder defaultBuilder() {
        return TestEventDTO.builder()
                .userIdList(List.of(UUID.randomUUID()))
                .type(EventType.WELCOME);
    }

}
