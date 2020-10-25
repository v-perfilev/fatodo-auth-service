package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service", primary = false)
public interface UserServiceClient {

    @GetMapping(value = "/api/system/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO getUserPrincipalByUsername(@PathVariable String username);

    @GetMapping(value = "/api/system/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO getUserPrincipalByEmail(@PathVariable String email);

    @GetMapping(value = "/api/system/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO getUserPrincipalById(@PathVariable UUID id);

    @PostMapping(value = "/api/system/oauth2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO createOAuth2User(@RequestBody OAuth2UserDTO oAuth2UserDTO);

    @PostMapping(value = "/api/system/local",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO createLocalUser(@RequestBody LocalUserDTO userLocalDTO);

    @GetMapping(value = "/api/system/activate/{userId}")
    void activate(@PathVariable UUID userId);

    @PostMapping(value = "/api/system/reset-password")
    void resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO);

}

