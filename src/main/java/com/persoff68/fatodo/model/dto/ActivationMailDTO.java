package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.UserPrincipal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ActivationMailDTO implements Serializable {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

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
