package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Activation;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestActivation extends Activation {

    @Builder
    TestActivation(UUID id, @NotNull UUID userId, @NotNull UUID code, boolean completed) {
        super(userId, code, completed);
        this.setId(id);
    }

    public static TestActivationBuilder defaultBuilder() {
        return TestActivation.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code(UUID.randomUUID())
                .completed(true);
    }

}
