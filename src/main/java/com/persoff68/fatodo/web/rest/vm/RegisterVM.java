package com.persoff68.fatodo.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private String language;

    @NotNull
    private String timezone;

    private String token;

}
