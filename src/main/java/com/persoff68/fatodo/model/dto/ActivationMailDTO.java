package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationMailDTO {

    private String language;
    private String email;
    private String username;
    private UUID code;

    public ActivationMailDTO(UserPrincipal user, UUID code) {
        this.language = user.getLanguage();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.code = code;
    }

}
