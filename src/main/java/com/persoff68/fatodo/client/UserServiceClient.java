package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", primary = false)
public interface UserServiceClient {

    @GetMapping(value = "/api/auth/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO getUserPrincipalByUsername(@PathVariable String username);

    @GetMapping(value = "/api/auth/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipalDTO getUserPrincipalByEmail(@PathVariable String email);

    @PostMapping(value = "/api/auth/oauth2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO createOAuth2User(OAuth2UserDTO oAuth2UserDTO);

    @PostMapping(value = "/api/auth/local",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO createLocalUser(LocalUserDTO userLocalDTO);

    @GetMapping(value = "/api/auth/activate/{userId}")
    void activate(@PathVariable String userId);

}

