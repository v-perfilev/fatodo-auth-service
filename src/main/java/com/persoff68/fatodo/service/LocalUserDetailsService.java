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

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LocalUserDetailsService {
    private static final Pattern EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+"
            + "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;

    public UserPrincipal loadUser(String emailOrUsername) {
        UserPrincipal userPrincipal = getUserPrincipalByEmailOrUserName(emailOrUsername);
        Provider provider = userPrincipal.getProvider();
        if (!provider.equals(Provider.LOCAL)) {
            throw new AuthWrongProviderException(provider.getValue());
        }
        return userPrincipal;
    }

    public UserPrincipal getUserPrincipalByEmailOrUserName(String emailOrUsername) {
        UserPrincipalDTO userPrincipalDTO = isEmail(emailOrUsername)
                ? getByEmail(emailOrUsername)
                : getByUsername(emailOrUsername);
        return userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
    }

    private UserPrincipalDTO getByEmail(String email) {
        try {
            return userServiceClient.getUserPrincipalByEmail(email);
        } catch (ModelNotFoundException e) {
            try {
                return userServiceClient.getUserPrincipalByUsername(email);
            } catch (ModelNotFoundException ex) {
                throw new AuthUserNotFoundException();
            }
        }
    }

    private UserPrincipalDTO getByUsername(String username) {
        try {
            return userServiceClient.getUserPrincipalByUsername(username);
        } catch (ModelNotFoundException e) {
            throw new AuthUserNotFoundException();
        }
    }

    private boolean isEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input).matches();
    }
}
