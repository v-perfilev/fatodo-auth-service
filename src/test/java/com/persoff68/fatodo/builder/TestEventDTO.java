package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestEventDTO extends EventDTO {

    @Builder
    TestEventDTO(List<UUID> userIdList, EventType type) {
        super(userIdList, type);
    }

    public static TestEventDTOBuilder defaultBuilder() {
        return TestEventDTO.builder()
                .userIdList(List.of(UUID.randomUUID()))
                .type(EventType.WELCOME);
    }

}
