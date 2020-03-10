package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.security.exception.AuthWrongProviderProblem;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.service.OAuth2UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.persoff68.fatodo:userservice:+:stubs")
public class OAuth2UserDetailsServiceIT {

    @Autowired
    OAuth2UserDetailsService oAuth2UserDetailsService;
    @MockBean
    DefaultOAuth2UserService defaultOAuth2UserService;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        when(jwtTokenProvider.createSystemJwt()).thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg");
    }

    @Test
    void testProcessOAuth2User_google_userNotExist() {
        OAuth2User oAuth2User = FactoryUtils.createOAuth2User("google");
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(oAuth2User);
        OAuth2UserRequest oAuth2UserRequest = FactoryUtils.createUserRequest(Providers.GOOGLE);
        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(oAuth2UserRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userNotExist() {
        OAuth2User oAuth2User = FactoryUtils.createOAuth2User("facebook");
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(oAuth2User);
        OAuth2UserRequest oAuth2UserRequest = FactoryUtils.createUserRequest(Providers.FACEBOOK);
        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(oAuth2UserRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist() {
        OAuth2User oAuth2User = FactoryUtils.createOAuth2User("facebook");
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(oAuth2User);
        OAuth2UserRequest oAuth2UserRequest = FactoryUtils.createUserRequest(Providers.FACEBOOK);
        OAuth2User resultOAuth2User = oAuth2UserDetailsService.loadUser(oAuth2UserRequest);
        assertThat(resultOAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist_wrongProvider() {
        OAuth2User oAuth2User = FactoryUtils.createOAuth2User("google");
        when(defaultOAuth2UserService.loadUser(any())).thenReturn(oAuth2User);
        OAuth2UserRequest oAuth2UserRequest = FactoryUtils.createUserRequest(Providers.FACEBOOK);
        assertThatThrownBy(() -> oAuth2UserDetailsService.loadUser(oAuth2UserRequest)).isInstanceOf(AuthWrongProviderProblem.class);
    }

}
