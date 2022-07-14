package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
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
