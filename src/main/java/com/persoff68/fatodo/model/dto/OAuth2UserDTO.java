package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OAuth2UserDTO extends AbstractDTO {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String email;
    private String username;
    private String provider;
    private String providerId;

}
