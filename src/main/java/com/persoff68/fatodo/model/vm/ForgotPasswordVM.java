package com.persoff68.fatodo.model.vm;

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
public class ForgotPasswordVM {

    @NotNull
    @Size(max = 50)
    private String user;

    private String token;

}
