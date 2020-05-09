package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ActivationDTO extends AbstractDTO {

    private String language;
    private String email;
    private String username;
    private String code;

    public ActivationDTO(UserDTO userDTO, String code) {
        this.language = userDTO.getLanguage();
        this.email = userDTO.getEmail();
        this.username = userDTO.getUsername();
        this.code = code;
    }
    
}
