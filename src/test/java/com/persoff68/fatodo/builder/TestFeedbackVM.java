package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.FeedbackVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;

public class TestFeedbackVM extends FeedbackVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestFeedbackVM(@NotNull String name,
                   @NotNull String email,
                   @NotNull String message,
                   String token) {
        super(name, email, message, token);
    }

    public static TestFeedbackVMBuilder defaultBuilder() {
        return TestFeedbackVM.builder()
                .name(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .message(DEFAULT_VALUE)
                .token(DEFAULT_VALUE);
    }

}
