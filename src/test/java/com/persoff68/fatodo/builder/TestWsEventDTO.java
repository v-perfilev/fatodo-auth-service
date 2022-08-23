package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.WsEventType;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestWsEventDTO extends WsEventDTO {

    @Builder
    TestWsEventDTO(List<UUID> userIdList, WsEventType type) {
        super(userIdList, type);
    }

    public static TestWsEventDTOBuilder defaultBuilder() {
        return TestWsEventDTO.builder()
                .userIdList(List.of(UUID.randomUUID()))
                .type(WsEventType.WELCOME);
    }

}
