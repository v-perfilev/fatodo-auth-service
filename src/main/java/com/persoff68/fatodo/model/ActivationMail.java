package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationMail implements Serializable {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String language;
    private String email;
    private String username;
    private UUID code;

    public ActivationMail(UserPrincipal user, UUID code) {
        this.language = user.getLanguage();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.code = code;
    }

}
