package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;

import java.util.Set;

@Data
public class UserPrincipalDTO {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String id;
    private String email;
    private String username;
    private String password;
    private String provider;
    private String providerId;
    private Set<String> authorities;

}
