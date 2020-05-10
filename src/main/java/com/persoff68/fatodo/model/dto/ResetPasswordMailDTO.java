package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.UserPrincipal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ResetPasswordMailDTO extends AbstractDTO {

    private String language;
    private String email;
    private String username;
    private String code;

    public ResetPasswordMailDTO(UserPrincipal userPrincipal, String code) {
        this.language = userPrincipal.getLanguage();
        this.email = userPrincipal.getEmail();
        this.username = userPrincipal.getUsername();
        this.code = code;
    }

}
