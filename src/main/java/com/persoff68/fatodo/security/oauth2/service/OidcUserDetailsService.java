package com.persoff68.fatodo.security.oauth2.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.security.oauth2.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.service.client.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OidcUserDetailsService extends RemoteUserDetailsService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService;

    public OidcUserDetailsService(OidcUserService oidcUserService,
                                  CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository,
                                  UserMapper userMapper,
                                  UserServiceClient userServiceClient,
                                  EventService eventService) {
        super(cookieAuthorizationRequestRepository, userMapper, userServiceClient, eventService);
        this.oidcUserService = oidcUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        String registrationId = oidcUserRequest.getClientRegistration().getRegistrationId();
        log.info("Start OIDC user loading with {}", registrationId);
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);
        log.info("OIDC user with email {} processed", oidcUser.getEmail());
        return processOAuth2User(oidcUserRequest, oidcUser);
    }


}
