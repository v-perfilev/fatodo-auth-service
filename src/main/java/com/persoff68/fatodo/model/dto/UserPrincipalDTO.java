package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPrincipalDTO extends AbstractDTO {

    private String email;

    private String username;

    private Set<String> authorities;

    private String password;

    private String provider;

    private String providerId;

    private boolean activated;

    private Info info;

    private Settings settings;

    @Data
    public static class Info {

        private String firstname;

        private String lastname;

        private String gender;

        private String imageFilename;

    }

    @Data
    public static class Settings {

        private String language;

        private String timezone;

        private String timeFormat;

        private String dateFormat;

    }

}
