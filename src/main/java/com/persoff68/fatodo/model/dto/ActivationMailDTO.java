package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.UserPrincipal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ActivationMailDTO extends AbstractDTO {

    private String language;
    private String email;
    private String username;
    private String code;

    public ActivationMailDTO(UserPrincipal user, String code) {
        this.language = user.getLanguage();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.code = code;
    }

}
