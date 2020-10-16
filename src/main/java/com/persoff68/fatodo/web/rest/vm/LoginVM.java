package com.persoff68.fatodo.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVM {

    @NotNull
    @Size(max = 50)
    private String user;

    @NotNull
    @Size(max = 100)
    private String password;

    private String token;

}
