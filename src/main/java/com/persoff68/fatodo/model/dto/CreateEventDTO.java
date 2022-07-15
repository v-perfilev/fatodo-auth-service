package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {

    @NotNull
    private EventType type;

    private List<UUID> recipientIds;

    public enum EventType {
        // DEFAULT EVENT TYPES
        WELCOME,
    }

}
