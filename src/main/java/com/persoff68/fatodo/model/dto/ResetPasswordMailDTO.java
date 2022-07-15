package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordMailDTO {

    private String language;
    private String email;
    private String username;
    private UUID code;

    public ResetPasswordMailDTO(UserPrincipal userPrincipal, UUID code) {
        this.language = userPrincipal.getLanguage();
        this.email = userPrincipal.getEmail();
        this.username = userPrincipal.getUsername();
        this.code = code;
    }

}
