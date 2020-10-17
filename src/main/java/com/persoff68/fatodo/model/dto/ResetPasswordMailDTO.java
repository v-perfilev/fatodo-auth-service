package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.UserPrincipal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResetPasswordMailDTO implements Serializable {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

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
