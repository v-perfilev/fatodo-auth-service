package com.persoff68.fatodo.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterVM {

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

}
