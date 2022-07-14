package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.RegisterVM;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestRegisterVM extends RegisterVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestRegisterVM(@NotNull @Size(min = 5, max = 50) String username,
                   @NotNull @Email @Size(min = 5, max = 50) String email,
                   @NotNull @Size(min = 5, max = 100) String password,
                   @NotNull String language,
                   @NotNull String timezone,
                   String token) {
        super(username, email, password, language, timezone, token);
    }

    public static TestRegisterVMBuilder defaultBuilder() {
        return TestRegisterVM.builder()
                .username(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .password(DEFAULT_VALUE)
                .language(DEFAULT_VALUE)
                .timezone("Europe/Berlin")
                .token(DEFAULT_VALUE);
    }

}
