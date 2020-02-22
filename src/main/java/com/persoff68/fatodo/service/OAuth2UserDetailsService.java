package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.exception.AuthWrongProviderException;
import com.persoff68.fatodo.exception.OAuth2EmailNotFoundException;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.constant.AuthProvider;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.security.oauth2user.OAuth2UserInfo;
import com.persoff68.fatodo.security.oauth2user.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class OAuth2UserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;
    private final DefaultOAuth2UserService defaultOAuth2UserService;

    @Autowired
    public OAuth2UserDetailsService(UserServiceClient userServiceClient, UserMapper userMapper) {
        this.userServiceClient = userServiceClient;
        this.userMapper = userMapper;
        this.defaultOAuth2UserService = new DefaultOAuth2UserService();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();
        if (StringUtils.isEmpty(email)) {
            throw new OAuth2EmailNotFoundException();
        }

        UserPrincipal userPrincipal = userServiceClient.getUserPrincipalByEmailNullable(email);
        if (userPrincipal == null) {
            userPrincipal = registerNewUser(provider, oAuth2UserInfo);
        } else if (!userPrincipal.getProvider().name().equals(provider)) {
            throw new AuthWrongProviderException(userPrincipal.getProvider().name());
        }

        return userPrincipal;
    }

    private UserPrincipal registerNewUser(String provider, OAuth2UserInfo oAuth2UserInfo) {
        OAuth2UserDTO oAuth2UserDTO = userMapper.oAuth2UserInfoToOAuth2UserDTO(oAuth2UserInfo);
        oAuth2UserDTO.setProvider(AuthProvider.valueOf(provider));
        UserDTO userDTO = userServiceClient.createOAuth2User(oAuth2UserDTO);
        return userMapper.userDTOToUserPrincipal(userDTO);
    }

}
