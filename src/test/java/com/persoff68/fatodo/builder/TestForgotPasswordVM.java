package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.web.rest.vm.ForgotPasswordVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestForgotPasswordVM extends ForgotPasswordVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestForgotPasswordVM(@NotNull @Size(max = 50) String user, String token) {
        super(user, token);
    }

    public static TestForgotPasswordVMBuilder defaultBuilder() {
        return TestForgotPasswordVM.builder()
                .user(DEFAULT_VALUE)
                .token(DEFAULT_VALUE);
    }

}
