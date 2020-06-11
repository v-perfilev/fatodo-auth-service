package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.security.exception.OAuth2UserNotFoundException;
import com.persoff68.fatodo.security.exception.OAuth2WrongProviderException;
import com.persoff68.fatodo.security.oauth2.userinfo.OAuth2UserInfo;
import com.persoff68.fatodo.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class OAuth2UserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String providerString = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2UserInfo oAuth2UserInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(providerString, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();
        if (StringUtils.isEmpty(email)) {
            throw new OAuth2UserNotFoundException();
        }

        try {
            UserPrincipalDTO userPrincipalDTO = userServiceClient.getUserPrincipalByEmail(email);
            UserPrincipal userPrincipal = userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
            String currentProviderString = userPrincipal.getProvider().getValue();
            if (!currentProviderString.equals(providerString)) {
                throw new OAuth2WrongProviderException(currentProviderString);
            }
            return userPrincipal;
        } catch (ModelNotFoundException e) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String language = cookieAuthorizationRequestRepository.loadLanguage(request);
            return registerNewUser(oAuth2UserInfo, providerString, language);
        }
    }

    private UserPrincipal registerNewUser(OAuth2UserInfo oAuth2UserInfo, String provider, String language) {
        OAuth2UserDTO oAuth2UserDTO = userMapper.oAuth2UserInfoToOAuth2UserDTO(oAuth2UserInfo);
        oAuth2UserDTO.setProvider(provider);
        oAuth2UserDTO.setLanguage(language);
        UserPrincipalDTO userPrincipalDTO = userServiceClient.createOAuth2User(oAuth2UserDTO);
        return userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
    }
}
