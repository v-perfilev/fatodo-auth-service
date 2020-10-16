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
public class ResetPasswordVM {

    @NotNull
    private String code;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    private String token;

}
