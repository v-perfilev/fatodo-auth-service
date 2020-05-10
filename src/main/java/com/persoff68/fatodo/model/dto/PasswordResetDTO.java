package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PasswordResetDTO extends AbstractDTO {

    private String language;
    private String email;
    private String username;
    private String code;

    public PasswordResetDTO(UserPrincipalDTO userPrincipalDTO, String code) {
        this.language = userPrincipalDTO.getLanguage();
        this.email = userPrincipalDTO.getEmail();
        this.username = userPrincipalDTO.getUsername();
        this.code = code;
    }

}
