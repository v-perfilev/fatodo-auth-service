package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CaptchaResponseDTO implements Serializable {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private boolean success;

}
