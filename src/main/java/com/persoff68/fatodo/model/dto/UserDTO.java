package com.persoff68.fatodo.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String username;
    private String provider;
    private Set<String> authorities;

}
