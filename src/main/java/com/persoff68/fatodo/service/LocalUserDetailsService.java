package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClientWrapper;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.security.exception.WrongProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserDetailsService implements UserDetailsService {

    private final UserServiceClientWrapper userServiceClientWrapper;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserPrincipalDTO userPrincipalDTO = userServiceClientWrapper.getUserPrincipalByUsername(username);
        UserPrincipal userPrincipal = userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
        Provider provider = userPrincipal.getProvider();
        if (!provider.equals(Provider.LOCAL)) {
            throw new WrongProviderException(provider.getValue());
        }
        return userPrincipal;
    }

}
