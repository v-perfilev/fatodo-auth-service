package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVM {

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    private String message;

    private String token;

}
