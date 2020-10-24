package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ResetPassword;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestResetPassword extends ResetPassword {

    @Builder
    TestResetPassword(UUID id, @NotNull UUID userId, @NotNull UUID code, boolean completed) {
        super(userId, code, completed);
        this.setId(id);
    }

    public static TestResetPasswordBuilder defaultBuilder() {
        return TestResetPassword.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code(UUID.randomUUID())
                .completed(true);
    }

}
