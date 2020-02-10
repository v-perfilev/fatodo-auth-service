package com.persoff68.fatodo.security.service;

import com.persoff68.fatodo.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userServiceClient.getUserPrincipalByUsername(login);
    }

}
