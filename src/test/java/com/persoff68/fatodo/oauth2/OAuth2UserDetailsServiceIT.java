package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.builder.TestOAuth2User;
import com.persoff68.fatodo.builder.TestOAuth2UserRequest;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.security.exception.OAuth2WrongProviderException;
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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
public class OAuth2UserDetailsServiceIT {

    private static final String GOOGLE_NAME = "google-name";
    private static final String FACEBOOK_NAME = "facebook-name";

    @Autowired
    OAuth2UserDetailsService oAuth2UserDetailsService;
    @MockBean
    DefaultOAuth2UserService defaultOAuth2UserService;
    @MockBean
    UserServiceClient userServiceClient;

    private final OAuth2User googleOAuth2User = TestOAuth2User.create(GOOGLE_NAME);

    private final OAuth2User facebookOAuth2User = TestOAuth2User.create(FACEBOOK_NAME);

    private final UserPrincipalDTO googleUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
            .username(GOOGLE_NAME)
            .provider(Provider.GOOGLE.getValue())
            .build();

    private final UserPrincipalDTO facebookUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
            .username(FACEBOOK_NAME)
            .provider(Provider.FACEBOOK.getValue())
            .build();

    private final OAuth2UserRequest googleRequest = TestOAuth2UserRequest.create(Provider.GOOGLE.getValue());
    private final OAuth2UserRequest facebookRequest = TestOAuth2UserRequest.create(Provider.FACEBOOK.getValue());

    @BeforeEach
    void setup() {
    }

    @Test
    void testProcessOAuth2User_google_userNotExist() {
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(googleOAuth2User);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(new ModelNotFoundException());
        when(userServiceClient.createOAuth2User(any())).thenReturn(googleUserPrincipalDTO);

        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(googleRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userNotExist() {
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(facebookOAuth2User);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(new ModelNotFoundException());
        when(userServiceClient.createOAuth2User(any())).thenReturn(facebookUserPrincipalDTO);

        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(facebookRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist() {
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(facebookOAuth2User);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(facebookUserPrincipalDTO);

        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(facebookRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist_wrongProvider() {
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(facebookOAuth2User);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(googleUserPrincipalDTO);

        assertThatThrownBy(() -> oAuth2UserDetailsService.loadUser(facebookRequest))
                .isInstanceOf(OAuth2WrongProviderException.class);
    }

}
