package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;

@Data
public class OAuth2UserDTO {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String email;
    private String username;
    private String provider;
    private String providerId;

}
