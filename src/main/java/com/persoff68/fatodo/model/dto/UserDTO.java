package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.AuthProvider;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String username;
    private AuthProvider provider;
    private String providerId;
    private Set<String> authorities;

}
