package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetVM {

    @NotNull
    private String code;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

}
