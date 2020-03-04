package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.constant.AuthProvider;
import lombok.Data;

@Data
public class LocalUserDTO {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String id;
    private String email;
    private String username;
    private String password;
    private String provider;

}
