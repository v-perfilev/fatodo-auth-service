package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("user-service")
public interface UserServiceClient {

    @GetMapping(value = "/details/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByUsername(@PathVariable("username") String username);

    @GetMapping(value = "/details/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByEmail(@PathVariable("email") String email);

    @GetMapping(value = "/details/email/{email}/nullable", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByEmailNullable(@PathVariable("email") String email);

    @PostMapping(value = "/users/oauth2", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO createOAuth2User(OAuth2UserDTO OAuth2UserDTO);

    @PostMapping(value = "/users/local", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO createLocalUser(LocalUserDTO userLocalDTO);

}

