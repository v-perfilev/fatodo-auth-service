package com.persoff68.fatodo.security.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.exception.OAuth2EmailNotFoundException;
import com.persoff68.fatodo.exception.OAuth2InternalException;
import com.persoff68.fatodo.exception.OAuth2WrongProviderException;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.security.oauth2user.OAuth2UserInfo;
import com.persoff68.fatodo.security.oauth2user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        try {
            OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception e) {
            throw new OAuth2InternalException();
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();
        if (StringUtils.isEmpty(email)) {
            throw new OAuth2EmailNotFoundException();
        }

        UserPrincipal userPrincipal = userServiceClient.getUserPrincipalByEmailOrNew(email);
        String userProvider = userPrincipal.getProvider();

        UserPrincipal userPrincipalResult;
        if (userProvider == null) {
            userPrincipalResult = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        } else if (userProvider.equals(provider)) {
            userPrincipalResult = updateExistingUser(userPrincipal, oAuth2UserInfo);
        } else {
            throw new OAuth2WrongProviderException(userProvider);
        }
        return userPrincipalResult;
    }

    private UserPrincipal registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(oAuth2UserInfo.getEmail());
        userDTO.setUsername(oAuth2UserInfo.getEmail());
        userDTO.setProvider(provider);
        userDTO.setProviderId(oAuth2UserInfo.getId());
        userDTO = userServiceClient.createUser(userDTO);
        return userMapper.userDTOToUserPrincipal(userDTO);
    }

    private UserPrincipal updateExistingUser(UserPrincipal userPrincipal, OAuth2UserInfo oAuth2UserInfo) {
        UserDTO userDTO = userMapper.userPrincipalToUserDTO(userPrincipal);
        userDTO = userServiceClient.updateUser(userDTO);
        return userMapper.userDTOToUserPrincipal(userDTO);
    }

}
