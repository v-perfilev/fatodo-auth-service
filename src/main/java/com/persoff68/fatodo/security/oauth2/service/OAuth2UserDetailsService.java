package com.persoff68.fatodo.security.oauth2.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.security.oauth2.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.service.client.EventService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserDetailsService extends RemoteUserDetailsService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;

    public OAuth2UserDetailsService(DefaultOAuth2UserService defaultOAuth2UserService,
                                    CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository,
                                    UserMapper userMapper,
                                    UserServiceClient userServiceClient,
                                    EventService eventService) {
        super(cookieAuthorizationRequestRepository, userMapper, userServiceClient, eventService);
        this.defaultOAuth2UserService = defaultOAuth2UserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

}
