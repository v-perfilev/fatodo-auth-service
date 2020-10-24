package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.web.rest.vm.ResetPasswordVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class TestResetPasswordVM extends ResetPasswordVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestResetPasswordVM(@NotNull UUID code, @NotNull @Size(min = 5, max = 100) String password, String token) {
        super(code, password, token);
    }

    public static TestResetPasswordVMBuilder defaultBuilder() {
        return TestResetPasswordVM.builder()
                .code(UUID.randomUUID())
                .password(DEFAULT_VALUE)
                .token(DEFAULT_VALUE);
    }

}
