package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.AuthProvider;
import lombok.Data;

@Data
public class OAuth2UserDTO {
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String username;
    private AuthProvider provider;
    private String providerId;

}
