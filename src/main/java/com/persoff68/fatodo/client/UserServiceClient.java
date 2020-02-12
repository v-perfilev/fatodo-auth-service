package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("user-service")
public interface UserServiceClient {

    @GetMapping(value = "/details/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByUsername(@PathVariable("username") String username);

    @GetMapping(value = "/details/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByEmail(@PathVariable("email") String email);

    @GetMapping(value = "/details/email-or-new/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserPrincipal getUserPrincipalByEmailOrNew(@PathVariable("email") String email);

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO createUser(UserDTO userDTO);

    @PutMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO updateUser(UserDTO userDTO);

}

