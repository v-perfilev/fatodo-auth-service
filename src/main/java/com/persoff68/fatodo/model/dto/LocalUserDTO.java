package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserDTO {

    private String email;
    private String username;
    private String password;
    private String language;
    private String timezone;

}
