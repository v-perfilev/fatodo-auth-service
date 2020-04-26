package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.security.exception.AuthWrongProviderException;
import com.persoff68.fatodo.service.OAuth2UserDetailsService;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
public class OAuth2UserDetailsServiceIT {

    @Autowired
    OAuth2UserDetailsService oAuth2UserDetailsService;
    @MockBean
    DefaultOAuth2UserService defaultOAuth2UserService;
    @MockBean
    UserServiceClient userServiceClient;

    @BeforeEach
    void setup() {
    }

    @Test
    void testProcessOAuth2User_google_userNotExist() {
        when(defaultOAuth2UserService.loadUser(any()))
                .thenReturn(FactoryUtils.createOAuth2User("google"));
        when(userServiceClient.getUserPrincipalByEmail(any()))
                .thenThrow(new ModelNotFoundException());
        when(userServiceClient.createOAuth2User(any()))
                .thenReturn(FactoryUtils.createUserDTO("google", Provider.Constants.GOOGLE_VALUE));

        OAuth2User resultOAuth2User = oAuth2UserDetailsService
                .loadUser(FactoryUtils.createUserRequest(Provider.Constants.GOOGLE_VALUE));
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userNotExist() {
        when(defaultOAuth2UserService.loadUser(any()))
                .thenReturn(FactoryUtils.createOAuth2User("facebook"));
        when(userServiceClient.getUserPrincipalByEmail(any()))
                .thenThrow(new ModelNotFoundException());
        when(userServiceClient.createOAuth2User(any()))
                .thenReturn(FactoryUtils.createUserDTO("facebook", Provider.Constants.FACEBOOK_VALUE));

        OAuth2User resultOAuth2User = oAuth2UserDetailsService
                .loadUser(FactoryUtils.createUserRequest(Provider.Constants.FACEBOOK_VALUE));
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist() {
        when(defaultOAuth2UserService.loadUser(any()))
                .thenReturn(FactoryUtils.createOAuth2User("facebook"));
        when(userServiceClient.getUserPrincipalByEmail(any()))
                .thenReturn(FactoryUtils.createUserPrincipalDTO("facebook", Provider.Constants.FACEBOOK_VALUE, null));

        OAuth2User resultOAuth2User = oAuth2UserDetailsService
                .loadUser(FactoryUtils.createUserRequest(Provider.Constants.FACEBOOK_VALUE));
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist_wrongProvider() {
        when(defaultOAuth2UserService.loadUser(any()))
                .thenReturn(FactoryUtils.createOAuth2User("facebook"));
        when(userServiceClient.getUserPrincipalByEmail(any()))
                .thenReturn(FactoryUtils.createUserPrincipalDTO("facebook", Provider.Constants.GOOGLE_VALUE, null));

        assertThatThrownBy(() -> oAuth2UserDetailsService
                .loadUser(FactoryUtils.createUserRequest(Provider.Constants.FACEBOOK_VALUE)))
                .isInstanceOf(AuthWrongProviderException.class);
    }

}
