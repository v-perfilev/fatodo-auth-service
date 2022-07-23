package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignUserServiceClient"})
public interface UserServiceClient {

    @GetMapping(value = "/api/system/principal/{id}/id")
    UserPrincipalDTO getUserPrincipalById(@PathVariable UUID id);

    @GetMapping(value = "/api/system/principal/{username}/username")
    UserPrincipalDTO getUserPrincipalByUsername(@PathVariable String username);

    @GetMapping(value = "/api/system/principal/{email}/email")
    UserPrincipalDTO getUserPrincipalByEmail(@PathVariable String email);

    @GetMapping(value = "/api/system/principal/{usernameOrEmail}/username-or-email")
    UserPrincipalDTO getUserPrincipalByUsernameOrEmail(@PathVariable String usernameOrEmail);

    @PostMapping(value = "/api/system/oauth2")
    UserPrincipalDTO createOAuth2User(@RequestBody OAuth2UserDTO oAuth2UserDTO);

    @PostMapping(value = "/api/system/local")
    UserPrincipalDTO createLocalUser(@RequestBody LocalUserDTO userLocalDTO);

    @GetMapping(value = "/api/system/activate/{userId}")
    void activate(@PathVariable UUID userId);

    @PostMapping(value = "/api/system/reset-password")
    void resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO);

}

