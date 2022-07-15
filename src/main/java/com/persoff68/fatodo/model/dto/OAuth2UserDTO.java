package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserDTO {

    private String email;

    private String username;

    private String provider;

    private String providerId;

    private String language;

    private String timezone;

}
