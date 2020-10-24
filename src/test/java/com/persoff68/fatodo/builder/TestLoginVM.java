package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.web.rest.vm.LoginVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestLoginVM extends LoginVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestLoginVM(@NotNull @Size(max = 50) String user, @NotNull @Size(max = 100) String password, String token) {
        super(user, password, token);
    }

    public static TestLoginVMBuilder defaultBuilder() {
        return TestLoginVM.builder()
                .user(DEFAULT_VALUE)
                .password(DEFAULT_VALUE)
                .token(DEFAULT_VALUE);
    }

}
