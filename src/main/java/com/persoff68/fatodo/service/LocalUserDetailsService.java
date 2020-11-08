package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.security.exception.AuthUserNotFoundException;
import com.persoff68.fatodo.security.exception.AuthWrongProviderException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserDetailsService {

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;

    public UserPrincipal loadUser(String usernameOrEmail) {
        UserPrincipal userPrincipal = getUserPrincipalByUsernameOrEmail(usernameOrEmail);
        Provider provider = userPrincipal.getProvider();
        if (!provider.equals(Provider.LOCAL)) {
            throw new AuthWrongProviderException(provider.getValue());
        }
        return userPrincipal;
    }

    public UserPrincipal getUserPrincipalByUsernameOrEmail(String usernameOrEmail) {
        try {
            UserPrincipalDTO userPrincipalDTO = userServiceClient.getUserPrincipalByUsernameOrEmail(usernameOrEmail);
            return userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
        } catch (ModelNotFoundException ex) {
            throw new AuthUserNotFoundException();
        }
    }

}
