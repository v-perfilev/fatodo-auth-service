package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.security.exception.AuthWrongProviderProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrincipalDTO userPrincipalDTO = userServiceClient.getUserPrincipalByUsername(username);
        UserPrincipal userPrincipal = userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
        String provider = userPrincipal.getProvider();
        if (!provider.equals(Providers.LOCAL)) {
            throw new AuthWrongProviderProblem(provider);
        }
        return userPrincipal;
    }

}
